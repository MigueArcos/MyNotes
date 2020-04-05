package com.zeus.migue.notes.infrastructure.contracts;

import com.zeus.migue.notes.infrastructure.utils.Utils;

public class JsonConverter {
    public String toJson(boolean useNetworkSerializer) {
        if (useNetworkSerializer) return Utils.NETWORK_SERIALIZER.toJson(this);
        return Utils.LOCAL_SERIALIZER.toJson(this);
    }
}
