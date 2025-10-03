package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.model.Inmueble;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InmueblesAdapter extends RecyclerView.Adapter<InmueblesAdapter.InmuebleViewHolder> {
    private List<Inmueble> inmuebles;

    public InmueblesAdapter(List<Inmueble> inmuebles) {
        this.inmuebles = inmuebles;
    }

    public void setInmuebles(List<Inmueble> inmuebles) {
        this.inmuebles = inmuebles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inmueble, parent, false);
        return new InmuebleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {
        Inmueble inmueble = inmuebles.get(position);
        holder.tvDireccion.setText("Dirección: " + inmueble.getDireccion());
        holder.tvUso.setText("Uso: " + inmueble.getUso());
        holder.tvTipo.setText("Tipo: " + inmueble.getTipo());
        holder.tvAmbientes.setText("Ambientes: " + inmueble.getAmbientes());
        holder.tvSuperficie.setText("Superficie: " + inmueble.getSuperficie());
        holder.tvLatitud.setText("Latitud: " + inmueble.getLatitud());
        holder.tvLongitud.setText("Longitud: " + inmueble.getLongitud());
        holder.tvValor.setText("Valor: $" + inmueble.getValor());
        holder.tvDisponible.setText("Disponible: " + (inmueble.getDisponible() != null && inmueble.getDisponible() ? "Sí" : "No"));
        holder.tvContratoVigente.setText("Contrato vigente: " + (inmueble.isTieneContratoVigente() ? "Sí" : "No"));
        if (inmueble.getDuenio() != null) {
            holder.tvDuenio.setText("Dueño: " + inmueble.getDuenio().getNombre());
        } else {
            holder.tvDuenio.setText("Dueño: -");
        }
        if (inmueble.getImagen() != null && !inmueble.getImagen().isEmpty()) {
            Picasso.get().load(inmueble.getImagen().replace("\\", "/")).into(holder.ivImagen);
        } else {
            holder.ivImagen.setImageResource(R.drawable.home);
        }
    }

    @Override
    public int getItemCount() {
        return inmuebles != null ? inmuebles.size() : 0;
    }

    static class InmuebleViewHolder extends RecyclerView.ViewHolder {
        TextView tvDireccion, tvUso, tvTipo, tvAmbientes, tvSuperficie, tvLatitud, tvLongitud, tvValor, tvDisponible, tvContratoVigente, tvDuenio;
        ImageView ivImagen;
        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvUso = itemView.findViewById(R.id.tvUso);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvAmbientes = itemView.findViewById(R.id.tvAmbientes);
            tvSuperficie = itemView.findViewById(R.id.tvSuperficie);
            tvLatitud = itemView.findViewById(R.id.tvLatitud);
            tvLongitud = itemView.findViewById(R.id.tvLongitud);
            tvValor = itemView.findViewById(R.id.tvValor);
            tvDisponible = itemView.findViewById(R.id.tvDisponible);
            tvContratoVigente = itemView.findViewById(R.id.tvContratoVigente);
            tvDuenio = itemView.findViewById(R.id.tvDuenio);
            ivImagen = itemView.findViewById(R.id.ivImagen);
        }
    }
}
