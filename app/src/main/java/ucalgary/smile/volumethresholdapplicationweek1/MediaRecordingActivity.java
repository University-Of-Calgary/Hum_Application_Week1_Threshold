package ucalgary.smile.volumethresholdapplicationweek1;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MediaRecordingActivity extends ActionBarActivity {

    Button button_recording, button_stop;
    TextView textView_recording;
    private MediaRecorder silentMediaRecorder = null;
    private Timer timerThread;
    private static int timerCounter = 0;
    private static int recordingNumber = 1;
    private int outputFileSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recording);

        button_recording = (Button) findViewById(R.id.mediaRecording_button_record);
        button_stop = (Button) findViewById(R.id.mediaRecording_button_stop);
        textView_recording = (TextView) findViewById(R.id.mediaRecording_tv_noise);

        button_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                silentMediaRecorder = new MediaRecorder();
                silentMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                silentMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                silentMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                silentMediaRecorder.setOutputFile("/dev/null");
                outputFileSet = 0;
                timerCounter = 0;

                try{
                    silentMediaRecorder.prepare();
                    silentMediaRecorder.start();
                    System.out.println("Hello World, Starting the silentMediaRecording");
                }catch(IOException e){
                    System.out.println("Exception starting silentMediaRecorder of type : " + e.toString());
                }catch(Exception e){
                    System.out.println("Exception starting silentMediaRecorder of type : " + e.toString());
                }

                timerThread = new Timer();
                timerThread.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        if (20 * Math.log10(silentMediaRecorder.getMaxAmplitude()) > 60){
                            if (outputFileSet == 0){
                                // Stop the silent recording and start the real recording
                                System.out.println("OutputFileSet set to 0, starting real recording : " + outputFileSet);
                                silentMediaRecorder.stop();
                                silentMediaRecorder.release();
                                silentMediaRecorder = null;
                                silentMediaRecorder = new MediaRecorder();
                                silentMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                silentMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                silentMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                                silentMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().
                                        getAbsolutePath() + "/soundRecording" + (recordingNumber++) +".3gp");
                                outputFileSet = 1;
                                timerCounter = 0;

                                try{
                                    silentMediaRecorder.prepare();
                                    silentMediaRecorder.start();
                                } catch(IOException e){
                                    System.out.println("Exception with outputFileSet set to 0 and starting recording " +
                                            "of type : " + e.toString());
                                } catch (Exception e){
                                    System.out.println("Exception with outputFileSet set to 0 and starting recording " +
                                            "of type : " + e.toString());
                                }
                            } else {
                                // Already recording, pass
                                System.out.println("OutputFileSet : " + outputFileSet + ". Already recording");
                                timerCounter = 0;
                            }
                        } else {
                            if (outputFileSet == 1){
                                // Already recording
                                if (timerCounter < 5) {
                                    timerCounter += 1;
                                    System.out.println("Incremented the timerCounter, already recording");
                                } else {
                                    // Stop the recording and start the silent recording
                                    System.out.println("Stopping the recording. File stored at location : " +
                                    Environment.getExternalStorageDirectory().getAbsolutePath() +
                                            "/recording" + recordingNumber +".3gp");
                                    outputFileSet = 0;
                                    silentMediaRecorder.stop();
                                    silentMediaRecorder.release();
                                    silentMediaRecorder = null;
                                    silentMediaRecorder = new MediaRecorder();
                                    silentMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                    silentMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                    silentMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                                    silentMediaRecorder.setOutputFile("/dev/null");
                                    try{
                                        silentMediaRecorder.prepare();
                                        silentMediaRecorder.start();
                                    } catch (IOException e){
                                        System.out.println("Exception stopping the recording of the type : " +
                                                e.toString());
                                    } catch (Exception e){
                                        System.out.println("Exception stopping the recording of the type : " +
                                               e.toString());
                                    }
                                }
                            } else {
                                // Not recording, pass
                            }
                        }
                    }
                }, 0, 1000);
            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (silentMediaRecorder != null){
                    timerThread.cancel();
                    silentMediaRecorder.stop();
                    silentMediaRecorder.release();
                    silentMediaRecorder = null;
                    System.out.println("Recording has been stopped");
                }
            }
        });
    }
}
