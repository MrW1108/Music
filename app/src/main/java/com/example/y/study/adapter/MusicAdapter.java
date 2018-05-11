package com.example.y.study.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.y.study.myclass.MyMusic;
import com.example.y.study.R;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> implements View.OnClickListener {
    private int selected;
    private List<MyMusic> musicList;
    private OnItemClickListener onItemClickListener;
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_music_content, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyMusic music = musicList.get(position);
        holder.content.removeAllViews();
        if (position==selected){
            holder.content.addView(addFocusView());
            TextView textView=holder.content.findViewById(R.id.music_playing);
            textView.setText("正在播放："+music.getMusicName());
        }else{
            holder.content.addView(addNormalView());
            TextView musicName=holder.content.findViewById(R.id.music_name);
            TextView musicSinger=holder.content.findViewById(R.id.music_singer);
            musicName.setText(music.getMusicName());
            musicSinger.setText(music.getSinger());
        }

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(view, (Integer) view.getTag());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout content;

        ViewHolder(View itemView) {
            super(itemView);
            content=itemView.findViewById(R.id.content);
        }
    }

    private View addFocusView(){
        return LayoutInflater.from(mContext).inflate(R.layout.item_music_focus,null,false);
    }
    private View addNormalView(){
        return LayoutInflater.from(mContext).inflate(R.layout.item_music_list,null,false);
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public MusicAdapter(List<MyMusic> musicList) {
        this.musicList = musicList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
