package com.example.spamovil.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
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
import com.example.spamovil.models.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {
    private ControllerUsers controllerUsers;
    private Users users;
    private Config configSystem;
    private EditText user;
    private EditText password;
    private Button btnLogin;
    private CheckBox showPassword;
    private TextView forgotPassword;
    private View backgroundSpinner;
    private ProgressBar spinner;
    private String text_user;
    private String text_password;
    private Context context;
    private RequestQueue queue;
    private String url;
    private String stringData;
    private Intent intent;
    private JSONObject jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();

        initComponents();
        initInstances();
        setLogin(false);
    }

    private void initComponents() {
        backgroundSpinner = findViewById(R.id.login_background_spinner);
        spinner = findViewById(R.id.login_spinner);
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
        user.clearFocus();
        if (isValidDataForm(text_user, text_password)) {
            url = configSystem.getURLUSERS() + "api/v1/usuarios/" + text_user + "/login";
            loginUser(url, text_user, text_password);
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

    private void loginUser(String url, String user, String password) {
        setLogin(true);
        JSONObject data = new JSONObject();
        try {
            data.put("password_user", password);
            stringData = data.toString();
        } catch (JSONException e) {}
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonResponse = new JSONObject(response);
                    if (jsonResponse.getBoolean("success")) {
                        JSONObject jsonData = jsonResponse.getJSONObject("data");
                        users = controllerUsers.getUser(user);
                        if (users == null) {
                            controllerUsers.createUser(
                                jsonData.getString("correo_user"), jsonData.getString("nombre_user"), jsonData.getString("apellido_p_user"),
                                jsonData.getString("apellido_m_user"), jsonData.getString("direccion_user"), jsonData.getString("sucursal_user"),
                                jsonData.getString("tipo_user"), jsonData.getString("access_to_user"), jsonData.getBoolean("activo_user"),
                                jsonData.getString("principal")
                            );
                        } else {
                            Log.d("activity_login_user", users.toString());
                            controllerUsers.changeDataUser(
                                    jsonData.getString("correo_user"), jsonData.getString("nombre_user"), jsonData.getString("apellido_p_user"),
                                    jsonData.getString("apellido_m_user"), jsonData.getString("direccion_user"), jsonData.getString("sucursal_user"),
                                    jsonData.getString("tipo_user"), jsonData.getString("access_to_user"), jsonData.getBoolean("activo_user"),
                                    jsonData.getString("principal"), true
                            );
                        }
                        Toast.makeText(context, jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
                        startMain();
                        setLogin(false);
                    }
                } catch (JSONException e) {
                    setLogin(false);
                    Toast.makeText(context, "Fallo al obtener datos de sesion", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    setLogin(false);
                    JSONObject response = new JSONObject(error.getMessage());
                    if (!response.getBoolean("success"))
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    setLogin(false);
                    Toast.makeText(context, "Fallo al iniciar sesion", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access-token", configSystem.getTOKENUSERS());
                params.put("header-spa-store", configSystem.getTOKENUSERS());
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return stringData.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    return super.parseNetworkError(new VolleyError(jsonError));
                }
                return super.parseNetworkError(volleyError);
            }
        };

        queue.add(stringRequest);
    }

    private void startMain() {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setLogin(boolean login) {
        if (login) {
            backgroundSpinner.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
        } else {
            backgroundSpinner.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.INVISIBLE);
        }
    }
}
