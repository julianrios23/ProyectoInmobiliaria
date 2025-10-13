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

public class NuevoInmuebleAdapter extends RecyclerView.Adapter<NuevoInmuebleAdapter.NuevoInmuebleViewHolder> {
    private final NuevoInmuebleViewModel viewModel;
    private List<Inmueble> inmuebles;

    public NuevoInmuebleAdapter(NuevoInmuebleViewModel viewModel) {
        this.viewModel = viewModel;
        this.inmuebles = viewModel.getInmueblesLiveData().getValue();
        viewModel.getInmueblesLiveData().observeForever(lista -> {
            this.inmuebles = lista;
            notifyDataSetChanged();
        });
    }

    @Override
    public void onBindViewHolder(@NonNull NuevoInmuebleViewHolder holder, int position) {
        int pos = holder.getBindingAdapterPosition();
        if (pos == RecyclerView.NO_POSITION || inmuebles == null || pos >= inmuebles.size()) return;
        try {
            Inmueble inmueble = inmuebles.get(pos);
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
            holder.tvIdInmueble.setText(holder.tvIdInmueble.getContext().getString(
                com.julian.proyectoinmobiliaria.R.string.label_id_inmueble,
                inmueble.getIdInmueble()
            ));
            holder.tvIdPropietario.setText(holder.tvIdPropietario.getContext().getString(
                com.julian.proyectoinmobiliaria.R.string.label_id_propietario,
                inmueble.getIdPropietario()
            ));
            ManejoImagenes.loadImage(inmueble.getImagen(), holder.ivImagen, "NuevoInmuebleAdapter");
        } catch (Exception e) {
            Log.e("NuevoInmuebleAdapter", "Error en onBindViewHolder pos=" + pos, e);
        }
    }

    @Override
    public @NonNull NuevoInmuebleViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
        android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                .inflate(com.julian.proyectoinmobiliaria.R.layout.item_nuevo_inmueble, parent, false);
        return new NuevoInmuebleViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (inmuebles != null) ? inmuebles.size() : 0;
    }

    public static class NuevoInmuebleViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdInmueble, tvIdPropietario;
        EditText etDireccion, etUso, etTipo, etAmbientes, etSuperficie, etLatitud, etLongitud, etValor;
        CheckBox cbDisponible, cbContrato;
        ImageView ivImagen;
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
