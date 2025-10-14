package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.model.Inmueble;

import java.util.ArrayList;

// defino el fragment y las variables para el viewmodel, el recyclerview y el adaptador
public class InmueblesFragment extends Fragment {

    private InmueblesViewModel mViewModel;
    private RecyclerView recyclerView;
    private InmueblesAdapter adapter;

    // creo una nueva instancia del fragment
    public static InmueblesFragment newInstance() {
        return new InmueblesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflo la vista del fragment y configuro el recyclerview
        View view = inflater.inflate(R.layout.fragment_inmuebles, container, false);
        recyclerView = view.findViewById(R.id.recyclerInmuebles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InmueblesAdapter(new ArrayList<Inmueble>(), this);
        recyclerView.setAdapter(adapter);
        // Listener para editar inmueble
        adapter.setOnEditarClickListener(new InmueblesAdapter.OnEditarClickListener() {
            @Override
            public void onEditarClick(Inmueble inmueble) {
                EditarInmuebleDialogFragment dialog = EditarInmuebleDialogFragment.newInstance(inmueble);
                dialog.show(requireActivity().getSupportFragmentManager(), "EditarInmuebleDialog");
            }
        });
        // Agrego el listener al FAB para mostrar NuevoInmuebleFragment
        View fabAgregarInm = view.findViewById(R.id.fabAgregarInm);
        fabAgregarInm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el DialogFragment superpuesto y transparente
                NuevoInmuebleFragment nuevoFragment = NuevoInmuebleFragment.newInstance();
                nuevoFragment.show(requireActivity().getSupportFragmentManager(), "NuevoInmuebleDialog");
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Usar el ViewModel compartido de la Activity
        mViewModel = new ViewModelProvider(requireActivity()).get(InmueblesViewModel.class);
        mViewModel.getInmueblesLiveData().observe(getViewLifecycleOwner(), inmuebles -> {
            adapter.setInmuebles(inmuebles);
            adapter.notifyDataSetChanged();
        });
        mViewModel.cargarInmuebles(); // Carga inicial
    }

}