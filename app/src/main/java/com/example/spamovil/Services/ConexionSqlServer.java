package com.example.spamovil.Services;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.example.spamovil.Configs.Config;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionSqlServer {
    private Connection connection;
    private Context context;
    private Config config;

    public ConexionSqlServer(Context context, Config config) {
        this.connection = null;
        this.config = config;
        this.context = context;
    }

    public Connection getConnection() {
        try {
            StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(threadPolicy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(""
                    //+ "jdbc:jtds:sqlserver://" + config.getHOST() + "/northwind;instance=SQL2008;"
                    + "jdbc:jtds:sqlserver://" + config.getHOST() + ";databaseName=" + config.getDATABASE()
                    + ";user=" + config.getUSERDATABASE() + ";password=" + config.getPASSWORDDATABASE() + ";");
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return connection;
    }
}
