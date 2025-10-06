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
        adapter = new InmueblesAdapter(new ArrayList<Inmueble>());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);
        mViewModel.getInmueblesLiveData().observe(getViewLifecycleOwner(), inmuebles -> {
            Log.d("InmueblesFragment", "observer: inmuebles=" + (inmuebles == null ? "null" : ("size=" + inmuebles.size())));
            if (inmuebles != null) {
                adapter.setInmuebles(inmuebles);
            }
        });
        mViewModel.cargarInmuebles();
    }

}