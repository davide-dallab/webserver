import java.net.URI;
import java.net.URISyntaxException;

public class HttpRequest {
    public final HttpComponents.Method method;
    public final URI uri;
    public final String version;
    public final HttpComponents.Headers headers;
    public final String body;

    public HttpRequest(HttpComponents.Method method, URI uri, String version, HttpComponents.Headers headers, String body) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest parse(String rawRequest) throws URISyntaxException{
        if(rawRequest == null || rawRequest.isEmpty()) return null;

        String[] parts = rawRequest.split("\n\r\n\r");
        String requestAndHeaders = parts[0];
        String body = parts.length > 1 ? parts[1] : "";
        String[] requestAndHeadersParts = requestAndHeaders.split("\n\r");
        String requestLine = requestAndHeadersParts[0];
        String[] requestLineParts = requestLine.split(" ");
        HttpComponents.Method method = HttpComponents.Method.valueOf(requestLineParts[0]);
        URI uri = new URI(requestLineParts[1]);
        String version = requestLineParts[2];
        HttpComponents.Headers headers = new HttpComponents.Headers();
        for (int i = 1; i < requestAndHeadersParts.length; i++) {
            String header = requestAndHeadersParts[i];
            String[] headerParts = header.split(" *: *");
            String headerName = headerParts[0];
            String headerValue = headerParts[1];
            headers.put(headerName, headerValue);
        }
        return new HttpRequest(method, uri, version, headers, body);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof HttpRequest)) return false;
        HttpRequest other = (HttpRequest) obj;
        if(!uri.equals(other.uri)) return false;
        return true;
    }
}
