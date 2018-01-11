package fr.simston.moodtracker.Controllers.Activities;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;

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

    private int positionOfMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical);

        layout1 = findViewById(R.id.linear1);
        layout2 = findViewById(R.id.linear2);
        layout3 = findViewById(R.id.linear3);
        layout4 = findViewById(R.id.linear4);
        layout5 = findViewById(R.id.linear5);
        layout6 = findViewById(R.id.linear6);
        layout7 = findViewById(R.id.linear7);

        ArrayList<MoodStock> mMoodStockArrayList = MainActivity.mMoodStockArrayList;
        int i = 1;
        for (MoodStock moodStock: mMoodStockArrayList) {
            positionOfMood = moodStock.getPositionOfMood();
            if(i == 1){
                modifParamOfLinearLayout(layout1, positionOfMood);
            }else if(i ==2){
                modifParamOfLinearLayout(layout2,positionOfMood);

            }else if(i ==3){
                modifParamOfLinearLayout(layout3,positionOfMood);

            }else if(i ==4){
                modifParamOfLinearLayout(layout4,positionOfMood);

            }else if(i ==5){
                modifParamOfLinearLayout(layout5,positionOfMood);

            }else if(i == 6){
                modifParamOfLinearLayout(layout6,positionOfMood);
            }
            else if(i == 7){
                modifParamOfLinearLayout(layout7,positionOfMood);
            }
            i++;
        }
    }

    private void modifParamOfLinearLayout(LinearLayout layout, int positionOfMood){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        int width = size.x;

        LinearLayout.LayoutParams lp;

        if(positionOfMood == 0){
            width = width - (width*70/100);
            layout.setBackgroundColor(getResources().getColor(R.color.faded_red));
            Log.e("width", String.valueOf(width));
            lp = new LinearLayout.LayoutParams(width,0);
            lp.weight =1;
            layout.setLayoutParams(lp);
        }else if(positionOfMood == 1){
            width = width - (width*60/100);
            layout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
            lp = new LinearLayout.LayoutParams(width,0);
            lp.weight =1;
            layout.setLayoutParams(lp);

        }else if(positionOfMood == 2){
            width = width - (width*40/100);
            layout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
            lp = new LinearLayout.LayoutParams(width,0);
            lp.weight =1;
            layout.setLayoutParams(lp);

        }else if(positionOfMood == 3){
            width = width - (width*20/100);
            layout.setBackgroundColor(getResources().getColor(R.color.light_sage));
            lp = new LinearLayout.LayoutParams(width,0);
            lp.weight =1;
            layout.setLayoutParams(lp);

        }else if(positionOfMood == 4){
            layout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
            lp = new LinearLayout.LayoutParams(width,0);
            lp.weight =1;
        }

    }
}