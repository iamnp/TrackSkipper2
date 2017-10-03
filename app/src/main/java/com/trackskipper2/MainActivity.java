package com.trackskipper2;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void checkRequirements(View view) {
        String s = VolumeKeyLongPressListener.checkRequirements(this);
        if (s == null) {
            if (checkCallingOrSelfPermission("android.permission.RECEIVE_BOOT_COMPLETED")
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission android.permission.RECEIVE_BOOT_COMPLETED not granted!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "OK!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ((getPackageName() + ".TrackSkipperService").equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void checkServiceStatus(View view) {
        if (isServiceRunning()) {
            Toast.makeText(this, "RUNNING", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NOT RUNNING", Toast.LENGTH_SHORT).show();
        }
    }

    public void startService(View view) {
        if (isServiceRunning()) {
            stopService(new Intent(this, TrackSkipperService.class));
        }
        startForegroundService(new Intent(this, TrackSkipperService.class));
    }

    public void stopService(View view) {
        if (isServiceRunning()) {
            stopService(new Intent(this, TrackSkipperService.class));
        }
    }
}