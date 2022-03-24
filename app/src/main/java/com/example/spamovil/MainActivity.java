package com.example.spamovil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.spamovil.Services.Instances;
import com.example.spamovil.controllers.ControllerConfigs;
import com.example.spamovil.models.Configs;
import com.example.spamovil.models.Users;
import com.example.spamovil.ui.ChecadorPreciosFragment;
import com.example.spamovil.ui.home.HomeFragment;
import com.example.spamovil.ui.slideshow.SlideshowFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spamovil.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private Instances instances;
    private ControllerConfigs controllerConfigs;
    private Configs configs;
    private AppBarConfiguration mAppBarConfiguration;
    private ActionBar actionBar;
    private ActivityMainBinding binding;
    private int visita = 0;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_26);
        actionBar.setDisplayHomeAsUpEnabled(true);

        instances = new Instances(getApplicationContext());
        controllerConfigs = Instances.getControllerConfigs();

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_checador_precios, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();*/

        setupNavigationDrawerContent(navigationView);
        /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController); */
        setTabMain();
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        item.setChecked(true);
                        setFragment("Inicio");
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_checador_precios:
                        item.setChecked(true);
                        setFragment("Checador de Precios");
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_slideshow:
                        item.setChecked(true);
                        setFragment("Codificador de articulos");
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                }
                return true;
            }
        });
    }

    private void setFragment(String tab) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (tab) {
            case "Checador de Precios":
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                ChecadorPreciosFragment checadorPreciosFragment = new ChecadorPreciosFragment(getWindow().getDecorView(), getSupportActionBar());
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, checadorPreciosFragment);
                fragmentTransaction.commit();
                break;
            case "Codificador de articulos":
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                SlideshowFragment slideshowFragment = new SlideshowFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, slideshowFragment);
                fragmentTransaction.commit();
                break;
            default:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, homeFragment);
                fragmentTransaction.commit();
                break;
        }
    }

    private void setTabMain() {
        configs = controllerConfigs.getConfig("TabMain");
        setFragment(configs.getValue());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    } */

}