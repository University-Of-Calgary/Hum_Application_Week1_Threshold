package ucalgary.smile.volumethresholdapplicationweek1;

import android.app.ProgressDialog;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

public class StartAsyncTask extends ActionBarActivity {

    ProgressDialog progressDialog;
    AudioRecord recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_async_task);
        new StartTask().execute();
    }

    private class StartTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(StartAsyncTask.this);
            progressDialog.setTitle("Async Task running");
            progressDialog.setMessage("Please Wait while the task completes");
            progressDialog.setCancelable(true);
            progressDialog.show();
            System.out.println("Inside the onPreExecute method");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(StartAsyncTask.this, "Progress Dialog has finished loading",
                    Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            System.out.println("Inside the onPostExecute method");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            }catch (Exception e){
                System.out.println("Exception of the type : " + e.toString());
            }
            System.out.println("Inside the doInBackground method");
            return null;
        }
    }
}