package com.julian.proyectoinmobiliaria.util;



import android.util.Log;
import android.widget.ImageView;

import com.julian.proyectoinmobiliaria.R;

import com.julian.proyectoinmobiliaria.service.ApiService;
import com.squareup.picasso.Picasso;

// en este archivo defino la clase para manejar la carga de imagenes
public class ManejoImagenes {

    // este metodo carga una imagen en un imageview usando picasso
    public static void loadImage(String imagen, ImageView imageView, String logTag) {
        try {
            // verifico que la imagen no sea nula ni vacia
            if (imagen != null && !imagen.isEmpty()) {
                // limpio la ruta de la imagen reemplazando las barras invertidas
                String imagenUrl = imagen.replace("\\", "/");

                // si la url no empieza con http, le agrego la base url
                if (!imagenUrl.startsWith("http")) {
                    imagenUrl = ApiService.BASE_URL + imagenUrl.replaceFirst("^/", "");
                }

                // muestro en el log la url que voy a cargar
                Log.d(logTag, "Cargando imagen: " + imagenUrl);

                // uso picasso para cargar la imagen en el imageview y muestro un placeholder mientras carga
                Picasso.get()
                        .load(imagenUrl)
                        .placeholder(R.drawable.home2)

                        .into(imageView);
            } else {
                // si la imagen es nula o vacia, muestro una imagen por defecto
                imageView.setImageResource(R.drawable.home2);
            }
        } catch (Exception e) {
            // si ocurre un error al cargar la imagen, lo muestro en el log
            Log.e(logTag, "Error al cargar imagen: " + imagen, e);

        }
    }
}
