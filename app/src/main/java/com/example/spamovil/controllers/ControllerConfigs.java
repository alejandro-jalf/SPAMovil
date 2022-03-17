package com.example.spamovil.controllers;

import com.example.spamovil.models.Configs;

import io.realm.Realm;
import io.realm.RealmResults;

public class ControllerConfigs {
    private Realm realm;
    private Configs configs;
    private RealmResults <Configs> resultsConfigs;

    public ControllerConfigs(Realm realm) {
        this.realm = realm;
    }

    public Configs getConfig(String name) {
        return realm.where(Configs.class).equalTo("name", name).findFirst();
    }

    public void changeConfig(String name, String value) {
        configs = getConfig(name);
        realm.beginTransaction();
        configs.setValue(value);
        realm.commitTransaction();
        configs = null;
    }

    public boolean thereAreRegister() {
        configs = realm.where(Configs.class).findFirst();
        return configs != null;
    }
}
