package com.yang.testservice;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class LocalVideoActivity extends AppCompatActivity {

    private VideoView videoView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video);

        //本地的视频  需要在手机SD卡根目录添加一个 fl1234.mp4 视频
        String videoUrl1 = Environment.getExternalStorageDirectory().getPath()+"/fl1234.mp4" ;

        //网络视频
        //String videoUrl2 = Utils.videoUrl ;

        Uri uri = Uri.parse( videoUrl1 );

        videoView = (VideoView)this.findViewById(R.id.videoView );

        //设置视频控制器
        videoView.setMediaController(new MediaController(this));

        //播放完成回调
        videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());

        //设置视频路径
        videoView.setVideoURI(uri);

        //开始播放视频
        videoView.start();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( LocalVideoActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
}