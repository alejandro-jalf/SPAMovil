package com.example.spamovil.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.spamovil.Configs.Config;
import com.example.spamovil.MainActivity;
import com.example.spamovil.R;
import com.example.spamovil.controllers.ControllerConfigs;
import com.example.spamovil.controllers.ControllerUsers;
import com.example.spamovil.models.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SplashActivity extends AppCompatActivity {
    private RealmConfiguration configuration;
    private ControllerUsers controllerUsers;
    private ControllerConfigs controllerConfigs;
    private Config configSystem;
    private RequestQueue queue;
    private TextView messagesSplash;
    private Intent intent;
    private Users users;
    private Context context;
    private String url;
    private JSONObject jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // messagesSplash = findViewById(R.id.splash_messages);

        context = getApplicationContext();

        initDatabase();
        initComponents();
        initInstances();
        loadConfigDefault();
        validaSesion();
    }

    private void initDatabase() {
        Realm.init(this);
        configuration = new RealmConfiguration.Builder()
                .name("spamovil")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    private void initInstances() {
        configSystem = new Config();
        queue = Volley.newRequestQueue(context);
        controllerUsers = new ControllerUsers(Realm.getDefaultInstance());
        controllerConfigs = new ControllerConfigs(Realm.getDefaultInstance());
    }
    private void initComponents() {
        messagesSplash = findViewById(R.id.splash_messages);
    }

    private void loadConfigDefault() {
        if (!controllerConfigs.thereAreRegister()) {
            messagesSplash.setText(R.string.splash_config);
            controllerConfigs.createConfig("TabMain", "Inicio");
            controllerConfigs.createConfig("SesionActiva", "false");
            controllerConfigs.createConfig("Sucursal", "ZR");
            controllerConfigs.createConfig("Host", "192.168.123.100");
            controllerConfigs.createConfig("DataBase", "SPASUPER1");
            messagesSplash.setText(R.string.splash_1);
        }
    }

    private void validaSesion() {
        Log.d("activity_splash", Boolean.toString(controllerUsers.thereAreRegisters()));
        if (!controllerUsers.thereAreRegisters()) startLogin();
        else {
            boolean sesionActiva = Boolean.parseBoolean(
                    controllerConfigs.getConfig("SesionActiva").getValue()
            );
            if (!sesionActiva) startLogin();
            else {
                messagesSplash.setText(R.string.splash_2);
                users = controllerUsers.getUserLogin();
                if (users == null) {
                    Toast.makeText(context, "No se ha encontrado alguna cuenta logueada", Toast.LENGTH_LONG).show();
                    startLogin();
                } else  {
                    url = configSystem.getURLUSERS() + "api/v1/usuarios/" + users.getCorreo_user();
                    requestDataUser(url);
                }
            }
        }
    }

    private void requestDataUser(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                messagesSplash.setText(R.string.splash_3);
                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                try {
                    jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");
                    JSONObject data = dataArray.getJSONObject(0);
                    controllerUsers.changeDataUser(
                            data.getString("correo_user"), data.getString("nombre_user"), data.getString("apellido_p_user"),
                            data.getString("apellido_m_user"), data.getString("direccion_user"), data.getString("sucursal_user"),
                            data.getString("tipo_user"), data.getString("access_to_user"), data.getBoolean("activo_user"),
                            data.getString("principal")
                    );
                    if (!data.getBoolean("activo_user")) {
                        Toast.makeText(context, "Su cuenta ha sido suspendida, comunicate con el administrador", Toast.LENGTH_LONG).show();
                        controllerUsers.setSessionInit(data.getString("correo_user"), false);
                        controllerConfigs.createConfig("SesionActiva", "false");
                        startLogin();
                    } else startMain();
                } catch (JSONException e) {
                    Toast.makeText(context, "Fallo al actualizar datos de usuario", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Fallas en la respuesta", Toast.LENGTH_LONG).show();
                startLogin();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access-token", configSystem.getTOKENUSERS());
                params.put("header-spa-store", configSystem.getTOKENUSERS());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void startLogin() {
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startMain() {
        Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show();
        intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }
}