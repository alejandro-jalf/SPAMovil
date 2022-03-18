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

import org.bson.json.JsonParseException;
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
        Log.d("activity_splash", Boolean.toString(controllerConfigs.thereAreRegister()));
        if (!controllerConfigs.thereAreRegister()) {
            messagesSplash.setText(R.string.splash_config);
            controllerConfigs.createConfig("TabMain", "index");
            controllerConfigs.createConfig("SesionActiva", "false");
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
                Log.d("activity_splash", "Entra a revisar sesion");
                messagesSplash.setText(R.string.splash_2);
                users = controllerUsers.getUser();
                url = configSystem.getURLUSERS() + "api/v1/usuarios/" + users.getCorreo_user();
                requestDataUser(url);
            }
        }
    }

    private void requestDataUser(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("activity_splash", "Respuesta de api" + response);
                messagesSplash.setText(R.string.splash_3);
                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                try {
                    jsonResponse = new JSONObject(response);
                    controllerUsers.changeDataUser(
                            jsonResponse.getString("correo_user"), jsonResponse.getString("nombre_user"), jsonResponse.getString("apellido_p_user"),
                            jsonResponse.getString("apellido_m_user"), jsonResponse.getString("direccion_user"), jsonResponse.getString("sucursal_user"),
                            jsonResponse.getString("tipo_user"), jsonResponse.getString("access_to_user"), jsonResponse.getBoolean("activo_user"),
                            jsonResponse.getString("principal")
                    );
                } catch (JSONException e) {
                    Toast.makeText(context, "Fallo al actualizar datos de usuario", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("activity_splash", "Respuesta de error " + error.getMessage());
                Log.d("activity_splash", "Respuesta de error 2 " + error.toString());
                Log.d("activity_splash", "Respuesta de error 3 " + error.networkResponse);
                Log.d("activity_splash", "Respuesta de error 4 " + error.getLocalizedMessage());
                Log.d("activity_splash", "Respuesta de error 5 " + error);
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
        this.finish();
        intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show();
    }
}