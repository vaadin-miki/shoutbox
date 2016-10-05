package org.vaadin.miki;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.UI;
import org.vaadin.miki.data.Message;

import javax.servlet.annotation.WebServlet;

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

    private final IndexedContainer rooms = new IndexedContainer();

    public ShoutboxUI() {
        super();

        this.rooms.addContainerProperty("name", String.class, "");
    }

    @Override
    public void close() {
        if(this.getNavigator().getCurrentView() instanceof Container.Viewer)
            ((Container.Viewer)this.getNavigator().getCurrentView()).setContainerDataSource(null);
        super.close();
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        MainLayout mainLayout = new MainLayout(MESSAGES, rooms);

        final Navigator navigator = new Navigator(this, mainLayout.getPlaceholder());
        navigator.addProvider(new RoomViewProvider(MESSAGES, rooms));

        this.setContent(mainLayout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ShoutboxUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
