package dk.composed.mapstate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.MapFragment;

public class TouchableMapFragment extends MapFragment {

    private View mOriginalContentView;
    private FrameLayout mTouchView;

    private OnTouchListener onTouchListener;

    private boolean isTouched = false;

    public void setTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, parent,
                savedInstanceState);

        mTouchView = new FrameLayout(getActivity());
        mTouchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(!isTouched) {
                            if(onTouchListener != null) {
                                onTouchListener.onTouch();
                            }
                        }
                        isTouched = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isTouched = false;
                        if(onTouchListener != null) {
                            onTouchListener.onRelease();
                        }
                        break;
                }

                return false;
            }
        });
        mTouchView.addView(mOriginalContentView);

        return mTouchView;
    }

    public interface OnTouchListener {
        public void onTouch();
        public void onRelease();
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }
}
