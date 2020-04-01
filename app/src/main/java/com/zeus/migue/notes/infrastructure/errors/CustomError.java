package com.zeus.migue.notes.infrastructure.errors;

import com.zeus.migue.notes.infrastructure.utils.Event;

public class CustomError extends Exception{
    private Event event;
    public CustomError(Event event){
        this.event = event;
    }
    public Event getEvent() {
        return event;
    }
}
