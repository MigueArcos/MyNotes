package com.zeus.migue.notes.data.DTO.sync;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.util.List;

public class EntityChanges<T> {
    @SerializedName("ToAdd")
    @Expose
    private List<T> toAdd;
    @SerializedName("ToModify")
    @Expose
    private List<T> toModify;
    @SerializedName("ToDelete")
    @Expose
    private List<String> toDelete;

    public EntityChanges() {
    }

    public List<T> getToAdd() {
        return toAdd;
    }

    public void setToAdd(List<T> toAdd) {
        this.toAdd = toAdd;
    }

    public List<T> getToModify() {
        return toModify;
    }

    public void setToModify(List<T> toModify) {
        this.toModify = toModify;
    }

    public List<String> getToDelete() {
        return toDelete;
    }

    public void setToDelete(List<String> toDelete) {
        this.toDelete = toDelete;
    }

    public int getChildsSize(){
        return Utils.getSafeListSize(toAdd) + Utils.getSafeListSize(toModify) + Utils.getSafeListSize(toDelete);
    }
}
