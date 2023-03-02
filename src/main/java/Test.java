import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

public class Test {
    public static void main(String[] args) throws URISyntaxException {

        try (ServerSocket server = new ServerSocket(80)) {
            Socket client = server.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while (true) {
                String line = null;
                do {
                    line = reader.readLine();
                    System.out.println(line);
                } while (line == null || line.isEmpty());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
