package umn.useit.model;

import java.util.List;

import umn.useit.model.ChatMessage;

public class Room {

    private String senderName;
    private String previewMessage;

    public Room(String senderName, String previewMessage) {
        this.senderName = senderName;
        this.previewMessage = previewMessage;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getPreviewMessage() {
        return previewMessage;
    }

    public void setChatMessages(String previewMessage) {
        this.previewMessage = previewMessage;
    }
}
