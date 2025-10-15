package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

// este es el paquete donde se encuentra mi fragmento para crear un nuevo inmueble

import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.databinding.FragmentNuevoInmuebleBinding; // importo la clase de binding que android genero para mi layout

// defino mi clase NuevoInmuebleFragment que extiende de DialogFragment
public class NuevoInmuebleFragment extends DialogFragment {

    private NuevoInmuebleViewModel mViewModel; // declaro el viewmodel que va a manejar la logica de negocio
    private InmueblesViewModel mInmueblesViewModel; // declaro el viewmodel que va a manejar la lista de inmuebles para recargarla
    private static final int PICK_IMAGE_REQUEST = 1; // esta es la constante para identificar la solicitud de seleccion de imagen
    private Uri imagenUri; // aqui guardo la uri de la imagen seleccionada
    private FragmentNuevoInmuebleBinding binding; // declaro la variable de binding para acceder a mis vistas

    // este metodo es para crear una nueva instancia de mi fragmento
    public static NuevoInmuebleFragment newInstance() {
        return new NuevoInmuebleFragment();
    }

    // inflo la vista y configuro los listeners de los botones
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // inicializo mi viewmodel de este fragmento
        mViewModel = new ViewModelProvider(this).get(NuevoInmuebleViewModel.class);
        // inicializo el viewmodel compartido para la lista de inmuebles desde la actividad
        mInmueblesViewModel = new ViewModelProvider(requireActivity()).get(InmueblesViewModel.class);
        
        // aqui inflo el layout usando el binding y obtengo la vista raiz
        binding = FragmentNuevoInmuebleBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // cuando el usuario toca el boton de seleccionar imagen, abro el selector de imagenes
        binding.btnSeleccionarImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // cuando el usuario toca el boton de guardar, voy recogiendo los datos y los paso al viewmodel
        binding.btnGuardar.setOnClickListener(v -> {
            // recojo los datos de los campos de la interfaz usando el binding
            String direccion = binding.etDireccion.getText().toString();
            String uso = binding.etUso.getText().toString();
            String tipo = binding.etTipo.getText().toString();
            String ambientes = binding.etAmbientes.getText().toString();
            String superficie = binding.etSuperficie.getText().toString();
            String latitud = binding.etLatitud.getText().toString();
            String longitud = binding.etLongitud.getText().toString();
            String valor = binding.etValor.getText().toString();
            boolean disponible = binding.cbDisponible.isChecked();
            boolean contratoVigente = binding.cbContrato.isChecked();

            // muestro un dialogo de confirmacion antes de guardar
            new AlertDialog.Builder(getContext())
                    .setTitle("confirmar guardado")
                    .setMessage("¿esta seguro de querer guardar este nuevo inmueble?")
                    .setPositiveButton("si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // si el usuario confirma, llamo al viewmodel para procesar el nuevo inmueble
                            mViewModel.procesarNuevoInmueble(
                                direccion, uso, tipo, ambientes, superficie, latitud, longitud, valor,
                                disponible, contratoVigente, imagenUri, getContext(),
                                () -> mInmueblesViewModel.cargarInmuebles() // recargo la lista solo despues de un exito
                            );
                        }
                    })
                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // si el usuario cancela, simplemente cierro el dialogo
                            dialog.dismiss();
                        }
                    })
                    .show();
        });
        
        // observo el resultado del viewmodel y muestro un mensaje segun corresponda
        mViewModel.getResultadoLiveData().observe(getViewLifecycleOwner(), result -> {
            Toast.makeText(getContext(), result.mensaje, Toast.LENGTH_LONG).show();
            if (result.exito) {
                // si el guardado fue exitoso, recargo la lista y cierro el dialogfragment
                // la recarga ya se hace en el callback del viewmodel, asi que solo cierro el dialog
                dismiss();

            }
        });
        
        // si toco el boton de atras, cierro el dialogo
        binding.btnAtras.setOnClickListener(v -> dismiss());
        return view;
    }

    // recibo el resultado de la seleccion de imagen y la muestro en el imageview
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imagenUri = data.getData(); // obtengo la uri de la imagen seleccionada
            binding.ivImagen.setImageURI(imagenUri); // muestro la imagen en el imageview usando el binding
        }
    }

    // ajusto el tamaño y fondo del dialogo al iniciar
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    // este metodo se llama cuando la vista del fragmento se destruye
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // libero la referencia del binding para evitar fugas de memoria
    }
}
