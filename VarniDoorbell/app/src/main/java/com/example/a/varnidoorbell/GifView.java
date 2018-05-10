package com.example.a.varnidoorbell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

/**
 * Created by a on 17-Feb-2016.
 */
public class GifView extends View {

    private InputStream gifinputStream;
    private Movie gifmovie;
    private int movieWidth, movieHeight;
    private long movieDuration;
    private long movieStart;

    public GifView(Context context) {
        super(context);
        init(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context){
        setFocusable(true);
        gifinputStream = context.getResources().openRawResource(R.drawable.update);
        gifmovie = Movie.decodeStream(gifinputStream);
        movieWidth = gifmovie.width();
        movieHeight = gifmovie.height();
        movieDuration = gifmovie.duration();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(movieWidth, movieHeight);
    }


    public int getMovieWidth() {
        return movieWidth;
    }

    public int getMovieHeight() {
        return movieHeight;
    }

    public long getMovieDuration() {
        return movieDuration;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = SystemClock.uptimeMillis();

        if(movieStart == 0){
            movieStart = now;
        }

        if(gifmovie != null){
            int dur = gifmovie.duration();

            if(dur == 0){
                dur = 1000;
            }
            int relTime = (int) ((now - movieStart) % dur);
            gifmovie.setTime(relTime);
            gifmovie.draw(canvas, 0, 0);
            invalidate();
        }

    }
}
