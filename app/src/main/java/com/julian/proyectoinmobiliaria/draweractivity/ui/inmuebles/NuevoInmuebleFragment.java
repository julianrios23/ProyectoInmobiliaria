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

import android.text.InputFilter;
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
import com.julian.proyectoinmobiliaria.util.InputFilters;

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

        // Aplico los filtros de input a los campos correspondientes
        binding.etDireccion.setFilters(new InputFilter[]{InputFilters.LETTERS_DIGITS_FILTER});
        binding.etUso.setFilters(new InputFilter[]{InputFilters.LETTERS_FILTER});
        binding.etTipo.setFilters(new InputFilter[]{InputFilters.LETTERS_FILTER});
        binding.etAmbientes.setFilters(new InputFilter[]{InputFilters.DIGITS_FILTER});
        binding.etSuperficie.setFilters(new InputFilter[]{InputFilters.DECIMAL_FILTER});
        binding.etLatitud.setFilters(new InputFilter[]{InputFilters.DECIMAL_FILTER});
        binding.etLongitud.setFilters(new InputFilter[]{InputFilters.DECIMAL_FILTER});
        binding.etValor.setFilters(new InputFilter[]{InputFilters.DECIMAL_FILTER});

        // cuando el usuario toca el boton de seleccionar imagen, abro el selector de imagenes
        binding.btnSeleccionarImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // cuando el usuario toca el boton de guardar, solo paso los datos al ViewModel y no hago validación aquí
        binding.btnGuardar.setOnClickListener(v -> {
            mViewModel.validarYProcesarNuevoInmueble(
                binding.etDireccion.getText().toString().trim(),
                binding.etUso.getText().toString().trim(),
                binding.etTipo.getText().toString().trim(),
                binding.etAmbientes.getText().toString().trim(),
                binding.etSuperficie.getText().toString().trim(),
                binding.etLatitud.getText().toString().trim(),
                binding.etLongitud.getText().toString().trim(),
                binding.etValor.getText().toString().trim(),
                binding.cbDisponible.isChecked(),
                binding.cbContrato.isChecked(),
                imagenUri,
                getContext(),
                () -> mInmueblesViewModel.cargarInmuebles()
            );
        });

        // Observar errores de validación y mostrarlos en los EditText
        mViewModel.errorDireccion.observe(getViewLifecycleOwner(), error -> binding.etDireccion.setError(error));
        mViewModel.errorUso.observe(getViewLifecycleOwner(), error -> binding.etUso.setError(error));
        mViewModel.errorTipo.observe(getViewLifecycleOwner(), error -> binding.etTipo.setError(error));
        mViewModel.errorAmbientes.observe(getViewLifecycleOwner(), error -> binding.etAmbientes.setError(error));
        mViewModel.errorSuperficie.observe(getViewLifecycleOwner(), error -> binding.etSuperficie.setError(error));
        mViewModel.errorLatitud.observe(getViewLifecycleOwner(), error -> binding.etLatitud.setError(error));
        mViewModel.errorLongitud.observe(getViewLifecycleOwner(), error -> binding.etLongitud.setError(error));
        mViewModel.errorValor.observe(getViewLifecycleOwner(), error -> binding.etValor.setError(error));

        // Observar el resultado general y mostrar Toast o cerrar el diálogo
        mViewModel.getResultadoLiveData().observe(getViewLifecycleOwner(), result -> {
            mViewModel.manejarResultado(requireContext(), result, this);
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
