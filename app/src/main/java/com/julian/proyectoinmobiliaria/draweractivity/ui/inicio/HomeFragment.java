package com.julian.proyectoinmobiliaria.draweractivity.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.SupportMapFragment;
import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.databinding.FragmentHomeBinding;

// aqui cree el fragment para la pantalla de inicio
public class HomeFragment extends Fragment {

    // aqui uso view binding para acceder a las vistas del layout
    private FragmentHomeBinding binding;

    // aqui inflo el layout
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // aqui obtengo el viewmodel asociado a este fragment
        HomeViewModel vm = new ViewModelProvider(this).get(HomeViewModel.class);
        // aqui obtengo el fragment del mapa y le pido al viewmodel que lo cargue
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        vm.cargarMapa(mapFragment);
        return root;
    }

    // aqui libero el binding cuando la vista se destruye
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}