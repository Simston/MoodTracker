package fr.simston.moodtracker.Controllers.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;
import java.util.TimeZone;

import fr.simston.moodtracker.Adapters.PageAdapter;
import fr.simston.moodtracker.Controllers.Fragments.PageFragment;
import fr.simston.moodtracker.Models.MoodStock;
import fr.simston.moodtracker.R;

import static android.support.v4.view.ViewPager.OnPageChangeListener;

public class MainActivity extends AppCompatActivity implements PageFragment.OnButtonClickedListener {

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
    private String commentMessage;

    private MediaPlayer[] bankSound;

    public static ArrayList<MoodStock> mMoodStockArrayList;
    public static  int finalDayForCalcul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init current date
        date = Calendar.getInstance().getTime();
        calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        // delete or use this for test with another day
        //currentDay = currentDay +30;

        currentMonth = calendar.get(Calendar.MONTH);
        currentMonth = currentMonth +1;

        // Instance of SharedPreferences
        preferences = this.getSharedPreferences("shared preferences", MODE_PRIVATE);

        loadMoodOfSharedPreferences();
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
                        // Stop old sound when change page
                        stopSound(lastKnownPosition);
                        // Start new sound for this Mood
                        playSound(position);
                        lastKnownPosition = position;
                        break;
                    case 1:
                        stopSound(lastKnownPosition);
                        playSound(position);
                        lastKnownPosition = position;
                        break;
                    case 2:
                        stopSound(lastKnownPosition);
                        playSound(position);
                        lastKnownPosition = position;
                        break;
                    case 3:
                        stopSound(lastKnownPosition);
                        playSound(position);
                        lastKnownPosition = position;
                        break;
                    case 4:
                        stopSound(lastKnownPosition);
                        playSound(position);
                        lastKnownPosition = position;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Method to save a new mood or change if it's the same day
     *
     * @param position of ViewPager
     */
    private void saveMoodOfDay(int position) {
        Log.e("POSITION DE MOOD", String.valueOf(position));
        // Storing the new Mood if day is different of mood in storage
        if (dayOfLastKnownMoodDay != currentDay || monthOfLastKnownMoodDay != currentMonth) {
            moodOfDay = new MoodStock(currentDay, currentMonth, position, date, "");
            // Replace dayOfLastKnowMoodDay by new current Day and lastKnownPosition for new Object
            dayOfLastKnownMoodDay = currentDay;
            monthOfLastKnownMoodDay = currentMonth;
            lastKnownPosition = position;
            // Storing the mood of the day in arraylist for 7 last mood storage.
            if (mMoodStockArrayList.size() - 1 == 6) {
                mMoodStockArrayList.remove(0);
                mMoodStockArrayList.add(6, moodOfDay);
            } else {
                mMoodStockArrayList.add(moodOfDay);
            }
            // If same day just update the position and date
        } else {
            this.moodOfDay.setPositionOfMood(position);
            this.lastKnownPosition = position;
            this.moodOfDay.setDate(date);
        }

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
            mMoodStockArrayList = new ArrayList<>();
            saveMoodOfDay(3);
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
            this.commentMessage = lastKnownMoodDay.getCommentMessage();
        }

        // Replace current mood by lastKnownMoodDay if same day and month
        if (dayOfLastKnownMoodDay == currentDay && monthOfLastKnownMoodDay == currentMonth) {
            this.moodOfDay = lastKnownMoodDay;
        }

        //if a new Day or new Month initialize and save it
        if (dayOfLastKnownMoodDay != currentDay) {
            // Delete Mood if > 7 days
            ListIterator<MoodStock> iter = mMoodStockArrayList.listIterator();
            while(iter.hasNext()){
                if(currentDay - iter.next().getDay() > 7){
                    iter.remove();
                }
            }
            saveMoodOfDay(3);

            // if a new month initialize and saver it
        }else if (monthOfLastKnownMoodDay != currentMonth){

            // Delete Mood if > 7 days
            ListIterator<MoodStock> iter = mMoodStockArrayList.listIterator();
            while(iter.hasNext()) {
                int dayMood = iter.next().getDay();

                // Recover max day of last month in data
                int endDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

                int dayForCalcul = endDayOfMonth - dayMood;
                finalDayForCalcul = dayForCalcul + currentDay;
                if (finalDayForCalcul > 7) {
                    iter.remove();
                }
            }
            saveMoodOfDay(3);
        }

        // Calculation of days following the current month and last month
        Calendar calForCalcul = Calendar.getInstance();
        calForCalcul.set(Calendar.MONTH, monthOfLastKnownMoodDay);


    }

    @Override
    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.imageBtnComment:
                alertCommentAndSaveIt();
                break;
            case R.id.imgBtnHistory:

                // Creating Bundle object
                Bundle bundle = new Bundle();
                // Storing date into bundle
                bundle.putInt("currentDay", currentDay);
                bundle.putInt("currentMonth", currentMonth);

                // Creating Intent object
                Intent i = new Intent(MainActivity.this, HistoricalActivity.class);

                // Storing bundle object into intent
                i.putExtras(bundle);
                this.startActivity(i);
                break;
            default:
                break;
        }
    }

    // Manage Button Comment
    private void alertCommentAndSaveIt() {

        // Create an AlertDialog with an EditText
        final EditText txtComment = new EditText(this);
        final AlertDialog.Builder alertComment = new AlertDialog.Builder(this);
        alertComment.setTitle("Commentaire")
                .setCancelable(false)
                .setView(txtComment)
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();

                    }
                })
                .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Save the comment string in current Mood
                        commentMessage = txtComment.getText().toString();
                        moodOfDay.setCommentMessage(commentMessage);
                    }
                }).create().show();
    }

    /**
     * Method of playing a sound when the viewpager changes position
     *
     * @param position int of ViewPager
     */
    private void playSound(int position) {
        // Create an Array with different sounds
        bankSound = new MediaPlayer[]{
                MediaPlayer.create(this, R.raw.verry_sad),
                MediaPlayer.create(this, R.raw.sad),
                MediaPlayer.create(this, R.raw.normal),
                MediaPlayer.create(this, R.raw.happy),
                MediaPlayer.create(this, R.raw.super_happy)
        };
        switch (position) {
            case 0:
                bankSound[position].start();
                break;
            case 1:
                bankSound[position].start();
                break;
            case 2:
                bankSound[position].start();
                break;
            case 3:
                bankSound[position].start();
                break;
            case 4:
                bankSound[position].start();
                break;
            default:
                break;
        }
    }

    /**
     * Method of stop a old sound when the viewpager changes position
     *
     * @param position int of ViewPager
     */
    private void stopSound(int position) {
        if (bankSound != null) {
            switch (position) {
                case 0:
                    bankSound[position].stop();
                    break;
                case 1:
                    bankSound[position].stop();
                    break;
                case 2:
                    bankSound[position].stop();
                    break;
                case 3:
                    bankSound[position].stop();
                    break;
                case 4:
                    bankSound[position].stop();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveMoodOfDay(lastKnownPosition);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveMoodOfDay(lastKnownPosition);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
