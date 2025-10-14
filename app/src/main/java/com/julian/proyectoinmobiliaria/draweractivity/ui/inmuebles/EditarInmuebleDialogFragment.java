package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.model.Inmueble;

public class EditarInmuebleDialogFragment extends DialogFragment {
    private EditText etDireccion, etUso, etAmbientes, etValor;
    private Switch swDisponible;
    private Inmueble inmueble;
    private EditarInmuebleViewModel editarViewModel;
    private InmueblesViewModel listaViewModel;

    public static EditarInmuebleDialogFragment newInstance(Inmueble inmueble) {
        EditarInmuebleDialogFragment frag = new EditarInmuebleDialogFragment();
        Bundle args = new Bundle();
        args.putString("inmueble_json", new Gson().toJson(inmueble));
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_editar_inmueble, null);
        etDireccion = view.findViewById(R.id.etDireccion);
        etUso = view.findViewById(R.id.etUso);
        etAmbientes = view.findViewById(R.id.etAmbientes);
        etValor = view.findViewById(R.id.etValor);
        swDisponible = view.findViewById(R.id.swDisponible);

        if (getArguments() != null && getArguments().containsKey("inmueble_json")) {
            inmueble = new Gson().fromJson(getArguments().getString("inmueble_json"), Inmueble.class);
            etDireccion.setText(inmueble.getDireccion());
            etUso.setText(inmueble.getUso());
            etAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
            etValor.setText(String.valueOf(inmueble.getValor()));
            swDisponible.setChecked(inmueble.getDisponible() != null && inmueble.getDisponible());
        }

        editarViewModel = new ViewModelProvider(this).get(EditarInmuebleViewModel.class);
        listaViewModel = new ViewModelProvider(requireActivity()).get(InmueblesViewModel.class);

        editarViewModel.getInmuebleActualizado().observe(this, actualizado -> {
            Toast.makeText(getContext(), "Inmueble actualizado", Toast.LENGTH_SHORT).show();
            listaViewModel.cargarInmuebles();
            dismiss();
        });
        editarViewModel.getError().observe(this, error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Editar inmueble")
                .setView(view)
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", (dialog, which) -> dismiss());
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dlg -> {
            Button btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btn.setOnClickListener(v -> {
                if (inmueble != null) {
                    inmueble.setDireccion(etDireccion.getText().toString());
                    inmueble.setUso(etUso.getText().toString());
                    inmueble.setAmbientes(Integer.parseInt(etAmbientes.getText().toString()));
                    inmueble.setValor(Double.parseDouble(etValor.getText().toString()));
                    inmueble.setDisponible(swDisponible.isChecked());
                    // Log para depuración
                    Log.d("EditarInmuebleDialog", "Enviando inmueble actualizado: " + new Gson().toJson(inmueble));
                    editarViewModel.actualizarInmueble(inmueble);
                }
            });
        });
        return dialog;
    }
}
