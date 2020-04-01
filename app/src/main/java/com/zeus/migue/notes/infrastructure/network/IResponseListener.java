package com.zeus.migue.notes.infrastructure.network;

public interface IResponseListener<Entity> {
    void onResponse(Entity entity);
}
