package com.bove.martin.pexel.utils;

import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;



/**
 * Created by Martín Bove on 13-Feb-20.
 * E-mail: mbove77@gmail.com
 */
public abstract class MyRecyclerScroll extends RecyclerView.OnScrollListener {
    private static final float HIDE_THRESHOLD = 150;
    private static final float SHOW_THRESHOLD = 150;

    int scrollDist = 0;
    private boolean isVisible = true;

    //    We dont use this method because its action is called per pixel value change
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.d("SCROLL", "onScrolled: ");

        //  Check scrolled distance against the minimum
        if (isVisible && scrollDist > HIDE_THRESHOLD) {
            //  Hide fab & reset scrollDist
            hide();
            scrollDist = 0;
            isVisible = false;
        }
        //  -MINIMUM because scrolling up gives - dy values
        else if (!isVisible && scrollDist < -SHOW_THRESHOLD) {
            //  Show fab & reset scrollDist
            show();

            scrollDist = 0;
            isVisible = true;
        }

        //  Whether we scroll up or down, calculate scroll distance
        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy;
        }

    }


    public abstract void show();

    public abstract void hide();
}
