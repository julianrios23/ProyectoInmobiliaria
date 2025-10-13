package com.julian.proyectoinmobiliaria.draweractivity.ui.pagos;

// en este archivo defino el fragmento para mostrar la lista de pagos
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.julian.proyectoinmobiliaria.R;
import java.util.ArrayList;

public class PagosFragment extends Fragment {
    // aqui defino las variables para el viewmodel, el adaptador y el recyclerview
    private PagosViewModel pagosViewModel;
    private PagosAdapter pagosAdapter;
    private RecyclerView recyclerPagos;
    private int idContrato;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // aqui inflo la vista y configuro el recyclerview
        View view = inflater.inflate(R.layout.fragment_pagos, container, false);
        recyclerPagos = view.findViewById(R.id.recyclerPagos);
        recyclerPagos.setLayoutManager(new LinearLayoutManager(getContext()));
        pagosAdapter = new PagosAdapter(new ArrayList<>());
        recyclerPagos.setAdapter(pagosAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // aqui valido que el fragment reciba los argumentos necesarios
        if (!getArguments().containsKey("idContrato")) {
            Toast.makeText(getContext(), "Falta el id de contrato", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }
        idContrato = getArguments().getInt("idContrato", -1);
        // inicializo el viewmodel y observo los datos y errores
        pagosViewModel = new ViewModelProvider(this).get(PagosViewModel.class);
        pagosViewModel.getPagosLiveData().observe(getViewLifecycleOwner(), pagos -> {
            pagosAdapter.setPagosList(pagos);
        });
        pagosViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            // aqui solo muestro el error si no es null ni vacio
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
        // llamo al viewmodel con el idContrato, la validacion se hace en el viewmodel
        pagosViewModel.obtenerPagosPorContrato(idContrato);
    }
}
