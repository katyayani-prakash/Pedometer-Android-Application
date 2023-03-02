package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
//import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static final String TAG = "MainActivityLog";

    public static SensorManager sensorManager;
    public static Sensor sensorSD;
    public static int stepDetect = 0;

    private ProgressBar progressBar;
    private TextView progressText;
    private TextView goals;

    private SharedPreferences sharedPreferencesSave, sharedPreferencesLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Progress bar
        progressText = findViewById(R.id.progress_text);
        progressText.setText(stepDetect + " steps completed");
        progressBar = findViewById(R.id.progress_bar);
        //defining the daily goals
        goals = findViewById(R.id.goals_display);

        //toolbar
        setSupportActionBar(binding.toolbar);

        //Step Detector
        resetSteps();
        //display text
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null){
            sensorSD = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        }else{
            progressText.setText(stepDetect+" steps completed");
        }

        //fragment
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //click message icon
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Message icon");
                Snackbar.make(view, "Good Morning!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });

    }


    @Override
    protected void onResume() {

        super.onResume();
        //Log.d(TAG, "onResume");

        stopService(new Intent (this, MainService.class));
        //StepCounter
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            sensorManager.registerListener(this, sensorSD, SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this, "sensorSD not found", Toast.LENGTH_SHORT).show();
        }
        //Display step goals
        goals.setText("Your Daily Step Goal = " +SecondActivity.goalInput);
        progressBar.setMax(SecondActivity.goalInput);

    }

    @Override
    protected void onPause() {

        super.onPause();
        // Log.d(TAG, "onPause");

        //StepCounter
        //if unregister the hardware will stop detecting steps
//        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
//            sensorManager.unregisterListener(this, sensorSD);
//        }

        startService(new Intent(this, MainService.class));

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Log.d(TAG, "onRestart");
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
        if (id == R.id.action_share) {
            Toast.makeText(getApplicationContext(), "You clicked share", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "You clicked settings", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), "You clicked search", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user touches the button */
    public void startSecondActivity(View view) {
        // Start Second Activity in response to button click
        Intent mIntent = new Intent(this, SecondActivity.class);
        startActivity(mIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Going up a level from arrow on header bar");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor == sensorSD){
            stepDetect = (int) (stepDetect + sensorEvent.values[0]);
            progressText.setText(stepDetect + " steps completed");
            progressBar.setProgress(stepDetect);
        }
        goalsMet();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public void resetSteps() {
        binding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepDetect = 0;
                progressText.setText(stepDetect + " steps completed");
                progressBar.setProgress(stepDetect);
            }
        });

    }
    public void goalsMet() {
        if((SecondActivity.goalInput == stepDetect)&&(SecondActivity.goalInput != 0)) {
            Toast.makeText(getApplicationContext(), "Congratulations! You've met your daily goal!", Toast.LENGTH_LONG).show();
        }
    }
}