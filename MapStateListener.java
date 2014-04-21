package dk.composed.mapstate;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.Timer;
import java.util.TimerTask;

public abstract class MapStateListener {

    private boolean mMapTouched = false;
    private boolean mMapSettled = false;
    private Timer mTimer;
    private static final int SETTLE_TIME = 500;

    private GoogleMap mMap;
    private CameraPosition mLastPosition;
    private Activity mActivity;

    public MapStateListener(GoogleMap map, TouchableMapFragment touchableMapFragment, Activity activity) {
        this.mMap = map;
        this.mActivity = activity;

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                unsettleMap();
                if(!mMapTouched) {
                    runSettleTimer();
                }
            }
        });

        touchableMapFragment.setTouchListener(new TouchableWrapper.OnTouchListener() {
            @Override
            public void onTouch() {
                touchMap();
                unsettleMap();
            }

            @Override
            public void onRelease() {
                releaseMap();
                runSettleTimer();
            }
        });
    }

    private void updateLastPosition() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLastPosition = MapStateListener.this.mMap.getCameraPosition();
            }
        });
    }

    private void runSettleTimer() {
        updateLastPosition();

        if(mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CameraPosition currentPosition = MapStateListener.this.mMap.getCameraPosition();
                        if (currentPosition.equals(mLastPosition)) {
                            settleMap();
                        }
                    }
                });
            }
        }, SETTLE_TIME);
    }

    private synchronized void releaseMap() {
        if(mMapTouched) {
            mMapTouched = false;
            onMapReleased();
        }
    }

    private void touchMap() {
        if(!mMapTouched) {
            if(mTimer != null) {
                mTimer.cancel();
                mTimer.purge();
            }
            mMapTouched = true;
            onMapTouched();
        }
    }

    public void unsettleMap() {
        if(mMapSettled) {
            if(mTimer != null) {
                mTimer.cancel();
                mTimer.purge();
            }
            mMapSettled = false;
            mLastPosition = null;
            onMapUnsettled();
        }
    }

    public void settleMap() {
        if(!mMapSettled) {
            mMapSettled = true;
            onMapSettled();
        }
    }

    public abstract void onMapTouched();
    public abstract void onMapReleased();
    public abstract void onMapUnsettled();
    public abstract void onMapSettled();
}
