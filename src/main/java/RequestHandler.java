public class RequestHandler {
    public final HttpRequest request;
    public final ResponseSender responseSender;

    public RequestHandler(HttpRequest request, ResponseSender responseSender) {
        this.request = request;
        this.responseSender = responseSender;
    }

    public static interface ResponseSender{
        public HttpResponse generateResponse(HttpRequest request);
    }
}
