package com.julian.proyectoinmobiliaria.draweractivity.ui.perfil;

// yo implemento las validaciones y filtros en este fragment

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.julian.proyectoinmobiliaria.databinding.FragmentPerfilBinding; // importo la clase de binding que android genero para mi layout
import android.text.InputFilter;
import android.text.InputType;


public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel; // aqui declaro mi viewmodel de perfil
    private FragmentPerfilBinding binding; // aqui declaro mi variable de binding para acceder a las vistas

    // aqui creo una instancia nueva del fragment
    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    // aqui inflo la vista usando view binding y obtengo referencias a los componentes
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false); // inflo el layout usando el binding
        View root = binding.getRoot(); // obtengo la vista raiz
        return root; // devuelvo la vista raiz
    }

    // aqui inicializo el viewmodel y configuro los observadores
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class); // inicializo mi viewmodel
        
        // observo el nombre del viewmodel y lo muestro en el edittext
        mViewModel.getNombre().observe(getViewLifecycleOwner(), nombre -> binding.etNombre.setText(nombre));
        // observo el apellido del viewmodel y lo muestro en el edittext
        mViewModel.getApellido().observe(getViewLifecycleOwner(), apellido -> binding.etApellido.setText(apellido));
        // observo el dni del viewmodel y lo muestro en el edittext
        mViewModel.getDni().observe(getViewLifecycleOwner(), dni -> binding.etDni.setText(dni));
        // observo el telefono del viewmodel y lo muestro en el edittext
        mViewModel.getTelefono().observe(getViewLifecycleOwner(), telefono -> binding.etTelefono.setText(telefono));
        // observo el email del viewmodel y lo muestro en el textview
        mViewModel.getEmail().observe(getViewLifecycleOwner(), email -> binding.tvEmail.setText(email));
        
        // observo el modo edicion del viewmodel para habilitar/deshabilitar campos y cambiar el texto del boton
        mViewModel.getModoEdicion().observe(getViewLifecycleOwner(), modoEdicion -> {
            binding.etNombre.setEnabled(modoEdicion);
            binding.etApellido.setEnabled(modoEdicion);
            binding.etDni.setEnabled(modoEdicion);
            binding.etTelefono.setEnabled(modoEdicion);
            binding.btnEditarGuardar.setText(modoEdicion ? "guardar" : "editar"); // cambio el texto del boton
        });

        // observo los errores de validacion ya procesados y los muestro directamente
        mViewModel.getErrorNombreFinal().observe(getViewLifecycleOwner(), error -> binding.etNombre.setError(error));
        mViewModel.getErrorApellidoFinal().observe(getViewLifecycleOwner(), error -> binding.etApellido.setError(error));
        mViewModel.getErrorDniFinal().observe(getViewLifecycleOwner(), error -> binding.etDni.setError(error));
        // observo el toast ya procesado y lo muestro directamente
        mViewModel.getToastFinal().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            mViewModel.toastMostrado();
        });

        // aplico filtros de entrada para mejorar la experiencia (nombre/apellido solo letras, dni solo digitos)
        binding.etNombre.setFilters(new InputFilter[]{ InputFilters.LETTERS_FILTER });
        binding.etApellido.setFilters(new InputFilter[]{ InputFilters.LETTERS_FILTER });
        binding.etDni.setFilters(new InputFilter[]{ InputFilters.DIGITS_FILTER });
        binding.etDni.setInputType(InputType.TYPE_CLASS_NUMBER);

        // aqui configuro el listener del boton para cambiar entre editar y guardar
        binding.btnEditarGuardar.setOnClickListener(v -> {
            // cuando se hace click, llamo al viewmodel con los datos actuales de los campos
            mViewModel.onBotonEditarGuardar(binding.etNombre.getText().toString(),
                                            binding.etApellido.getText().toString(),
                                            binding.etDni.getText().toString(),
                                            binding.etTelefono.getText().toString());
        });
        
        // aqui cargo los datos iniciales del perfil desde el viewmodel
        mViewModel.cargarPerfil();
    }

    @Override
    // este metodo se llama cuando la vista del fragment se destruye
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // libero la referencia del binding
    }
}
