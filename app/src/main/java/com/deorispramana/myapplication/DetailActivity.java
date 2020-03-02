package com.deorispramana.myapplication;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileDescriptor;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {
    TextView tvJenis;
    ImageView ivJenis;
    TextView tv_judul_jenis;
    ImageButton playBtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_judul_jenis = findViewById(R.id.tv_judul_jenis);
        tvJenis = findViewById(R.id.tv_jenis);
        ivJenis = findViewById(R.id.imageView);
        playBtn = findViewById(R.id.imageButton2);
//        tv_judu_jenis1 = findViewById(R.id.tv_judul_jenis1);

        Intent intent = getIntent();
        final String flag = intent.getStringExtra("flag");
        if(flag.equals("0")){
            tv_judul_jenis.setText("Kendang Cedugan Lanang");
//            tv_judu_jenis1.setText("Lanang");
            tvJenis.setText("Kendang Cedugan Lanang berukuran 70 - 72 cm dengan diameter 28 - 32 cm. Kendang Cedugan dengan panggul dan digunakan dalam gambelan Gong Gede, Gong Kebyar, dan Baleganjur");
             Glide.with(this).load(getDrawable(R.drawable.cedugan_lanang1)).into(ivJenis);
        }else if (flag.equals("1")) {
            tv_judul_jenis.setText("Kendang Cedugan Wadon");
//            tv_judu_jenis1.setText("Wadon");
            tvJenis.setText("Kendang Cedugan Wadon berukuran 70 - 72 cm dengan diameter 28 - 32 cm. Kendang Cedugan dimainkan dengan panggul dan digunakan dalam gambelan Gong Gede, Gong Kebyar, dan Baleganjur");
            Glide.with(this).load(getDrawable(R.drawable.detail_cedugan_wadon)).into(ivJenis);
        }
        else if (flag.equals("2")) {
            tv_judul_jenis.setText("Kendang Angklung Lanang");
//            tv_judu_jenis1.setText("Lanang");
            tvJenis.setText("Kendang Angklung Lanang berukuran 25 - 27 cm dengan diameter 7 - 12 cm. Kendang Angklung dimainkan dengan panggul untuk untuk mengiringi gamelan Angklung. Gamelan Angklung adalah Gamelan khas bali yang sering digunakan dalam prosesi/upacara kematian.");
            Glide.with(this).load(getDrawable(R.drawable.detail_angklung_lanang)).into(ivJenis);
        }
        else if (flag.equals("3")) {
            tv_judul_jenis.setText("Kendang Angklung Wadon");
//            tv_judu_jenis1.setText("Wadon");
            tvJenis.setText("Kendang Angklung Wadon berukuran 25 - 27 cm dengan diameter 7 - 12 cm. Kendang Angklung dimainkan dengan panggul untuk untuk mengiringi gamelan Angklung. Gamelan Angklung adalah Gamelan khas bali yang sering digunakan dalam prosesi/upacara kematian.");
            Glide.with(this).load(getDrawable(R.drawable.detail_angklung_wadon)).into(ivJenis);
        }
        else if (flag.equals("4")) {
            tv_judul_jenis.setText("Kendang Krumpung Lanang");
//            tv_judu_jenis1.setText("Lanang");
            tvJenis.setText("Kendang Krumpung Lanang berukuran 56 - 60 cm dengan diameter 21 - 25 cm. Kendang Krumpung dimainkan untuk mengiringi gamelan palegongan 25 - 27 cm dengan diameter 7 - 12 cm.");
            Glide.with(this).load(getDrawable(R.drawable.detail_krumpung)).into(ivJenis);
        }
        else if (flag.equals("5")) {
            tv_judul_jenis.setText("Kendang Krumpung Wadon");
//            tv_judu_jenis1.setText("Wadon");
            tvJenis.setText("Kendang Krumpung Wadon berukuran 56 - 60 cm dengan diameter 21 - 25 cm. Kendang Krumpung dimainkan untuk mengeringi gamelan palegongan 25 - 27 cm dengan diameter 7 - 12 cm.");
            Glide.with(this).load(getDrawable(R.drawable.detail_krumpung)).into(ivJenis);
        }

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MediaPlayer player = MediaPlayer.create(DetailActivity.this, R.raw.lanang1_01_bersih);
//                player.start();
                switch (flag){
                    case "0":
                        MediaPlayer player = MediaPlayer.create(DetailActivity.this, R.raw.lanang1_01_bersih);
                        player.start();
                        break;
                    case "1":
                        player = MediaPlayer.create(DetailActivity.this, R.raw.wadon1_02_bersih);
                        player.start();
                        break;
                    case "2":
                        player = MediaPlayer.create(DetailActivity.this, R.raw.lanang_angklung);
                        player.start();
                        break;
                    case "3":
                        player = MediaPlayer.create(DetailActivity.this, R.raw.wadon_angklung);
                        player.start();
                        break;
                    case "4":
                        player = MediaPlayer.create(DetailActivity.this, R.raw.lanang_krumpung);
                        player.start();
                        break;
                    case "5":
                        player = MediaPlayer.create(DetailActivity.this, R.raw.wadon_krumpung);
                        player.start();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
