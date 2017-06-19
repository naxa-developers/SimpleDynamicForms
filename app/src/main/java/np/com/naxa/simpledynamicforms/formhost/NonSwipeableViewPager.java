package np.com.naxa.simpledynamicforms.formhost;

/**
 * Created by Nishon Tandukar on 18 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import timber.log.Timber;

public class NonSwipeableViewPager extends ViewPager implements onAnswerSelectedListener {

    private boolean shouldAllowSwipe = true;

    public NonSwipeableViewPager(Context context) {
        super(context);
        setMyScroller();
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!shouldAllowSwipe) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!shouldAllowSwipe) {
            return false;
        }

        return super.onInterceptTouchEvent(event);
    }


    //down one is added for smooth scrolling
    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAnswerSelected(String question, String answer) {
        Timber.d("Non swipe asnwer");
    }

    @Override
    public void shoudStopSwipe(boolean shouldAllowSwipe) {

        Timber.d("Non swipe");
        //  this.shouldStopSwipe = shouldStopSwipe;
    }

    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
        }
    }
}
