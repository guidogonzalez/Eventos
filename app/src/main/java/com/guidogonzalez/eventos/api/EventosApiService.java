package com.guidogonzalez.eventos.api;

import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.utils.Utils;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Part;

public class EventosApiService {

    private EventosApi api;

    public EventosApiService() {
        api = new Retrofit.Builder()
                .baseUrl(Utils.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(EventosApi.class);
    }

    public Single<List<Evento>> getEventos() {
        return api.getEventos();
    }

    public Single<Evento> nuevoEvento(RequestBody nombre,
                                      RequestBody descripcion,
                                      RequestBody fechaEvento,
                                      MultipartBody.Part fotos,
                                      RequestBody precio,
                                      RequestBody idCreador) {
        return api.nuevoEvento(nombre, descripcion, fechaEvento, fotos, precio, idCreador);
    }
}
