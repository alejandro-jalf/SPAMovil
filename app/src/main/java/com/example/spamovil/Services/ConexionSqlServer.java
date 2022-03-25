package com.example.spamovil.Services;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.example.spamovil.Configs.Config;
import com.example.spamovil.controllers.ControllerConfigs;
import com.example.spamovil.models.Configs;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionSqlServer {
    private Connection connection;
    private Context context;
    private Config config;
    private Configs configs;
    private String host;
    private String dataBase;
    private ControllerConfigs controllerConfigs;

    public ConexionSqlServer(Context context, Config config, ControllerConfigs controllerConfigs) {
        this.controllerConfigs = controllerConfigs;
        this.connection = null;
        this.config = config;
        this.context = context;
    }


    private String getHostLocal() {
        configs = controllerConfigs.getConfig("Host");
        if (configs == null) {
            Toast.makeText(context, "No se ha declarado el host destino", Toast.LENGTH_LONG).show();
            return null;
        }
        return configs.getValue();
    }

    private String getDataBase() {
        configs = controllerConfigs.getConfig("DataBase");
        if (configs == null) {
            Toast.makeText(context, "No se ha declarado la base de datos destino", Toast.LENGTH_LONG).show();
            return null;
        }
        return configs.getValue();
    }

    public Connection getConnection() {
        host = getHostLocal();
        dataBase = getDataBase();
        if (host != null && dataBase != null) {
            try {
                StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(threadPolicy);
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(""
                        + "jdbc:jtds:sqlserver://" + host + ";databaseName=" + dataBase
                        + ";user=" + config.getUSERDATABASE() + ";password=" + config.getPASSWORDDATABASE() + ";");
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return connection;
    }
}
