package com.julian.proyectoinmobiliaria.draweractivity.ui.pagos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.julian.proyectoinmobiliaria.R;
import com.julian.proyectoinmobiliaria.model.Pagos;
import java.util.List;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.PagoViewHolder> {
    private List<Pagos> pagosList;

    public PagosAdapter(List<Pagos> pagosList) {
        this.pagosList = pagosList;
    }

    public void setPagosList(List<Pagos> pagosList) {
        this.pagosList = pagosList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pago, parent, false);
        return new PagoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagoViewHolder holder, int position) {
        Pagos pago = pagosList.get(position);
        holder.tvFecha.setText("Fecha: " + pago.fechaPago);
        holder.tvMonto.setText("Monto: $" + pago.monto);
        holder.tvDetalle.setText("Detalle: " + pago.detalle);
        holder.tvEstado.setText("Estado: " + (pago.estado ? "Pagado" : "Pendiente"));
    }

    @Override
    public int getItemCount() {
        return pagosList != null ? pagosList.size() : 0;
    }

    static class PagoViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvMonto, tvDetalle, tvEstado;
        public PagoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFechaPago);
            tvMonto = itemView.findViewById(R.id.tvMontoPago);
            tvDetalle = itemView.findViewById(R.id.tvDetallePago);
            tvEstado = itemView.findViewById(R.id.tvEstadoPago);
        }
    }
}

