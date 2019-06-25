package com.fivefivelike.mybaselibrary.utils;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * @ author      Qsy
 * @ date        16/12/29 下午4:05
 * @ description 播放器Util
 */
public class MediaPlayerUtil {

    private static MediaPlayerUtil instance;
    private static MediaPlayer player;
    private String currentFilePath;
    /**
     * 当前监听器
     */
    private MediaListener listener = new MediaListener() {
        @Override
        public void performStart() {

        }

        @Override
        public void performPause() {

        }

        @Override
        public void onPrepared() {

        }

        @Override
        public void onCompleted() {

        }
    };

    public static MediaPlayerUtil getInstance() {
        if (instance == null) {
            instance = new MediaPlayerUtil();
            player = new MediaPlayer();
        }
        return instance;
    }

    public void prepare(String fileName, final MediaListener newListener) {
        if (fileName.equals(currentFilePath)) {
            if (player.isPlaying()) {
                player.pause();
                if (listener != null)
                    listener.performPause();
            } else {
                player.start();
                if (listener != null)
                    listener.performStart();
            }
            return;
        } else {
            listener.performPause();
        }
        listener = newListener;

        player.reset();

        try {
            player.setDataSource(fileName);
            currentFilePath = fileName;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        player.setLooping(false);// 是否轮训
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                if (listener != null) {
                    listener.onPrepared();
                }
                player.start();
            }
        });
        player.prepareAsync();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.seekTo(0);
                listener.onCompleted();
            }
        });
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void play() {
        player.pause();
    }

    public void start() {
        player.pause();
    }

    public boolean isPlayingCurrentFile(String path) {
        return currentFilePath!=null&&path.equals(currentFilePath) && player.isPlaying();
    }

    public interface MediaListener {
        void performStart();

        void performPause();

        void onPrepared();

        void onCompleted();
    }

}
