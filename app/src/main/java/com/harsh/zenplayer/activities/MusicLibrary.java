package com.harsh.zenplayer.activities;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.harsh.zenplayer.adapters.Adapter;
import com.harsh.zenplayer.databinding.SongListBinding;
import com.harsh.zenplayer.listeners.SongListener;

import java.io.File;
import java.util.ArrayList;

public class MusicLibrary extends AppCompatActivity implements SongListener {
    private SongListBinding binding;
    Adapter adapter;
    private ArrayList<File> songList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= SongListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        song();
    }
    private void song(){
        songList=fetch(Environment.getExternalStorageDirectory());
        ArrayList<String> songNames = new ArrayList<>();
        for(int i=0; i<songList.size(); i++){
            songNames.add(songList.get(i).getName().replace(".mp3", ""));
        }
        if(songNames.size()>0){
            adapter=new Adapter(songNames, this);
            binding.RecyclerView.setAdapter(adapter);
            binding.RecyclerView.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(this, "No Songs Found", Toast.LENGTH_SHORT).show();
        }
    }
    private ArrayList<File> fetch(File file){
        ArrayList<File> list=new ArrayList<>();
        File[] songs=file.listFiles();
        if(songs!=null){
            for(File myFile: songs){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    list.addAll(fetch(myFile));
                }
                else {
                    if(myFile.getName().endsWith(".mp3") && (!myFile.getName().startsWith("."))){
                        list.add(myFile);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public void onSongClicked(String song, int position) {
        Intent intent =new Intent(getApplicationContext(), SongPlayer.class);
        intent.putExtra("songName", song);
        intent.putExtra("songList", songList);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
