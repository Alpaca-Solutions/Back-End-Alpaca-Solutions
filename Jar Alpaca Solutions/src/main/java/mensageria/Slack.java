package mensageria;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
public class Slack {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String URL = "https://hooks.slack.com/services/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";

    public static void sendMessage(JSONObject content) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(URL))
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(content.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        System.out.println(String.format("Status: %s", response.statusCode()));
        System.out.println(String.format("Resposta da Solicitação: %s", response.body()));
    }
}
