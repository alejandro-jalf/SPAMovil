package com.example.spamovil.ui;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spamovil.R;
import static com.example.spamovil.Services.Instances.getControllerConfigs;
import com.example.spamovil.controllers.ControllerConfigs;
import com.example.spamovil.models.Articulos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class ChecadorPreciosFragment extends Fragment {

    private ControllerConfigs controllerConfigs;
    private FloatingActionButton fabFullScreen;
    private View decorView;
    private ActionBar actionBar;
    private Articulos articulos;
    private JSONObject response;
    private TextView nameArticle;
    private TextView precio1Article;
    private TextView amount2Article;
    private TextView precio2Article;
    private TextView amount3Article;
    private TextView precio3Article;
    private TextView descriptionArticle;
    private View line2Article;
    private View line3Article;
    private EditText keyBoardCode;
    private View layoutFragment;

    public ChecadorPreciosFragment(View decorView, ActionBar actionBar) {
        this.decorView = decorView;
        this.actionBar = actionBar;
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    actionBar.show();
                    fabFullScreen.setVisibility(View.VISIBLE);
                } else {
                    actionBar.hide();
                    fabFullScreen.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public ChecadorPreciosFragment() {}

    public static ChecadorPreciosFragment newInstance(String param1, String param2) {
        ChecadorPreciosFragment fragment = new ChecadorPreciosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checador_precios, container, false);
        initInstances();
        initComponents(view);

        return view;
    }

    private void initInstances() {
        controllerConfigs = getControllerConfigs();
        articulos = new Articulos(getContext());
    }

    private String barCode = "";
    private void initComponents(View view) {
        layoutFragment = view;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("fragment_checador", "click");
                keyBoardCode.requestFocus();
            }
        });
        keyBoardCode = view.findViewById(R.id.text_key_board_code);
        keyBoardCode.setInputType(InputType.TYPE_NULL);
        keyBoardCode.requestFocus();
        keyBoardCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(
                        i == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                ) {
                    Log.d("fragment_checador", "enter");
                    getPriceArticle(barCode);
                    barCode = "";
                    keyBoardCode.setText("");
                    return true;

                }
                return false;
            }
        });
        keyBoardCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //int keyCode = Integer.parseInt(charSequence.toString());
                Log.d("fragment_checador_lis", charSequence.toString());
                barCode = charSequence.toString();
                /*if (keyCode >= 7 && keyCode <= 16)
                    barCode += String.valueOf(keyEvent.getNumber());
                else if (keyCode == 66) {
                    getPriceArticle(barCode);
                    barCode = "";
                }*/
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        nameArticle = view.findViewById(R.id.fcp_name);
        descriptionArticle = view.findViewById(R.id.fcp_descripcion);
        precio1Article = view.findViewById(R.id.fcp_precio_1);
        precio2Article = view.findViewById(R.id.fcp_precio_2);
        precio3Article = view.findViewById(R.id.fcp_precio_3);
        amount2Article = view.findViewById(R.id.fcp_cantidad_precio_2);
        amount3Article = view.findViewById(R.id.fcp_cantidad_precio_3);
        line2Article = view.findViewById(R.id.fcp_divisor_2);
        line3Article = view.findViewById(R.id.fcp_divisor_3);
        fabFullScreen = view.findViewById(R.id.fcp_fullscreen);
        fabFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullScreen();
            }
        });
    }

    private boolean isValue(JSONObject data, String value) {
        try {
            data.getString(value);
            return true;
        } catch (JSONException je) {
            return false;
        }
    }

    public void fullScreen() {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        actionBar.hide();
    }

    public void getPriceArticle(String barCode) {
        String sucursal = "ZR";
        try {
            sucursal = controllerConfigs.getConfig("Sucursal").getValue();
            keyBoardCode.requestFocus();
        } catch (Exception e) {
            Toast.makeText(getContext(), "No se pudo obtener la sucursal", Toast.LENGTH_SHORT).show();
            keyBoardCode.requestFocus();
        }
        response = articulos.getPrecioArticulo(sucursal, barCode);
        if (response != null) {
            try {
                if (isValue(response, "Nombre"))
                    nameArticle.setText(response.getString("Nombre"));
                else nameArticle.setText(R.string.checador_articulo);
                if (isValue(response, "Precio1IVAUV"))
                    precio1Article.setText("A solo: " + response.getString("Precio1IVAUV"));
                else precio1Article.setText(R.string.checador_precio);
                if (isValue(response, "Precio2IVAUV") && isValue(response, "CantidadParaPrecio2")) {
                    amount2Article.setText("A partir de " + response.getString("CantidadParaPrecio2") + " pz: ");
                    precio2Article.setText("A solo: " + response.getString("Precio2IVAUV"));
                    amount2Article.setVisibility(View.VISIBLE);
                    precio2Article.setVisibility(View.VISIBLE);
                    line2Article.setVisibility(View.VISIBLE);
                } else {
                    amount2Article.setText("A partir de 1000 pz: ");
                    precio2Article.setText(R.string.checador_precio);
                    amount2Article.setVisibility(View.GONE);
                    precio2Article.setVisibility(View.GONE);
                    line2Article.setVisibility(View.GONE);
                }
                if (isValue(response, "Precio3IVAUV") && isValue(response, "CantidadParaPrecio3")) {
                    amount3Article.setText("A partir de " + response.getString("CantidadParaPrecio3") + " pz: ");
                    precio3Article.setText("A solo: " + response.getString("Precio3IVAUV"));
                    amount3Article.setVisibility(View.VISIBLE);
                    precio3Article.setVisibility(View.VISIBLE);
                    line3Article.setVisibility(View.VISIBLE);
                } else {
                    amount3Article.setText("A partir de 1000 pz: ");
                    precio3Article.setText(R.string.checador_precio);
                    amount3Article.setVisibility(View.GONE);
                    precio3Article.setVisibility(View.GONE);
                    line3Article.setVisibility(View.GONE);
                }
                if (isValue(response, "Descripcion"))
                    descriptionArticle.setText(response.getString("Descripcion"));
                else descriptionArticle.setText(R.string.checador_articulo);
                layoutFragment.performClick();
            } catch (Exception e) {
                layoutFragment.performClick();
                e.printStackTrace();
            }
            layoutFragment.performClick();
        }
    }
}
