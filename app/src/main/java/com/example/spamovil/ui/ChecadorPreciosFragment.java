package com.example.spamovil.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spamovil.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.zip.Inflater;

public class ChecadorPreciosFragment extends Fragment {

    private FloatingActionButton fabFullScreen;

    public ChecadorPreciosFragment() {
        // Required empty public constructor
    }

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

            }
        });
    }
}
