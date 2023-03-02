import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class WebServer extends Thread {
    public static final String HTTP_VERSION = "HTTP/1.1";
    private final int port;
    private String documentRoot = "./";
    private final ArrayList<RequestHandler> handlers = new ArrayList<>();
    private RequestHandler.ResponseSender defaultResponseSender;
    private boolean autoFetch = false;

    public WebServer(String documentRoot) {
        this(80, documentRoot);
    }

    public WebServer(int port, String documentRoot) {
        this.port = port;
        this.documentRoot = documentRoot;
        this.autoFetch = true;
    }

    public WebServer() {
        this(80);
    }

    public WebServer(int port) {
        this.port = port;
    }

    public void setAutoFetch(boolean autoFetch){
        this.autoFetch = autoFetch;
    }

    public void addRequestHandler(HttpComponents.Method method, URI uri, RequestHandler.ResponseSender requestHandler) {
        handlers.add(new RequestHandler(new HttpRequest(method, uri, null, null, null), requestHandler));
    }

    public void setDefaultResponseSender(RequestHandler.ResponseSender defaultResponseSender) {
        this.defaultResponseSender = defaultResponseSender;
    }

    @Override
    public void run() {
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        }
    }

    public byte[] readFile(String pathname) {
        try {
            Path path = Paths.get(documentRoot, pathname);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class ClientHandler extends Thread {
        private final Socket clientSocket;
        private BufferedReader requestReader;
        private OutputStream responseSender;

        private ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            setName(clientSocket.getRemoteSocketAddress().toString());
        }

        @Override
        public void run() {
            initialize();
            try {
                HttpRequest request = waitForRequest();
                HttpResponse response = handleRequest(request);
                sendResponse(response);
            } catch (IOException | URISyntaxException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        private void initialize() {
            try {
                requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                responseSender = clientSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private HttpRequest waitForRequest() throws IOException, URISyntaxException {
            StringBuilder requestBuilder = new StringBuilder();
            while (true) {
                String line = requestReader.readLine();
                if (line == null || line.isEmpty())
                    break;
                requestBuilder.append(line + "\n\r");
            }
            return HttpRequest.parse(requestBuilder.toString());
        }

        private HttpResponse handleRequest(HttpRequest request) {
            for (RequestHandler requestHandler : handlers) {
                if (request.equals(requestHandler.request))
                    return requestHandler.responseSender.generateResponse(request);
            }
            if(autoFetch){
                String path = request.uri.getPath();
                File file = new File(documentRoot, path);
                if(file.exists() && file.isFile()) return getFileFromPath(path);
            }
            return defaultResponseSender.generateResponse(request);
        }

        private HttpResponse getFileFromPath(String path){
            String extension = path.replaceAll("^.+\\.", "");
            return HttpResponse.basicIncapsulate(200, "OK", getContentTypeFromExtension(extension), readFile(path));
        }

        private String getContentTypeFromExtension(String extension){
            switch (extension) {
                case "html": case "htm": return "text/html";
                case "css": return "text/css";
                case "js": return "text/js";
                case "jpg": return "image/jpg";
                case "png": return "image/png";
                case "gif": return "image/gif";
                default: return "text/plain";
            }
        }

        private void sendResponse(HttpResponse response) throws IOException {
            responseSender.write(response.constructResponseLine());
            responseSender.flush();
            byte[][] headers = response.constructHeaders();
            for (byte[] bytes : headers) {
                responseSender.write(bytes);
                responseSender.flush();
            }
            responseSender.write("\n".getBytes());
            responseSender.write(response.body);
            responseSender.close();
        }
    }
}
