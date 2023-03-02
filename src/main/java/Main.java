import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.awt.Desktop;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        WebServer server = new WebServer("resources");
        server.setAutoFetch(false);

        Chat chat = new Chat();

        try {
            // PAGES
            server.addRequestHandler(HttpComponents.Method.GET, new URI("/"), request -> {
                return HttpResponse.basicIncapsulate(200, "OK", "text/html", server.readFile("index.html"));
            });
            server.addRequestHandler(HttpComponents.Method.GET, new URI("/links"), request -> {
                return HttpResponse.basicIncapsulate(200, "OK", "text/html", server.readFile("links.html"));
            });
            server.addRequestHandler(HttpComponents.Method.GET, new URI("/constructor"), request -> {
                HTMLElement container = createDynamicPage();
                return HttpResponse.basicIncapsulate(200, "OK", "text/html", container.getBytes());
            });
            server.addRequestHandler(HttpComponents.Method.GET, new URI("/readme"), request -> {
                try {
                    String readme = new String(server.readFile("../README.md"), "UTF-8");
                    String html = MarkdownParser.markup(readme);
                    HTMLElement container = HTMLElement.createElement("div")
                        .withContent(html)
                        .withAttribute("class", "markdown")
                        .insertInBody("README.md", 
                            HTMLElement.createElement("link")
                                .withAttribute("rel", "stylesheet")
                                .withAttribute("href", "styles")
                        );
                    return HttpResponse.basicIncapsulate(200, "OK", "text/html", container.getBytes());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return HttpResponse.basicIncapsulate(404, "NotFound", "text/html", server.readFile("error.html"));
                }
            });
            server.addRequestHandler(HttpComponents.Method.GET, new URI("/messages"), request -> {
                return HttpResponse.basicIncapsulate(200, "OK", "text/html", chat.generateHTML().getBytes());
            });

            // IMAGES
            server.addRequestHandler(HttpComponents.Method.GET, new URI("/arena"), request -> {
                return HttpResponse.basicIncapsulate(200, "OK", "image/jpg", server.readFile("arena.jpg"));
            });
            server.addRequestHandler(HttpComponents.Method.GET, new URI("/client-server"), request -> {
                return HttpResponse.basicIncapsulate(200, "OK", "image/png", server.readFile("client-server.png"));
            });
            server.addRequestHandler(HttpComponents.Method.GET, new URI("/http-logo"), request -> {
                return HttpResponse.basicIncapsulate(200, "OK", "image/png", server.readFile("http-logo.png"));
            });

            // STYLES
            server.addRequestHandler(HttpComponents.Method.GET, new URI("/styles"), request -> {
                return HttpResponse.basicIncapsulate(200, "OK", "text/css", server.readFile("styles.css"));
            });

            // SCRIPTS
            server.addRequestHandler(HttpComponents.Method.GET, new URI("/script"), request -> {
                return HttpResponse.basicIncapsulate(200, "OK", "text/js", server.readFile("script.js"));
            });

            // POST
            server.addRequestHandler(HttpComponents.Method.GET, new URI("/messages"), request -> {
                String message = new String(request.body);
                System.out.println(message);
                return HttpResponse.basicIncapsulate(200, "OK", "text/json", "message sent".getBytes());
            });
            
            // DEFAULT
            server.setDefaultResponseSender(request -> {
                return HttpResponse.basicIncapsulate(404, "NotFound", "text/html", server.readFile("error.html"));
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        server.start();
        printIpAddresses();
        Desktop.getDesktop().browse(new URI("http://127.0.0.1"));
    }

    private static HTMLElement createDynamicPage() {
        HTMLElement styleElement = HTMLElement.createElement("link")
            .withAttribute("rel", "stylesheet")
            .withAttribute("href", "styles");

        HTMLElement article = HTMLElement.createElement("article")
            .withContent(
                HTMLElement.createElement(
                    "h2",
                    "Articolo di esempio"
                ),
                HTMLElement.createElement(
                    "p",
                    "Questo articolo è stato creato nel metodo sotto descritto."
                )
            );
            
        HTMLElement container = HTMLElement.createElement("div")
            .withAttribute("class", "container")
            .withContent(
                HTMLElement.createElement(
                    "h1",
                    "HTMLElement"),
                HTMLElement.createElement(
                    "p",
                    "Questa pagina è stata creata dinamicamente tramite l'utilizzo di una classe" +
                        " java \"HTMLElement\" che permette di generare un codice HTML."
                ),
                HTMLElement.createElement(
                    "p",
                    "Questa classe è in grado di creare pagine dinamicamente e potenzialmente esse possono essere create in automatico sulla base di alcuni " +
                        "dati e quindi senza l'intervento di nessuno (come si vede nell'esempio del parsing markdown)."
                ),
                HTMLElement.createElement(
                    "p",
                    "Segue una dimostrazione di come creare un semplice articolo:"
                ),
                HTMLElement.createElement("div")
                    .withAttribute("class", "example")
                    .withContent(
                        article,
                        HTMLElement.createElement(
                            "pre",
                            "<span style=\"color: hsl(150deg 60% 50%)\">HTMLElement</span> <span style=\"color: hsl(180deg 100% 50%)\">article</span> = <span style=\"color: hsl(150deg 60% 50%)\">HTMLElement</span>.<span style=\"color: hsl(60deg 80% 50%)\">createElement</span>(<span style=\"color: hsl(40deg 60% 60%)\"/>\"article\"</span>)<br/>"
                            +
                            "    .<span style=\"color: hsl(60deg 80% 50%)\">withContent</span>(<br/>"
                            +
                            "        <span style=\"color: hsl(150deg 60% 50%)\">HTMLElement</span>.<span style=\"color: hsl(60deg 80% 50%)\">createElement</span>(<br/>"
                            +
                            "            <span style=\"color: hsl(40deg 60% 60%)\"/>\"h2\"</span>,<br/>"
                            +
                            "            <span style=\"color: hsl(40deg 60% 60%)\"/>\"Articolo di esempio\"</span><br/>"
                            +
                            "        ),<br/>"
                            +
                            "        <span style=\"color: hsl(150deg 60% 50%)\">HTMLElement</span>.<span style=\"color: hsl(60deg 80% 50%)\">createElement</span>(<br/>"
                            +
                            "            <span style=\"color: hsl(40deg 60% 60%)\"/>\"p\"</span>,<br/>"
                            +
                            "            <span style=\"color: hsl(40deg 60% 60%)\"/>\"Questo articolo è stato creato nel metodo sotto descritto.\"</span><br/>"
                            +
                            "        )<br/>" +
                            "    );"
                        )
                    ),
                    HTMLElement.createElement("ul")
                        .withAttribute("class", "menu")
                        .withContent(
                            HTMLElement.createElement("li")
                                .withContent(
                                    HTMLElement.createElement("a", "HOME PAGE")
                                        .withAttribute("href", "/")
                                )
                        )
                )
            .insertInBody("PROVA CONSTRUCTOR", styleElement);
        return container;
    }

    public static void printIpAddresses() {
        try {
            System.out.println("Available IP addresses:");
            int count = 1;
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address) {
                        System.out.printf("%d. %s\n", count, address.getHostAddress());
                        count++;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
