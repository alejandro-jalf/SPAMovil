package com.example.spamovil.ui;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spamovil.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.zip.Inflater;

public class ChecadorPreciosFragment extends Fragment {

    private FloatingActionButton fabFullScreen;
    private View decorView;
    private ActionBar actionBar;

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
        initComponents(view);

        return view;
    }

    private void initComponents(View view) {
        fabFullScreen = view.findViewById(R.id.fcp_fullscreen);
        fabFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullScreen();
            }
        });
    }

    public void fullScreen() {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        actionBar.hide();
    }
}
