package com.harsh.zenplayer.activities;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.harsh.zenplayer.R;
import com.harsh.zenplayer.databinding.SongPlayerBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class SongPlayer extends AppCompatActivity {

    private ArrayList<File> songs;
    private String songName;
    private int position;
    private MediaPlayer mediaPlayer;
    private SongPlayerBinding binding;
    private Thread updateSeek;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= SongPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        playSong();
    }
    private void setListeners(){
        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position=position-1;
                }
                else {
                    position=songs.size()-1;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
                binding.pausePlay.setImageResource(R.drawable.pause);
                mediaPlayer.start();
                binding.songName.setText(songs.get(position).getName().replace(".mp3", ""));
                binding.songName.setSelected(true);
                binding.seekBar.setProgress(0);
                binding.seekBar.setMax(mediaPlayer.getDuration());
                updateSeek();
            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position=position+1;
                }
                else{
                    position=0;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
                binding.pausePlay.setImageResource(R.drawable.pause);
                mediaPlayer.start();
                binding.songName.setText(songs.get(position).getName().replace(".mp3", ""));
                binding.songName.setSelected(true);
                binding.seekBar.setProgress(0);
                binding.seekBar.setMax(mediaPlayer.getDuration());
                updateSeek();
            }
        });
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
        binding.shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                Random random = new Random();
                int position = random.nextInt((songs.size()-1) + 1);
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
                binding.pausePlay.setImageResource(R.drawable.pause);
                mediaPlayer.start();
                binding.songName.setText(songs.get(position).getName().replace(".mp3", ""));
                binding.songName.setSelected(true);
                binding.seekBar.setProgress(0);
                binding.seekBar.setMax(mediaPlayer.getDuration());
                updateSeek();
            }
        });
    }
    private void playSong(){
        Intent intent=getIntent();
        songs= (ArrayList) intent.getParcelableArrayListExtra("songList");
        songName=intent.getStringExtra("songName");
        binding.songName.setText(songName);
        binding.songName.setSelected(true);
        position=intent.getIntExtra("position", 0);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this, uri);
        mediaPlayer.start();
        binding.seekBar.setMax(mediaPlayer.getDuration());

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
        updateSeek();
    }
    private void updateSeek(){
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
        updateSeek.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }
}
