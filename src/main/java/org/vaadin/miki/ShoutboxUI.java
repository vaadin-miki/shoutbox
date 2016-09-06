package org.vaadin.miki;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.*;
import com.vaadin.shared.Position;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.*;
import org.vaadin.miki.data.Message;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Push(PushMode.MANUAL)
public class ShoutboxUI extends UI {

    private static final BeanItemContainer<Message> MESSAGES = new BeanItemContainer<>(Message.class);

    private final Properties properties = new Properties();
    private final VerticalLayout layout = new VerticalLayout();

    private int lastMessage = 0;

    private boolean loadProperties(String filename) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
            this.properties.load(inputStream);
            return true;
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    public ShoutboxUI() {
        super();
        MESSAGES.addItemSetChangeListener(this::messagesChanged);
    }

    private void messagesChanged(Container.ItemSetChangeEvent event) {
        this.access(new Runnable() {
            @Override
            public void run() {
                List<Message> messages = MESSAGES.getItemIds();
                for(; lastMessage < messages.size(); lastMessage++) {
                    layout.addComponent(new Label(messages.get(lastMessage).getText()));

                push();
            }
        }});
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        // list of seven dirty words by George Carlin
        if(!this.loadProperties("words.properties"))
            System.err.println("Word filter not loaded.");

        final TextField text = new TextField();
        text.setCaption("You were saying?");
        text.setInputPrompt("(type something)");

        Button button = new Button("Shout");

        button.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        button.addClickListener( e -> {
                if(text.getValue().isEmpty())
                        onEmptyTextSubmitted();
                else {
                    onTextSubmitted(text.getValue());
                    text.clear();
                }
        });

        layout.addComponents(text, button);
        layout.setMargin(true);
        layout.setSpacing(true);
        
        setContent(layout);
    }

    private void onTextSubmitted(String text) {
        List<String> dirty_words = Arrays.asList(this.properties.getProperty("filter", "").split("\\s*,\\s*"));
        String[] text_words = text.split("\\s+");
        StringBuilder result = new StringBuilder();
        for(String word: text_words) {
            if (dirty_words.contains(word.toLowerCase()))
                result.append(" *#@!&");
            else result.append(" " + word);
        }
        MESSAGES.addBean(new Message(result.substring(1)));
    }

    private void onEmptyTextSubmitted() {
        Notification notification = new Notification(
                "You said it best, but you said nothing at all",
                Notification.Type.ERROR_MESSAGE
        );
        notification.setDescription("You have to enter some text to have it shouted. Please try again.");
        notification.setPosition(Position.MIDDLE_CENTER);
        // icon by http://rokey.deviantart.com/art/The-Blacy-11327960
        // (c) NetEase.com - for non-commercial purposes only
        notification.setIcon(new ExternalResource("http://findicons.com/files/icons/376/the_blacy/128/nothing_to_say.png"));
        notification.show(this.getPage());
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ShoutboxUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
