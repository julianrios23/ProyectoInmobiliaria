package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

// este es el paquete donde se encuentra mi fragmento de inmuebles

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.databinding.FragmentInmueblesBinding; // importo la clase de binding que android genero para mi layout
import com.julian.proyectoinmobiliaria.model.Inmueble;

import java.util.ArrayList;


public class InmueblesFragment extends Fragment {

    private InmueblesViewModel mViewModel; // declaro una variable para mi viewmodel de inmuebles
    private FragmentInmueblesBinding binding; // declaro la variable de binding para acceder a mis vistas
    private InmueblesAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // aqui inflo el layout usando el binding y obtengo la vista raiz
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // configuro el recyclerview con un layout manager y le asigno mi adapter
        binding.recyclerInmuebles.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InmueblesAdapter(new ArrayList<Inmueble>(), this);
        binding.recyclerInmuebles.setAdapter(adapter);

        // configuro el listener para el evento de click en el boton de editar de cada item
        adapter.setOnEditarClickListener(new InmueblesAdapter.OnEditarClickListener() {
            @Override
            public void onEditarClick(Inmueble inmueble) {
                // cuando se hace click en editar, creo y muestro un dialogfragment para editar el inmueble
                EditarInmuebleDialogFragment dialog = EditarInmuebleDialogFragment.newInstance(inmueble);
                dialog.show(requireActivity().getSupportFragmentManager(), "EditarInmuebleDialog");
            }
        });

        // configuro el listener para el boton flotante de agregar nuevo inmueble
        binding.fabAgregarInm.setOnClickListener(v -> mViewModel.onFabAgregarInmuebleClick()); // delego la accion al viewmodel

        return root; // devuelvo la vista raiz inflada
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // inicializo mi viewmodel, usaremos el de la actividad para que sea compartido
        mViewModel = new ViewModelProvider(requireActivity()).get(InmueblesViewModel.class);
        
        // observo los cambios en la lista de inmuebles del viewmodel para actualizar mi adaptador
        mViewModel.getInmueblesLiveData().observe(getViewLifecycleOwner(), inmuebles -> {
            adapter.setInmuebles(inmuebles);
            adapter.notifyDataSetChanged();
        });

        // observo un evento del viewmodel para saber cuando debo mostrar el dialog de nuevo inmueble
        mViewModel.getShowNuevoInmuebleDialog().observe(getViewLifecycleOwner(), aVoid -> {
            NuevoInmuebleFragment nuevoFragment = NuevoInmuebleFragment.newInstance();
            nuevoFragment.show(requireActivity().getSupportFragmentManager(), "NuevoInmuebleDialog");
        });

        mViewModel.cargarInmuebles(); // inicio la carga de inmuebles
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // libero la referencia del binding para evitar fugas de memoria
    }
}
