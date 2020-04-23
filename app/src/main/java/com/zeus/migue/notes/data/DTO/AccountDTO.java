package com.zeus.migue.notes.data.DTO;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zeus.migue.notes.data.room.entities.Account;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.contracts.IFilterable;
import com.zeus.migue.notes.infrastructure.contracts.JsonConverter;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.util.Objects;

public class AccountDTO extends JsonConverter implements IEntityConverter<Account>, IFilterable {
    @ColumnInfo(name = "Id")
    private long id;

    @SerializedName("Name")
    @ColumnInfo(name = "Name")
    @Expose
    private String name;

    @SerializedName("Total")
    @ColumnInfo(name = "Total")
    @Expose
    private double total;

    @SerializedName("CreationDate")
    @ColumnInfo(name = "CreationDate")
    @Expose
    private String creationDate;

    @SerializedName("ModificationDate")
    @ColumnInfo(name = "ModificationDate")
    @Expose
    private String modificationDate;


    public AccountDTO() {

    }
    @Ignore
    public AccountDTO(String name, double total, String creationDate, String modificationDate) {
        this.name = name;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }


    @Override
    public Account toEntity() {
        return new Account(id, Utils.EMPTY_STRING, name, total, creationDate, modificationDate);
    }

    @Override
    public boolean passFilter(String filter) {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, total, creationDate, modificationDate);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof AccountDTO)) return false;
        AccountDTO source = (AccountDTO) obj;
        return source.id == id && Objects.equals(source.name, name) && source.total == total && Objects.equals(source.creationDate, creationDate) && Objects.equals(source.modificationDate, modificationDate);
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
