package com.example.productscanner.ui.registra;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistraViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RegistraViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Questo è il fragmento per registrare");
    }

    public LiveData<String> getText() {
        return mText;
    }
}