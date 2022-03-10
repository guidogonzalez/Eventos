package com.guidogonzalez.eventos.viewmodel.nuevoevento;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.guidogonzalez.eventos.api.EventosApiService;
import com.guidogonzalez.eventos.model.Evento;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

public class NuevoEventoViewModel extends AndroidViewModel {


    public MutableLiveData<Evento> mldEvento = new MutableLiveData<>();
    public MutableLiveData<Boolean> bEventoErrorCargar = new MutableLiveData<>();
    public MutableLiveData<Boolean> bEventoCargando = new MutableLiveData<>();

    private EventosApiService eventosApiService = new EventosApiService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public NuevoEventoViewModel(@NonNull Application application) {
        super(application);
    }

    public void crearEvento(RequestBody nombre,
                            RequestBody descripcion,
                            RequestBody fechaEvento,
                            MultipartBody.Part fotos,
                            RequestBody precio,
                            RequestBody idCreador) {

        bEventoCargando.setValue(true);

        compositeDisposable.add(
                eventosApiService.nuevoEvento(nombre, descripcion, fechaEvento, fotos, precio, idCreador)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Evento>() {

                            @Override
                            public void onSuccess(Evento evento) {
                                eventoCreado(evento);

                                Log.i("SUCCESS", evento.toString());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i("ERROR", e.getMessage());
                                bEventoErrorCargar.setValue(true);
                                bEventoCargando.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    private void eventoCreado(Evento evento) {

        mldEvento.setValue(evento);
        bEventoErrorCargar.setValue(false);
        bEventoCargando.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}