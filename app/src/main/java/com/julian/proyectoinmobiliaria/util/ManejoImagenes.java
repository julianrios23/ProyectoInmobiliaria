package com.julian.proyectoinmobiliaria.util;



import android.util.Log;
import android.widget.ImageView;

import com.julian.proyectoinmobiliaria.R;

import com.julian.proyectoinmobiliaria.service.ApiService;
import com.squareup.picasso.Picasso;

public class ManejoImagenes {


    public static void loadImage(String imagen, ImageView imageView, String logTag) {
        try {
            if (imagen != null && !imagen.isEmpty()) {
                // Limpieza de la ruta
                String imagenUrl = imagen.replace("\\", "/");

                // Si no empieza con http, agregamos la base URL
                if (!imagenUrl.startsWith("http")) {
                    imagenUrl = ApiService.BASE_URL + imagenUrl.replaceFirst("^/", "");
                }

                Log.d(logTag, "Cargando imagen: " + imagenUrl);

                Picasso.get()
                        .load(imagenUrl)
                        .placeholder(R.drawable.home)

                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.home);
            }
        } catch (Exception e) {
            Log.e(logTag, "Error al cargar imagen: " + imagen, e);

        }
    }
}
