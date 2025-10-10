package com.julian.proyectoinmobiliaria.draweractivity.ui.contratos;

import static com.julian.proyectoinmobiliaria.service.ApiService.BASE_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.model.Contrato;
import com.julian.proyectoinmobiliaria.util.ManejoImagenes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContratoVigenteAdapter extends RecyclerView.Adapter<ContratoVigenteAdapter.ViewHolder> {

    private List<Contrato> contratos;

    // Constructor: inicializa el adapter con la lista de contratos
    public ContratoVigenteAdapter(List<Contrato> contratos) {
        this.contratos = contratos;
    }

    // Actualiza la lista y notifica los cambios
    public void setContratos(List<Contrato> contratos) {
        this.contratos = contratos;
        notifyDataSetChanged();
    }

    // Crea el ViewHolder inflando el layout correspondiente
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contratovigente, parent, false);
        return new ViewHolder(view);
    }

    // Vincula los datos del contrato con los elementos de la vista
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Contrato contrato = contratos.get(position);

            // muestro los datos del inmueble
            holder.tvDireccion.setText("Dirección: " + contrato.inmueble.getDireccion());
            holder.tvUso.setText("Uso: " + contrato.inmueble.getUso());
            holder.tvTipo.setText("Tipo: " + contrato.inmueble.getTipo());
            holder.tvAmbientes.setText("Ambientes: " + contrato.inmueble.getAmbientes());
            holder.tvValor.setText("Valor: $ " + contrato.inmueble.getValor());
            // muestro los datos del contrato

            holder.tvContratoVigente.setText("Contrato Vigente: " + (contrato.estado ? "Sí" : "No"));
            // muestro fechas y monto
            holder.tvFechaInicio.setText("Inicio: " + contrato.fechaInicio);
            holder.tvFechaFin.setText("Fin: " + contrato.fechaFinalizacion);
            // aqui muestro el nombre y apellido del inquilino usando los campos publicos
            holder.tvInquilino.setText("Inquilino: " + contrato.inquilino.nombre + " " + contrato.inquilino.apellido);
            // aqui uso el metodo loadImage para mostrar la imagen del inmueble
            ManejoImagenes.loadImage(contrato.inmueble.getImagen(), holder.ivImagen, "ContratoVigenteAdapter");
            // Manejo del click en btnPagos
            holder.btnPagos.setOnClickListener(v -> {
                SharedPreferences sp = v.getContext().getSharedPreferences("datos", Context.MODE_PRIVATE);
                java.util.Map<String, ?> allPrefs = sp.getAll();
                for (String key : allPrefs.keySet()) {
                    Log.d("btnPagos", "sharedpreferences key: " + key + ", value: " + allPrefs.get(key));
                }
                String token = sp.getString("token", "");
                if (token.isEmpty()) {
                    token = sp.getString("Token", "");
                }
                if (token.isEmpty() && allPrefs.containsKey("token")) {
                    token = String.valueOf(allPrefs.get("token"));
                }
                if (token.isEmpty() && allPrefs.containsKey("Token")) {
                    token = String.valueOf(allPrefs.get("Token"));
                }
                Log.d("btnPagos", "token final usado: " + token);
                Bundle bundle = new Bundle();
                bundle.putInt("idContrato", contrato.idContrato);
                bundle.putString("token", token);
                Log.d("btnPagos", "navegando a pagosFragment con idContrato: " + contrato.idContrato + ", token: " + token);
                try {
                    Navigation.findNavController(v).navigate(R.id.pagosFragment, bundle);
                    Log.d("btnPagos", "navegacion a pagosFragment exitosa");
                } catch (Exception e) {
                    Log.e("btnPagos", "error al navegar a pagosFragment: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("ContratoVigenteAdapter", "Error en onBindViewHolder: " + e.getMessage());
        }
    }

    // Devuelve la cantidad de contratos
    @Override
    public int getItemCount() {
        return contratos != null ? contratos.size() : 0;
    }

    // Define el ViewHolder con los elementos de la vista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImagen;
        TextView tvDireccion, tvUso, tvTipo, tvAmbientes, tvValor, tvDisponible, tvContratoVigente, tvFechaInicio, tvFechaFin, tvMonto, tvInquilino;
        Button btnPagos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvUso = itemView.findViewById(R.id.tvUso);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvAmbientes = itemView.findViewById(R.id.tvAmbientes);
            tvValor = itemView.findViewById(R.id.tvValor);
            tvDisponible = itemView.findViewById(R.id.tvDisponible);
            tvContratoVigente = itemView.findViewById(R.id.tvContratoVigente);
            tvFechaInicio = itemView.findViewById(R.id.tvFechaInicio);
            tvFechaFin = itemView.findViewById(R.id.tvFechaFin);
            tvInquilino = itemView.findViewById(R.id.tvInquilino);
            btnPagos = itemView.findViewById(R.id.btnPagos);
        }
    }
}
