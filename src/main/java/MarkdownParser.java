import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public final class MarkdownParser {
    private MarkdownParser(){}

    public static String markup(String markdown){
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);
        return html;
    }
}
