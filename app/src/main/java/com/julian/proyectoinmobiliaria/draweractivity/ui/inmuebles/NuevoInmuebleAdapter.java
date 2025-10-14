package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.julian.proyectoinmobiliaria.model.Inmueble;
import com.julian.proyectoinmobiliaria.util.ManejoImagenes;
import com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles.NuevoInmuebleViewModel;

import java.util.List;

// aqui defino mi adaptador para el recyclerview de nuevos inmuebles.
public class NuevoInmuebleAdapter extends RecyclerView.Adapter<NuevoInmuebleAdapter.NuevoInmuebleViewHolder> {
    // declaro mis variables, un viewmodel para la logica y una lista de inmuebles.
    private final NuevoInmuebleViewModel viewModel;
    private List<Inmueble> inmuebles;

    // en el constructor, inicializo el viewmodel y la lista de inmuebles.
    // tambien observo los cambios en la lista de inmuebles del viewmodel para notificar al adaptador.
    public NuevoInmuebleAdapter(NuevoInmuebleViewModel viewModel) {
        this.viewModel = viewModel;
        this.inmuebles = viewModel.getInmueblesLiveData().getValue();
        viewModel.getInmueblesLiveData().observeForever(lista -> {
            this.inmuebles = lista;
            notifyDataSetChanged();
        });
    }

    // aqui es donde vinculo los datos de un inmueble a las vistas de un item del recyclerview.
    @Override
    public void onBindViewHolder(@NonNull NuevoInmuebleViewHolder holder, int position) {
        int pos = holder.getBindingAdapterPosition();
        // me aseguro de que la posicion sea valida.
        if (pos == RecyclerView.NO_POSITION || inmuebles == null || pos >= inmuebles.size()) return;
        try {
            // obtengo el inmueble en la posicion actual.
            Inmueble inmueble = inmuebles.get(pos);

            // aqui asigno los valores del inmueble a las vistas correspondientes.
            // y agrego listeners para actualizar el viewmodel cuando el usuario edita los campos.
            holder.etDireccion.setText(inmueble.getDireccion());
            holder.etDireccion.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int p = holder.getBindingAdapterPosition();
                    if (p != RecyclerView.NO_POSITION) {
                        viewModel.updateDireccion(p, s.toString());
                    }
                }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });

            holder.etUso.setText(inmueble.getUso());
            holder.etUso.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int p = holder.getBindingAdapterPosition();
                    if (p != RecyclerView.NO_POSITION) {
                        viewModel.updateUso(p, s.toString());
                    }
                }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });

            holder.etTipo.setText(inmueble.getTipo());
            holder.etTipo.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int p = holder.getBindingAdapterPosition();
                    if (p != RecyclerView.NO_POSITION) {
                        viewModel.updateTipo(p, s.toString());
                    }
                }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });

            holder.etAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
            holder.etAmbientes.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int p = holder.getBindingAdapterPosition();
                    if (p != RecyclerView.NO_POSITION) {
                        try { viewModel.updateAmbientes(p, Integer.parseInt(s.toString())); } catch (Exception ignored) {}
                    }
                }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });

            holder.etSuperficie.setText(String.valueOf(inmueble.getSuperficie()));
            holder.etSuperficie.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int p = holder.getBindingAdapterPosition();
                    if (p != RecyclerView.NO_POSITION) {
                        try { viewModel.updateSuperficie(p, Integer.parseInt(s.toString())); } catch (Exception ignored) {}
                    }
                }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });

            holder.etLatitud.setText(String.valueOf(inmueble.getLatitud()));
            holder.etLatitud.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int p = holder.getBindingAdapterPosition();
                    if (p != RecyclerView.NO_POSITION) {
                        try { viewModel.updateLatitud(p, Double.parseDouble(s.toString())); } catch (Exception ignored) {}
                    }
                }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });

            holder.etLongitud.setText(String.valueOf(inmueble.getLongitud()));
            holder.etLongitud.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int p = holder.getBindingAdapterPosition();
                    if (p != RecyclerView.NO_POSITION) {
                        try { viewModel.updateLongitud(p, Double.parseDouble(s.toString())); } catch (Exception ignored) {}
                    }
                }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });

            holder.etValor.setText(String.valueOf(inmueble.getValor()));
            holder.etValor.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int p = holder.getBindingAdapterPosition();
                    if (p != RecyclerView.NO_POSITION) {
                        try { viewModel.updateValor(p, Double.parseDouble(s.toString())); } catch (Exception ignored) {}
                    }
                }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });

            holder.cbDisponible.setChecked(inmueble.getDisponible() != null && inmueble.getDisponible());
            holder.cbDisponible.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int p = holder.getBindingAdapterPosition();
                if (p != RecyclerView.NO_POSITION) {
                    viewModel.updateDisponible(p, isChecked);
                }
            });

            holder.cbContrato.setChecked(inmueble.isTieneContratoVigente());
            holder.cbContrato.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int p = holder.getBindingAdapterPosition();
                if (p != RecyclerView.NO_POSITION) {
                    inmueble.setTieneContratoVigente(isChecked);
                }
            });

            // aqui muestro el id del inmueble y del propietario.
            holder.tvIdInmueble.setText(holder.tvIdInmueble.getContext().getString(
                com.julian.proyectoinmobiliaria.R.string.label_id_inmueble,
                inmueble.getIdInmueble()
            ));
            holder.tvIdPropietario.setText(holder.tvIdPropietario.getContext().getString(
                com.julian.proyectoinmobiliaria.R.string.label_id_propietario,
                inmueble.getIdPropietario()
            ));

            // aqui cargo la imagen del inmueble.
            ManejoImagenes.loadImage(inmueble.getImagen(), holder.ivImagen, "NuevoInmuebleAdapter");
        } catch (Exception e) {

        }
    }

    // aqui creo una nueva vista para un item del recyclerview.
    @Override
    public @NonNull NuevoInmuebleViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
        android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                .inflate(com.julian.proyectoinmobiliaria.R.layout.item_nuevo_inmueble, parent, false);
        return new NuevoInmuebleViewHolder(view);
    }

    // aqui devuelvo la cantidad de items en mi lista de inmuebles.
    @Override
    public int getItemCount() {
        return (inmuebles != null) ? inmuebles.size() : 0;
    }

    // esta es mi clase interna para el viewholder, que contiene las vistas de un item.
    public static class NuevoInmuebleViewHolder extends RecyclerView.ViewHolder {
        // declaro todas las vistas que usare en un item.
        TextView tvIdInmueble, tvIdPropietario;
        EditText etDireccion, etUso, etTipo, etAmbientes, etSuperficie, etLatitud, etLongitud, etValor;
        CheckBox cbDisponible, cbContrato;
        ImageView ivImagen;
        // en el constructor, inicializo todas las vistas encontrandolas por su id.
        public NuevoInmuebleViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            tvIdInmueble = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.tvIdInmueble);
            tvIdPropietario = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.tvIdPropietario);
            etDireccion = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.etDireccion);
            etUso = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.etUso);
            etTipo = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.etTipo);
            etAmbientes = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.etAmbientes);
            etSuperficie = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.etSuperficie);
            etLatitud = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.etLatitud);
            etLongitud = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.etLongitud);
            etValor = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.etValor);
            cbDisponible = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.cbDisponible);
            cbContrato = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.cbContrato);
            ivImagen = itemView.findViewById(com.julian.proyectoinmobiliaria.R.id.ivImagen);
        }
    }
}
