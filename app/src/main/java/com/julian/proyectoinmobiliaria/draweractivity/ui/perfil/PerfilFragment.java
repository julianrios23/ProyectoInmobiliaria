package com.julian.proyectoinmobiliaria.draweractivity.ui.perfil;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.model.Propietario;

public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel;
    private EditText etNombre, etApellido, etDni, etTelefono;
    private TextView tvEmail;
    private Button btnEditarGuardar;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etDni = view.findViewById(R.id.etDni);
        etTelefono = view.findViewById(R.id.etTelefono);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnEditarGuardar = view.findViewById(R.id.btnEditarGuardar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        mViewModel.getNombre().observe(getViewLifecycleOwner(), nombre -> etNombre.setText(nombre));
        mViewModel.getApellido().observe(getViewLifecycleOwner(), apellido -> etApellido.setText(apellido));
        mViewModel.getDni().observe(getViewLifecycleOwner(), dni -> etDni.setText(dni));
        mViewModel.getTelefono().observe(getViewLifecycleOwner(), telefono -> etTelefono.setText(telefono));
        mViewModel.getEmail().observe(getViewLifecycleOwner(), email -> tvEmail.setText(email));
        mViewModel.getModoEdicion().observe(getViewLifecycleOwner(), modoEdicion -> {
            etNombre.setEnabled(modoEdicion);
            etApellido.setEnabled(modoEdicion);
            etDni.setEnabled(modoEdicion);
            etTelefono.setEnabled(modoEdicion);
            btnEditarGuardar.setText(modoEdicion ? "Guardar" : "Editar");
        });
        mViewModel.getMostrarToast().observe(getViewLifecycleOwner(), mostrar -> {
            if (mostrar != null && mostrar) {
                Toast.makeText(getContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                mViewModel.toastMostrado();
            }
        });
        btnEditarGuardar.setOnClickListener(v -> {
            mViewModel.onBotonEditarGuardar(etNombre.getText().toString(), etApellido.getText().toString(), etDni.getText().toString(), etTelefono.getText().toString());
        });
        mViewModel.cargarPerfil();
    }

}