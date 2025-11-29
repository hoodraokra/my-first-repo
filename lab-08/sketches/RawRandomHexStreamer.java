import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RawRandomHexStreamer {
    public static void main(String[] args) {
        String apiUrl = "https://www.random.org/cgi-bin/randbyte?nbytes=16&format=h";

        while (true) {
            HttpURLConnection connection = null;

            try {
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response as text
                    StringBuilder responseBuilder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                    }

                    // Remove all whitespace and print hex string
                    String hexString = responseBuilder.toString().replaceAll("\\s+", "");
                    System.out.println(hexString);
                } else {
                    System.err.println("GET request failed with response code: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            // Wait 1 second before the next request to avoid spamming the server
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; // stop if interrupted
            }
        }
    }
}