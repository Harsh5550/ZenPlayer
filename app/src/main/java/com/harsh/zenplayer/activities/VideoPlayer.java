package com.harsh.zenplayer.activities;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.harsh.zenplayer.R;
import com.harsh.zenplayer.databinding.VideoPlayerBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class VideoPlayer extends AppCompatActivity {
    VideoPlayerBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= VideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        playVideo();
    }
    private ArrayList<File> videos;
    private String videoName;
    private int position;
    private MediaPlayer mediaPlayer;
    private Thread updateSeek;
    private void setListeners(){
        binding.pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    binding.pausePlay.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else {
                    binding.pausePlay.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
    }
    private void playVideo(){
        Intent intent=getIntent();
        videos= (ArrayList) intent.getParcelableArrayListExtra("videoList");
        videoName=intent.getStringExtra("videoName");
        binding.videoName.setText(videoName);
        binding.videoName.setSelected(true);
        position=intent.getIntExtra("position", 0);
        Uri uri=Uri.parse(videos.get(position).toString());
        binding.surfaceView.setKeepScreenOn(true);
        SurfaceHolder surfaceHolder=binding.surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri, surfaceHolder);
                mediaPlayer.start();
                binding.seekBar.setMax(mediaPlayer.getDuration());
                binding.seekBar.setProgress(0);
                updateSeek.start();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                mediaPlayer.stop();
                mediaPlayer.release();
                updateSeek.interrupt();
            }
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek=new Thread(){
            public void run(){
                int currentPosition=0;
                try{
                    while (currentPosition<mediaPlayer.getDuration()){
                        currentPosition=mediaPlayer.getCurrentPosition();
                        binding.seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
    }
}
