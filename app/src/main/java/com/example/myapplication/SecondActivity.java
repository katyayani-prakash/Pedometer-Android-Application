package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;

public class SecondActivity extends AppCompatActivity {

    public static TextView goals;
    public TextView welcome;
    public static Button mButton;
    public static EditText steps;
    public static EditText cals;
    public static EditText time;
    public static int goalInput;

    //public static int steps = MainActivity.stepDetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding = SecondActivity.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.secondactivity);


        goals = findViewById(R.id.goals_display);
        welcome = findViewById(R.id.welcome_message);
        mButton = (Button)findViewById(R.id.button1);
        steps = (EditText) findViewById(R.id.editText1);
        cals = (EditText)findViewById(R.id.editText2);
        time = (EditText)findViewById(R.id.editText3);

        String message = ("Please select one of the three options below to set daily your step goals");
        welcome.setText(message);
    }
    @Override
    protected void onResume() {

        super.onResume();
        setGoalInput();
        goals.setText("Your Daily Step Goal = " + goalInput);
        goalsMet();

    }

    public void setGoalInput() {
       //binding.resetButton.setOnClickListener(new View.OnClickListener()
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!steps.getText().toString().matches("")) {
                    goalInput = Integer.parseInt(steps.getText().toString());
                }
                else if(!cals.getText().toString().matches("")) {
                    goalInput = Integer.parseInt(cals.getText().toString()) * 25;
                }
                else if(!time.getText().toString().matches("")) {
                    goalInput = Integer.parseInt(time.getText().toString()) * 80;
                }
                goals.setText("Your Daily Step Goal = " + goalInput);
                Toast.makeText(getApplicationContext(), "Goal is set!", Toast.LENGTH_LONG).show();
                Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mIntent);
            }
        });
    }

    public void goalsMet() {
        if((goalInput == MainActivity.stepDetect)&&(goalInput != 0)) {
            Toast.makeText(getApplicationContext(), "Congratulations! You've met your daily goal!", Toast.LENGTH_LONG).show();
        }
    }
}