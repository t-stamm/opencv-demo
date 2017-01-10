package com.catira.opencvdemo.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.catira.opencvdemo.R;


/**
 * Listens on a touch event on the parent of this Fragment
 * and displays a part of the parent with a zoomfactor
 */
public class ZoomFragment extends Fragment implements View.OnDragListener {

    private float mZoomFactor = 2.0f;
    private Canvas mDrawableCanvas = null;
    private static int mSIZE = 100;

    public ZoomFragment() {

    }

    private View getParentView() {
        return (View)getView().getParent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bitmap b = Bitmap.createBitmap(mSIZE, mSIZE, Bitmap.Config.ARGB_8888);
        mDrawableCanvas = new Canvas(b);
        getView().draw(mDrawableCanvas);
        hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zoom, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getParentView().setOnDragListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Click hander that reacts on clicks of the fragment parent
     * @param v
     * @param event
     */
    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_EXITED:
            case DragEvent.ACTION_DRAG_ENDED:
                hide();
            case DragEvent.ACTION_DRAG_ENTERED:
            case DragEvent.ACTION_DRAG_STARTED:
                show();
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                updateImage(v, event);
            default:
                break;
        }
        // always return false since the event should
        // propagate further
        return false;
    }

    private void show() {
        getView().setVisibility(View.VISIBLE);
    }

    private void hide() {
        getView().setVisibility(View.INVISIBLE);
    }

    private void updateImage(View v, DragEvent e) {
        int helper = mSIZE/2;
        v.layout((int)e.getX()-helper, (int)e.getY()-helper, (int)e.getX()+helper, (int)e.getY()+helper);
        v.draw(mDrawableCanvas);
    }
}
