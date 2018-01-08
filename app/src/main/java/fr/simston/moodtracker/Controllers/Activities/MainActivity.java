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
    private int lastKnownPosition;
    private int currentDay;
    private int currentMonth;
    private Date date;
    private Calendar calendar;

    private MoodStock moodOfDay;
    private ArrayList<MoodStock> mMoodStockArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMoodOfSharedPreferences();
        // Instance of SharedPreferences
        preferences = this.getSharedPreferences("shared preferences", MODE_PRIVATE);

        // init information TimeZone of device
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID());

        // init current date
        date = Calendar.getInstance().getTime();
        calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentMonth = currentMonth + 1;

        Log.e("Current date", String.valueOf(date));
        Log.e("Current day", String.valueOf(currentDay));
        Log.e("Current month", String.valueOf(currentMonth));
        configureViewPage();

    }

    private void configureViewPage() {
        // 1 - Get ViewPager from layout
        ViewPager pager = findViewById(R.id.activity_main_viewpager);
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(), getResources().getIntArray(R.array.colorsPagesViewPager)));
        // 2 - Attach on page change listener with current pager
        onPageChangeListener(pager);
        // 3 - Retrieve data from shared preferences
        loadSaveDataInUi(pager);
    }

    /**
     * Method of refreshing the interface, data backup
     *
     * @param pager ViewPager
     */
    private void loadSaveDataInUi(ViewPager pager) {
        // initializing lastKnownPosition with the stored data
        lastKnownPosition = getSharedPreferences("shared preferences",MODE_PRIVATE).getInt("lastKnownPosition", lastKnownPosition);
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
                        //saveMoodOfDay(position);
                        saveMoodOfDay(position);
                        break;
                    case 1:
                        lastKnownPosition = position;
                        saveMoodOfDay(position);

                        //saveMoodOfDay(position);
                        break;
                    case 2:
                        lastKnownPosition = position;
                        saveMoodOfDay(position);

                        //saveMoodOfDay(position);
                        break;

                    case 3:
                        lastKnownPosition = position;
                        saveMoodOfDay(position);

                        //saveMoodOfDay(position);
                        break;

                    case 4:
                        lastKnownPosition = position;
                        saveMoodOfDay(position);
                        Log.e("TABLEAU", "My tab" + mMoodStockArrayList);
                        //saveMoodOfDay(position);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void saveMoodOfDay(int position) {
        moodOfDay = new MoodStock(currentDay, currentMonth, position, date);

        // Storing the mood of the day in arraylist for 7 last mood storage.
        // TO DO create here the function for compare Mood with new day if new day stock the current mood.
        if(mMoodStockArrayList.size()-1 == 6){
            mMoodStockArrayList.remove(0);
            mMoodStockArrayList.add(6,moodOfDay);
        }else{
            mMoodStockArrayList.add(moodOfDay);
        }

        Log.e("Test", String.valueOf(position));
        Log.e("Object", String.valueOf(moodOfDay));
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mMoodStockArrayList);
        editor.putString("mood_list", json);
        editor.apply();

    }

    private void loadMoodOfSharedPreferences() {
        preferences = this.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("mood_list", null);
        Type type = new TypeToken<ArrayList<MoodStock>>() {
        }.getType();
        mMoodStockArrayList = gson.fromJson(json, type);
        if (mMoodStockArrayList == null) {
            mMoodStockArrayList = new ArrayList<>();
        }
        for (MoodStock object : mMoodStockArrayList
                ) {
            Log.e("Objectz", String.valueOf(object));
            Log.e("position", String.valueOf(object.getPositionOfMood()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the position of current View Fragment when activity is onPause()
        preferences.edit().putInt("lastKnownPosition", lastKnownPosition).apply();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Save the position of current View Fragment when activity is onDestroy()
        preferences.edit().putInt("lastKnownPosition", lastKnownPosition).apply();

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
