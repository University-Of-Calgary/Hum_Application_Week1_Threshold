package ucalgary.smile.volumethresholdapplicationweek1;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecorderActivity extends ActionBarActivity {

    Button button_record, button_stop, button_play, button_playStop;
    File recordingFile;
    boolean isRecording = false, isPlaying = false;
    int frequency = 11025, channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO,
    audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        button_record = (Button) findViewById(R.id.audioRecord_record);
        button_stop = (Button) findViewById(R.id.audioRecord_stop);
        button_play = (Button) findViewById(R.id.audioRecord_play);
        button_playStop = (Button) findViewById(R.id.audioRecord_playStop);

        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
         + "/AudioRecordings/");
        path.mkdirs();

        try {
            recordingFile = File.createTempFile("audioRecordRecording", ".wav", path);
        } catch (IOException e) {
            System.out.println("Exception of the type : " + e.toString());
        } catch (Exception e) {
            System.out.println("Exception of the type : " + e.toString());
        }

        // Start the recording
        button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new StartRecordingTask().execute();
            }
        });

        // Stop the recording
        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = false;
            }
        });

        // Play the recorded audio
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new StartPlayingTask().execute();
            }
        });

        // Stop the play of the recorded audio
        button_playStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = false;
            }
        });
    }

    // Task for recording the audio
    private class StartRecordingTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            isRecording = true;
            try {
                DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(recordingFile)));
                int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
                AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding,
                        bufferSize);
                if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
                    // Recording device initialization failed
                    System.out.println("Recording device initialization failed.");
                    audioRecord.release();
                    audioRecord = null;
                }
                short[] buffer = new short[bufferSize];
                audioRecord.startRecording();
                int r = 0;
                while (isRecording) {
                    int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                    System.out.println("The result of the buffer Read result is : " + bufferReadResult);
                    for (int i=0; i<bufferReadResult; i++) outputStream.writeShort(buffer[i]);
                    publishProgress(new Integer(r));
                    r++;
                }
                audioRecord.stop();
                outputStream.close();
                System.out.println("The system has stopped recording");

            } catch (IOException e){
                System.out.println("AudioRecord record failed. Exception of the type : " + e.toString());
                e.printStackTrace();
            } catch(Exception e){
                System.out.println("AudioRecord record failed. Exception of the type : " + e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            System.out.println("The Audio Progress Update is : " + values[0].toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    // Task for playing back the recorded audio
    private class StartPlayingTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            isPlaying = true;
            int bufferSize = AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            short[] audioData = new short[bufferSize / 4];

            try {
                DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(recordingFile)));
                AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, channelConfiguration, audioEncoding,
                        bufferSize, AudioTrack.MODE_STREAM);
                audioTrack.play();
                while (isPlaying && inputStream.available() > 0){
                    int i = 0;
                    while (inputStream.available() > 0 && i < audioData.length) {
                        audioData[i] = inputStream.readShort();
                        i++;
                    }
                    audioTrack.write(audioData, 0, audioData.length);
                }
                inputStream.close();
            } catch (IOException e) {
                System.out.println("AudioTrack playback failed. Exception of the type : " + e.toString());
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("AudioTrack playback failed. Exception of the type : " + e.toString());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
