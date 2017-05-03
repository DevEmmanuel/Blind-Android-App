/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.perchtech.humraz.blind;

import android.annotation.TargetApi;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;

import com.perchtech.humraz.blind.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class librarydetector implements Detector.Processor<TextBlock> {


    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private TextToSpeech tts;
    public String SingBookName;
    public ArrayList<String> BookName = new ArrayList<>();
    int i;


    librarydetector(GraphicOverlay<OcrGraphic> ocrGraphicOverlay) {
        mGraphicOverlay = ocrGraphicOverlay;
    }


    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {

        mGraphicOverlay.clear();
        BookName.clear();

        SparseArray<TextBlock> items = detections.getDetectedItems();

/*
        for (i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                Log.d("OcrDetectorProcessor", "Text detected! " + item.getValue());
                BookName.add(item.getValue().toString());
            }
            tts = new TextToSpeech(OcrCaptureActivity.getContext(), new TextToSpeech.OnInitListener() {

                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        tts.setLanguage(Locale.US);
                        tts.speak(BookName.get(i).toString(), TextToSpeech.QUEUE_FLUSH, null);

                    }

                }
            });*/
        for ( i = 0; i < items.size(); ++i) {

            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                Log.d("OcrDetectorProcessor", "Text detected! " + item.getValue());
                SingBookName=item.getValue().toString();
                tts = new TextToSpeech(libraryact.getContext(), new TextToSpeech.OnInitListener() {

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            tts.setLanguage(Locale.US);
                            // tts.speak(SingBookName, TextToSpeech.QUEUE_FLUSH, null);
                            tts.speak(SingBookName, TextToSpeech.QUEUE_FLUSH, null, "DEFAULT");

                            SingBookName="";
                        }
                    }
                });
            }

            OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);


        }
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }
}
