package com.example.y.study.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.y.study.R;
import com.example.y.study.myclass.MyMusic;

import java.util.List;

public class CheckMusicAdapter extends RecyclerView.Adapter<CheckMusicAdapter.ViewHolder> implements View.OnClickListener{
    private List<MyMusic> myMusicList;
    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener!=null){
            onItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public CheckMusicAdapter(List<MyMusic> myMusicList) {
        this.myMusicList = myMusicList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_music, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyMusic myMusic = myMusicList.get(position);
        holder.musicName.setText(myMusic.getMusicName());
        holder.musicSinger.setText(myMusic.getSinger());
        holder.itemView.setTag(position);

        holder.checkMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if (myMusic.getIsCheck()){
                      myMusic.setIsCheck(false);
                  }else {
                      myMusic.setIsCheck(true);
                  }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myMusicList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkMusic;
        TextView musicName, musicSinger;

        ViewHolder(View itemView) {
            super(itemView);
            checkMusic = itemView.findViewById(R.id.music_check_box);
            musicName = itemView.findViewById(R.id.check_music_name);
            musicSinger = itemView.findViewById(R.id.check_music_singer);

        }
    }
}
