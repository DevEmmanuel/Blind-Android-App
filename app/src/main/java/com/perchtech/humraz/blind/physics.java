package com.perchtech.humraz.blind;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyfishjy.library.RippleBackground;

import net.gotev.speech.Speech;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;


public class physics extends ActionBarActivity {
    ArrayList<Card> cards = new ArrayList<Card>();
    ArrayList<String> posts = new ArrayList<String>();
    ArrayList<String> times = new ArrayList<String>();
    int i;
    RippleBackground rippleBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physics);
        Firebase.setAndroidContext(this);
        Speech.getInstance().say("You Are In The Data Structures Page.");
        read();
        rippleBackground=(RippleBackground)findViewById(R.id.content);
    }

    public void read() {
        final Firebase ref = new Firebase("https://adaa-45b17.firebaseio.com/datastruct");
        //Value event listener for realtime data update


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersSnapshot) {

                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    book forums = userSnapshot.getValue(book.class);
                    String forumname = forums.getPost();
                    String forumdesc = forums.getTime();
                    posts.add(forumname);
                    times.add(forumdesc);

                }
                i = 0;
                View view = null;
                cardpop(view);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    public void cardpop(View view) {
        if(i<posts.size()) {
            Card card = new Card(getApplicationContext());
            final String post = posts.get(i);
            final String time = times.get(i);
            CardHeader header = new CardHeader(getApplicationContext());
            header.setTitle(time);
            card.setTitle(post);

            Speech.getInstance().say(post);
            card.addCardHeader(header);
            cards.add(card);

            i++;
        }
        else
            Speech.getInstance().say("This is the end of the lesson on Data Structures");
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getApplicationContext(), cards);

        CardListView listView = (CardListView) findViewById(R.id.myList);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }


    @Override
    public void onBackPressed() {
        Speech.getInstance().say("");
        Speech.getInstance().unregisterDelegate();
        super.onBackPressed();
    }
}
