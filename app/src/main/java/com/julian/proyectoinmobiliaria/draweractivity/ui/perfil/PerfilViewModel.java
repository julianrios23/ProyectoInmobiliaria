package com.julian.proyectoinmobiliaria.draweractivity.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.julian.proyectoinmobiliaria.model.Propietario;
import com.julian.proyectoinmobiliaria.service.ApiService;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// aqui defino el viewmodel gestiona los datos del perfil y la logica de edicion
public class PerfilViewModel extends AndroidViewModel {
    // aqui declaro los livedata para los campos del propietario y el estado de la vista
    private final MutableLiveData<Propietario> propietario = new MutableLiveData<>();
    private final MutableLiveData<String> nombre = new MutableLiveData<>("");
    private final MutableLiveData<String> apellido = new MutableLiveData<>("");
    private final MutableLiveData<String> dni = new MutableLiveData<>("");
    private final MutableLiveData<String> telefono = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> modoEdicion = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mostrarToast = new MutableLiveData<>(false);
    private final MutableLiveData<String> mensajeToast = new MutableLiveData<>("");

    // LiveData para errores de validación por campo
    private final MutableLiveData<String> errorNombre = new MutableLiveData<>(null);
    private final MutableLiveData<String> errorApellido = new MutableLiveData<>(null);
    private final MutableLiveData<String> errorDni = new MutableLiveData<>(null);
    // livedata para errores ya procesados (string vacio si no hay error)
    private final MutableLiveData<String> errorNombreFinal = new MutableLiveData<>("");
    private final MutableLiveData<String> errorApellidoFinal = new MutableLiveData<>("");
    private final MutableLiveData<String> errorDniFinal = new MutableLiveData<>("");
    // livedata para toast ya procesado (string vacio si no hay toast)
    private final MutableLiveData<String> toastFinal = new MutableLiveData<>("");

    private final ApiService.ServiceInterface apiService;

    // inicializo el viewmodel
    public PerfilViewModel(@NonNull Application application) {
        super(application);
        // Uso el cliente por defecto centralizado en ApiService
        OkHttpClient client = ApiService.getDefaultClient();
        apiService = ApiService.getApiService(client);
    }

    // aqui  los livedata para que el fragment observe
    public LiveData<Propietario> getPropietario() { return propietario; }
    public LiveData<String> getNombre() { return nombre; }
    public LiveData<String> getApellido() { return apellido; }
    public LiveData<String> getDni() { return dni; }
    public LiveData<String> getTelefono() { return telefono; }
    public LiveData<String> getEmail() { return email; }
    public LiveData<Boolean> getModoEdicion() { return modoEdicion; }
    public LiveData<Boolean> getMostrarToast() { return mostrarToast; }
    public LiveData<String> getMensajeToast() { return mensajeToast; }

    // getters para errores
    public LiveData<String> getErrorNombre() { return errorNombre; }
    public LiveData<String> getErrorApellido() { return errorApellido; }
    public LiveData<String> getErrorDni() { return errorDni; }
    public LiveData<String> getErrorNombreFinal() { return errorNombreFinal; }
    public LiveData<String> getErrorApellidoFinal() { return errorApellidoFinal; }
    public LiveData<String> getErrorDniFinal() { return errorDniFinal; }
    public LiveData<String> getToastFinal() { return toastFinal; }

    // cambio el modo edicion econ booleano
    public void toggleModoEdicion() {
        Boolean actual = modoEdicion.getValue();
        if (actual == null) actual = false;
        modoEdicion.setValue(!actual);
        if (!modoEdicion.getValue()) {
            // si salimos del modo edición, limpiar errores
            clearErrors();
        }
    }

