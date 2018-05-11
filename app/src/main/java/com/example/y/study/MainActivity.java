package com.example.y.study;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.y.study.adapter.MusicAdapter;
import com.example.y.study.myclass.MyMusic;
import org.litepal.crud.DataSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private MusicAdapter musicAdapter;//recyclerView的适配器，用于显示音乐列表
    private List<MyMusic> musicList;
    private AppCompatSeekBar seekBar;
    private TextView timeStart, timeEnd;
    private int mPosition = -1;//定位当前播放的音乐
    private Button playB;//播放、暂停Button


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(new Handler.Callback() {  //在这里实现seekBar的动态更新
        @Override
        public boolean handleMessage(Message message) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            timeStart.setText(parseDate(mediaPlayer.getCurrentPosition()));
            updateProgress();//发送更新seekBar的消息
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();//获取权限，6.0之后读文件被设为危险权限，需要运行时请求
        initView();
       // Connector.getDatabase();
        //queryMusicFromDataBase();
        queryMusic();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "拒绝权限将无法正常使用程序！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                //queryMusic();
                break;
        }
        return true;
    }

    @Override//退出程序时要销毁mediaPlayer
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void initView() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        timeStart = findViewById(R.id.time_start);
        timeEnd = findViewById(R.id.time_end);
        seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);

        playB = findViewById(R.id.music_play);
        Button lastB = findViewById(R.id.last_music);
        Button nextB = findViewById(R.id.next_music);

        playB.setOnClickListener(this);
        lastB.setOnClickListener(this);
        nextB.setOnClickListener(this);

        //初始化RecyclerView
        final RecyclerView musicListView = findViewById(R.id.music_list);
        musicList = new ArrayList<>();
        musicListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        musicAdapter = new MusicAdapter(musicList);
        musicListView.setAdapter(musicAdapter);
        musicAdapter.setSelected(-1);
        musicAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                mPosition = position;
                changeMusic(position);
            }
        });
        musicListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    private void queryMusic() {
        //通过Cursor找出本地音乐文件（MP3）
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {

            while (cursor.moveToNext()) {
                //从属性名很容易看出所代表的音乐文件属性，所以一下属性不做讲解了
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                int time = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                int size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                //myMusic是我自己定义的一个javaBean，用来存放音乐文件
                MyMusic myMusic = new MyMusic(id, title, singer, path, size, time, album);
                //myMusic.save();//存数据库
                 musicList.add(myMusic);
            }
            musicAdapter.notifyDataSetChanged();
        }
        //queryMusicFromDataBase();
    }

    private void queryMusicFromDataBase() {
        musicList.clear();
        musicList.addAll(DataSupport.findAll(MyMusic.class));
        musicAdapter.notifyDataSetChanged();
    }


    //每秒发送一个空的message，提示handler更新
    private void updateProgress() {
        handler.sendMessageDelayed(Message.obtain(), 1000);
    }


    private void playMusic(String url,int position){
        Intent playIntent=new Intent(this,MusicService.class);
        playIntent.putExtra("url",url);
        playIntent.putExtra("position",position);
        startService(playIntent);
    }

    

    //音乐播放
    private void playMusic(MyMusic myMusic) {
        try {
            if (mediaPlayer == null) {  //判断是否为空，避免重复创建
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnCompletionListener(this);
            }
            mediaPlayer.reset();//播放前重置播放器，其实第一次播放时不需要做此操作，但为了这一方法复用性我选择在这里使用
            mediaPlayer.setDataSource(myMusic.getPath());//设置播放源
            mediaPlayer.prepare();//准备，这一步很关键，在新播放一首歌的时候必不可少
            mediaPlayer.start();//开始播放
            timeEnd.setText(parseDate(mediaPlayer.getDuration()));//用来显示音乐时长
            seekBar.setMax(mediaPlayer.getDuration());//设置seekBar的时长与音乐文件相同
            updateProgress();//开启seekBar的更新

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String parseDate(int time) {//cursor获取的时间是毫秒，这里将它转成常用的时间格式
        time = time / 1000;
        int min = time / 60;
        int second = time % 60;
        return min + ":" + second;
    }

    private void changeMusic(int position) {    //实现歌曲的切换
        if (position < 0) {
            mPosition = musicList.size() - 1;
            playMusic(musicList.get(mPosition));
        } else if (position > musicList.size() - 1) {
            mPosition = 0;
            playMusic(musicList.get(0));
        } else {
            playMusic(musicList.get(position));
        }
        musicAdapter.setSelected(mPosition);    //设置选中音乐

        //更新RecyclerView，有这一步的原因是我设置了两个布局，正在播放的音乐行布局变更
        musicAdapter.notifyDataSetChanged();
        playB.setBackgroundResource(R.drawable.ic_playing); //更新播放、暂停键的图标
    }


    private void startOrPause() {   //播放或暂停逻辑实现
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playB.setBackgroundResource(R.drawable.ic_pause);

        } else {
            mediaPlayer.start();
            playB.setBackgroundResource(R.drawable.ic_playing);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.last_music:   //上一首
                changeMusic(--mPosition);
                break;
            case R.id.music_play:   //播放/暂停
                if (mediaPlayer == null) {
                    changeMusic(0);
                    mPosition = 0;
                } else {
                    startOrPause();
                }
                break;
            case R.id.next_music://下一首
                changeMusic(++mPosition);
                break;
        }
    }

    //下面三个方法是OnSeekBarChangeListener需重写的方法，此处只需重写第三个
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(seekBar.getProgress());//将音乐定位到seekBar指定的位置
        updateProgress();
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) { //OnCompletionListener 重写方法，实现轮播效果
        changeMusic(++mPosition);
    }
}
