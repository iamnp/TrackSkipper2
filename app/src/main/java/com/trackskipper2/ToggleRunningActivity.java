package com.trackskipper2;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class ToggleRunningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isServiceRunning()) {
            stopService(new Intent(this, TrackSkipperService.class));
        } else {
            startForegroundService(new Intent(this, TrackSkipperService.class));
        }
        finish();
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

    }

    public void startService(View view) {

    }

    public void stopService(View view) {

    }
}