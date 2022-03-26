package com.example.spamovil.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.spamovil.Configs.Config;
import com.example.spamovil.R;
import com.example.spamovil.controllers.ControllerConfigs;
import com.example.spamovil.controllers.ControllerUsers;
import com.example.spamovil.databinding.ActivityMainBinding;
import com.example.spamovil.databinding.FragmentHomeBinding;
import com.example.spamovil.models.Configs;
import com.example.spamovil.models.Users;
import com.google.android.material.navigation.NavigationView;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.spamovil.Services.Instances.getConfigs;
import static com.example.spamovil.Services.Instances.getControllerConfigs;
import static com.example.spamovil.Services.Instances.getControllerUsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private ControllerUsers controllerUsers;
    private ControllerConfigs controllerConfigs;
    private Config configSystem;
    private Users user;
    private Configs configs;
    private FragmentHomeBinding binding;
    private SwitchCompat switchSession;
    private TextView textName, textEmail, textAddress, textSucursal, textMessagePassword, textMessagePasswordRepeat, textMessagePasswordOld;
    private EditText inputNewPassword, inputNewPasswordRepeat, inputPasswordOld;
    private TextView textSucursalDL;
    private TextView textHost;
    private TextView textDataBase;
    private ArrayList<RadioButton> listOptions = new ArrayList<>();
    private RadioButton radioInicio;
    private RadioButton radioChecadorPrecios;
    private RadioButton radioCodificador;
    private Button btnChangePassword;
    private Button btnChangeDataLocal;
    private ImageButton btnHelpTab;
    private final Pattern expresionNumber = Pattern.compile("\\d+");
    private final Pattern expresionLetter = Pattern.compile("[a-z]+|[A-Z]+");
    private Matcher matchNumber, matchLetter;
    private Boolean resultNumber, resultLetter;
    private String password, passwordRepeat, passwordOld;
    private AlertDialog alertDialogHelp;
    private AlertDialog.Builder builder;
    private View loading;

    private RequestQueue queue;
    private AlertDialog alertDialogChangeDataLocal;
    private AlertDialog.Builder builderChangeDataLocal;
    private View viewChangeDataLocal;
    private EditText ChangeDataLocalSucursal;
    private EditText ChangeDataLocalHost;
    private EditText ChangeDataLocalDataBase;
    private EditText ChangeDataLocalUser;
    private EditText ChangeDataPassword;
    private TextView ChangeDatatextBtnCancel;
    private TextView ChangeDatatextBtnAccept;
    private String ChangeDataLocalSucursalText;
    private String ChangeDataLocalHostText;
    private String ChangeDataLocalDataBaseText;
    private String ChangeDataLocalUserText;
    private String ChangeDataPasswordText;
    private String stringData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initInstances();
        initAlerDialogHelp();
        initComponents(root);
        setDataPerfil();
        getConfigSession();
        getConfigTabMain();
        setDataLocal();
        initAlertChangeDataLocal();
        return root;
    }

    private void initInstances(){
        queue = Volley.newRequestQueue(getContext());
        configSystem = getConfigs();
        controllerUsers = getControllerUsers();
        controllerConfigs = getControllerConfigs();
    }

    private void initComponents(View view) {
        textName = view.findViewById(R.id.fh_perfil_name_label);
        textEmail = view.findViewById(R.id.fh_pefil_email_label);
        textAddress = view.findViewById(R.id.fh_perfil_label_address);
        textSucursal = view.findViewById(R.id.fh_perfil_label_sucursal);
        textMessagePassword = view.findViewById(R.id.fh_avanzado_message_new_passsword);
        textMessagePasswordRepeat = view.findViewById(R.id.fh_avanzado_message_new_passsword_repeat);
        textMessagePasswordOld = view.findViewById(R.id.fh_avanzado_message_old_password);
        loading = view.findViewById(R.id.fh_loading);

        textSucursalDL = view.findViewById(R.id.fh_admin_sucursal_label);
        textHost = view.findViewById(R.id.fh_admin_host_label);
        textDataBase = view.findViewById(R.id.fh_admin_database_label);
        btnChangeDataLocal = view.findViewById(R.id.fh_admin_database_btn);
        btnChangeDataLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogChangeDataLocal.show();
            }
        });

        switchSession = view.findViewById(R.id.fh_avanzado_switch_sesion);
        switchSession.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean session) {
                controllerConfigs.changeConfig("SesionActiva", Boolean.toString(session));
            }
        });

        inputNewPassword = view.findViewById(R.id.fh_avanzado_text_password_new);
        inputNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int star, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int star, int count, int after) {
                validatePasswordNew(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        inputNewPasswordRepeat = view.findViewById(R.id.fh_avanzado_text_password_new_repeat);
        inputNewPasswordRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isEqualsPasswordRepeat(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        inputPasswordOld = view.findViewById(R.id.fh_avanzado_text_password_old);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validataDataPassword();
            }
        });
        btnHelpTab = view.findViewById(R.id.fh_avanzado_tab_btn_help);
        btnHelpTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogHelp.show();
            }
        });
        radioInicio = view.findViewById(R.id.fh_avanzado_tab_opcion_home);
        addTabRadio(radioInicio);
        radioCodificador = view.findViewById(R.id.fh_avanzado_tab_opcion_codificador);
        addTabRadio(radioCodificador);
        radioChecadorPrecios = view.findViewById(R.id.fh_avanzado_tab_opcion_checador);
        addTabRadio(radioChecadorPrecios);
    }

    private void validatePasswordNew(String password) {
        password = password.trim();
        matchNumber = expresionNumber.matcher(password);
        matchLetter = expresionLetter.matcher(password);
        resultNumber = matchNumber.find();
        resultLetter = matchLetter.find();
        if (password.equals("")) textMessagePassword.setText("Nueva contraseña no puede ser vacia");
        else if (!resultLetter)
            textMessagePassword.setText("La nueva contraseña debe contener por lo menos una letra");
        else if (!resultNumber)
            textMessagePassword.setText("La nueva contraseña debe contener por lo menos un numero");
        else if (password.length() <= 6)
            textMessagePassword.setText("Nueva contraseña debe ser mayor de 6 caracteres");
        else textMessagePassword.setText("");
    }

    private void isEqualsPasswordRepeat(String passwordRepeat) {
        password = inputNewPassword.getText().toString();
        if (!password.trim().equals(passwordRepeat.trim()))
            textMessagePasswordRepeat.setText("Las contraseñas no coinciden");
        else textMessagePasswordRepeat.setText("");
    }

    private void validataDataPassword() {
        password = inputNewPassword.getText().toString().trim();
        passwordRepeat = inputNewPasswordRepeat.getText().toString().trim();
        passwordOld = inputPasswordOld.getText().toString().trim();
        matchNumber = expresionNumber.matcher(password);
        matchLetter = expresionLetter.matcher(password);
        resultNumber = matchNumber.find();
        resultLetter = matchLetter.find();
        if (password.equals(""))
            Toast.makeText(getContext(), "Contraseña nueva no puede quedar vacia", Toast.LENGTH_LONG).show();
        else if (passwordRepeat.equals(""))
            Toast.makeText(getContext(), "Campo repetir contraseña no puede quedar vacio", Toast.LENGTH_LONG).show();
        else if (passwordOld.equals(""))
            Toast.makeText(getContext(), "Contraseña actual no puede quedar vacia", Toast.LENGTH_LONG).show();
        else if (!resultLetter)
            Toast.makeText(getContext(), "La nueva contraseña debe contener por lo menos una letra", Toast.LENGTH_LONG).show();
        else if (!resultNumber)
            Toast.makeText(getContext(), "La nueva contraseña debe contener por lo menos un numero", Toast.LENGTH_LONG).show();
        else if (password.length() <= 6)
            Toast.makeText(getContext(), "Nueva contraseña debe ser mayor de 6 caracteres", Toast.LENGTH_LONG).show();
        else if (!password.trim().equals(passwordRepeat.trim()))
            Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
        else Toast.makeText(getContext(), "Evento activado", Toast.LENGTH_LONG).show();
    }

    private void setDataPerfil() {
        user = controllerUsers.getUserLogin();
        if (user != null) {
            textName.setText(user.getNombre_user());
            textEmail.setText(user.getCorreo_user());
            textAddress.setText(user.getDireccion_user());
            textSucursal.setText(user.getSucursal_user());
        }
    }

    private void setDataLocal() {
        configs = controllerConfigs.getConfig("Sucursal");
        if (configs != null)
            textSucursalDL.setText(configs.getValue());
        configs = controllerConfigs.getConfig("Host");
        if (configs != null)
            textHost.setText(configs.getValue());
        configs = controllerConfigs.getConfig("DataBase");
        if (configs != null)
            textDataBase.setText(configs.getValue());
    }

    private void getConfigSession() {
        configs = controllerConfigs.getConfig("SesionActiva");
        if (configs != null)
            switchSession.setChecked(Boolean.parseBoolean(configs.getValue()));
    }

    private void getConfigTabMain() {
        configs = controllerConfigs.getConfig("TabMain");
        RadioButton radioTab;
        for (int index = 0; index < listOptions.size(); index++) {
            radioTab = listOptions.get(index);
            Log.d("activity_home", radioTab.getText().toString());
            if (radioTab.getText().toString().equals(configs.getValue()))
                radioTab.setChecked(true);
        }
    }

    private void addTabRadio(RadioButton tab) {
        String nameOption = tab.getText().toString();
        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controllerConfigs.changeConfig("TabMain", nameOption);
            }
        });
        listOptions.add(tab);
    }

    private void initAlerDialogHelp() {
        builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.fhome_avanzado_txt_tab_help);
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialogHelp.dismiss();
            }
        });
        alertDialogHelp = builder.create();
        alertDialogHelp = builder.create();
    }

    private void initAlertChangeDataLocal() {
        viewChangeDataLocal = getLayoutInflater().inflate(R.layout.change_data_local, null);
        builderChangeDataLocal = new AlertDialog.Builder(getContext());
        builderChangeDataLocal.setView(viewChangeDataLocal);
        alertDialogChangeDataLocal = builderChangeDataLocal.create();

        ChangeDataLocalSucursal = viewChangeDataLocal.findViewById(R.id.text_sucursal_change);
        ChangeDataLocalHost = viewChangeDataLocal.findViewById(R.id.text_host_change);
        ChangeDataLocalDataBase = viewChangeDataLocal.findViewById(R.id.text_database_change);
        ChangeDataLocalUser = viewChangeDataLocal.findViewById(R.id.fh_text_user_change);
        ChangeDataPassword = viewChangeDataLocal.findViewById(R.id.fh_text_password_change);
        ChangeDatatextBtnAccept = viewChangeDataLocal.findViewById(R.id.fh_accept_change);
        ChangeDatatextBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = configSystem.getURLUSERS() + "api/v1/usuarios/" + ChangeDataLocalUserText;
                changeDataLocal(url);
            }
        });
        ChangeDatatextBtnCancel = viewChangeDataLocal.findViewById(R.id.fh_cancel_change);
        ChangeDatatextBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogChangeDataLocal.dismiss();
            }
        });
    }

    private void changeDataLocal(String url) {
        if (validateDatachangeData()) {
            loading.setVisibility(View.VISIBLE);
            JSONObject data = new JSONObject();
            try {
                data.put("password_user", ChangeDataPasswordText);
                stringData = data.toString();
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Error al cargar el payload", Toast.LENGTH_LONG).show();
                loading.setVisibility(View.INVISIBLE);
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    loading.setVisibility(View.INVISIBLE);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray dataArray = jsonResponse.getJSONArray("data");
                        JSONObject data = dataArray.getJSONObject(0);
                        if (!data.getString("tipo_user").equals("manager")) {
                            Toast.makeText(getContext(), "No cuenta con permisos de administrador", Toast.LENGTH_LONG).show();
                        } else if (!data.getBoolean("activo_user")) {
                            Toast.makeText(getContext(), "Su cuenta ha sido suspendida, comunicate con el administrador", Toast.LENGTH_LONG).show();
                        } else {
                            configs = controllerConfigs.getConfig("Sucursal");
                            configs.setValue(ChangeDataLocalSucursalText);
                            configs = controllerConfigs.getConfig("Host");
                            configs.setValue(ChangeDataLocalHostText);
                            configs = controllerConfigs.getConfig("DataBase");
                            configs.setValue(ChangeDataLocalDataBaseText);
                            alertDialogChangeDataLocal.dismiss();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error al intentar actualizar datos locales", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.setVisibility(View.INVISIBLE);
                    try {
                        JSONObject response = new JSONObject(error.getMessage());
                        if (!response.getBoolean("success"))
                            Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Fallo al iniciar sesion", Toast.LENGTH_SHORT).show();
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
    }

    private boolean validateDatachangeData() {
        ChangeDataLocalSucursalText = ChangeDataLocalSucursal.getText().toString().trim();
        ChangeDataLocalHostText = ChangeDataLocalHost.getText().toString().trim();
        ChangeDataLocalDataBaseText = ChangeDataLocalDataBase.getText().toString().trim();
        ChangeDataLocalUserText = ChangeDataLocalUser.getText().toString().trim();
        ChangeDataPasswordText = ChangeDataPassword.getText().toString().trim();
        if (ChangeDataLocalSucursalText.equals("")) {
            Toast.makeText(getContext(), "Sucursal no puede quedar vacio", Toast.LENGTH_LONG).show();
            return false;
        }
        if (ChangeDataLocalHostText.equals("")) {
            Toast.makeText(getContext(), "Host no puede quedar vacio", Toast.LENGTH_LONG).show();
            return false;
        }
        if (ChangeDataLocalDataBaseText.equals("")) {
            Toast.makeText(getContext(), "Base de datos no puede quedar vacia", Toast.LENGTH_LONG).show();
            return false;
        }
        if (ChangeDataLocalUserText.equals("")) {
            Toast.makeText(getContext(), "Usuario no puede quedar vacio", Toast.LENGTH_LONG).show();
            return false;
        }
        if (ChangeDataPasswordText.equals("")) {
            Toast.makeText(getContext(), "Contraseña no puede quedar vacia", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}