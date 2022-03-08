package com.guidogonzalez.eventos.viewmodel.nuevoevento;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NuevoEventoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NuevoEventoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}