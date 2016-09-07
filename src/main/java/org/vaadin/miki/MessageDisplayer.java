package org.vaadin.miki;

import org.vaadin.miki.data.Message;

/**
 * Created by Sorrow on 2016-09-07.
 */
public interface MessageDisplayer {
    String getRoomName();

    void displayMessage(Message message);
}
