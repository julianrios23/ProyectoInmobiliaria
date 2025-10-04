package com.julian.proyectoinmobiliaria.draweractivity.ui.logout;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LogoutViewModel extends AndroidViewModel {
    // aqui uso un livedata para notificar cuando se completa el logout
    private MutableLiveData<Boolean> logoutLiveData = new MutableLiveData<>();
    // aqui uso un livedata para notificar cuando se debe navegar a login
    private MutableLiveData<Boolean> navigateToLoginLiveData = new MutableLiveData<>();
    // aqui uso un livedata para mostrar mensajes de logout
    private MutableLiveData<String> logoutMessageLiveData = new MutableLiveData<>();
    // aqui uso un livedata para manejar la confirmacion del logout
    private MutableLiveData<Boolean> confirmLogoutLiveData = new MutableLiveData<>();
    // LiveData para navegar a la vista de inicio (mapa)
    private MutableLiveData<Boolean> navigateToInicioLiveData = new MutableLiveData<>();

    public LogoutViewModel(@NonNull Application application) {
        super(application);
    }

    // aqui expongo el livedata para que el fragment lo observe
    public MutableLiveData<Boolean> getLogoutLiveData() {
        return logoutLiveData;
    }
    // aqui expongo el livedata para navegar a login
    public MutableLiveData<Boolean> getNavigateToLoginLiveData() {
        return navigateToLoginLiveData;
    }
    // aqui expongo el livedata para el mensaje de logout
    public MutableLiveData<String> getLogoutMessageLiveData() {
        return logoutMessageLiveData;
    }
    // aqui expongo el livedata para la confirmacion del logout
    public MutableLiveData<Boolean> getConfirmLogoutLiveData() {
        return confirmLogoutLiveData;
    }
    // Exponer el LiveData para navegar a inicio
    public MutableLiveData<Boolean> getNavigateToInicioLiveData() {
        return navigateToInicioLiveData;
    }

    // Método para activar la navegación a inicio (mapa)
    public void onCancelLogout() {
        navigateToInicioLiveData.setValue(true);
    }

    // aqui implemento el metodo para manejar toda la logica de logout desde el viewmodel
    // aqui recibo el contexto y el binding desde el fragmento
    public void iniciarLogout(Context context, com.julian.proyectoinmobiliaria.databinding.FragmentLogoutBinding binding) {
        // aqui observo el livedata del mensaje y actualizo el textview
        getLogoutMessageLiveData().observeForever(mensaje -> {
            binding.tvLogout.setText(mensaje);
        });
        // aqui observo el livedata para navegar a login
        getNavigateToLoginLiveData().observeForever(navigate -> {
            if (navigate != null && navigate) {
                Intent intent = new Intent(context, com.julian.proyectoinmobiliaria.LoginActivity.class);
                context.startActivity(intent);
            }
        });
        // aquí activo el LiveData para mostrar el diálogo de confirmación
        getConfirmLogoutLiveData().setValue(true);
        // aqui limpio el mensaje
        logoutMessageLiveData.setValue("");
    }

    // aqui muestro el dialogo de logout desde el viewmodel
    public void mostrarDialogoLogout(Context context) {
        if (confirmLogoutLiveData.getValue() != null && confirmLogoutLiveData.getValue()) {
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Confirmar !!")
                    .setMessage("¿Deseas cerrar sesion?")
                    .setPositiveButton("Si", (dialog, which) -> {
                        // aqui solo cierro el dialogo y navego a login usando el livedata
                        navigateToLoginLiveData.setValue(true);
                        logoutMessageLiveData.setValue("sesion cerrada correctamente");
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                        onCancelLogout();
                    })
                    .show();
        }
    }

    // aqui navego a la pantalla de inicio desde el viewmodel
    public void navegarInicio(android.app.Activity activity) {
        if (navigateToInicioLiveData.getValue() != null && navigateToInicioLiveData.getValue()) {
            androidx.appcompat.app.AppCompatActivity compatActivity = (androidx.appcompat.app.AppCompatActivity) activity;
            compatActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(com.julian.proyectoinmobiliaria.R.id.nav_host_fragment_content_drawer, new com.julian.proyectoinmobiliaria.draweractivity.ui.inicio.HomeFragment())
                    .addToBackStack(null)
                    .commit();
            navigateToInicioLiveData.setValue(false);
        }
    }
}