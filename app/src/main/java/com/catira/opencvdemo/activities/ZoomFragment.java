package com.catira.opencvdemo.activities;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.catira.opencvdemo.R;


/**
 * Listens on a touch event on the parent of this Fragment
 * and displays a part of the parent with a zoomfactor
 */
public class ZoomFragment extends Fragment implements View.OnTouchListener {

    private float mZoomFactor = 2.0f;
    private Canvas mDrawableCanvas = null;
    private Bitmap mBitmap = null;
    private static int mSIZE = 300;
    private int mTargetId = -1;
    private View mTargetView = null;
    private ImageView mZoomImage = null;

    public static String TARGET_ID = "targetId";

    public ZoomFragment() {
    }

    @Override
    public void setArguments(Bundle bundle){
        super.setArguments(bundle);
        mTargetId = bundle.getInt(TARGET_ID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zoom, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        hide();
    }

    public static ZoomFragment newInstance(int targetId) {
        ZoomFragment f = new ZoomFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt(TARGET_ID, targetId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        if(mTargetId >= 0) {
            mTargetView = ((View) getView().getParent()).findViewById(mTargetId);
        } else {
            throw new ExceptionInInitializerError("No '"+TARGET_ID+"' argument was found.");
        }
        mZoomImage = (ImageView) getView().findViewById(R.id.zoomImage);
    }

    /**
     * Click hander that reacts on clicks of the fragment parent
     * @param v
     * @param event
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                hide();
                break;
            case MotionEvent.ACTION_DOWN:
                show();
                updateImage(v, event);
                break;
            case MotionEvent.ACTION_MOVE:
                updateImage(v, event);
                break;
            default:
                break;
        }
        // always return false since the event should
        // propagate further
        return true;
    }

    private void show() {
        mBitmap = Bitmap.createBitmap(mTargetView.getWidth(), mTargetView.getHeight(), Bitmap.Config.ARGB_8888);
        mDrawableCanvas = new Canvas(mBitmap);
        mTargetView.draw(mDrawableCanvas);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, (int)(mBitmap.getWidth()*mZoomFactor), (int)(mBitmap.getHeight()*mZoomFactor), false);
        getView().setVisibility(View.VISIBLE);
    }

    private void hide() {
        getView().setVisibility(View.INVISIBLE);
    }

    private void updateImage(View v, MotionEvent e) {
        // Move image to the right of the window if the cursor would collide with it
        double size = mSIZE * 1.1;
        if(e.getX() < size && e.getY() < (size * 1.1)) {
            getView().setX(v.getWidth() - mSIZE);
        } else {
            getView().setX(0f);
        }

        int x = Math.max(0, (int)((e.getX()*mZoomFactor)-(mSIZE*0.5)));
        int y = Math.max(0, (int)((e.getY()*mZoomFactor)-(mSIZE*0.5)));//-(v.getY()+mSIZE)*0.5)) + 60);
        if ((int)Math.ceil(Math.min(mBitmap.getHeight()-y, mSIZE)) == 300) {
            Bitmap unscaledBmp = Bitmap.createBitmap(mBitmap, x, y, (int)Math.ceil(Math.min(mBitmap.getWidth()-x, mSIZE)), (int)Math.ceil(Math.min(mBitmap.getHeight()-y, mSIZE)), null, false);
            mZoomImage.setImageBitmap(Bitmap.createScaledBitmap(unscaledBmp, mSIZE, mSIZE, false));
        }
    }
}
