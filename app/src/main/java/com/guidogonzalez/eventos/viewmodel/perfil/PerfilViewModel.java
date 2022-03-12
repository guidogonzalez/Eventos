package com.guidogonzalez.eventos.viewmodel.perfil;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.guidogonzalez.eventos.api.ApiService;
import com.guidogonzalez.eventos.model.Usuario;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PerfilViewModel extends AndroidViewModel {

    public MutableLiveData<Usuario> mldConsultarUsuarioResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> bConsultarUsuarioError = new MutableLiveData<>();
    public MutableLiveData<Boolean> bConsultarUsuarioCargando = new MutableLiveData<>();

    public MutableLiveData<Usuario> mldActualizarResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> bActualizarError = new MutableLiveData<>();
    public MutableLiveData<Boolean> bActualizarCargando = new MutableLiveData<>();

    private ApiService apiService = new ApiService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public void consultarUsuario(String idUsuario) {

        bConsultarUsuarioCargando.setValue(true);

        // Cuando obtenemos los datos de la API, no queremos hacerlo en el hilo principal de la aplicación para no bloquearla
        compositeDisposable.add(
                apiService.consultarUsuario(idUsuario)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Usuario>() {

                            @Override
                            public void onSuccess(Usuario usuario) {
                                consultaUsuario(usuario);
                            }

                            @Override
                            public void onError(Throwable e) {
                                bConsultarUsuarioError.setValue(true);
                                bConsultarUsuarioCargando.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    public void actualizarUsuario(String idUsuario,
                                  RequestBody nombre,
                                  RequestBody apellidos,
                                  RequestBody email,
                                  RequestBody fechaNacimiento,
                                  MultipartBody.Part foto,
                                  RequestBody contrasena) {

        bActualizarCargando.setValue(true);

        // Cuando obtenemos los datos de la API, no queremos hacerlo en el hilo principal de la aplicación para no bloquearla
        compositeDisposable.add(
                apiService.actualizarUsuario(idUsuario, nombre, apellidos, email, fechaNacimiento, foto, contrasena)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Usuario>() {

                            @Override
                            public void onSuccess(Usuario usuario) {
                                actualizarUsuario(usuario);
                            }

                            @Override
                            public void onError(Throwable e) {
                                bActualizarError.setValue(true);
                                bActualizarCargando.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    private void consultaUsuario(Usuario usuario) {
        mldConsultarUsuarioResponse.setValue(usuario);
        bConsultarUsuarioError.setValue(false);
        bConsultarUsuarioCargando.setValue(false);
    }

    private void actualizarUsuario(Usuario usuario) {
        mldActualizarResponse.setValue(usuario);
        bActualizarError.setValue(false);
        bActualizarCargando.setValue(false);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}