package com.zeus.migue.notes.infrastructure.utils;

public class LiveDataEvent<T> {
    private boolean hasBeenHandled = false;
    private T content;

    public LiveDataEvent(T content) {
        this.content = content;
    }

    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }
}
