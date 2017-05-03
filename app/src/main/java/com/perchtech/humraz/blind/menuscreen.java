package com.perchtech.humraz.blind;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.skyfishjy.library.RippleBackground;

import net.gotev.speech.Speech;

import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;


public class menuscreen extends ActionBarActivity implements AIListener {
    private Button listenButton;
    private TextView resultTextView;
    private AIService aiService;
    private static String TAG = "PermissionDemo";
    private static final int RECORD_REQUEST_CODE = 101;
    String intent;
    RippleBackground rippleBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuscreen);
        Speech.getInstance().say("You Are In The Main Menu. Touch The Top Most Portion Of The screen to speak");
       rippleBackground=(RippleBackground)findViewById(R.id.content);

        resultTextView = (TextView) findViewById(R.id.resultTextView);
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
        scheduleAlarm();
    }
    public void scheduleAlarm() {

        Intent intent = new Intent(getApplicationContext(), MyAlarmReciever.class);

        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReciever.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                1, pIntent);
    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_REQUEST_CODE);
    }
    public void listenButtonOnClick(final View view) {
        aiService.startListening();
        rippleBackground.startRippleAnimation();
        Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 500 milliseconds
        v.vibrate(1500);
    }
    public void onResult(final AIResponse response) {
        Result result = response.getResult();


        rippleBackground.stopRippleAnimation();
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
        else if (intent.equals("books"))
        {
            Intent in = new Intent(this, books.class);
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
        resultTextView.setText(
"You Asked: " +result.getResolvedQuery()+
                "\n\nY.EYE Says: " + result.getFulfillment().getSpeech());
        Speech.getInstance().say(result.getFulfillment().getSpeech());
    }
    @Override
    public void onError(final AIError error) {
        resultTextView.setText(error.toString());
    }
    @Override
    public void onListeningStarted() {}

    public void book(View view)
    {
       Intent in = new Intent(this, books.class);
        startActivity(in);
    }
    public void calc(View view)
    {
        Intent in = new Intent(this, calculator.class);
        startActivity(in);
    }
    public void LIB(View view)
    {
        Intent in = new Intent(this, libraryact.class);
        startActivity(in);
    }
    public void teach(View view)
    {
        Intent in = new Intent(this, TeachActivity.class);
        startActivity(in);
    }
    public void recognition(View view)
    {
        Intent in = new Intent(this, OcrCaptureActivity.class);
        startActivity(in);
    }

    @Override
    public void onListeningCanceled() {}

    @Override
    public void onListeningFinished() {}

    @Override
    public void onAudioLevel(final float level) {}

    @Override
    public void onBackPressed() {
       Speech.getInstance().say("You Have Closed The Application");
        Speech.getInstance().unregisterDelegate();
        super.onBackPressed();
    }


}
