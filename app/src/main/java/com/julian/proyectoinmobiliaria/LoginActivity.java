package com.julian.proyectoinmobiliaria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.julian.proyectoinmobiliaria.databinding.ActivityLoginBinding;
import com.julian.proyectoinmobiliaria.LoginActivityViewModel;
import com.julian.proyectoinmobiliaria.draweractivity.DrawerActivity;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginActivityViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicia el binding para acceder a las vistas
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtiene el ViewModel asociado a esta actividad
        vm = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        // Configura el listener para el botón de inicio de sesión
        binding.btnIniciar.setOnClickListener(v -> {
            // Obtiene el texto ingresado por el usuario
            String usuario = binding.etUsuario.getText().toString();
            String clave = binding.etPass.getText().toString();
            // Llama al método login del ViewModel
            vm.login(usuario, clave);
        });
        // Observa el resultado del login para navegar a DrawerActivity si es exitoso
        vm.handleLoginResult(this);

    }
}