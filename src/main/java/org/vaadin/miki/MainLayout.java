package org.vaadin.miki;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.miki.design.MainDesign;
import org.vaadin.miki.data.Message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Wiring for the design.
 * Created by miki on 2016-10-05.
 */
public class MainLayout extends MainDesign {

  public static final String FILTER = "*#@!&";

  private final BeanItemContainer<Message> container;

  private final Properties properties = new Properties();

  public MainLayout(BeanItemContainer<Message> container, Container rooms) {
    super();

    this.container = container;
    // list of seven dirty words by George Carlin
    if(!this.loadProperties("words.properties"))
      System.err.println("Word filter not loaded.");

    Responsive.makeResponsive(this);

    button.setClickShortcut(ShortcutAction.KeyCode.ENTER);

    button.addClickListener( e -> {
      if(text.getValue().isEmpty())
        onEmptyTextSubmitted();
      else {
        onTextSubmitted(text.getValue());
        text.clear();
      }
    });

    roomSelect.setContainerDataSource(rooms);
    roomSelect.addValueChangeListener(e -> UI.getCurrent().getNavigator().navigateTo(e.getProperty().getValue().toString()));
    text.focus();
  }

  private void onTextSubmitted(String text) {
    List<String> dirty_words = Arrays.asList(this.properties.getProperty("filter", "").split("\\s*,\\s*"));
    String[] text_words = text.split("\\s+");
    StringBuilder result = new StringBuilder();
    for(String word: text_words) {
      if (dirty_words.contains(word.toLowerCase()))
        result.append(" "+FILTER);
      else result.append(" " + word);
    }
    Message message = new Message(result.substring(1));
    if(UI.getCurrent().getNavigator().getCurrentView() instanceof MessageDisplayer)
      message.setRoom(((MessageDisplayer) UI.getCurrent().getNavigator().getCurrentView()).getRoomName());

    this.container.addBean(message);
  }

  private void onEmptyTextSubmitted() {
    Notification notification = new Notification(
        "You said it best, but you said nothing at all",
        Notification.Type.ERROR_MESSAGE
    );
    notification.setDescription("You have to enter some text to have it shouted. Please try again.");
    notification.setPosition(Position.MIDDLE_CENTER);
    notification.setStyleName(ValoTheme.NOTIFICATION_BAR);
    // icon by http://rokey.deviantart.com/art/The-Blacy-11327960
    // (c) NetEase.com - for non-commercial purposes only
    notification.setIcon(new ExternalResource("http://findicons.com/files/icons/376/the_blacy/128/nothing_to_say.png"));
    notification.show(UI.getCurrent().getPage());
  }

  private boolean loadProperties(String filename) {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
      this.properties.load(inputStream);
      return true;
    } catch (IOException | NullPointerException e) {
      return false;
    }
  }

  public SingleComponentContainer getPlaceholder() {
    return this.placeholder;
  }

}
