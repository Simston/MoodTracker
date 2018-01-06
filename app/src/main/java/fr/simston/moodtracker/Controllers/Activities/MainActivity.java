package fr.simston.moodtracker.Controllers.Activities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import fr.simston.moodtracker.Adapters.PageAdapter;
import fr.simston.moodtracker.R;

import static android.support.v4.view.ViewPager.OnPageChangeListener;

public class MainActivity extends AppCompatActivity {

        private SharedPreferences preferences;
        private int lastKnownPosition;
        Date date = new Date();

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // init information TimeZone of device
            TimeZone tz = TimeZone.getDefault();
            System.out.println("TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezon id :: " +tz.getID());

            LocalTime midnight = LocalTime.MIDNIGHT;

            LocalDateTime todayT= LocalDateTime.now(ZoneId.of(tz.getID()));

            LocalDate today = LocalDate.now(ZoneId.of("Europe/Paris"));
            LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
            LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
            Log.e("Current date", String.valueOf(todayT));

            //if(currentDate == getEndOfDay(currentDate)){
            // save Data at Midnight
            //}

            configureViewPage();
            // Instance of SharedPreferences
            preferences = getPreferences(MODE_PRIVATE);

        }

    private void configureViewPage(){
        // 1 - Get ViewPager from layout
        ViewPager pager = findViewById(R.id.activity_main_viewpager);
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(), getResources().getIntArray(R.array.colorsPagesViewPager)));
        onPageChangeListener(pager);
        loadSaveDataInUi(pager);
    }

    // Add Listener for ViewPager
    private void onPageChangeListener(ViewPager pager){
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
                    case 1:
                        lastKnownPosition = position;
                    case 2:
                        lastKnownPosition = position;
                    case 3:
                        lastKnownPosition = position;
                    case 4:
                        lastKnownPosition = position;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Save the position of current View Fragment when activity is destroyed
        preferences.edit().putInt("lastKnownPosition", lastKnownPosition).apply();
    }

    /**
     * Method of refreshing the interface, data backup
     * @param pager ViewPager
     */
    private void loadSaveDataInUi(ViewPager pager){
        lastKnownPosition = getPreferences(MODE_PRIVATE).getInt("lastKnownPosition", lastKnownPosition);
        pager.setCurrentItem(lastKnownPosition);

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
