package fr.simston.moodtracker.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.simston.moodtracker.Controllers.Fragments.PageFragment;

/**
 * Created by St&eacute;phane Simon on 03/01/2018.
 *
 * @version 1.0
 */

public class PageAdapter extends FragmentPagerAdapter {

    // 1 - Array of colors that will be passed to PageFragment
    private int[] colors;

    // 2 - Default Constructor
    public PageAdapter(FragmentManager fm, int[] colors) {
        super(fm);
        this.colors = colors;
    }

    @Override
    public int getCount() {
        return (5); // 3 - Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        // 4 - Page to return
        return (PageFragment.newInstance(position, this.colors[position]));
    }
}
