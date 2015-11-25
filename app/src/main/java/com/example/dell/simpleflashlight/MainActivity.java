package com.example.dell.simpleflashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera.*;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.Policy;

public class MainActivity extends AppCompatActivity {
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    android.hardware.Camera.Parameters param;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // primero checamos if el dispositivo soporta o no el flash
        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            //dispositivo no soporta flash
            //muestra mensajes de alerta y cierra la aplicacion

            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Error");
            alert.setMessage("Your Device does'nt support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            alert.show();
        }
        getCamera();
        ToggleButton flashSwitch = (ToggleButton) findViewById(R.id.flash_switch);
        flashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    turnOnFlash();
                } else {
                    turnOffFlash();
                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void getCamera() {
        if (camera == null) {
            try {
                camera = camera.open();
                param = camera.getParameters();
            }catch (RuntimeException e){
                Log.e("Camera Error. Failed to open . error: ", e.getMessage());
            }
        }
    }
    // Turning On flash

    private  void turnOnFlash(){
        if(!isFlashOn){
            if(camera==null ||param==null){
               return;
            }
            param = camera.getParameters();
            param.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(param);
            camera.startPreview();
            isFlashOn=true;
            Log.v("AndoridATC","Flash has been turned on...");
        }
    }

    private void turnOffFlash(){
        if(isFlashOn){
            if(camera==null || param == null){
                return;
            }

            param = camera.getParameters();
            param.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(param);
            camera.stopPreview();
            isFlashOn = false;
            Log.v("AndroidATC", "Flash has been turned off ...");

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

    @Override
    public void onStart() {
        super.onStart();
        getCamera();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.dell.simpleflashlight/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.dell.simpleflashlight/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();

        if(camera != null){
            camera.release();
            camera =null;
        }
    }
}
