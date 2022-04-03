package com.example.productscanner.ui.registra;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.productscanner.ListActivity;
import com.example.productscanner.R;
import com.example.productscanner.SharedViewModel;
import com.example.productscanner.databinding.FragmentRegistraBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegistraFragment extends Fragment{

    private RegistraViewModel registraViewModel;
    private SharedViewModel sharedViewModel;
    private FragmentRegistraBinding binding;

    //public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private TextView textView;
    private ImageView micButton;
    private LinearLayout lista;
    private List<String> allowedWords = Arrays.asList( "barilla", "rummo", "dececco", "birra", "acqua", "banana", "cipolla", "pane", "detersivo", "formaggio",
        "carote", "pomodoro");
    private Map<String,Boolean> shoppingList;
    private boolean attivo = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registraViewModel =
                new ViewModelProvider(this).get(RegistraViewModel.class);

        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        shoppingList = sharedViewModel.getLista();

        binding = FragmentRegistraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }

        textView = binding.textRegistra;
        micButton = binding.buttonRegistra;
        lista = binding.lista;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.getContext(), ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"));

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "it-IT");
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "it-IT");

        inizializzaLista();
        textView.setText("Premi il microfono per parlare");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                textView.setText("");
                textView.setHint("Sono in ascolto dell'utente...");
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                textView.setText("");
            }

            @Override
            public void onError(int error) {
                textView.setText("C'è stato un errore, riprova");
            }

            @Override
            public void onResults(Bundle bundle) {
                micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String word = data.get(0).toLowerCase(Locale.ROOT);
                textView.setText(word);
                if (allowedWords.contains(word) && !shoppingList.keySet().contains(word)) {
                    shoppingList.put(word,Boolean.FALSE);
                    LinearLayout newLine = new LinearLayout(getContext());
                    //newLine.setLayoutParams(params);
                    newLine.setOrientation(LinearLayout.HORIZONTAL);
                    newLine.setId(shoppingList.size());

                    TextView txt = new TextView(getContext());
                    //txt.setLayoutParams(params);
                    txt.setText(word);
                    txt.setId(shoppingList.size()+200);

                    Button btn = new Button(getContext());

                    //btn.setLayoutParams(params);
                    btn.setText("RIMUOVI");
                    btn.setId(shoppingList.size()+500);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewGroup linea = (ViewGroup) v.getParent();;
                            for (int itemPos = 0; itemPos < linea.getChildCount(); itemPos++) {
                                View view = linea.getChildAt(itemPos);
                                if (view instanceof TextView) {
                                    shoppingList.remove(((TextView) view).getText()); //Found it!
                                    break;
                                }
                            }
                            //lista.removeView((View) btn.getParent());
                            lista.removeView((View) btn.getParent());
                        }
                    });
                    newLine.addView(txt);
                    newLine.addView(btn);
                    lista.addView(newLine);
                    Toast.makeText(getContext(), "Il prodotto: " + word + " è stato aggiunto con successo all lista della spesa.", Toast.LENGTH_LONG).show();
                }
                else if(!allowedWords.contains(word)) {
                    Toast.makeText(getContext(), "La parola/frase: " + word + " non può essere accettata!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "La parola/frase: " + word + " è già presente nella lista!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }

        });

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attivo==true){
                    attivo=false;
                    micButton.setImageResource(R.drawable.ic_mic_black_off);
                    speechRecognizer.stopListening();
                }else if (attivo==false){
                    attivo=true;
                    micButton.setImageResource(R.drawable.ic_mic_red);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        speechRecognizer.destroy();
    }

    public void inizializzaLista() {
        int countVal=0;
        for (String val : shoppingList.keySet()) {
            LinearLayout newLine = new LinearLayout(getContext());
            //newLine.setLayoutParams(params);
            newLine.setOrientation(LinearLayout.HORIZONTAL);
            newLine.setId(countVal);

            TextView txt = new TextView(getContext());
            //txt.setLayoutParams(params);
            txt.setText(val);
            if(shoppingList.get(val).booleanValue()==true) {
                txt.setPaintFlags(txt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            txt.setId(countVal+200);

            newLine.addView(txt);

            if(shoppingList.get(val).booleanValue()==false) {

                Button btn = new Button(getContext());
                //btn.setLayoutParams(params);
                btn.setText("RIMUOVI");
                btn.setId(countVal + 500);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewGroup linea = (ViewGroup) v.getParent();
                        ;
                        for (int itemPos = 0; itemPos < linea.getChildCount(); itemPos++) {
                            View view = linea.getChildAt(itemPos);
                            if (view instanceof TextView) {
                                shoppingList.remove(((TextView) view).getText()); //Found it!
                                break;
                            }
                        }
                        //lista.removeView((View) btn.getParent());
                        lista.removeView((View) btn.getParent());
                    }
                });

                newLine.addView(btn);
            }

            lista.addView(newLine);
            countVal++;
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.println(Log.WARN,"Viva dio","Bravo");
                } else {
                    Log.println(Log.WARN,"Mannaggia a maronna","Dio cane");
                }
            });
}