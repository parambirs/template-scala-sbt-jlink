import java.net.URI
import java.net.http.HttpResponse.BodyHandlers
import java.net.http.{HttpClient, HttpRequest}

@main
def app(): Unit =
  println("Hello, world!")

  val uri = URI("https://google.ca")
  val client = HttpClient.newHttpClient()
  val request = HttpRequest.newBuilder(uri).build()
  val response = client.send(request, BodyHandlers.ofString())
  println(Console.YELLOW + response.body() + Console.RESET)
