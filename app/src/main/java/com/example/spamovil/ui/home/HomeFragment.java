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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spamovil.R;
import com.example.spamovil.controllers.ControllerConfigs;
import com.example.spamovil.controllers.ControllerUsers;
import com.example.spamovil.databinding.FragmentHomeBinding;
import com.example.spamovil.models.Configs;
import com.example.spamovil.models.Users;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.spamovil.Services.Instances.getControllerConfigs;
import static com.example.spamovil.Services.Instances.getControllerUsers;

public class HomeFragment extends Fragment {

    private ControllerUsers controllerUsers;
    private ControllerConfigs controllerConfigs;
    private Users user;
    private Configs configs;
    private FragmentHomeBinding binding;
    private SwitchCompat switchSession;
    private TextView textName, textEmail, textAddress, textSucursal, textMessagePassword, textMessagePasswordRepeat, textMessagePasswordOld;
    private EditText inputNewPassword, inputNewPasswordRepeat, inputPasswordOld;
    private Button btnChangePassword;
    private ImageButton btnHelpTab;
    private Pattern expresionNumber = Pattern.compile("\\d+"), expresionLetter = Pattern.compile("[a-z]+|[A-Z]+");
    private Matcher matchNumber, matchLetter;
    private Boolean resultNumber, resultLetter;
    private String password, passwordRepeat, passwordOld;
    private AlertDialog alertDialogHelp;
    private AlertDialog.Builder builder;

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
        return root;
    }

    private void initInstances(){
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

    private void getConfigSession() {
        configs = controllerConfigs.getConfig("SesionActiva");
        if (configs != null)
            switchSession.setChecked(Boolean.parseBoolean(configs.getValue()));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}