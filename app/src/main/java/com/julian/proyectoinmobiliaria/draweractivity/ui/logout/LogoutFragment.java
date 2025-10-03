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
        android.util.Log.d("OUT", "onCreateView de LogoutFragment");
        com.julian.proyectoinmobiliaria.databinding.FragmentLogoutBinding binding = com.julian.proyectoinmobiliaria.databinding.FragmentLogoutBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);
        mViewModel.iniciarLogout(requireContext(), binding);
        // aqui observo el livedata para mostrar el dialogo de confirmacion
        mViewModel.getConfirmLogoutLiveData().observe(getViewLifecycleOwner(), show -> {
            android.util.Log.d("OUT", "Observer confirmLogoutLiveData: " + show);
            if (show != null && show) {
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Confirmar !!")
                        .setMessage("¿Deseas cerrar sesion?")
                        .setPositiveButton("Si", (dialog, which) -> mViewModel.confirmarLogout())
                        .setNegativeButton("No", (dialog, which) -> {
                            mViewModel.cancelarLogout();
                            // Navegar a Home al cancelar usando binding
                            androidx.navigation.NavController navController = androidx.navigation.Navigation.findNavController(binding.getRoot());
                            navController.navigate(binding.getRoot().getResources().getIdentifier("nav_home", "id", binding.getRoot().getContext().getPackageName()));
                        })
                        .setCancelable(false)
                        .show();
                // aqui reseteo el livedata para evitar mostrar multiples dialogos
                mViewModel.getConfirmLogoutLiveData().setValue(false);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}