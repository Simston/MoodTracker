package fr.simston.moodtracker.Views;
/**
 * Created by St&eacute;phane Simon on 03/01/2018.
 *
 * @version 1.0
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VerticalViewPager extends ViewPager {
    //private static final float MIN_SCALE = 0.75f;

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return super.canScrollHorizontally(direction);
    }

    private void init() {
        setPageTransformer(true, new VerticalPageTransformer());
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final boolean toIntercept = super.onInterceptTouchEvent(flipXY(ev));
        flipXY(ev);
        return toIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final boolean toHandle = super.onTouchEvent(flipXY(ev));
        flipXY(ev);
        return toHandle;
    }

    private MotionEvent flipXY(MotionEvent ev) {
        final float width = getWidth();
        final float height = getHeight();
        final float x = (ev.getY() / height) * width;
        final float y = (ev.getX() / width) * height;
        ev.setLocation(x, y);
        return ev;
    }

    private static final class VerticalPageTransformer implements PageTransformer {
        @Override
        public void transformPage(View view, float position) {

            final int pageWidth = view.getWidth();
            final int pageHeight = view.getHeight();
            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 1) {
                view.setAlpha(1);
                view.setTranslationX(pageWidth * -position);

                /*Test for animation transition
                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor); */

                float yPosition = position * pageHeight;
                view.setTranslationY(yPosition);
            } else {
                view.setAlpha(0);
            }
        }
    }
}