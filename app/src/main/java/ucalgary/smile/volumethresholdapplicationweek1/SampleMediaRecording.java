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
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class SampleMediaRecording extends ActionBarActivity {

    Button button_play, button_stop, button_record;
    private MediaRecorder mediaRecorder;
    private String outputFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_media_recording);

        button_play = (Button) findViewById(R.id.button_play);
        button_stop = (Button) findViewById(R.id.button_stop);
        button_record = (Button) findViewById(R.id.button_record);

        button_play.setEnabled(false);
        button_stop.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);

        button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                }catch(IllegalStateException e){
                    Log.e("TAG_EXCEPTION", "Exception of the type : " + e.toString());
                }catch(Exception e){
                    Log.e("TAG_EXCEPTION", "Exception of the type : " + e.toString());
                }
                button_stop.setEnabled(true);
                button_record.setEnabled(false);

                Toast.makeText(SampleMediaRecording.this, "Recording Started", Toast.LENGTH_SHORT).show();
            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;

                button_stop.setEnabled(false);
                button_play.setEnabled(true);

                Toast.makeText(SampleMediaRecording.this, "Audio Recorder Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        });

        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException,
                    IllegalStateException{
                MediaPlayer mediaPlayer = new MediaPlayer();
                try{
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }catch(IOException e){
                    Log.e("EXCEPTION_TAG", "Exception of the type : " + e.toString());
                }
                Toast.makeText(SampleMediaRecording.this, "Playing Audio using Media Player",
                        Toast.LENGTH_SHORT).show();
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
        return super.onOptionsItemSelected(item);
    }
}