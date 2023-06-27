package com.harsh.zenplayer.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harsh.zenplayer.databinding.SongContainerBinding;
import com.harsh.zenplayer.listeners.SongListener;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<String> songList;
    private final SongListener songListener;
    public Adapter(ArrayList<String> songList, SongListener songListener) {
        this.songList = songList;
        this.songListener = songListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SongContainerBinding songContainerBinding= SongContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new songViewHolder(songContainerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((songViewHolder) holder).setData(songList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
    class songViewHolder extends RecyclerView.ViewHolder{
        SongContainerBinding binding;
        public songViewHolder(@NonNull SongContainerBinding songContainerBinding) {
            super(songContainerBinding.getRoot());
            binding=songContainerBinding;
        }
        void setData(String songName, int position){
            binding.songName.setText(songName);
            binding.getRoot().setOnClickListener(v-> songListener.onSongClicked(songName, position));
        }
    }
}
