package com.example.study06;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    TextView txt_alarm;
    Button btn_cancel,btn_timepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txt_alarm=findViewById(R.id.txt_alarm);
        btn_cancel=findViewById(R.id.btn_cancel);
        btn_timepicker=findViewById(R.id.btn_timepicker);

        btn_timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker= new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"time picker");
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelAlarm();
            }
        });
    }

    public void onTimeSet(TimePicker view, int hour, int min){

        Calendar c= Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,min);
        c.set(Calendar.SECOND,0);

        updateTimeText(c);
        startAlarm(c);

    }

    private void updateTimeText(Calendar c) {

        String timeText=" Alarm Setting : ";
        timeText+= DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());


        txt_alarm.setText(timeText);

    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,AlertReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,1,intent, Intent.FILL_IN_ACTION);

        if(c.before(Calendar.getInstance())){
            c.add(Calendar.DATE,1);
        }


        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
    }


    private void cancelAlarm() {
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,AlertReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,1,intent,0);

        alarmManager.cancel(pendingIntent);
        txt_alarm.setText("Alarm is canceled");
    }
}