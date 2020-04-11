package com.zeus.migue.notes.data.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "Accounts")
public class Account extends BaseEntity {
    @ColumnInfo(name = "Name")
    private String name;
    @ColumnInfo(name = "Total")
    private double total;

    public Account() {
    }

    @Ignore
    public Account(long id, String remoteId, String name, double total, String creationDate, String modificationDate) {
        super(id, remoteId, creationDate, modificationDate, false);
        this.name = name;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}