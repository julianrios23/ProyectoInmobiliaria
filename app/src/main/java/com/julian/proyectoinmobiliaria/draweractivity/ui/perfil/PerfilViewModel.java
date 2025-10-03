package com.julian.proyectoinmobiliaria.draweractivity.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.julian.proyectoinmobiliaria.model.Propietario;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PerfilViewModel extends AndroidViewModel {
    private MutableLiveData<Propietario> propietario = new MutableLiveData<>();
    private final MutableLiveData<String> nombre = new MutableLiveData<>("");
    private final MutableLiveData<String> apellido = new MutableLiveData<>("");
    private final MutableLiveData<String> dni = new MutableLiveData<>("");
    private final MutableLiveData<String> telefono = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> modoEdicion = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mostrarToast = new MutableLiveData<>(false);

    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Propietario> getPropietario() {
        return propietario;
    }
    public LiveData<String> getNombre() { return nombre; }
    public LiveData<String> getApellido() { return apellido; }
    public LiveData<String> getDni() { return dni; }
    public LiveData<String> getTelefono() { return telefono; }
    public LiveData<String> getEmail() { return email; }
    public LiveData<Boolean> getModoEdicion() { return modoEdicion; }
    public LiveData<Boolean> getMostrarToast() { return mostrarToast; }

    public void toggleModoEdicion() {
        Boolean actual = modoEdicion.getValue();
        if (actual == null) actual = false;
        modoEdicion.setValue(!actual);
    }

    public void cargarPerfil() {
        new PerfilTask().execute();
    }

    private class PerfilTask extends AsyncTask<Void, Void, Propietario> {
        @Override
        protected Propietario doInBackground(Void... voids) {
            try {
                SharedPreferences sp = getApplication().getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
                String token = sp.getString("token", "");
                URL url = new URL("https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/api/Propietarios");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    JSONObject obj = new JSONObject(sb.toString());
                    Propietario p = new Propietario();
                    p.setIdPropietario(obj.getInt("idPropietario"));
                    p.setNombre(obj.getString("nombre"));
                    p.setApellido(obj.getString("apellido"));
                    p.setDni(obj.getString("dni"));
                    p.setTelefono(obj.getString("telefono"));
                    p.setEmail(obj.getString("email"));
                    p.setClave(obj.getString("clave"));
                    return p;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Propietario p) {
            if (p != null) {
                propietario.setValue(p);
                nombre.setValue(p.getNombre());
                apellido.setValue(p.getApellido());
                dni.setValue(p.getDni());
                telefono.setValue(p.getTelefono());
                email.setValue(p.getEmail());
            }
        }
    }

    public void guardarPerfil() {
        if (modoEdicion.getValue() != null && modoEdicion.getValue()) {
            new GuardarPerfilTask().execute(
                nombre.getValue(),
                apellido.getValue(),
                dni.getValue(),
                telefono.getValue(),
                email.getValue()
            );
        }
    }

    private class GuardarPerfilTask extends AsyncTask<String, Void, Propietario> {
        @Override
        protected Propietario doInBackground(String... datos) {
            try {
                SharedPreferences sp = getApplication().getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
                String token = sp.getString("token", "");
                URL url = new URL("https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/api/Propietarios/actualizar");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                JSONObject obj = new JSONObject();
                obj.put("idPropietario", propietario.getValue() != null ? propietario.getValue().getIdPropietario() : 0);
                obj.put("nombre", datos[0]);
                obj.put("apellido", datos[1]);
                obj.put("dni", datos[2]);
                obj.put("telefono", datos[3]);
                obj.put("email", datos[4]);
                obj.put("clave", propietario.getValue() != null ? propietario.getValue().getClave() : "");
                String json = obj.toString();
                java.io.OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    JSONObject resp = new JSONObject(sb.toString());
                    Propietario p = new Propietario();
                    p.setIdPropietario(resp.getInt("idPropietario"));
                    p.setNombre(resp.getString("nombre"));
                    p.setApellido(resp.getString("apellido"));
                    p.setDni(resp.getString("dni"));
                    p.setTelefono(resp.getString("telefono"));
                    p.setEmail(resp.getString("email"));
                    p.setClave(resp.getString("clave"));
                    return p;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Propietario p) {
            if (p != null) {
                propietario.setValue(p);
                nombre.setValue(p.getNombre());
                apellido.setValue(p.getApellido());
                dni.setValue(p.getDni());
                telefono.setValue(p.getTelefono());
                email.setValue(p.getEmail());
                modoEdicion.setValue(false);
                mostrarToast.setValue(true);
            }
        }
    }

    // Métodos para actualizar los LiveData desde el fragment
    public void setNombre(String value) { nombre.setValue(value); }
    public void setApellido(String value) { apellido.setValue(value); }
    public void setDni(String value) { dni.setValue(value); }
    public void setTelefono(String value) { telefono.setValue(value); }
    public void setEmail(String value) { email.setValue(value); }
    public void toastMostrado() { mostrarToast.setValue(false); }

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
}