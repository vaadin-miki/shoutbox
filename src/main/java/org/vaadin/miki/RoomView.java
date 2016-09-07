package org.vaadin.miki;

import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;

/**
 * A room view.
 */
public class RoomView extends RoomDisplay implements View {

    private Container roomContainer;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        String viewName = viewChangeEvent.getViewName();

        if(!this.roomContainer.containsId(viewName)) {
            this.roomContainer.addItem(viewName).getItemProperty("name").setValue(viewName);
        }

        this.setRoomName(viewName);
    }

    public Container getRoomContainer() {
        return roomContainer;
    }

    public void setRoomContainer(Container roomContainer) {
        this.roomContainer = roomContainer;
    }
}
