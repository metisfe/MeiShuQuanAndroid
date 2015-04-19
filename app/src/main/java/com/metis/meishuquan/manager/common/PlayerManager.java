package com.metis.meishuquan.manager.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by gaoyunfei on 15/4/19.
 */
public class PlayerManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{
    private static PlayerManager sManager = new PlayerManager();

    public static PlayerManager getInstance (Context context) {
        sManager.setContext(context);
        return sManager;
    }

    private Context mContext = null;
    private AudioManager mAudioManager = null;
    private MediaPlayer mPlayer = null;

    private PlayerManager () {

    }

    private void setContext (Context context) {
        mContext = context.getApplicationContext();
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public void start (String path) {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        try {
            mPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepareAsync();
    }

    public void stop () {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}
