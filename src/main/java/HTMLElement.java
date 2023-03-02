import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class HTMLElement {
    public static HTMLElement createElement(String tag) {
        return new HTMLElement(tag);
    }

    public static HTMLElement createElement(String tag, String textContent) {
        return new HTMLElement(tag).withContent(textContent);
    }

    public static HTMLElement createElement(String tag, HTMLElement... elements) {
        return new HTMLElement(tag).withContent(elements);
    }

    public final String tag;
    public final HashMap<String, String> attributes;
    public HTMLContent content;

    private HTMLElement(String tag) {
        this.tag = tag;
        this.attributes = new HashMap<>();
    }

    public HTMLElement withAttribute(String name, String value) {
        attributes.put(name, value);
        return this;
    }

    public HTMLElement withContent(String textContent) {
        this.content = new HTMLContent.HTMLTextContent(textContent);
        return this;
    }

    public HTMLElement withContent(HTMLElement... elements) {
        this.content = new HTMLContent.HTMLElementsContent(elements);
        return this;
    }

    public HTMLElement insertInBody(String title, HTMLElement... headElements) {
        headElements = Arrays.copyOf(headElements, headElements.length + 1);
        headElements[headElements.length - 1] = createElement("title").withContent(title);
        return createElement("html")
                .withContent(
                        createElement("head")
                                .withContent(
                                        headElements),
                        createElement("body")
                                .withContent(
                                        this));
    }

    public HTMLElement insertInBody(HTMLElement... headElements) {
        return createElement("html")
                .withContent(
                        createElement("head")
                                .withContent(
                                        headElements),
                        createElement("body")
                                .withContent(
                                        this));
    }

    public byte[] getBytes() {
        return toString().getBytes();
    }

    @Override
    public String toString() {
        StringBuffer constructor = new StringBuffer();
        constructor.append(String.format("<%s", tag));
        for (Entry<String, String> entry : attributes.entrySet()) {
            constructor.append(String.format(" %s=\"%s\"", entry.getKey(), entry.getValue()));
        }
        if (content != null) {
            constructor.append(">");
            constructor.append(content.toString());
            constructor.append(String.format("</%s>", tag));
        } else {
            constructor.append("/>");
        }
        return constructor.toString();
    }

    private static abstract class HTMLContent {
        private static class HTMLElementsContent extends HTMLContent {
            private final HTMLElement[] elements;

            private HTMLElementsContent(HTMLElement[] elements) {
                this.elements = elements;
            }

            @Override
            public String toString() {
                StringBuffer constructor = new StringBuffer();
                for (int i = 0; i < elements.length; i++) {
                    constructor.append(String.format("%s", elements[i]));
                }
                return constructor.toString();
            }
        }

        private static class HTMLTextContent extends HTMLContent {
            private final String value;

            private HTMLTextContent(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }
        }
    }
}
