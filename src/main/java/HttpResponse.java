import java.util.ArrayList;
import java.util.Map.Entry;

public class HttpResponse {
    public final String version;
    public final int statusCode;
    public final String statusMessage;
    public final HttpComponents.Headers headers;
    public final byte[] body;

    public HttpResponse(String version, int statusCode, String statusMessage, HttpComponents.Headers headers, byte[] body) {
        this.version = version;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.headers = headers;
        this.body = body;
    }

    public static HttpResponse basicIncapsulate(int statusCode, String statusMessage, String contentType, byte[] body){
        HttpComponents.Headers headers = getBasicHeaders(contentType, body.length);
        return new HttpResponse(WebServer.HTTP_VERSION, statusCode, statusMessage, headers, body);
    }

    private static HttpComponents.Headers getBasicHeaders(String contentType, int length) {
        HttpComponents.Headers headers = new HttpComponents.Headers();
        headers.put("Content-Type", contentType);
        headers.put("Content-Length", Integer.toString(length));
        headers.put("Connection", "close");
        return headers;
    }

    @SafeVarargs
    public static HttpResponse incapsulate(int statusCode, String statusMessage, byte[] body, Entry<String, String> ... headers){
        return new HttpResponse(WebServer.HTTP_VERSION, statusCode, statusMessage, HttpComponents.Headers.fromEntryArray(headers), body);
    }

    public byte[] constructResponseLine(){
        return String.format("%s %d %s\n", version, statusCode, statusMessage).getBytes();
    }

    public byte[][] constructHeaders(){
        ArrayList<byte[]> headersAsByteList = new ArrayList<>();
        for (Entry<String, String> header : headers.entrySet()) {
            headersAsByteList.add(String.format("%s : %s\n", header.getKey(), header.getValue()).getBytes());
        }
        return headersAsByteList.toArray(new byte[headersAsByteList.size()][]);
    }
}
