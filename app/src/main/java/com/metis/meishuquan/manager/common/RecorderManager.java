package com.metis.meishuquan.manager.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Created by WJ on 2015/4/17.
 */
public class RecorderManager implements MediaRecorder.OnInfoListener,
        MediaRecorder.OnErrorListener, AudioManager.OnAudioFocusChangeListener{

    private Context mContext = null;
    private OnRecorderListener mRecorderListener = null;

    private AudioManager mManager = null;
    private MediaRecorder mRecorder = null;

    private String mPath = null;

    private boolean isRecording = false;

    private int mOutputFormat, mEncoder, mEncodingBitRate, mSimpleRate;
    private int mDuration;



    private static RecorderManager sManager = new RecorderManager();

    public static RecorderManager getInstance (Context context) {
        sManager.setContext(context);
        return sManager;
    }

    /*public RecorderManager(Context context) {
        mContext = context.getApplicationContext();
        mManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        mRecoder = new MediaRecorder();
    }*/

    private void setContext (Context context) {
        mContext = context.getApplicationContext();
        mContext = context.getApplicationContext();
        mManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        mRecorder = new MediaRecorder();
    }

    public void start (String path) {
        if (isRecording()) {
            return;
        }
        mPath = path;

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setOutputFile(path);
        //mRecoder.setAudioEncodingBitRate(32);
        //mRecoder.setAudioSamplingRate(1);
        mRecorder.setMaxDuration(10 * 1000);
        mRecorder.setOnErrorListener(this);
        mRecorder.setOnInfoListener(this);
        try {
            mManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            mRecorder.prepare();
            mRecorder.start();
            isRecording = true;
            if (mRecorderListener != null) {
                mRecorderListener.onStarted(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mRecorderListener.onException(e.getLocalizedMessage());
            mManager.abandonAudioFocus(this);
            isRecording = false;
        }
    }

    public void stop () {
        stop(true);
    }

    private void stop (boolean userDone) {
        if (isRecording()) {
            isRecording = false;
            mRecorder.stop();
            if (mRecorderListener != null) {
                mRecorderListener.onStopped(mPath, userDone);
            }
            mManager.abandonAudioFocus(this);
        }

    }

    public boolean isRecording () {
        return isRecording;
    }

    public int getOutputFormat() {
        return mOutputFormat;
    }

    public void setOutputFormat(int mOutputFormat) {
        this.mOutputFormat = mOutputFormat;
    }

    public int getEncoder() {
        return mEncoder;
    }

    public void setEncoder(int mEncoder) {
        this.mEncoder = mEncoder;
    }

    public int getEncodingBitRate() {
        return mEncodingBitRate;
    }

    public void setEncodingBitRate(int mEncodingBitRate) {
        this.mEncodingBitRate = mEncodingBitRate;
    }

    public int getSimpleingRate() {
        return mSimpleRate;
    }

    public void setSimpleingRate(int mSimpleingRate) {
        this.mSimpleRate = mSimpleingRate;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public void setOnRecoderListener (OnRecorderListener listener) {
        mRecorderListener = listener;
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        stop();
        switch (what) {
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN:
                break;
        }
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        isRecording = false;
        switch (what) {
            case MediaRecorder.MEDIA_ERROR_SERVER_DIED:
                break;
            case MediaRecorder.MEDIA_RECORDER_ERROR_UNKNOWN:
                break;
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                stop(false);
                break;
        }
    }

    public static interface OnRecorderListener {
        public void onStarted(String path);
        public void onRecording();
        public void onStopped(String path, boolean userDone);
        public void onException(String errorMsg);
    }

}
