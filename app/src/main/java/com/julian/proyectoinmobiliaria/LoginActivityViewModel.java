package com.julian.proyectoinmobiliaria;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;



import com.julian.proyectoinmobiliaria.draweractivity.DrawerActivity;
import com.julian.proyectoinmobiliaria.login.LoginApi;
import com.julian.proyectoinmobiliaria.login.LoginRequest;
import com.google.gson.Gson;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

// importé las librerías necesarias para la comunicación con la API, manejo de respuestas y conversión de datos.

public class LoginActivityViewModel extends AndroidViewModel {
    // creé un MutableLiveData para almacenar el resultado del login y exponerlo como LiveData.
    private final MutableLiveData<String> loginResult = new MutableLiveData<>();
    public LiveData<String> getLoginResult() {
        return loginResult;
    }

    // declaré la interfaz de la API y las preferencias compartidas.
    private final LoginApi loginApi;
    private final SharedPreferences prefs;

    // inicialicé el ViewModel y configuré Retrofit con un interceptor para agregar headers y los convertidores necesarios.
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loginApi = retrofit.create(LoginApi.class);
        prefs = application.getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
    }

    // implement el método login que realiza la llamada a la API y maneja la respuesta.
    public void login(String usuario, String clave) {
        loginApi.loginForm(usuario, clave).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Context context = getApplication().getApplicationContext();
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    prefs.edit().putString("token", token).putString("email", usuario).apply();
                    String tokenGuardado = prefs.getString("token", "no_token");
                    Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show();
                    loginResult.postValue("success");
                } else {
                    try {
                        response.errorBody().string(); // Solo para consumir el error, sin log
                    } catch (Exception e) {
                        // No hacer nada
                    }
                    Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    loginResult.postValue("Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loginResult.postValue("Error de red: " + t.getMessage());
            }
        });
    }

    //observa permanentemente el resultado del login usando livedata. cuando el resultado es "success", navega a la actividad principal
    public void handleLoginResult(Context context) {
        getLoginResult().observeForever(result -> {
            if ("success".equals(result)) {
                Intent intent = new Intent(context, DrawerActivity.class);
                context.startActivity(intent);
                if (context instanceof android.app.Activity) {
                    ((android.app.Activity) context).finish();
                }
            } else if (result != null) {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para verificar los campos y ocultar el teclado
    public void checkFieldsAndHideKeyboard(String usuario, String clave, Context context) {
        if (!usuario.isEmpty() && !clave.isEmpty()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && context instanceof android.app.Activity) {
                android.view.View view = ((android.app.Activity) context).getCurrentFocus();
                if (view != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }
}
