package org.vaadin.miki;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import org.vaadin.miki.data.Message;

/**
 * A room view.
 */
public class RoomView extends FormLayout implements View, MessageDisplayer {

    private String roomName;

    public RoomView() {
        super();
        this.setSizeFull();
        this.setMargin(true);
        this.setSpacing(true);
    }

    @Override
    public String getRoomName() {
        return roomName;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this.setRoomName(viewChangeEvent.getViewName());

    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
        this.removeAllComponents();
    }

    @Override
    public void displayMessage(Message message) {
        if(message.getRoom().equals(this.getRoomName())) {
            Label label = new Label(message.getText());
            label.setCaption(message.getRoom());
            if (label.getValue().indexOf(ShoutboxUI.FILTER) != -1)
                label.addStyleName("redacted");
            label.addStyleName("message");
            this.addComponentAsFirst(label);
        }
    }
}
