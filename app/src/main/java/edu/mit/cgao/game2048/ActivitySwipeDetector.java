package edu.mit.cgao.game2048;

/**
 * Created by cgao on 2/1/15.
 */

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

public class ActivitySwipeDetector implements View.OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
    private MainActivity activity;
    static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;

    public ActivitySwipeDetector(MainActivity activity){
        this.activity = activity;
    }

    public void onRightToLeftSwipe(){
//        Log.i(logTag, "RightToLeftSwipe!");
        activity.onSwipe(3);
    }

    public void onLeftToRightSwipe(){
//        Log.i(logTag, "LeftToRightSwipe!");
        activity.onSwipe(4);
    }

    public void onTopToBottomSwipe(){
//        Log.i(logTag, "onTopToBottomSwipe!");
        activity.onSwipe(2);
    }

    public void onBottomToTopSwipe(){
//        Log.i(logTag, "onBottomToTopSwipe!");
        activity.onSwipe(1);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > Math.abs(deltaY))
                {
                    if(Math.abs(deltaX) > MIN_DISTANCE){
                        // left or right
                        if(deltaX < 0) { this.onLeftToRightSwipe(); return true; }
                        if(deltaX > 0) { this.onRightToLeftSwipe(); return true; }
                    }
                    else {
//                        Log.i(logTag, "Horizontal Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                        return false; // We don't consume the event
                    }
                }
                // swipe vertical?
                else
                {
                    if(Math.abs(deltaY) > MIN_DISTANCE){
                        // top or down
                        if(deltaY < 0) { this.onTopToBottomSwipe(); return true; }
                        if(deltaY > 0) { this.onBottomToTopSwipe(); return true; }
                    }
                    else {
//                        Log.i(logTag, "Vertical Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                        return false; // We don't consume the event
                    }
                }

                return true;
            }
        }
        return false;
    }

}
