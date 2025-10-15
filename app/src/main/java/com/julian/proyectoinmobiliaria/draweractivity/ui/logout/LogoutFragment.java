package com.julian.proyectoinmobiliaria.draweractivity.ui.logout;

// este es el paquete donde se encuentra mi fragmento de cerrar sesion

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.databinding.FragmentLogoutBinding; // importo la clase de binding que android genero para mi layout


public class LogoutFragment extends Fragment {

    private LogoutViewModel mViewModel; // declaro una variable para mi viewmodel de logout
    private FragmentLogoutBinding binding; // declaro la variable de binding para acceder a mis vistas

    // este es un metodo estatico para crear una nueva instancia de mi fragmento
    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // aqui inflo el layout usando el binding y lo asigno a mi variable de clase
        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        
        // inicializo mi viewmodel de logout
        mViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);
        // llamo al metodo iniciarlogout de mi viewmodel, pasandole el contexto y el binding
        mViewModel.iniciarLogout(requireContext(), binding);
        
        // aqui observo el livedata para mostrar el dialogo de confirmacion
        mViewModel.getConfirmLogoutLiveData().observe(getViewLifecycleOwner(), show -> {
            mViewModel.mostrarDialogoLogout(requireContext()); // muestro el dialogo de confirmacion de logout
        });
        
        // aqui observo el livedata para navegar a la actividad de inicio (mapa)
        mViewModel.getNavigateToInicioLiveData().observe(getViewLifecycleOwner(), goToInicio -> {
            mViewModel.navegarInicio(requireActivity()); // navego a la actividad de inicio
        });
        
        return binding.getRoot(); // devuelvo la vista raiz del binding
    }

    // este metodo ya no es necesario, ya que el viewmodel se inicializa en onCreateView
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel // este comentario es un recordatorio para usar el viewmodel
    }

    @Override
    // este metodo se llama cuando la vista del fragment se destruye
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // libero la referencia del binding
    }
}
