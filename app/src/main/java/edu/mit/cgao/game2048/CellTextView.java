package edu.mit.cgao.game2048;

/**
 * Created by cgao on 2/1/15.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CellTextView extends TextView {

    public CellTextView(Context context) {
        super(context);
    }

    public CellTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CellTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }

}