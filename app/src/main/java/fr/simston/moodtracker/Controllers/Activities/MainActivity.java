package fr.simston.moodtracker.Controllers.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import fr.simston.moodtracker.Adapters.PageAdapter;
import fr.simston.moodtracker.Models.MoodStock;
import fr.simston.moodtracker.R;

import static android.support.v4.view.ViewPager.OnPageChangeListener;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    private Date date;
    private Calendar calendar;

    private MoodStock lastKnownMoodDay;
    private int dayOfLastKnownMoodDay;
    private int monthOfLastKnownMoodDay;
    private Date dateOfLastKnownMoodDay;
    private int lastKnownPosition;

    private MoodStock moodOfDay;
    private int currentDay;
    private int currentMonth;

    private ArrayList<MoodStock> mMoodStockArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init current date
        date = Calendar.getInstance().getTime();
        calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentMonth = currentMonth + 1;

        Log.e("Current date", String.valueOf(date));

        // Instance of SharedPreferences
        preferences = this.getSharedPreferences("shared preferences", MODE_PRIVATE);

        loadMoodOfSharedPreferences();

        // init information TimeZone of device
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID());

        configureViewPage();
    }

    private void configureViewPage() {
        // 1 - Get ViewPager from layout
        ViewPager pager = findViewById(R.id.activity_main_viewpager);
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(), getResources().getIntArray(R.array.colorsPagesViewPager)));

        // 2 - Initialize position of ViewPager with position shared/
        loadSaveDataInUi(pager);
        // 3 - Attach on page change listener with current pager
        onPageChangeListener(pager);
    }

    /**
     * Method of refreshing the interface, data backup
     *
     * @param pager ViewPager
     */
    private void loadSaveDataInUi(ViewPager pager) {
        // initializing lastKnownPosition with the stored data
        // set the pager at last know position
        pager.setCurrentItem(lastKnownPosition);

    }

    // Add OnPageChangeListener for ViewPager
    private void onPageChangeListener(ViewPager pager) {
        pager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            // Add action on ViewPager when page is selected
            @Override
            public void onPageSelected(int position) {
                // Save position of ViewPager
                switch (position) {
                    case 0:
                        lastKnownPosition = position;
                        break;
                    case 1:
                        lastKnownPosition = position;

                        break;
                    case 2:
                        lastKnownPosition = position;

                        break;
                    case 3:
                        lastKnownPosition = position;

                        break;
                    case 4:
                        lastKnownPosition = position;

                        Log.e("TABLEAU", "My tab" + mMoodStockArrayList);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void saveMoodOfDay(int position) {
        // Storing the new Mood if day is different of mood in storage
        if (dayOfLastKnownMoodDay != currentDay) {
            moodOfDay = new MoodStock(currentDay, currentMonth, position, date);

            // Replace dayOfLastKnowMoodDay by new current Day
            dayOfLastKnownMoodDay = currentDay;

            // Storing the mood of the day in arraylist for 7 last mood storage.
            if (mMoodStockArrayList.size() - 1 == 6) {
                mMoodStockArrayList.remove(0);
                mMoodStockArrayList.add(6, moodOfDay);
            } else {
                mMoodStockArrayList.add(moodOfDay);
            }
            // If same day just update the position and date
        } else {
            moodOfDay.setPositionOfMood(position);
            moodOfDay.setDate(date);
        }
        Log.e("Test", String.valueOf(position));
        Log.e("Object", String.valueOf(moodOfDay));

        // Save the new object Json in SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mMoodStockArrayList);
        editor.putString("mood_list", json);
        editor.apply();
    }

    private void loadMoodOfSharedPreferences() {
        // Recover ArrayList from SharedPreferences
        preferences = this.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("mood_list", null);
        Type type = new TypeToken<ArrayList<MoodStock>>() {
        }.getType();
        mMoodStockArrayList = gson.fromJson(json, type);

        // Initialize a first object and create an empty ArrayList if doesn't exist
        if (mMoodStockArrayList == null) {
            moodOfDay = new MoodStock(currentDay, currentMonth, 0, date);
            mMoodStockArrayList = new ArrayList<>();
            saveMoodOfDay(0);
        }

        // Loop for read all object in ArrayList mMoodStockArrayList
        for (MoodStock object : mMoodStockArrayList) {
            Log.e("Object In Array", String.valueOf(object));
            Log.e("Position Pager", String.valueOf(object.getPositionOfMood()));

        }

        compareLastKnownMoodDayWithCurrentMood();

    }

    /**
     * Method for compare the current Mood with the last Mood in storage
     */
    private void compareLastKnownMoodDayWithCurrentMood() {
        // Recover the last object in ArrayList mMoodStockArrayList
        if (mMoodStockArrayList != null && !mMoodStockArrayList.isEmpty()) {
            this.lastKnownMoodDay = mMoodStockArrayList.get(mMoodStockArrayList.size() - 1);
            // Recover all Data in object
            this.dayOfLastKnownMoodDay = lastKnownMoodDay.getDay();
            this.monthOfLastKnownMoodDay = lastKnownMoodDay.getMonth();
            this.dateOfLastKnownMoodDay = lastKnownMoodDay.getDate();
            this.lastKnownPosition = lastKnownMoodDay.getPositionOfMood();
        }

        Log.e("LastKnowPosition", String.valueOf(lastKnownPosition));
        //if a new Day position of ViewPager is 0
        if(dayOfLastKnownMoodDay != currentDay){
            dayOfLastKnownMoodDay = currentDay;
            this.lastKnownPosition = 0;
        }
        Log.e("LastKnowPosition If ND", String.valueOf(lastKnownPosition));


        // Replace current mood by lastKnownMoodDay if same day and month
        if (dayOfLastKnownMoodDay == currentDay && monthOfLastKnownMoodDay == currentMonth) {
            this.moodOfDay = lastKnownMoodDay;
        }
        Log.e("Info Last Mood", String.valueOf("La date est " + dateOfLastKnownMoodDay + " jour " +
                dayOfLastKnownMoodDay + " month " + monthOfLastKnownMoodDay + " position pager " + lastKnownPosition));
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveMoodOfDay(lastKnownPosition);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e("On Stop", "On Stop");
        saveMoodOfDay(lastKnownPosition);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadMoodOfSharedPreferences();

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadMoodOfSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
