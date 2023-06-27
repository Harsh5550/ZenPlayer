package com.harsh.zenplayer.adapters;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harsh.zenplayer.databinding.VideoContainerBinding;
import com.harsh.zenplayer.listeners.VideoListener;

import java.util.ArrayList;

public class videoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<String> videoList;
    private final VideoListener videoListener;
    public videoAdapter(ArrayList<String> videoList, VideoListener videoListener) {
        this.videoList = videoList;
        this.videoListener = videoListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VideoContainerBinding videoContainerBinding=VideoContainerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new videoViewHolder(videoContainerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((videoViewHolder) holder).setData(videoList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class videoViewHolder extends RecyclerView.ViewHolder{
        VideoContainerBinding binding;
        public videoViewHolder(@NonNull VideoContainerBinding videoContainerBinding) {
            super(videoContainerBinding.getRoot());
            binding=videoContainerBinding;
        }
        public void setData(String videoName, int position){
            binding.videoName.setText(videoName);
            binding.getRoot().setOnClickListener(v-> videoListener.onVideoClicked(videoName, position));
        }
    }
}
