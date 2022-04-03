package com.example.productscanner.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Benvenuto in ProductScanner!\nCrea la tua lista della spesa " +
                "e usa la telecamera per scannerizzare i prodotti che hai scelto.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}