import java.util.LinkedList;

public class Chat {
    private final LinkedList<Message> chat = new LinkedList<>();

    public void newMessage(String user, String text){
        chat.add(new Message(user, text));
    }

    public HTMLElement generateHTML(){
        int messageCount = chat.size();
        HTMLElement[] messages = new HTMLElement[messageCount];
        for (int i = 0; i < messageCount; i++) {
            messages[i] = chat.get(i).generateHTML();
        }
        return HTMLElement.createElement("div", messages).withAttribute("class", "chat");
    }

    public static class Message{
        public final String user;
        public final String text;

        public Message(String user, String text) {
            this.user = user;
            this.text = text;
        }

        public HTMLElement generateHTML() {
            return HTMLElement.createElement("div")
                .withAttribute("calss", "message")
                .withContent(
                    HTMLElement.createElement("h3", user),
                    HTMLElement.createElement("p", text)
                );
        }
    }
}
