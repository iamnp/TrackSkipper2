package com.trackskipper2;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;

public class TrackSkipperService extends Service {
    private VolumeKeyLongPressListener volumeKeyLongPressListener;
    private AudioManager am;
    private PowerManager pm;
    private final VolumeKeyLongPressListener.OnVolumeKeyLongPressListener onVolumeKeyLongPressListener = new VolumeKeyLongPressListener.OnVolumeKeyLongPressListener() {
        @Override
        public void onVolumeKeyLongPress(KeyEvent ev) {
            // we react on long press when screen is off
            if (pm.isInteractive()) return;

            // we react on long press when music is playing
            if (!am.isMusicActive()) return;

            // we react on long press when it is a down press
            if (ev.getAction() != KeyEvent.ACTION_DOWN) return;

            // we react on long press when it is the first one
            if (ev.getRepeatCount() != 0) return;

            if (ev.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                pressButton(SystemClock.uptimeMillis(), KeyEvent.KEYCODE_MEDIA_NEXT);
            } else if (ev.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                pressButton(SystemClock.uptimeMillis(), KeyEvent.KEYCODE_MEDIA_PREVIOUS);
            }
        }
    };

    @Override
    public void onCreate() {
        Notification.Builder builder = new Notification.Builder(this, "lolkek123")
                .setContentTitle(getString(R.string.app_name))
                .setContentText("TrackSkipper")
                .setAutoCancel(true);

        Notification notification = builder.build();
        startForeground(1, notification);
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        pm = (PowerManager) getSystemService(POWER_SERVICE);
        volumeKeyLongPressListener = new VolumeKeyLongPressListener(this, onVolumeKeyLongPressListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        volumeKeyLongPressListener.bind();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        volumeKeyLongPressListener.unbind();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void pressButton(long eventTime, int keyEvent) {
        KeyEvent downEvent = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, keyEvent, 0);
        am.dispatchMediaKeyEvent(downEvent);
        KeyEvent upEvent = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_UP, keyEvent, 0);
        am.dispatchMediaKeyEvent(upEvent);
    }
}