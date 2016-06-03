package ucalgary.smile.volumethresholdapplicationweek1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class SleepRecordActivity extends ActionBarActivity {

    Button button_record, button_analysis;
    private double THRESHOLD = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_record);

        button_record = (Button) findViewById(R.id.sleep_record_button_record);
        button_analysis = (Button) findViewById(R.id.sleep_record_button_stop);
        /**
        button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SleepRecordActivity.this, "Recording Sleep Activity",
                //        Toast.LENGTH_SHORT).show();

                Intent broadcastIntent = new Intent(SleepRecordActivity.this, MyBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(SleepRecordActivity.this,
                        234324243, broadcastIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3, pendingIntent);
                Toast.makeText(SleepRecordActivity.this, "Alarm set in " + 3 + " seconds ",
                        Toast.LENGTH_SHORT).show();

                SoundMeter soundMeter = new SoundMeter();
                soundMeter.start();

                HandlerThread handlerThread = new HandlerThread("NewThreadHandler");
                handlerThread.start();
                Handler handler = new Handler(handlerThread.getLooper()){
                    @Override
                    public void handleMessage(Message msg) {
                        Log.d("MESSAGE_HANDLED", "handlerMessage " + msg.what + " in " + Thread.currentThread());
                    };
                };
                for (int i=0; i<5; i++){
                    Log.d("MESSAGE_HANDLED", "sending " + i + " in " + Thread.currentThread());
                    handler.sendEmptyMessageDelayed(i, 3000 + i * 1000);
                }

                SoundMeter soundMeter = new SoundMeter();
                soundMeter.start();

            }
        });*/
        button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaRecorder silentMediaRecorder = new MediaRecorder();
                silentMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                silentMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                silentMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                silentMediaRecorder.setOutputFile("/dev/null");

                try{
                    silentMediaRecorder.prepare();
                    silentMediaRecorder.start();
                }catch(IOException e){
                    System.out.println("Exception in silentMediaRecorder : " + e.toString());
                }catch(Exception e){
                    System.out.println("General Exception in SilentMediaRecorder : " + e.toString());
                }

                while(true){
                    try {
                        Thread.sleep(1000);

                    }catch(InterruptedException e){
                        System.out.println("Interrupted Exception with the main thread : " + e.toString());
                    }
                }

            }
        });
    }

    public class SoundMeter{
        private MediaRecorder mediaRecorder = null;
        private MediaRecorder mediaRecording = null;

        public void record(int recordNumber){
            mediaRecording = new MediaRecorder();
            mediaRecording.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecording.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecording.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecording.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Ringtones/new_recording.wav");

            try{
                mediaRecording.prepare();
                mediaRecording.start();
            }catch(IOException e){
                System.out.println("Exception of the type : " + e.toString());
            }catch(Exception e){
                System.out.println("Exception of the type : " + e.toString());
            }
        }

        public void start(){
            if (mediaRecorder == null){

                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.setOutputFile("/dev/null");

                try{
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                }catch(IOException e){
                    System.out.println("Exception of the type : " + e.toString());
                }catch(Exception e){
                    System.out.println("Exception of the type : " + e.toString());
                }
            }
        }

        public void stop(){
            if(mediaRecorder != null){
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }
        }

        public void stopRecording(){
            if(mediaRecording != null){
                mediaRecording.stop();
                mediaRecording.release();
                mediaRecording = null;
            }else{
                System.out.println("Not doing a recording currently");
            }
        }

        public double getAmplitude(){
            if(mediaRecorder != null)
                return mediaRecorder.getMaxAmplitude();
            else
                return 0;
        }
    }
}
