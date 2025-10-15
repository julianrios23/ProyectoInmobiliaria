package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

// este es el paquete donde se encuentra mi dialogfragment de editar inmueble

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
import com.julian.proyectoinmobiliaria.databinding.DialogEditarInmuebleBinding; // importo la clase de binding generada para mi layout
import com.julian.proyectoinmobiliaria.model.Inmueble;


public class EditarInmuebleDialogFragment extends DialogFragment {


    private DialogEditarInmuebleBinding binding; // declaro la variable de binding para acceder a mis vistas
    private Inmueble inmueble; // esta es la variable para el objeto inmueble que voy a editar
    private EditarInmuebleViewModel editarViewModel; // este es mi viewmodel para la logica de edicion
    private InmueblesViewModel listaViewModel; // este es mi viewmodel de la lista para refrescar despues de editar

    // este es un metodo estatico para crear una nueva instancia de mi dialogfragment con el inmueble a editar
    public static EditarInmuebleDialogFragment newInstance(Inmueble inmueble) {
        EditarInmuebleDialogFragment frag = new EditarInmuebleDialogFragment(); // creo una nueva instancia
        Bundle args = new Bundle(); // creo un bundle para pasar argumentos
        args.putString("inmueble_json", new Gson().toJson(inmueble)); // convierto el inmueble a json y lo pongo en el bundle
        frag.setArguments(args); // establezco los argumentos del fragmento
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // aqui inflo el layout usando el binding
        binding = DialogEditarInmuebleBinding.inflate(LayoutInflater.from(getContext()));
        View view = binding.getRoot(); // obtengo la vista raiz del binding


        // verifico si recibi argumentos y si contienen el inmueble_json
        if (getArguments() != null && getArguments().containsKey("inmueble_json")) {
            // si hay un inmueble, lo deserializo y cargo sus datos en los campos de la interfaz
            inmueble = new Gson().fromJson(getArguments().getString("inmueble_json"), Inmueble.class);
            binding.etDireccion.setText(inmueble.getDireccion()); // uso el binding para acceder a etDireccion
            binding.etUso.setText(inmueble.getUso()); // uso el binding para acceder a etUso
            binding.etAmbientes.setText(String.valueOf(inmueble.getAmbientes())); // uso el binding para acceder a etAmbientes
            binding.etValor.setText(String.valueOf(inmueble.getValor())); // uso el binding para acceder a etValor
            binding.swDisponible.setChecked(inmueble.getDisponible() != null && inmueble.getDisponible()); // uso el binding para acceder a swDisponible
        }

        // inicializo mi viewmodel de edicion y el viewmodel de la lista de inmuebles (compartido con la actividad)
        editarViewModel = new ViewModelProvider(this).get(EditarInmuebleViewModel.class);
        listaViewModel = new ViewModelProvider(requireActivity()).get(InmueblesViewModel.class);

        // observo el livedata del inmueble actualizado en mi viewmodel de edicion
        editarViewModel.getInmuebleActualizado().observe(this, actualizado -> {
            // si el inmueble se actualizo, muestro un toast, recargo la lista y cierro el dialog
            Toast.makeText(getContext(), "Inmueble actualizado", Toast.LENGTH_SHORT).show();
            listaViewModel.cargarInmuebles();
            dismiss();
        });
        // observo el livedata de errores en mi viewmodel de edicion
        editarViewModel.getError().observe(this, error -> {
            // si hay un error, muestro un toast con el mensaje de error
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        });

        // creo el builder para mi alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Editar inmueble") // establezco el titulo del dialog
                .setView(view) // establezco la vista que inflé con binding
                .setPositiveButton("Guardar", null) // establezco el boton positivo, null para manejar el clic despues
                .setNegativeButton("Cancelar", (dialog, which) -> dismiss()); // establezco el boton negativo para cerrar el dialog
        AlertDialog dialog = builder.create(); // creo el dialog

        // este listener se ejecuta cuando el dialog se muestra
        dialog.setOnShowListener(dlg -> {
            Button btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE); // obtengo el boton positivo del dialog
            // configuro el click listener para el boton guardar
            btn.setOnClickListener(v -> {
                // si tengo un inmueble para editar
                if (inmueble != null) {
                    // actualizo las propiedades del inmueble con los valores de los campos de texto
                    inmueble.setDireccion(binding.etDireccion.getText().toString()); // uso el binding
                    inmueble.setUso(binding.etUso.getText().toString()); // uso el binding
                    inmueble.setAmbientes(Integer.parseInt(binding.etAmbientes.getText().toString())); // uso el binding
                    inmueble.setValor(Double.parseDouble(binding.etValor.getText().toString())); // uso el binding
                    inmueble.setDisponible(binding.swDisponible.isChecked()); // uso el binding
                    // log para depuracion: muestro el inmueble actualizado en formato json
                    Log.d("EditarInmuebleDialog", "enviando inmueble actualizado: " + new Gson().toJson(inmueble));
                    // llamo al viewmodel para actualizar el inmueble
                    editarViewModel.actualizarInmueble(inmueble);
                }
            });
        });
        return dialog; // devuelvo el dialog creado
    }

    @Override
    // este metodo se llama cuando la vista del fragment se destruye
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // libero la referencia del binding para evitar fugas de memoria
    }
}
