package com.julian.proyectoinmobiliaria;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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

        // oculta el teclado cuando ambos campos se rellenan
        android.text.TextWatcher watcher = new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String usuario = binding.etUsuario.getText().toString();
                String clave = binding.etPass.getText().toString();
                vm.checkFieldsAndHideKeyboard(usuario, clave, LoginActivity.this);
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        };
        binding.etUsuario.addTextChangedListener(watcher);
        binding.etPass.addTextChangedListener(watcher);

        // Configura el listener para el botón de inicio de sesión
        binding.btnIniciar.setOnClickListener(v -> {
            // Obtiene el texto ingresado por el usuario
            String usuario = binding.etUsuario.getText().toString();
            String clave = binding.etPass.getText().toString();
            // Llama al método login del ViewModel
            vm.login(usuario, clave);
        });
        // Observa el resultado del login para ir a DrawerActivity si es exitoso
        vm.handleLoginResult(this);

        // Migración: manejar retroceso con OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                vm.handleBackPressed(LoginActivity.this);
            }
        });
    }
}