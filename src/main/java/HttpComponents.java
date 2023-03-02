import java.util.HashMap;

public class HttpComponents {
    public static class Headers extends HashMap<String, String>{
        public static Headers fromEntryArray(Entry<String, String>[] entries){
            Headers headers = new Headers();
            for (Entry<String,String> entry : entries) {
                headers.put(entry.getKey(), entry.getValue());
            }
            return headers;
        }
    }
    public static enum Method{
        GET,
        HEAD,
        POST,
        PUT,
        DELETE,
        CONNECT,
        OPTIONS,
        TRACE,
        PATCH
    }
}
