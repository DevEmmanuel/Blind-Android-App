package com.perchtech.humraz.blind;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class TeachActivity extends AppCompatActivity {

    private final int SPEECH_RECOGNITION_CODE = 1;

    private ImageView speak;
    private TextToSpeech tts1,tts;
    private String Index="Reading Book Index for you..  1.Introduction.   2.Theory.   3.Examples. 4.Repeat.  Please tap on screen and say your option";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach);
        speak = (ImageView) findViewById(R.id.btn);
        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d("OnInitListener", "Text to speech engine started successfully.");
                            tts1.setLanguage(Locale.US);
                        } else {
                            Log.d("OnInitListener", "Error starting the text to speech engine.");
                        }
                    }
                };
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                    tts.speak(Index, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
       /* tts1 = new TextToSpeech(this.getApplicationContext(), listener);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.speak("Reading Index For you", TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 1000);*/

        speak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Tell Book Info");
                try {
                    startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Speech recognition is not supported in this device.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        tts.speak("", TextToSpeech.QUEUE_ADD, null, "DEFAULT");
        super.onBackPressed();
    }
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    Toast.makeText(getApplicationContext(),
                            text,
                            Toast.LENGTH_SHORT).show();
                    text = text.replaceAll("\\D+","");
                    switch (text)
                    {
                        case "1": tts.speak("A general-purpose database management system is designed to allow the definition.", TextToSpeech.QUEUE_ADD, null); break;
                        case "2": tts.speak("DBMS are super.", TextToSpeech.QUEUE_ADD, null); break;
                        case "3": tts.speak("Thank you and have a nice day.", TextToSpeech.QUEUE_ADD, null); break;
                        case "4": tts.speak(Index, TextToSpeech.QUEUE_ADD, null); break;

                    }
                }
                break;
            }

        }
    }
}
