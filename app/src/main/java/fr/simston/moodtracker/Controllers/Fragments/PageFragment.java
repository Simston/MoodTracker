package fr.simston.moodtracker.Controllers.Fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import fr.simston.moodtracker.R;

/**
 * Created by St&eacute;phane Simon on 05/01/2018.
 *
 * @version 1.0
 */

public class PageFragment extends Fragment {

    // 1 - Create keys for our Bundle
    public static final String KEY_POSITION = "position";
    public static final String KEY_COLOR = "color";

    public PageFragment() {
        // Required empty public constructor
    }

    // 2 - Method that will create a new instance of PageFragment, and add data to its bundle.

    public static PageFragment newInstance(int position, int color){

        // 2.1 Create a new Fragment
        PageFragment frag = new PageFragment();

        // 2.2 Create bundle and add it some data
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_COLOR, color);
        frag.setArguments(args);

        return (frag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 3 - Get layout of PageFragment
        View result = inflater.inflate(R.layout.fragment_page, container, false);

        // 4 - Get widgets from layout and serialise it
        LinearLayout rootView = (LinearLayout) result.findViewById(R.id.fragment_page_rootview);
        ImageView imageView = result.findViewById(R.id.fragment_page_smiley_img);

        // 5 - Get data from Bundle (created in method newInstance
        int position = getArguments().getInt(KEY_POSITION, -1);
        int color = getArguments().getInt(KEY_COLOR, -1);
        rootView.setBackgroundColor(color);

        TypedArray imgs = getResources().obtainTypedArray(R.array.smileys_array);
        imageView.setImageResource(imgs.getResourceId(position,-1));

        return  result;
    }

}