    // inicio la carga de los datos del perfil desde la api
    public void cargarPerfil() {
        SharedPreferences sp = getApplication().getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        if (token == null || token.trim().isEmpty()) {
            // Token inválido, mostrar error y evitar llamada a la API
            mensajeToast.postValue("Token de sesión inválido. Por favor, vuelva a iniciar sesión.");
            mostrarToast.postValue(true);
            toastFinal.postValue("Token de sesión inválido. Por favor, vuelva a iniciar sesión.");
            return;
        }
        Call<Propietario> call = apiService.getPropietarios("Bearer " + token);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Propietario p = response.body();
                    // Validar campos nulos antes de asignar
                    propietario.postValue(p);
                    nombre.postValue(p.getNombre() != null ? p.getNombre() : "");
                    apellido.postValue(p.getApellido() != null ? p.getApellido() : "");
                    dni.postValue(p.getDni() != null ? p.getDni() : "");
                    telefono.postValue(p.getTelefono() != null ? p.getTelefono() : "");
                    email.postValue(p.getEmail() != null ? p.getEmail() : "");
                } else {
                    // Respuesta no exitosa o sin datos
                    mensajeToast.postValue("No se pudo cargar el perfil. Intente nuevamente.");
                    mostrarToast.postValue(true);
                    toastFinal.postValue("No se pudo cargar el perfil. Intente nuevamente.");
                }
            }
            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                // Registrar el error y mostrar mensaje al usuario
                t.printStackTrace();
                mensajeToast.postValue("Error de conexión al cargar el perfil: " + t.getMessage());
                mostrarToast.postValue(true);
                toastFinal.postValue("Error de conexión al cargar el perfil: " + t.getMessage());
            }
        });
    }

    // guardo los datos editados del perfil si el modo edicion esta activo
    public void guardarPerfil() {
        if (modoEdicion.getValue() != null && modoEdicion.getValue()) {
            // validar antes de enviar
            if (!validateAll()) {
                // validation failed; mensaje ya seteado en validateAll()
                return;
            }

            SharedPreferences sp = getApplication().getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
            String token = sp.getString("token", "");
            Propietario p = new Propietario();
            if (propietario.getValue() != null) {
                p.setIdPropietario(propietario.getValue().getIdPropietario());
                // siempre null al enviar a la api NO CAMBIAR LA CLAVE!!!!!!!!!!!!
                p.setClave(null);
            } else {
                p.setClave(null);
            }
            p.setNombre(nombre.getValue());
            p.setApellido(apellido.getValue());
            p.setDni(dni.getValue());
            p.setTelefono(telefono.getValue());
            p.setEmail(email.getValue());
            Call<Propietario> call = apiService.actualizarPropietario("Bearer " + token, p);
            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Propietario actualizado = response.body();
                        propietario.postValue(actualizado);
                        nombre.postValue(actualizado.getNombre());
                        apellido.postValue(actualizado.getApellido());
                        dni.postValue(actualizado.getDni());
                        telefono.postValue(actualizado.getTelefono());
                        email.postValue(actualizado.getEmail());
                        modoEdicion.postValue(false);
                        mostrarToast.postValue(true);
                        mensajeToast.postValue("Perfil actualizado con éxito");
                        clearErrors();
                    } else {
                        // respuesta no exitosa: mostrar mensaje genérico
                        mostrarToast.postValue(true);
                        mensajeToast.postValue("Error al actualizar el perfil");
                    }
                }
                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    t.printStackTrace();
                    mostrarToast.postValue(true);
                    mensajeToast.postValue("Falla en la conexión: " + t.getMessage());
                }
            });
        }
    }

    // defino los metodos para actualizar los livedata desde el fragmento
    public void setNombre(String value) { nombre.setValue(value); }
    public void setApellido(String value) { apellido.setValue(value); }
    public void setDni(String value) { dni.setValue(value); }
    public void setTelefono(String value) { telefono.setValue(value); }
    public void setEmail(String value) { email.setValue(value); }
    public void toastMostrado() {
        if (toastFinal.getValue() != null && !toastFinal.getValue().isEmpty()) {
            toastFinal.setValue("");
        }
    }

    // metodos para errores
    private void clearErrors() {
        errorNombre.postValue(null);
        errorApellido.postValue(null);
        errorDni.postValue(null);
        errorNombreFinal.postValue("");
        errorApellidoFinal.postValue("");
        errorDniFinal.postValue("");
    }

    private boolean isValidNamePattern(String s) {
        if (s == null) return false;
        String trimmed = s.trim();
        if (trimmed.isEmpty()) return false;
        // permite letras (incluye acentos y ñ) y espacios
        return trimmed.matches("^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$");
    }

    private boolean isNumeric(String s) {
        if (s == null) return false;
        String trimmed = s.trim();
        return !trimmed.isEmpty() && trimmed.matches("^\\d+$");
    }

    // valida todos los campos y setea los LiveData de error; retorna true si todo OK
    private boolean validateAll() {
        clearErrors();
        boolean ok = true;

        String n = nombre.getValue() != null ? nombre.getValue().trim() : "";
        String a = apellido.getValue() != null ? apellido.getValue().trim() : "";
        String d = dni.getValue() != null ? dni.getValue().trim() : "";

        if (n.isEmpty()) {
            errorNombre.postValue("El nombre es requerido");
            errorNombreFinal.postValue("El nombre es requerido");
            if (ok) {
                mensajeToast.postValue("El nombre es requerido");
                mostrarToast.postValue(true);
                toastFinal.postValue("El nombre es requerido");
            }
            ok = false;
        } else if (!isValidNamePattern(n)) {
            errorNombre.postValue("El nombre sólo puede contener letras y espacios");
            errorNombreFinal.postValue("El nombre sólo puede contener letras y espacios");
            if (ok) {
                mensajeToast.postValue("El nombre sólo puede contener letras y espacios");
                mostrarToast.postValue(true);
                toastFinal.postValue("El nombre sólo puede contener letras y espacios");
            }
            ok = false;
        } else {
            errorNombreFinal.postValue("");
        }

        if (a.isEmpty()) {
            errorApellido.postValue("El apellido es requerido");
            errorApellidoFinal.postValue("El apellido es requerido");
            if (ok) {
                mensajeToast.postValue("El apellido es requerido");
                mostrarToast.postValue(true);
                toastFinal.postValue("El apellido es requerido");
            }
            ok = false;
        } else if (!isValidNamePattern(a)) {
            errorApellido.postValue("El apellido sólo puede contener letras y espacios");
            errorApellidoFinal.postValue("El apellido sólo puede contener letras y espacios");
            if (ok) {
                mensajeToast.postValue("El apellido sólo puede contener letras y espacios");
                mostrarToast.postValue(true);
                toastFinal.postValue("El apellido sólo puede contener letras y espacios");
            }
            ok = false;
        } else {
            errorApellidoFinal.postValue("");
        }

        if (d.isEmpty()) {
            errorDni.postValue("El DNI es requerido");
            errorDniFinal.postValue("El DNI es requerido");
            if (ok) {
                mensajeToast.postValue("El DNI es requerido");
                mostrarToast.postValue(true);
                toastFinal.postValue("El DNI es requerido");
            }
            ok = false;
        } else if (!isNumeric(d)) {
            errorDni.postValue("El DNI sólo puede contener números");
            errorDniFinal.postValue("El DNI sólo puede contener números");
            if (ok) {
                mensajeToast.postValue("El DNI sólo puede contener números");
                mostrarToast.postValue(true);
                toastFinal.postValue("El DNI sólo puede contener números");
            }
            ok = false;
        } else {
            errorDniFinal.postValue("");
        }

        if (ok) {
            toastFinal.postValue("");
        }
        return ok;
    }

    // gestiono la logica del boton editar/guardar segun el modo edicion
    public void onBotonEditarGuardar(String nombre, String apellido, String dni, String telefono) {
        if (modoEdicion.getValue() != null && modoEdicion.getValue()) {
            setNombre(nombre);
            setApellido(apellido);
            setDni(dni);
            setTelefono(telefono);
            guardarPerfil();
        } else {
            toggleModoEdicion();
        }
    }

    // Maneja el click del botón editar/guardar desde el Fragment
    public void onClickEditarGuardar(boolean modoEdicionActual, String nombreNuevo, String apellidoNuevo, String dniNuevo, String telefonoNuevo) {
        if (modoEdicionActual) {
            // Si está en modo edición, setea los valores y guarda (con validación)
            setNombre(nombreNuevo);
            setApellido(apellidoNuevo);
            setDni(dniNuevo);
            setTelefono(telefonoNuevo);
            guardarPerfil();
        } else {
            // Si está en modo solo lectura, cambia a modo edición
            modoEdicion.setValue(true);
        }
    }
}