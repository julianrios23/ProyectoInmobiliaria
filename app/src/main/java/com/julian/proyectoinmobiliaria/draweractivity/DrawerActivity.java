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
import androidx.lifecycle.ViewModelProvider;
import com.julian.proyectoinmobiliaria.draweractivity.ui.perfil.PerfilViewModel;

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
        // Obtenemos los TextView del header para mostrar nombre y email
        android.widget.TextView tvNombreApellido = headerView.findViewById(R.id.tvNombreApellido);
        android.widget.TextView tvMail2 = headerView.findViewById(R.id.tvMail2);

        // Instanciamos el ViewModel para obtener los datos del usuario
        PerfilViewModel perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        // Cargamos el perfil del usuario desde la API
        perfilViewModel.cargarPerfil();
        // Observamos los datos del ViewModel y actualizamos los TextView
        perfilViewModel.getNombre().observe(this, nombre -> {
            String apellido = perfilViewModel.getApellido().getValue();
            if (apellido == null) apellido = "";
            tvNombreApellido.setText(nombre + " " + apellido); // Muestra el nombre completo
        });
        perfilViewModel.getApellido().observe(this, apellido -> {
            String nombre = perfilViewModel.getNombre().getValue();
            if (nombre == null) nombre = "";
            tvNombreApellido.setText(nombre + " " + apellido); // Actualiza si cambia el apellido
        });
        perfilViewModel.getEmail().observe(this, email -> {
            tvMail2.setText(email); // Muestra el email del usuario
        });
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
                // Navegar manualmente al fragment de logout
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
        // inicializo el viewmodel para delegar toda la logica
        DrawerActivityViewModel viewModel = new ViewModelProvider(this).get(DrawerActivityViewModel.class);
        // observo si debo mostrar el nombre y apellido
        viewModel.getNombreApellido().observe(this, nombreApellido -> {
            tvNombreApellido.setText(nombreApellido);
        });
        // observo si debo mostrar el email
        viewModel.getEmail().observe(this, email -> {
            tvMail2.setText(email);
        });
        // observo si debo mostrar el fragment de logout
        viewModel.getMostrarLogout().observe(this, mostrar -> {
            viewModel.ejecutarAccionLogout(this, mostrar);
        });
        // observo si debo cerrar la app
        viewModel.getCerrarApp().observe(this, cerrar -> {
            viewModel.ejecutarAccionCerrar(this, cerrar);
        });
        // delego el manejo del back al viewmodel
        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.manejarBackPressed(DrawerActivity.this);
            }
        });
        // delego la navegacion del menu al viewmodel
        navigationView.setNavigationItemSelectedListener(item -> {
            viewModel.manejarMenu(item, DrawerActivity.this, binding.drawerLayout);
            return true;
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

    @Override
    protected void onResume() {
        super.onResume();
        // Actualiza los datos del usuario cada vez que se muestra DrawerActivity
        PerfilViewModel perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        perfilViewModel.cargarPerfil();
    }
}