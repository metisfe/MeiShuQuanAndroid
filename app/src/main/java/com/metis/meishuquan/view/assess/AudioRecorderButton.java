package com.metis.meishuquan.view.assess;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.assess.DialogManager;
import com.metis.meishuquan.manager.common.AudioManager;

/**
 * 自定义Button:录音按钮（用于点评详情中）
 * <p/>
 * Created by wangjin on 15/5/6.
 */
public class AudioRecorderButton extends Button implements AudioManager.AudioStateListener {

    private static final int DISTANCE_Y_CANCEL = 50;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;

    private int mCurState = STATE_NORMAL;
    private boolean isRecording;

    private DialogManager mDialogManager;
    private AudioManager mAudioManager;

    private float mTime;
    private boolean mReady;//是否触发longclick

    public AudioRecorderButton(Context context) {
        this(context, null);

    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDialogManager = new DialogManager(getContext());

        String dir = Environment.getExternalStorageDirectory() + "/msq_recorder";

        mAudioManager = AudioManager.getInstance(dir);
        mAudioManager.setOnAudioStateListener(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mReady = true;
                mAudioManager.prepareAudio();
                return false;
            }
        });
    }

    public interface AudioFinishRecorderListener {
        void onFinish(float seconds, String filePath);
    }

    private AudioFinishRecorderListener mAudioFinishRecorderListener;

    public void setmAudioFinishRecorderListener(AudioFinishRecorderListener mAudioFinishRecorderListener) {
        this.mAudioFinishRecorderListener = mAudioFinishRecorderListener;
    }

    //获取音量大小的Runable
    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_AUDIO_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private static final int MSG_AUDIO_PREPARED = 0X100;
    private static final int MSG_AUDIO_CHANGED = 0X111;
    private static final int MSG_AUDIO_DIALOG_DIMISS = 0X112;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    mDialogManager.showRecordingDialog();
                    isRecording = true;
                    new Thread(mGetVoiceLevelRunnable).start();

                    break;
                case MSG_AUDIO_CHANGED:
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                    break;
                case MSG_AUDIO_DIALOG_DIMISS:
                    mDialogManager.dimissDialog();
                    break;
            }
        }
    };


    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:

                if (isRecording) {
                    //根据x，y的坐标，判断是否想要取消
                    if (wangToCancel(x, y)) {
                        changState(STATE_WANT_TO_CANCEL);
                    } else {
                        changState(STATE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording || mTime < 0.6f) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_AUDIO_DIALOG_DIMISS, 1300);
                } else if (mCurState == STATE_RECORDING) {//正常录制结束
                    mDialogManager.dimissDialog();
                    mAudioManager.release();
                    if (mAudioFinishRecorderListener != null) {
                        mAudioFinishRecorderListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
                    }

                } else if (mCurState == STATE_WANT_TO_CANCEL) {
                    //cancel
                    mDialogManager.dimissDialog();
                    mAudioManager.cancel();
                }

                reset();
                break;
        }


        return super.onTouchEvent(event);
    }

    //恢复状态及标志位
    private void reset() {
        isRecording = false;
        mReady = false;
        mTime = 0;
        changState(STATE_NORMAL);
    }

    private boolean wangToCancel(int x, int y) {
        if (x < 0 || x > getWidth()) {
            return true;
        }
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }
        return false;
    }

    private void changState(int state) {
        if (mCurState != state) {
            mCurState = state;
            switch (state) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recorder_recording);
                    setText(R.string.str_recorder_recording);
                    if (isRecording) {
                        mDialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_want_cancel);
                    mDialogManager.wantToCancel();
                    break;
            }
        }
    }
}
