package com.zeus.migue.notes.infrastructure.utils;

import com.zeus.migue.notes.R;

public class Event {
    private int localResId;
    private String message;
    private int messageType;
    public static final Event ROOM_INSERT_ERROR = new Event(R.string.room_insert_error, MessageType.SHOW_IN_DIALOG);
    public static final Event ROOM_UPDATE_ERROR = new Event(R.string.room_update_error, MessageType.SHOW_IN_DIALOG);
    public static final Event ROOM_DELETE_ERROR = new Event(R.string.room_delete_error, MessageType.SHOW_IN_DIALOG);
    public static final Event PASSWORDS_DO_NOT_MATCH = new Event(R.string.activity_login_fragment_sign_up_mismatch_passwords, MessageType.SHOW_IN_DIALOG);

    public Event(int localResId, int messageType) {
        this.localResId = localResId;
        this.messageType = messageType;
    }

    public Event(String message, int messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    public int getLocalResId() {
        return localResId;
    }

    public String getMessage() {
        return message;
    }

    public int getMessageType() {
        return messageType;
    }

    public class MessageType{
        public static final int SHOW_IN_DIALOG = 1;
        public static final int SHOW_IN_TOAST = 2;
        public static final int DO_NOT_SHOW = 3;
    }
}
