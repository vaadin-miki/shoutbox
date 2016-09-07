package org.vaadin.miki;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import org.vaadin.miki.data.Message;

/**
 * Provides room views.
 */
public class RoomViewProvider implements ViewProvider {
    @Override
    public String getViewName(String s) {
        if(s == null || s.isEmpty())
            return Message.DEFAULT_ROOM;
        else return s;
    }

    @Override
    public View getView(String s) {
        return new RoomView();
    }
}
