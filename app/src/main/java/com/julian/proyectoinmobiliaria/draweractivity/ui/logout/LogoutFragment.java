package com.julian.proyectoinmobiliaria.draweractivity.ui.logout;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julian.proyectoinmobiliaria.R;

public class LogoutFragment extends Fragment {

    private LogoutViewModel mViewModel;

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        com.julian.proyectoinmobiliaria.databinding.FragmentLogoutBinding binding = com.julian.proyectoinmobiliaria.databinding.FragmentLogoutBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);
        mViewModel.iniciarLogout(requireContext(), binding);
        // aqui observo el livedata para mostrar el dialogo de confirmacion
        mViewModel.getConfirmLogoutLiveData().observe(getViewLifecycleOwner(), show -> {
            mViewModel.mostrarDialogoLogout(requireContext());
        });
        // aqui observo el livedata para navegar a inicio (mapa)
        mViewModel.getNavigateToInicioLiveData().observe(getViewLifecycleOwner(), goToInicio -> {
            mViewModel.navegarInicio(requireActivity());
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}