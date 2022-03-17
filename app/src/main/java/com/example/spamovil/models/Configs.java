package com.example.spamovil.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Configs extends RealmObject {
    @PrimaryKey
    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
