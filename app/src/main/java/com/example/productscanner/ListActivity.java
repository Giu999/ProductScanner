package com.example.productscanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends Activity {
    private ArrayList<String> shoppingList;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_registra);
        layout = findViewById(R.id.lista);
        shoppingList = this.getIntent().getStringArrayListExtra("shoppingList");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int countVal=0;
        for (String val : shoppingList) {
            LinearLayout newLine = new LinearLayout(this);
            newLine.setLayoutParams(params);
            newLine.setOrientation(LinearLayout.HORIZONTAL);
            newLine.setId(500+countVal);

            TextView txt = new TextView(this);
            txt.setLayoutParams(params);
            txt.setText(val);
            txt.setId(countVal+100);

            Button btn = new Button(this);

            btn.setLayoutParams(params);
            btn.setText("DELETE ITEM");
            btn.setId(countVal);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.removeViewAt(500+(btn.getId()));  //gestire rimozione del linear layout newLine
                    shoppingList.remove(btn.getId());      //quando elimino i prodotti, quelli seguenti all'eliminato scalano di indice
                }
            });

            newLine.addView(txt);
            newLine.addView(btn);
            layout.addView(newLine);
            countVal++;
        }
    }
}
