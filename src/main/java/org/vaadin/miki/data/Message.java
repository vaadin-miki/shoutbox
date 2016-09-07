package org.vaadin.miki.data;

import java.util.Objects;

/**
 * A shoutbox message.
 */
public class Message {

    public static final String DEFAULT_ROOM = "main";

    private String text;

    private String room;

    public Message() {
        this("");
    }

    public Message(String string) {
        this(DEFAULT_ROOM, string);
    }

    public Message(String room, String text) {this.setText(text); this.setRoom(room);}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Message) &&
                Objects.equals(this.getText(), ((Message)obj).getText()) &&
                Objects.equals(this.getRoom(), ((Message) obj).getRoom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getRoom(), this.getText());
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
