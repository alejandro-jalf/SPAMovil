package com.example.spamovil.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.example.spamovil.controllers.ControllerUsers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LoginActivity extends AppCompatActivity {
    private ControllerUsers controllerUsers;
    private Config configSystem;
    private EditText user;
    private EditText password;
    private Button btnLogin;
    private CheckBox showPassword;
    private TextView forgotPassword;
    private String text_user;
    private String text_password;
    private Context context;
    private RequestQueue queue;
    private String url;
    private Intent intent;
    private JSONObject jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();

        initComponents();
        initInstances();
    }

    private void initComponents() {
        user = findViewById(R.id.login_user);
        password = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSesion();
            }
        });
        showPassword = findViewById(R.id.login_view_password);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean show) {
                if (show) password.setInputType(InputType.TYPE_CLASS_TEXT);
                else password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        forgotPassword = findViewById(R.id.login_recupera_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "No habilidato por el momento", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initInstances() {
        configSystem = new Config();
        queue = Volley.newRequestQueue(context);
        controllerUsers = new ControllerUsers(Realm.getDefaultInstance());
    }

    private void initSesion() {
        text_user = user.getText().toString().trim();
        text_password = password.getText().toString().trim();
        if (isValidDataForm(text_user, text_password)) {
            url = configSystem.getURLUSERS() + "api/v1/usuarios/" + text_user;
            loginUser(url);
        }
    }

    private boolean isValidDataForm(String user, String password) {
        if (user.equals("")) {
            Toast.makeText(context, "No ingreso usuario", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.equals("")) {
            Toast.makeText(context, "No ingreso contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loginUser(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("activity_login", "Respuesta de api: " + response);
                try {
                    jsonResponse = new JSONObject(response);
                    Toast.makeText(context, jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
                    if (jsonResponse.getBoolean("success")) {
                        /* controllerUsers.changeDataUser(
                                jsonResponse.getString("correo_user"), jsonResponse.getString("nombre_user"), jsonResponse.getString("apellido_p_user"),
                                jsonResponse.getString("apellido_m_user"), jsonResponse.getString("direccion_user"), jsonResponse.getString("sucursal_user"),
                                jsonResponse.getString("tipo_user"), jsonResponse.getString("access_to_user"), jsonResponse.getBoolean("activo_user"),
                                jsonResponse.getString("principal")
                        );*/
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Fallo al obtener datos de sesion", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Fallo al iniciar sesion", Toast.LENGTH_LONG).show();
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

    private void startMain() {
        intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
