package com.guidogonzalez.eventos.api;

import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.model.ListaEventos;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EventosApi {

    @GET("eventos")
    Single<List<Evento>> getEventos();

    @POST("nuevoEvento")
    Observable<Evento> nuevoEvento(@Field("nombre") String nombre,
                                   @Field("descripcion") String descripcion,
                                   @Field("fechaEvento") Date fechaEvento,
                                   @Field("fotos") String fotos,
                                   @Field("precio") Integer precio);

}
