package fr.simston.moodtracker.Controllers.Activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fr.simston.moodtracker.Models.MoodStock;
import fr.simston.moodtracker.R;

/**
 * Created by St&eacute;phane Simon on 10/01/2018.
 *
 * @version 1.0
 */

public class HistoricalActivity extends AppCompatActivity {
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;
    private LinearLayout layout5;
    private LinearLayout layout6;
    private LinearLayout layout7;

    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;

    private int positionOfMood;

    private int currentDay;
    private int currentMonth;

    private int dayOfMoodInArray;
    private int monthOfMoodInArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical);


        //get the Intent that started this Activity
        Intent in = getIntent();

        //get the Bundle that sotres the data of this Activity
        Bundle bundle = in.getExtras();

        // getting data from bundle
        currentDay = bundle.getInt("currentDay");
        currentMonth = bundle.getInt("currentMonth");

        Log.e("current day", String.valueOf(currentDay));
        // initialize all LinearLayout
        initializeViews();
    }

    private void initializeViews() {
        layout1 = findViewById(R.id.linear1);
        layout2 = findViewById(R.id.linear2);
        layout3 = findViewById(R.id.linear3);
        layout4 = findViewById(R.id.linear4);
        layout5 = findViewById(R.id.linear5);
        layout6 = findViewById(R.id.linear6);
        layout7 = findViewById(R.id.linear7);

        tv1 = findViewById(R.id.tvLinear1);
        tv2 = findViewById(R.id.tvLinear2);
        tv3 = findViewById(R.id.tvLinear3);
        tv4 = findViewById(R.id.tvLinear4);
        tv5 = findViewById(R.id.tvLinear5);
        tv6 = findViewById(R.id.tvLinear6);
        tv7 = findViewById(R.id.tvLinear7);

        ArrayList<MoodStock> mMoodStockArrayList = MainActivity.mMoodStockArrayList;

        // Retrieving objects in the ArrayList<MoodStock>
        int i = 1;
        for (MoodStock moodStock : mMoodStockArrayList) {
            positionOfMood = moodStock.getPositionOfMood();
            dayOfMoodInArray = moodStock.getDay();
            monthOfMoodInArray = moodStock.getMonth();
            String forTv = calculForDaysDisplay(dayOfMoodInArray, monthOfMoodInArray);
            if (i == 1) {
                modifParamOfLinearLayout(layout1, positionOfMood);
                tv1.setText(forTv);
                Log.e("forTv", forTv);
            } else if (i == 2) {
                modifParamOfLinearLayout(layout2, positionOfMood);
                tv2.setText(forTv);

            } else if (i == 3) {
                modifParamOfLinearLayout(layout3, positionOfMood);
                tv3.setText(forTv);

            } else if (i == 4) {
                modifParamOfLinearLayout(layout4, positionOfMood);
                tv4.setText(forTv);

            } else if (i == 5) {
                modifParamOfLinearLayout(layout5, positionOfMood);
                tv5.setText(forTv);

            } else if (i == 6) {
                modifParamOfLinearLayout(layout6, positionOfMood);
                tv6.setText(forTv);

            } else if (i == 7) {
                modifParamOfLinearLayout(layout7, positionOfMood);
                tv7.setText(forTv);

            }
            i++;
        }
    }

    private String calculForDaysDisplay(int dayOfMoodInArray, int monthOfMoodInArray) {
        String stringForTextView;
        int dayForMessage;

        if (currentMonth == monthOfMoodInArray) {
            dayForMessage = currentDay - dayOfMoodInArray;

            if (dayForMessage == 0) {
                stringForTextView = "Aujourd'hui";
            } else if (dayForMessage == 1) {
                stringForTextView = "Hier";
            } else if (dayForMessage == 2) {
                stringForTextView = "Avant-hier";
            } else if (dayForMessage == 7) {
                stringForTextView = "Il y a une semaine";
            } else {
                stringForTextView = "Il y a " + dayForMessage + " jours";
            }
        } else {
            stringForTextView = "Il y'as plus d'un mois";
        }
        return stringForTextView;
    }

    /**
     * Method to change the appearance of LinearLayout according to stored moods
     *
     * @param layout         LinearLayout
     * @param positionOfMood int
     */
    private void modifParamOfLinearLayout(LinearLayout layout, int positionOfMood) {

        // Get the screen width of device
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        int width = size.x;

        // Here lp will be used to change the width of our LinearLayout
        LinearLayout.LayoutParams lp;

        // Next position record (mood) we change the Layout settings
        if (positionOfMood == 0) {

            // Changing the desired width according to the desired percentage
            width = width - (width * 75 / 100);

            // Set the color desired
            layout.setBackgroundColor(getResources().getColor(R.color.faded_red));

            // Applying the new parameters to our LinearLayout
            lp = new LinearLayout.LayoutParams(width, 0);
            lp.weight = 1;
            layout.setLayoutParams(lp);

        } else if (positionOfMood == 1) {
            width = width - (width * 60 / 100);
            layout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
            lp = new LinearLayout.LayoutParams(width, 0);
            lp.weight = 1;
            layout.setLayoutParams(lp);

        } else if (positionOfMood == 2) {
            width = width - (width * 40 / 100);
            layout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
            lp = new LinearLayout.LayoutParams(width, 0);
            lp.weight = 1;
            layout.setLayoutParams(lp);

        } else if (positionOfMood == 3) {
            width = width - (width * 20 / 100);
            layout.setBackgroundColor(getResources().getColor(R.color.light_sage));
            lp = new LinearLayout.LayoutParams(width, 0);
            lp.weight = 1;
            layout.setLayoutParams(lp);

        } else if (positionOfMood == 4) {
            layout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
            lp = new LinearLayout.LayoutParams(width, 0);
            lp.weight = 1;
            layout.setLayoutParams(lp);

        }
    }
}