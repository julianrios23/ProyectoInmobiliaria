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
import com.julian.proyectoinmobiliaria.databinding.FragmentPagosBinding; // importo la clase de binding que android genero para mi layout
import java.util.ArrayList;


public class PagosFragment extends Fragment {
    // aqui declaro las variables para el viewmodel, el adaptador y el binding
    private PagosViewModel pagosViewModel;
    private PagosAdapter pagosAdapter;
    private FragmentPagosBinding binding; // declaro la variable de binding para acceder a mis vistas
    private int idContrato; // esta variable guarda el id del contrato

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // aqui inflo la vista usando el binding y obtengo la vista raiz
        binding = FragmentPagosBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        
        // configuro el recyclerview con un layout manager y le asigno mi adaptador
        binding.recyclerPagos.setLayoutManager(new LinearLayoutManager(getContext())); // uso el binding para acceder a recyclerPagos
        pagosAdapter = new PagosAdapter(new ArrayList<>());
        binding.recyclerPagos.setAdapter(pagosAdapter); // uso el binding para acceder a recyclerPagos
        return view; // devuelvo la vista raiz inflada
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // aqui valido que el fragment reciba los argumentos necesarios
        if (getArguments() == null || !getArguments().containsKey("idContrato")) {
            Toast.makeText(getContext(), "falta el id de contrato", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }
        idContrato = getArguments().getInt("idContrato", -1); // obtengo el id del contrato de los argumentos
        
        // inicializo el viewmodel y observo los datos y errores
        pagosViewModel = new ViewModelProvider(this).get(PagosViewModel.class);
        // observo los cambios en la lista de pagos del viewmodel para actualizar mi adaptador
        pagosViewModel.getPagosLiveData().observe(getViewLifecycleOwner(), pagos -> {
            pagosAdapter.setPagosList(pagos);
        });
        // observo los errores del viewmodel
        pagosViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            // aqui solo muestro el error si no es null ni vacio
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
        // llamo al viewmodel con el idContrato para cargar los pagos, la validacion se hace en el viewmodel
        pagosViewModel.obtenerPagosPorContrato(idContrato);
    }

    @Override
    // este metodo se llama cuando la vista del fragment se destruye
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // libero la referencia del binding
    }
}
