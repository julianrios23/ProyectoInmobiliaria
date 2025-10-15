package com.julian.proyectoinmobiliaria.draweractivity.ui.inquilinos;

// este es el paquete donde se encuentra mi fragmento de inquilinos

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
// importo la clase de binding que android genero para mi layout de inquilinos
import com.julian.proyectoinmobiliaria.databinding.FragmentInquilinosBinding;
import com.julian.proyectoinmobiliaria.model.Inquilino;

import java.util.ArrayList;


public class InquilinosFragment extends Fragment {

    private InquilinosViewModel mViewModel; // declaro una variable para mi viewmodel de inquilinos
    private FragmentInquilinosBinding binding; // declaro la variable de binding para acceder a mis vistas
    private InquilinosAdapter adapter; // declaro una variable para mi adaptador del recyclerview

    // este es un metodo estatico para crear una nueva instancia de mi fragmento
    public static InquilinosFragment newInstance() {
        return new InquilinosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // aqui inflo el layout usando el binding y obtengo la vista raiz
        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // configuro el recyclerview con un layout manager y le asigno mi adaptador
        binding.rvInquilinos.setLayoutManager(new LinearLayoutManager(getContext())); // uso el binding para acceder a rvInquilinos
        adapter = new InquilinosAdapter(new ArrayList<Inquilino>());
        binding.rvInquilinos.setAdapter(adapter); // uso el binding para acceder a rvInquilinos
        return view; // devuelvo la vista raiz inflada
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // inicializo mi viewmodel en onViewCreated
        mViewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);
        // observo los cambios en la lista de inquilinos del viewmodel para actualizar mi adaptador
        mViewModel.getInquilinos().observe(getViewLifecycleOwner(), inquilinos -> {
            adapter.setInquilinos(inquilinos);
        });
        // inicio la carga de inquilinos
        mViewModel.cargarInquilinos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // libero la referencia del binding para evitar fugas de memoria
    }
}
