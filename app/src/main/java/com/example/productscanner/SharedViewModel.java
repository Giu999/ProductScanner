package com.example.productscanner;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SharedViewModel extends ViewModel {

    private Map<String, Boolean> shoppingList = new HashMap<>();


    public void setLista(HashMap<String,Boolean> list) {
        shoppingList = list;
    }

    public Map<String,Boolean> getLista() {
        if(shoppingList.isEmpty()) {
            shoppingList.put("barilla",Boolean.FALSE);
            shoppingList.put("birra",Boolean.FALSE);
        }
        return shoppingList;
    }
}
