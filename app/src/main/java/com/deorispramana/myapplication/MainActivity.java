package com.deorispramana.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.deorispramana.myapplication.model.Response;
import com.deorispramana.myapplication.network.ApiCall;
import com.deorispramana.myapplication.network.ApiService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    Button btnBrowse;
    String dirpath;
    ApiService apiService;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                1);
        apiService = ApiCall.getClient();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait...");

        Button Record = findViewById(R.id.button);
        Record.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getApplicationContext(), RecordActivity.class);
               startActivity(i);
            }
       });

        Button Tentang = findViewById(R.id.button2);
        Tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i =new Intent(getApplicationContext(), AboutActivity.class);
                    startActivity(i);
            }
        });

        btnBrowse = findViewById(R.id.buttonbrowse);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
            }
        });
        dirpath = Environment.getExternalStorageDirectory() + File.separator + "recorded.wav";;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100){
            if (resultCode == RESULT_OK){
                Uri uri = data.getData();
                openPath(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openPath(Uri uri){
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            convertStream(is);
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void convertStream(InputStream uri){
        try {
            File files = new File(dirpath);
            try (OutputStream output = new FileOutputStream(files)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = uri.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadFile(files);
        } finally {
            try {
                uri.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void uploadFile(File file){
        progressDialog.setMessage("Uploading file to server...");
        progressDialog.show();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        apiService.postData(body).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equals("0")){
                        Toast.makeText(MainActivity.this, "Cedugan Lanang", Toast.LENGTH_SHORT).show();
                        goToActivity("0");
                    }else if(response.body().getData().equals("1")){
                        Toast.makeText(MainActivity.this, "Cedugan Wadon", Toast.LENGTH_SHORT).show();
                        goToActivity("1");
                    }
                    else if(response.body().getData().equals("2")){
                        Toast.makeText(MainActivity.this, "Angklung Lanang", Toast.LENGTH_SHORT).show();
                        goToActivity("2");
                    }
                    else if(response.body().getData().equals("3")){
                        Toast.makeText(MainActivity.this, "Angklung Wadon", Toast.LENGTH_SHORT).show();
                        goToActivity("3");
                    }
                    else if(response.body().getData().equals("4")){
                        Toast.makeText(MainActivity.this, "Krumpung Lanang", Toast.LENGTH_SHORT).show();
                        goToActivity("4");
                    }
                    else if(response.body().getData().equals("5")){
                        Toast.makeText(MainActivity.this, "Krumpung Wadon", Toast.LENGTH_SHORT).show();
                        goToActivity("5");
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Connection Error "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void goToActivity(String flag){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("flag", flag);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}