package ucalgary.smile.volumethresholdapplicationweek1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RecorderReceiver extends BroadcastReceiver {

    private MediaRecorder mediaRecorder = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // For the recurring task, just display a toast message
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        System.out.println("I am running after every few minutes / seconds");

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        // Performs the recording after every 10 seconds
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() +
        "/Weekend_Recordings/recording_" + ts + ".3gp");
        try{
            System.out.println("Starting Recording");
            mediaRecorder.prepare();
            mediaRecorder.start();
            System.out.println("Going to Sleep for 5 seconds");
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
            System.out.println("Stopping Recording");
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        } catch (IOException e) {
            System.out.println("Exception recording of type : " + e.toString());
        } catch (Exception e) {
            System.out.println("Exception recording of type : " + e.toString());
        }
    }
}
