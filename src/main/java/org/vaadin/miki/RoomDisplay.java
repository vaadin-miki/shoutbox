package org.vaadin.miki;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import org.vaadin.miki.data.Message;

import java.util.List;
import java.util.ArrayList;

public class RoomDisplay extends CustomComponent implements MessageDisplayer, Container.Viewer {

    private String roomName;

    private final FormLayout layout = new FormLayout();

    private int lastMessage = 0;

    private Container container;

    public RoomDisplay() {
        super();
        this.setSizeFull();
        this.layout.setSizeFull();
        this.layout.setMargin(true);
        this.layout.setSpacing(true);
        this.setCompositionRoot(this.layout);
    }

    @Override
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
        this.layout.removeAllComponents();
    }

    @Override
    public void displayMessage(Message message) {
        if(message.getRoom().equals(this.getRoomName())) {
            Label label = new Label(message.getText());
            label.setCaption(message.getRoom());
            if (label.getValue().indexOf(ShoutboxUI.FILTER) != -1)
                label.addStyleName("redacted");
            label.addStyleName("message");
            this.layout.addComponentAsFirst(label);
        }
    }

    @Override
    public void setContainerDataSource(Container container) {
        if(this.container instanceof Container.ItemSetChangeNotifier)
            ((Container.ItemSetChangeNotifier)this.container).removeItemSetChangeListener(this::onContainerItemSetChanged);

        this.container = container;

        this.layout.removeAllComponents();

        if(this.container instanceof Container.ItemSetChangeNotifier)
            ((Container.ItemSetChangeNotifier)this.container).addItemSetChangeListener(this::onContainerItemSetChanged);

    }

    private void onContainerItemSetChanged(Container.ItemSetChangeEvent event) {
        if(this.getUI().isAttached()) {
            this.getUI().access(() -> {
                List<Object> messages = new ArrayList<>(event.getContainer().getItemIds());
                for (; lastMessage < messages.size(); lastMessage++) {
                    // type cast ok, because of bean item container
                    displayMessage((Message) messages.get(lastMessage));
                }
                getUI().push();
            });
        }
    }

    @Override
    public Container getContainerDataSource() {
        return this.container;
    }
}
