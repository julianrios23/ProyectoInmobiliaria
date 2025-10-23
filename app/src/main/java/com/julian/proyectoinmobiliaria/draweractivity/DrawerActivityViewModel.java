package com.julian.proyectoinmobiliaria.draweractivity;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

public class DrawerActivityViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> mostrarLogout = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> cerrarApp = new MutableLiveData<>(false);
    private final MutableLiveData<String> nombreApellido = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");

    public DrawerActivityViewModel(android.app.Application application) {
        super(application);
    }

    // este metodo lo llamo para actualizar el nombre y apellido
    public void actualizarNombreApellido(String nombre, String apellido) {
        if (nombre == null) nombre = "";
        if (apellido == null) apellido = "";
        nombreApellido.postValue(nombre + " " + apellido);
    }

    // este metodo lo llamo para actualizar el email
    public void actualizarEmail(String mail) {
        if (mail == null) mail = "";
        email.postValue(mail);
    }

    // este metodo lo llamo desde el activity para observar el nombre y apellido
    public LiveData<String> getNombreApellido() {
        return nombreApellido;
    }

    // este metodo lo llamo desde el activity para observar el email
    public LiveData<String> getEmail() {
        return email;
    }

    // este metodo verifica si debe mostrar el fragmento de logout al presionar atrás
    public void manejarBackPressed(androidx.fragment.app.FragmentActivity activity) {
        if (activity != null) {
            androidx.fragment.app.Fragment currentFragment = activity.getSupportFragmentManager().findFragmentById(com.julian.proyectoinmobiliaria.R.id.nav_host_fragment_content_drawer);
            if (!(currentFragment instanceof com.julian.proyectoinmobiliaria.draweractivity.ui.logout.LogoutFragment)) {
                mostrarLogout.setValue(true);
            }
            // Nunca cerrar la app ni minimizar
        }
    }

    // este metodo lo llamo desde el activity para saber si debo mostrar el fragmento de logout
    public LiveData<Boolean> getMostrarLogout() {
        return mostrarLogout;
    }

    // este metodo lo llamo desde el activity para saber si debo cerrar la app
    public LiveData<Boolean> getCerrarApp() {
        return cerrarApp;
    }

    // este metodo lo llamo para resetear los flags despues de usarlos
    public void resetFlags() {
        mostrarLogout.setValue(false);
        cerrarApp.setValue(false);
    }

    // este metodo maneja la navegacion del menu
    public void manejarMenu(android.view.MenuItem item, androidx.fragment.app.FragmentActivity activity, androidx.drawerlayout.widget.DrawerLayout drawerLayout) {
        // Desmarcar todos los ítems antes de marcar el nuevo
        com.google.android.material.navigation.NavigationView navigationView = activity.findViewById(com.julian.proyectoinmobiliaria.R.id.nav_view);
        if (navigationView != null) {
            android.view.Menu menu = navigationView.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setChecked(false);
            }
        }
        item.setChecked(true);
        int id = item.getItemId();
        if (id == com.julian.proyectoinmobiliaria.R.id.nav_logout) {
            activity.getSupportFragmentManager().beginTransaction()
                .replace(com.julian.proyectoinmobiliaria.R.id.nav_host_fragment_content_drawer, new com.julian.proyectoinmobiliaria.draweractivity.ui.logout.LogoutFragment())
                .addToBackStack(null)
                .commit();
            drawerLayout.closeDrawers();
        } else {
            androidx.navigation.NavController navController = androidx.navigation.Navigation.findNavController(activity, com.julian.proyectoinmobiliaria.R.id.nav_host_fragment_content_drawer);
            boolean handled = androidx.navigation.ui.NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                drawerLayout.closeDrawers();
            }
        }
    }

    // este metodo ejecuta la accion de mostrar logout
    public void ejecutarAccionLogout(androidx.fragment.app.FragmentActivity activity, Boolean mostrar) {
        if (Boolean.TRUE.equals(mostrar)) {
            activity.getSupportFragmentManager().beginTransaction()
                .replace(com.julian.proyectoinmobiliaria.R.id.nav_host_fragment_content_drawer, new com.julian.proyectoinmobiliaria.draweractivity.ui.logout.LogoutFragment())
                .addToBackStack(null)
                .commit();
            resetFlags();
        }
    }

    // este metodo ejecuta la accion de cerrar la app
    public void ejecutarAccionCerrar(androidx.fragment.app.FragmentActivity activity, Boolean cerrar) {
        if (Boolean.TRUE.equals(cerrar)) {
            activity.finish();
            resetFlags();
        }
    }
}
