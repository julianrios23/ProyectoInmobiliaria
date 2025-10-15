package com.julian.proyectoinmobiliaria.draweractivity.ui.contratos;

// este es el paquete donde se encuentra mi fragmento de contratos

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.databinding.FragmentContratosBinding; // importo la clase de binding que android genero para mi layout
import com.julian.proyectoinmobiliaria.model.Contrato;

import java.util.ArrayList;
import java.util.List;


public class ContratosFragment extends Fragment {
    private ContratosViewModel mViewModel; // declaro la variable para el viewmodel de contratos
    private FragmentContratosBinding binding; // declaro la variable de binding para acceder a mis vistas

    // creo el metodo estatico para instanciar el fragment
    public static ContratosFragment newInstance() {
        return new ContratosFragment();
    }

    // sobreescribo el metodo oncreateview para inflar el layout del fragment usando binding
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // aqui inflo el layout usando el binding y obtengo la vista raiz
        binding = FragmentContratosBinding.inflate(inflater, container, false);
        return binding.getRoot(); // devuelvo la vista raiz inflada
    }

    // sobreescribo el metodo onviewcreated para inicializar componentes y observar los datos del viewmodel
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // inicializo mi viewmodel
        mViewModel = new ViewModelProvider(this).get(ContratosViewModel.class);
        // configuro el recyclerview con un layout manager
        binding.rvContratosVigentes.setLayoutManager(new LinearLayoutManager(getContext())); // uso el binding para acceder a rvContratosVigentes
        
        // observo los cambios en la lista de contratos vigentes del viewmodel para actualizar el adaptador
        mViewModel.getContratosVigentes().observe(getViewLifecycleOwner(), lista -> {
            ContratoVigenteAdapter adapter = new ContratoVigenteAdapter(lista != null ? lista : new ArrayList<>());
            binding.rvContratosVigentes.setAdapter(adapter); // uso el binding para acceder a rvContratosVigentes
        });
        
        // observo el estado de la ui del viewmodel para controlar la visibilidad del recyclerview y del texto de vacio
        mViewModel.getUIState().observe(getViewLifecycleOwner(), uiState -> {
            binding.rvContratosVigentes.setVisibility(uiState.rvVisibility); // uso el binding para acceder a rvContratosVigentes
            binding.tvEmpty.setVisibility(uiState.tvEmptyVisibility); // uso el binding para acceder a tvEmpty
        });
        // inicio la carga de contratos vigentes
        mViewModel.cargarContratosVigentes();
    }

    @Override
    // este metodo se llama cuando la vista del fragment se destruye
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // libero la referencia del binding
    }
}
