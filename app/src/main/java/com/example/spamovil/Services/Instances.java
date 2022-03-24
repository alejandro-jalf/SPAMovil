package com.example.spamovil.Services;

import android.content.Context;

import com.example.spamovil.Configs.Config;
import com.example.spamovil.controllers.ControllerConfigs;
import com.example.spamovil.controllers.ControllerUsers;

import io.realm.Realm;

public class Instances {
    private static ControllerConfigs controllerConfigs = null;
    private static ControllerUsers controllerUsers = null;
    private static Config configs = null;
    private static Context context;
    private static ConexionSqlServer conexionSqlServer = null;

    public Instances(ControllerConfigs controllerConfigs, ControllerUsers controllerUsers, Config configs) {
        this.controllerConfigs = controllerConfigs;
        this.controllerUsers = controllerUsers;
        this.configs = configs;
    }

    public Instances() {}

    public Instances(Context context) {
        this.context = context;
    }

    public static ConexionSqlServer getConexionSqlServer() {
        if (conexionSqlServer == null)
            conexionSqlServer = new ConexionSqlServer(context, configs);
        return conexionSqlServer;
    }

    public static ControllerUsers getControllerUsers() {
        if (controllerUsers == null)
            controllerUsers = new ControllerUsers(Realm.getDefaultInstance());
        return controllerUsers;
    }

    public static ControllerConfigs getControllerConfigs() {
        if (controllerConfigs == null)
            controllerConfigs = new ControllerConfigs(Realm.getDefaultInstance());
        return controllerConfigs;
    }

    public static Config getConfigs() {
        if (configs == null)
            configs = new Config();
        return configs;
    }
}
