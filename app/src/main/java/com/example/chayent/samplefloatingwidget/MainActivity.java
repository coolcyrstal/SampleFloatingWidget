package com.example.chayent.samplefloatingwidget;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chayent.samplefloatingwidget.service.ChatMenuService;

import io.mattcarroll.hover.HoverView;
import io.mattcarroll.hover.overlay.OverlayPermission;

public class MainActivity extends AppCompatActivity {

    private static final int DRAW_OVER_OTHER_APP_PERMISSION = 123;
    private Button mButton;
    private TextView mTextView;
    FloatingWidgetService mService;
    boolean mBound = false;
    private HoverView mHoverView;

    private boolean mPermissionsRequested = false;

    private BroadcastReceiver mBroadcastReceiverReplaceFragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            replaceChatFragment();
            Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
        }
    };

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            FloatingWidgetService.LocalBinder binder = (FloatingWidgetService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        askForSystemOverlayPermission();

        mButton = (Button) findViewById(R.id.button);
        mTextView = (TextView) findViewById(R.id.textView);
//        mHoverView = findViewById(R.id.hovermenu);
//
//        mHoverView = HoverView.createForWindow(getApplicationContext(), new WindowViewController((WindowManager)getSystemService(Context.WINDOW_SERVICE)));



        int badge_count = getIntent().getIntExtra("badge_count", 0);
        mTextView.setText(badge_count + " messages received previously");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(MainActivity.this)) {
//                    startService(new Intent(MainActivity.this, FloatingWidgetService.class));
//                } else {
//                    errorToast();
//                }
                ChatMenuService.showFloatingMenu(MainActivity.this);
            }
        });

        if (!mPermissionsRequested && !OverlayPermission.hasRuntimePermissionToDrawOverlay(this)) {
            @SuppressWarnings("NewApi")
            Intent myIntent = OverlayPermission.createIntentToRequestOverlayPermission(this);
            startActivityForResult(myIntent, 1000);
        }

        Intent intent = new Intent(this, FloatingWidgetService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mBroadcastReceiverReplaceFragment, new IntentFilter("EVENT_REPLACE_CHAT"));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onPause() {
        super.onPause();

        // To prevent starting the service if the required permission is NOT granted.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
//            startService(new Intent(MainActivity.this, FloatingWidgetService.class).putExtra("mActivityBackground", true));
//            finish();
        } else {
            errorToast();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(mConnection);
        mBound = false;

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiverReplaceFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (!Settings.canDrawOverlays(this)) {
//                    //Permission is not available. Display error text.
//                    errorToast();
//                    finish();
//                }
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }

        if (1000 == requestCode) {
            mPermissionsRequested = true;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available to open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    private void errorToast() {
        Toast.makeText(this, "Draw over other app permission not available. Can't start the application without the permission.", Toast.LENGTH_LONG).show();
    }

    private void replaceChatFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.chat_box_layout, new ChatPageFragment(), "Chat Page");
        fragmentTransaction.commitAllowingStateLoss();
    }
}
