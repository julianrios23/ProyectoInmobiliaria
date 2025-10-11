package com.julian.proyectoinmobiliaria.draweractivity.ui.inquilinos;

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
import com.julian.proyectoinmobiliaria.model.Inquilino;

import java.util.ArrayList;

public class InquilinosFragment extends Fragment {

    private InquilinosViewModel mViewModel;
    private RecyclerView rvInquilinos;
    private InquilinosAdapter adapter;

    public static InquilinosFragment newInstance() {
        return new InquilinosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inquilinos, container, false);
        rvInquilinos = view.findViewById(R.id.rvInquilinos);
        rvInquilinos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InquilinosAdapter(new ArrayList<Inquilino>());
        rvInquilinos.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);
        mViewModel.getInquilinos().observe(getViewLifecycleOwner(), inquilinos -> {

            adapter.setInquilinos(inquilinos);
        });
        mViewModel.cargarInquilinos();
    }

}