package org.vaadin.miki;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import org.vaadin.miki.data.Message;

/**
 * Provides room views.
 */
public class RoomViewProvider implements ViewProvider {
    private final RoomView view = new RoomView();

    public RoomViewProvider(Container messages) {
        this.view.setContainerDataSource(messages);
    }

    @Override
    public String getViewName(String s) {
        if(s == null || s.isEmpty())
            return Message.DEFAULT_ROOM;
        else return s;
    }

    @Override
    public View getView(String s) {
        return this.view;
    }
}
