package com.guidogonzalez.eventos.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.guidogonzalez.eventos.R;
import com.guidogonzalez.eventos.databinding.ItemViewEventoBinding;
import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.utils.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
        Date fechaEvento = listaEventos.get(position).fechaEvento;
        Integer precio = listaEventos.get(position).precio;
        String idCreador = listaEventos.get(position).idCreador;
        String nombreCreador = listaEventos.get(position).nombreCreador;
        String fotoCreador = listaEventos.get(position).fotoCreador;
        List<String> fotoEvento = Arrays.asList(listaEventos.get(position).fotos.split(","));

        // Si somos el creador del evento mostramos el botón de opciones para editar y eliminar
        if (idCreador == Utils.obtenerValorSharedPreferences(holder.itemView.getRoot().getContext(), "idUsuario")) {
            holder.itemView.ivPopUpMenu.setVisibility(View.VISIBLE);
        }

        Utils.aplicarImagen(holder.itemView.ivFotoEvento.getContext(), Utils.URL_BASE_IMAGEN + fotoEvento.get(0), holder.itemView.ivFotoEvento);
        holder.itemView.tvNombreEvento.setText(nombre);
        holder.itemView.tvFechaEvento.setText(Utils.transformarDatetimeBd(fechaEvento));
        holder.itemView.tvDescripcionEvento.setText(descripcion);
        holder.itemView.tvPrecioEvento.setText(precio + "€");
        holder.itemView.tvCreador.setText(nombreCreador);
        Utils.aplicarImagen(holder.itemView.civFotoCreador.getContext(), Utils.URL_BASE_IMAGEN + fotoCreador, holder.itemView.civFotoCreador);

        holder.itemView.ivPopUpMenu.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(holder.itemView.ivPopUpMenu.getContext(), holder.itemView.ivPopUpMenu);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(item -> {

                switch (item.getItemId()) {
                    case R.id.menu_editar:
                        Bundle bundle = new Bundle();
                        bundle.putString("idEvento", listaEventos.get(position)._id);
                        Navigation.findNavController(holder.itemView.ivPopUpMenu).navigate(R.id.action_listaEventosFragment_to_editarEventoFragment, bundle);
                        break;
                    case R.id.menu_eliminar:
                        break;
                }
                return true;
            });
            try {
                // Para que sea un menú con íconos
                Field[] fields = popupMenu.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popupMenu);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper
                                .getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod(
                                "setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
            }

            popupMenu.show();
        });

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
