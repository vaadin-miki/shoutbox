package org.vaadin.miki.data;

import java.util.Objects;

/**
 * A shoutbox message.
 */
public class Message {

    private String text;

    public Message() {
        this("");
    }

    public Message(String string) {
        this.setText(string);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Message) && Objects.equals(this.getText(), ((Message)obj).getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getText());
    }
}
