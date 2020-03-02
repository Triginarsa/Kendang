package com.deorispramana.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.deorispramana.myapplication.model.Response;
import com.deorispramana.myapplication.network.ApiCall;
import com.deorispramana.myapplication.network.ApiService;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RecordActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 600000;

    TextView mTextViewCountdown;
    boolean mTimerRunning;
    long mTimeLeftInMills = START_TIME_IN_MILLIS;
    ApiService apiService;
    ImageButton rec;
    Boolean record;
    DynamicSineWaveView wavesView;
    String dirpath;
    String fileName;
    String server_link;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_kendang);
        apiService = ApiCall.getClient();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait...");

        record = false;
        rec = (ImageButton) findViewById(R.id.imageButton);
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!record) {
                    record = true;
                    new recordSound().execute();
                    rec.setImageResource(R.drawable.pause1);
                    wavesView.startAnimation();

                } else {
                    record = false;
                    rec.setImageResource(R.drawable.mic1);
                    wavesView.stopAnimation();
                }

            }
        });

        wavesView = (DynamicSineWaveView)findViewById(R.id.view_sine_wave);
        wavesView.addWave(0.5f, 0.5f, 0, 0, 0);
        wavesView.addWave(0.5f, 2f, 0.5f, getResources().getColor(android.R.color.holo_orange_dark), 1);
        wavesView.addWave(0.1f, 2f, 0.7f, getResources().getColor(android.R.color.holo_blue_light), 1);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_record, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.browse_file) {
//            Intent intent = new Intent();
//            intent.setType("audio/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(intent, 100);
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }

    public void uploadFile(){
        progressDialog.setMessage("Uploading file to server...");
        progressDialog.show();

        File file = new File(String.valueOf(new File(dirpath+fileName)));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        apiService.postData(body).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equals("0")){
                        Toast.makeText(RecordActivity.this, "Cedugan Lanang", Toast.LENGTH_SHORT).show();
                        goToActivity("0");
                    }else if(response.body().getData().equals("1")){
                        Toast.makeText(RecordActivity.this, "Cedugan Wadon", Toast.LENGTH_SHORT).show();
                        goToActivity("1");
                    }
                    else if(response.body().getData().equals("2")){
                        Toast.makeText(RecordActivity.this, "Angklung Lanang", Toast.LENGTH_SHORT).show();
                        goToActivity("2");
                    }
                    else if(response.body().getData().equals("3")){
                        Toast.makeText(RecordActivity.this, "Angklung Wadon", Toast.LENGTH_SHORT).show();
                        goToActivity("3");
                    }
                    else if(response.body().getData().equals("4")){
                        Toast.makeText(RecordActivity.this, "Krumpung Lanang", Toast.LENGTH_SHORT).show();
                        goToActivity("4");
                    }
                    else if(response.body().getData().equals("5")){
                        Toast.makeText(RecordActivity.this, "Krumpung Wadon", Toast.LENGTH_SHORT).show();
                        goToActivity("5");
                    }
                }else{
                    Toast.makeText(RecordActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(RecordActivity.this, "Connection Error "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void goToActivity(String flag){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("flag", flag);
        startActivity(intent);
    }
    class recordSound extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new ProgressDialog(StageActivity.this);
//            pDialog.setMessage("Merekam suara...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                dirpath = Environment.getExternalStorageDirectory()
                    + File.separator;

                fileName = "recorded.wav";
                WavAudioRecorder mRecorder = WavAudioRecorder.getInstanse();
                mRecorder.setOutputFile(dirpath + fileName);

                mRecorder.prepare();
                mRecorder.start();

                while (record) {
                        Thread.sleep(500);
                }
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "";
        }

        @Override
        protected void onPostExecute(String s) {
            uploadFile();
            super.onPostExecute(s);
        }
    }
}


