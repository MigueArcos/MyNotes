package com.zeus.migue.notes.infrastructure.contracts;

import com.zeus.migue.notes.infrastructure.network.HttpClient;

public class JsonConverter {
    public String toJson() {
        return HttpClient.JSON_SERIALIZER.toJson(this);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return HttpClient.JSON_SERIALIZER.fromJson(json, clazz);
    }
}
