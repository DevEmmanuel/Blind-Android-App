package com.perchtech.humraz.blind;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.JsonElement;
import com.skyfishjy.library.RippleBackground;

import net.gotev.speech.Speech;

import java.util.ArrayList;
import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;


public class books extends ActionBarActivity implements AIListener{
    ArrayList<Card> cards = new ArrayList<Card>();
    private AIService aiService;
    private static String TAG = "PermissionDemo";
    private static final int RECORD_REQUEST_CODE = 101;
    String intent;
    RippleBackground rippleBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        Speech.getInstance().say("Please Select a book. Choose between Databases, Data Structures and Physics.");
        cards();
        rippleBackground=(RippleBackground)findViewById(R.id.content);
        final AIConfiguration config = new AIConfiguration("1e03efe2dfdc4a37a3e21e5d1056a4ec",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            makeRequest();
        }
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_REQUEST_CODE);
    }
    public void listenButtonOnClick(final View view) {
        Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(1500);
     aiService.startListening();
        rippleBackground.startRippleAnimation();
    }
    public void onResult(final AIResponse response) {
        Result result = response.getResult();



        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }
        intent= result.getAction();
        if(intent.equals("cam"))
        {
            Intent in = new Intent(this, OcrCaptureActivity.class);
            startActivity(in);
        }
        else if (intent.equals("calculator"))
        {
            Intent in = new Intent(this, calculator.class);
            startActivity(in);
        }

        else if(intent.equals("cn"))
        {
            Intent in = new Intent(this, cn.class);
            startActivity(in);
        }
        else if(intent.equals("data"))
        {
            Intent in = new Intent(this, physics.class);
            startActivity(in);
        }

    }
    @Override
    public void onError(final AIError error) {

    }
    @Override
    public void onListeningStarted() {}

    @Override
    public void onListeningCanceled() {}

    @Override
    public void onListeningFinished() {}

    @Override
    public void onAudioLevel(final float level) {}

    @Override
    public void onBackPressed() {
        Speech.getInstance().say("You Have Gone Back To The Previous Activity");
        Speech.getInstance().unregisterDelegate();
        super.onBackPressed();
    }

public void cards()
{

    Card card = new Card(getApplicationContext());
    CardHeader header = new CardHeader(getApplicationContext());
    header.setTitle("Databases");
    card.setTitle("Learn Database Management");
    card.setCardElevation(20);


    CardThumbnail thumb = new CardThumbnail(getApplicationContext());
    thumb.setDrawableResource(R.drawable.database);
    card.addCardThumbnail(thumb);
 //   card.setBackgroundResourceId(R.color.colorPrimary);
    card.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {

            Intent in = new Intent(books.this, cn.class);
            startActivity(in);
        }
    });
    card.addCardHeader(header);
    cards.add(card);


    Card card2 = new Card(getApplicationContext());
    CardHeader header2 = new CardHeader(getApplicationContext());
    header2.setTitle("Data Structures");
    card2.setTitle("Learn Data Structures In Computer Science");


    CardThumbnail thumb2 = new CardThumbnail(getApplicationContext());
    thumb2.setDrawableResource(R.drawable.struct);
    card2.addCardThumbnail(thumb2);
    card2.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {

            Intent in = new Intent(books.this, physics.class);
            startActivity(in);
        }
    });
    card2.addCardHeader(header2);
    //card2.setBackgroundResourceId(R.color.colorPrimary);
    cards.add(card2);

    Card card3 = new Card(getApplicationContext());
    CardHeader header3 = new CardHeader(getApplicationContext());
    header3.setTitle("Physics");
    card3.setTitle("Learn Physics");

   // card3.setBackgroundResourceId(R.color.colorPrimary);
    CardThumbnail thumb3 = new CardThumbnail(getApplicationContext());
    thumb3.setDrawableResource(R.drawable.physics);
    card3.addCardThumbnail(thumb3);
    card3.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {

            Intent in = new Intent(books.this, physics.class);
            startActivity(in);
        }
    });
    card3.addCardHeader(header3);
    cards.add(card3);
    CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getApplicationContext(),cards);

    CardListView listView = (CardListView) findViewById(R.id.myList);
    if (listView!=null){
        listView.setAdapter(mCardArrayAdapter);
    }
}


}




