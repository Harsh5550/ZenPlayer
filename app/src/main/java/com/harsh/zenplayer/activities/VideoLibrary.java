package com.harsh.zenplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.harsh.zenplayer.adapters.videoAdapter;
import com.harsh.zenplayer.databinding.VideoListBinding;
import com.harsh.zenplayer.listeners.VideoListener;

import java.io.File;
import java.util.ArrayList;

public class VideoLibrary extends AppCompatActivity implements VideoListener {
    videoAdapter adapter;
    private VideoListBinding binding;
    private ArrayList<File> videoList;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding=VideoListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        video();
    }
    private void video(){
        videoList=fetch(Environment.getExternalStorageDirectory());
        ArrayList<String> videoNames = new ArrayList<>();
        for(int i=0; i<videoList.size(); i++){
            if(videoList.get(i).getName().endsWith(".mp4")){
                videoNames.add(videoList.get(i).getName().replace(".mp4", ""));
            }
            else{
                videoNames.add(videoList.get(i).getName().replace(".mkv", ""));
            }
        }
        if(videoNames.size()>0){
            adapter= new videoAdapter(videoNames, this);
            binding.RecyclerView.setAdapter(adapter);
            binding.RecyclerView.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(this, "No Videos Found", Toast.LENGTH_SHORT).show();
        }
    }
    private ArrayList<File> fetch(File file){
        ArrayList<File> list=new ArrayList<>();
        File[] videos=file.listFiles();
        if(videos!=null){
            for(File myFiles: videos){
                if(!myFiles.isHidden() && myFiles.isDirectory()){
                    list.addAll(fetch(myFiles));
                }
                else {
                    if ((myFiles.getName().endsWith(".mp4")||(myFiles.getName().endsWith(".mkv"))) && !myFiles.getName().startsWith(".")){
                        list.add(myFiles);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public void onVideoClicked(String video, int position) {
        Intent intent =new Intent(getApplicationContext(), VideoPlayer.class);
        intent.putExtra("videoName", video);
        intent.putExtra("videoList", videoList);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
