import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RandomHexPrinter {
    public static void main(String[] args) {
        // Random.org API URL: 16 random bytes returned as hexadecimal
        String apiUrl = "https://www.random.org/cgi-bin/randbyte?nbytes=16&format=h";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and print the response
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line); // print each line (hex numbers)
                    }
                }
            } else {
                System.err.println("GET request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}