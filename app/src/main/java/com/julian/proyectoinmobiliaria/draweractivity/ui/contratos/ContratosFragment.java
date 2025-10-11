package com.julian.proyectoinmobiliaria.draweractivity.ui.contratos;

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
import com.julian.proyectoinmobiliaria.model.Contrato;

import java.util.ArrayList;
import java.util.List;

public class ContratosFragment extends Fragment {
    // declaro la variable para el viewmodel de contratos
    private ContratosViewModel mViewModel;
    // creo el metodo estatico para instanciar el fragment
    public static ContratosFragment newInstance() {
        return new ContratosFragment();
    }
    // sobreescribo el metodo oncreateview para inflar el layout del fragment
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contratos, container, false);
    }
    // sobreescribo el metodo onviewcreated para inicializar componentes y observar los datos del viewmodel
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ContratosViewModel.class);
        RecyclerView rv = view.findViewById(R.id.rvContratosVigentes);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.getContratosVigentes().observe(getViewLifecycleOwner(), lista -> {
            ContratoVigenteAdapter adapter = new ContratoVigenteAdapter(lista != null ? lista : new ArrayList<>());
            rv.setAdapter(adapter);
        });
        mViewModel.getUIState().observe(getViewLifecycleOwner(), uiState -> {
            rv.setVisibility(uiState.rvVisibility);
            view.findViewById(R.id.tvEmpty).setVisibility(uiState.tvEmptyVisibility);
        });
        mViewModel.cargarContratosVigentes();
    }

}