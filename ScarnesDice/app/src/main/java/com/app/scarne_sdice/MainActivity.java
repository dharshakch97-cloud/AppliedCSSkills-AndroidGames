package com.app.scarne_sdice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.RunnableFuture;
import java.util.*;
public class MainActivity extends AppCompatActivity {

    int user_oscore = 0;
    int user_tscore = 0;
    int comp_oscore = 0;
    int comp_tscore = 0;

    ImageView iv;
    TextView tv1, tv2;
    Button b1, b2, b3;

    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.image_view);
        tv1 = (TextView) findViewById(R.id.textview1);
        tv2 = (TextView) findViewById(R.id.textview2);
        b1 = (Button) findViewById(R.id.button1_roll);
        b2 = (Button) findViewById(R.id.button2_hold);
        b3 = (Button) findViewById(R.id.button3_reset);
    }

    public void roll(View v) {
        int t = rand.nextInt(6) + 1;
        setImage(t);

        if (t == 1) {
            user_tscore = 0;
            hold(tv1);
        }
        else {
            user_tscore += t;
            tv1.setText("Your score: " + user_tscore + " computer score: " + comp_oscore);
            winner();
            if (tv2.getText() == "You Won!!")
                reset(tv1);
        }
    }

    public void reset(View v) {
        user_oscore = 0; user_tscore = 0;
        comp_oscore = 0; comp_tscore = 0;

        b1.setEnabled(true);
        b2.setEnabled(true);
        tv1.setText("Your turn score will display here");
        tv2.setText("Your overall score will display here");
        setImage(1);
    }

    public void hold(View v) {
        user_oscore += user_tscore;
        comp_oscore += comp_tscore;

        user_tscore = 0;
        comp_tscore = 0;

        tv1.setText("Your score: " + user_tscore + "computer score: " + comp_tscore);
        tv2.setText("Your overall score: " + user_oscore + "computer's score: " + comp_oscore);
        setImage(1);

        setImage(1);
        winner();
        computerTurn();
    }

    public void setImage(int s) {
        iv.setImageResource(getResources().getIdentifier("@drawable/dice" + s, "drawable", getPackageName()));
    }

    Handler h = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {
            int t = rand.nextInt(6) + 1;
            setImage(t);
            if (t != 1 && comp_tscore < 15) {
                comp_tscore += t;
                tv1.setText("Your score: " + user_tscore + " computer score: " + comp_tscore);
                h.postDelayed(run, 500);
            }

            else if (t == 1) {
                comp_tscore = 0;
                tv1.setText("Your score: " + user_tscore + " computer score: " + comp_tscore);
                h.removeCallbacks(run);
            }

            else {
                comp_oscore += comp_tscore;
                comp_tscore = 0;
                tv2.setText("Your Overall score: "+ user_oscore + " Computer's overall score: " + comp_oscore);
                h.removeCallbacks(run);
            }
        }
    };

    Runnable run1 = new Runnable() {
        @Override
        public void run() {
            if(user_oscore >= 50 || comp_oscore >= 50) {
                tv2.setText("You Won!!");
                h.postDelayed(run1, 1000);
            }
            h.removeCallbacks(run1);
        }
    };

    public void computerTurn() {
        b1.setClickable(false);
        b2.setClickable(false);
        h.postDelayed(run, 500);
        b1.setClickable(true);
        b2.setClickable(true);
    }

    public void winner() {
        h.postDelayed(run1, 1000);
    }
}
