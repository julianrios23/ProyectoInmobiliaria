package com.julian.proyectoinmobiliaria.draweractivity;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.databinding.ActivityDrawerBinding;

public class DrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDrawerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDrawer.toolbar);
        binding.appBarDrawer.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(binding.appBarDrawer.fab)
                        .show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);
        android.widget.TextView emailTextView = headerView.findViewById(R.id.textView);
        android.content.SharedPreferences prefs = getSharedPreferences("token_prefs", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        emailTextView.setText(email);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                binding.navView.getMenu().getItem(0).getItemId())
                .setOpenableLayout(drawer)
                .build();
        // Usar el método tradicional con R para obtener el NavController, ya que binding no soporta fragments
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            android.util.Log.d("OUT", "Menu seleccionado: " + id);
            if (id == R.id.nav_logout) {
                android.util.Log.d("OUT", "Logout seleccionado");
                // Navegar manualmente al fragmento de logout
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_drawer, new com.julian.proyectoinmobiliaria.draweractivity.ui.logout.LogoutFragment())
                        .addToBackStack(null)
                        .commit();
                DrawerLayout drawer1 = binding.drawerLayout;
                drawer1.closeDrawers();
                return true;
            }
            // Dejar que NavigationUI maneje el resto
            NavController navController1 = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController1);
            if (handled) {
                DrawerLayout drawer1 = binding.drawerLayout;
                drawer1.closeDrawers();
            }
            return handled;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Restaurar el uso de R para inflar el menú
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}