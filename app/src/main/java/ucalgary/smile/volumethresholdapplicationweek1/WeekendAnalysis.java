package ucalgary.smile.volumethresholdapplicationweek1;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WeekendAnalysis extends ActionBarActivity {

    Button button_record, button_analyse;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekend_analysis);

        button_record = (Button) findViewById(R.id.weekend_record);
        button_analyse = (Button) findViewById(R.id.weekend_analyse);
        button_analyse.setEnabled(false);

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(WeekendAnalysis.this, RecorderReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_analyse.setEnabled(true);
                button_record.setEnabled(false);
                startAlarm(v);
            }
        });

        button_analyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_analyse.setEnabled(false);
                button_record.setEnabled(true);
                cancelAlarm(v);
            }
        });
    }

    public void startAlarm(View view){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 10000; // 10 seconds

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                interval, pendingIntent);
        Toast.makeText(WeekendAnalysis. this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(View view){
        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
            Toast.makeText(WeekendAnalysis.this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
        }
    }

}
