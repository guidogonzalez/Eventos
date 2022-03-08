package com.guidogonzalez.eventos.viewmodel.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.guidogonzalez.eventos.api.EventosApiService;
import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.model.ListaEventos;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {

    public MutableLiveData<List<Evento>> listaEventos = new MutableLiveData<>();
    public MutableLiveData<Boolean> bEventoErrorCargar = new MutableLiveData<>();
    public MutableLiveData<Boolean> bEventoCargando = new MutableLiveData<>();

    private EventosApiService eventosApiService = new EventosApiService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void cargarEventos() {
        bEventoCargando.setValue(true);

        compositeDisposable.add(
                eventosApiService.getEventos()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<Evento>>() {

                            @Override
                            public void onSuccess(List<Evento> bebidas) {
                                eventosRecibidos(bebidas);

                                Log.i("SUCCESS", bebidas.toString());
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

    private void eventosRecibidos(List<Evento> listaBebidas) {

        listaEventos.setValue(listaBebidas);
        bEventoErrorCargar.setValue(false);
        bEventoCargando.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}