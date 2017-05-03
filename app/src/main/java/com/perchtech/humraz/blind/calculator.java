package com.perchtech.humraz.blind;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonElement;

import net.gotev.speech.Speech;

import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;


public class calculator extends ActionBarActivity  implements AIListener {
    private TextView resultTextView;
    private AIService aiService;
    private static String TAG = "PermissionDemo";
    private static final int RECORD_REQUEST_CODE = 101;
    String intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);


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
}
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_REQUEST_CODE);
    }
    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }
    public void onResult(final AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
           }
        }

        // Show results in TextView.
        resultTextView.setText(result.getResolvedQuery() +
             //   "\nAction: " + result.getAction() +
                "\nResult: " + result.getFulfillment().getSpeech());
        intent= result.getAction();
        if(intent.equals("cam"))
        {
            Intent in = new Intent(this, OcrCaptureActivity.class);
            startActivity(in);
        }
        // Show results in TextView.
       // resultTextView.setText(
               /* "\nAction: " + result.getAction() +*/
    //            "Solution To Your Problem:\n\n" + parameterString);
        Speech.getInstance().say("Result is"+result.getFulfillment().getSpeech());
    }
    @Override
    public void onError(final AIError error) {
        resultTextView.setText(error.toString());
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

}
