package ucalgary.smile.volumethresholdapplicationweek1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends ActionBarActivity {

    Button analyze_sleep, waited_recording, sample_recording;
    ListView listView_additional_items;
    String[] listViewItems = {"Start Async Task", "Record Audio AudioRecorder"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        analyze_sleep = (Button) findViewById(R.id.main_analyze_sleep);
        waited_recording = (Button) findViewById(R.id.main_wait_recording);
        sample_recording = (Button) findViewById(R.id.main_sample_recording);
        listView_additional_items = (ListView) findViewById(R.id.list_display_items);

        listView_additional_items.setAdapter(new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, listViewItems));
        listView_additional_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Intent asyncTaskIntent = new Intent(MainActivity.this, StartAsyncTask.class);
                    startActivity(asyncTaskIntent);
                } else if (position == 1){
                    Intent audioRecordIntent = new Intent(MainActivity.this, AudioRecorderActivity.class);
                    startActivity(audioRecordIntent);
                }
            }
        });

        File folder = new File(Environment.getExternalStorageDirectory() + "/Weekend_Recordings");
        boolean success = true;
        if (!folder.exists()){
            success = folder.mkdir();
        }
        if (success){
            System.out.println("Folder has been successfully created");
        } else {
            System.out.println("Error creating file or already exists");
        }

        analyze_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sleepIntent = new Intent(MainActivity.this, MediaRecordingActivity.class);
                startActivity(sleepIntent);
            }
        });

        waited_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent waitedRecording = new Intent(MainActivity.this, WeekendAnalysis.class);
                startActivity(waitedRecording);
            }
        });

        sample_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sampleRecordingIntent = new Intent(MainActivity.this, SampleMediaRecording.class);
                startActivity(sampleRecordingIntent);
            }
        });
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
        if (id == R.id.main_sample_recording){
            Intent sampleRecordingIntent = new Intent(MainActivity.this, SampleMediaRecording.class);
            startActivity(sampleRecordingIntent);
        }
        else if (id == R.id.action_sleepanalysis){
            Intent thresholdIntent = new Intent(MainActivity.this, MediaRecordingActivity.class);
            startActivity(thresholdIntent);
            return true;
        }
        else if (id == R.id.action_weekendanalysis){
            Intent weekendIntent = new Intent(MainActivity.this, WeekendAnalysis.class);
            startActivity(weekendIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
