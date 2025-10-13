package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
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

public class NuevoInmuebleFragment extends DialogFragment {

    // declaro el viewmodel que va a manejar la logica de negocio
    private NuevoInmuebleViewModel mViewModel;
    // declaro el viewmodel que va a manejar la lista de inmuebles
    private InmueblesViewModel mInmueblesViewModel;
    // constante para identificar la solicitud de seleccion de imagen
    private static final int PICK_IMAGE_REQUEST = 1;
    // uri donde guardo la imagen seleccionada
    private Uri imagenUri;
    // referencia al imageview donde muestro la imagen
    private ImageView ivImagen;
    // botones para guardar y seleccionar imagen
    private Button btnGuardar, btnSeleccionarImagen;
    // campos de texto para los datos del inmueble
    private EditText etDireccion, etUso, etTipo, etAmbientes, etSuperficie, etLatitud, etLongitud, etValor;
    // checkboxes para disponible y contrato vigente
    private CheckBox cbDisponible, cbContrato;

    // metodo para crear una nueva instancia del fragment
    public static NuevoInmuebleFragment newInstance() {
        return new NuevoInmuebleFragment();
    }

    // inflo la vista y configuro los listeners de los botones
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inicializar el ViewModel antes de cualquier uso
        mViewModel = new ViewModelProvider(this).get(NuevoInmuebleViewModel.class);
        // Inicializar el ViewModel compartido para la lista de inmuebles
        mInmueblesViewModel = new ViewModelProvider(requireActivity()).get(InmueblesViewModel.class);
        // uso view binding en vez de R para acceder a los elementos de la vista
        com.julian.proyectoinmobiliaria.databinding.FragmentNuevoInmuebleBinding binding = com.julian.proyectoinmobiliaria.databinding.FragmentNuevoInmuebleBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // obtengo referencias a los elementos de la vista usando binding
        ivImagen = binding.ivImagen;
        btnGuardar = binding.btnGuardar;
        btnSeleccionarImagen = binding.btnSeleccionarImagen;
        etDireccion = binding.etDireccion;
        etUso = binding.etUso;
        etTipo = binding.etTipo;
        etAmbientes = binding.etAmbientes;
        etSuperficie = binding.etSuperficie;
        etLatitud = binding.etLatitud;
        etLongitud = binding.etLongitud;
        etValor = binding.etValor;
        cbDisponible = binding.cbDisponible;
        cbContrato = binding.cbContrato;
        Button btnAtras = binding.btnAtras;

        // cuando el usuario toca el boton de seleccionar imagen, abro el selector de imagenes
        btnSeleccionarImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // cuando el usuario toca el boton de guardar, voy guardando los datos y los paso al viewmodel
        btnGuardar.setOnClickListener(v -> {
            String direccion = etDireccion.getText().toString();
            String uso = etUso.getText().toString();
            String tipo = etTipo.getText().toString();
            String ambientes = etAmbientes.getText().toString();
            String superficie = etSuperficie.getText().toString();
            String latitud = etLatitud.getText().toString();
            String longitud = etLongitud.getText().toString();
            String valor = etValor.getText().toString();
            boolean disponible = cbDisponible.isChecked();
            boolean contratoVigente = cbContrato.isChecked();
            mViewModel.procesarNuevoInmueble(
                direccion, uso, tipo, ambientes, superficie, latitud, longitud, valor,
                disponible, contratoVigente, imagenUri, getContext(),
                () -> mInmueblesViewModel.cargarInmuebles() // recarga la lista solo tras exito
            );
        });
        // observo el resultado del viewmodel y muestro un mensaje segun corresponda
        mViewModel.getResultadoLiveData().observe(getViewLifecycleOwner(), result -> {
            Toast.makeText(getContext(), result.mensaje, Toast.LENGTH_LONG).show();
            if (result.exito) {
                // Si el guardado fue exitoso, recargo la lista y cierro el DialogFragment
                mInmueblesViewModel.cargarInmuebles();
                dismiss();

            }
        });
        // si toco atras, cierro el dialogo
        btnAtras.setOnClickListener(v -> dismiss());
        return view;
    }

    // recibo el resultado de la seleccion de imagen y la muestro en el imageview
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imagenUri = data.getData();
            ivImagen.setImageURI(imagenUri);
        }
    }

    // inicializo el viewmodel cuando el fragment esta creado
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NuevoInmuebleViewModel.class);
    }

    // ajusto el tamano y fondo del dialogo al iniciar
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

}