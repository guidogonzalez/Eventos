package com.guidogonzalez.eventos.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guidogonzalez.eventos.databinding.ItemViewEventoBinding;
import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.EventosViewHolder> {

    private ArrayList<Evento> listaEventos;

    public EventosAdapter(ArrayList<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public void actualizarListaEventos(List<Evento> nuevaListaEventos) {
        listaEventos.clear();
        listaEventos.addAll(nuevaListaEventos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemViewEventoBinding view = ItemViewEventoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new EventosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventosViewHolder holder, int position) {

        String nombre = listaEventos.get(position).nombre;
        String descripcion = listaEventos.get(position).descripcion;
        List<String> fotoEvento = Arrays.asList(listaEventos.get(position).fotos.split(","));

        Utils.aplicarImagen(holder.itemView.ivFotoEvento.getContext(), Utils.URL_BASE_IMAGEN + fotoEvento.get(0), holder.itemView.ivFotoEvento);
        holder.itemView.tvNombreEvento.setText(nombre);
        holder.itemView.tvDescripcionEvento.setText(descripcion);
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public class EventosViewHolder extends RecyclerView.ViewHolder {

        public ItemViewEventoBinding itemView;

        public EventosViewHolder(ItemViewEventoBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }
}
