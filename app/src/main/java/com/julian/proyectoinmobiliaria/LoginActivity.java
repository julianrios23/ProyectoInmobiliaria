package com.julian.proyectoinmobiliaria;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.julian.proyectoinmobiliaria.databinding.ActivityLoginBinding;
import com.julian.proyectoinmobiliaria.LoginActivityViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginActivityViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicia el binding para acceder a las vistas
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.setContentView(binding.getRoot());
        // Obtiene el ViewModel asociado a esta actividad
        vm = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        // Inicializa el detector de shake para llamada
        vm.setCallPermissionChecker(this::checkAndRequestCallPermission);
        vm.setOnShakeCall(() -> {

            checkAndRequestCallPermission();
        });
        vm.initShakeDetector(this);

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
        // observer el resultado del login para ir a DrawerActivity si es exitoso
        vm.handleLoginResult(this);

        // Delegar la observación del permiso de llamada al ViewModel
        vm.handleCallPermissionResult(this);

        // manejar retroceso con OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                vm.handleBackPressed(LoginActivity.this);
            }
        });
    }

    private void checkAndRequestCallPermission() {
        vm.checkAndRequestCallPermission(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        vm.handleRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vm.releaseShakeDetector();
    }
}