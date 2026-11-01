template-scala2-sbt-jlink
=========================

This template project builds an application written in Scala that can be bundled as a custom JRE 
using the [jlink]([jlink](https://docs.oracle.com/javase/9/tools/jlink.htm)) tool. `jlink` allows us to create a distributable custom JRE containing only 
the specific JDK modules that our application needs. We wouldn't need to expect user to have Java 
installed on their machine or bundle the complete JRE (a few hundred MBs) with our app. 

Run the app via sbt
-------------------

```
> sbt run
......
[info] running (fork) App 
...
[success] elapsed time: 1 s
```

`sbt-native-packager` plugin
--------------------------------

[sbt-native-packager](https://www.scala-sbt.org/sbt-native-packager/index.html) is an sbt plugin that makes it easy to build packages for different 
operating systems. We use its [Jlink plugin](https://www.scala-sbt.org/sbt-native-packager/archetypes/jlink_plugin.html) to generate a custom JRE for our application.

It's specified in the `project/plugins.sbt` file:

```scala
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.13.0")
```

We need to enable the `JlinkPlugin` in `build.sbt`:

```scala
enablePlugins(JlinkPlugin)
```

Create custom distribution for the app
--------------------------------------

```
> sbt stage
```

The app with the custom JRE is available in `target/universal/stage` directory:

```
> ./target/universal/stage/bin/simple-scala-cli 
Hello, world!
<HTML><HEAD>...
...
```

**Verify that the app is not using the system JRE**

How can we be sure that it's not using the system wide Java installation on the machine? We can 
rename the `stage/jre` directory to something else and try again:

```
> mv ./target/universal/stage/jre ./target/universal/stage/xyz

> ./target/universal/stage/bin/simple-scala-cli
No java installations was detected.
Please go to http://www.java.com/getjava/ and download
```

What does jlink plugin do?
--------------------------

The Jlink plugin performs two important steps in the build process. Here're the two relevant lines 
from the `sbt stage` command output. It first runs `jdeps` to determine which java modules are being 
used by our codebase:

```
[info] Running: jdeps --multi-release 21 -R template-scala-sbt-jlink/target/scala-3.10.0/classes 
/Users/.../Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.10.0/scala3-library_3-3.10.0.jar 
```

It then runs `jlink` to build a custom JRE that includes all the Java modules our app will need:

```
[info] Running: jlink --output template-scala-sbt-jlink/target/jlink/output --add-modules 
java.base,java.net.http,jdk.unsupported,jdk.crypto.ec
```

Specifying jlink dependencies manually
--------------------------------------

In some cases, `jdeps` isn't able to figure out all the required modules automatically. For example,
the `jdk.crypto.ec` module is required for SSL to work correctly in our sample code. The solution is 
to add this jlink dependency in our `build.sbt` file:

```
jlinkModules += "jdk.crypto.ec"
```
