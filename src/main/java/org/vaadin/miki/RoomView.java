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
public class RoomView extends RoomDisplay implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this.setRoomName(viewChangeEvent.getViewName());
    }

}
