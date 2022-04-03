package com.example.productscanner.ui.rileva;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RilevaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RilevaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Viva gesù");
    }

    public LiveData<String> getText() {
        return mText;
    }
}