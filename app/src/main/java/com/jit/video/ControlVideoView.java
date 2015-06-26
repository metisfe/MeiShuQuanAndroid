package com.jit.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.metis.meishuquan.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Beak on 2015/6/26.
 */
public class ControlVideoView extends RelativeLayout {

    private SeekBar mSeekBar;
    private ImageView mPlay;
    private VideoView mVideoView;

    private String mVideoPath = null;

    // 音频管理器
    private AudioManager mAudioManager;

    // 声音调节Toast
    private VolumnController volumnController;

    // 原始屏幕亮度
    private int orginalLight;

    public ControlVideoView(Context context) {
        this(context, null);
    }

    public ControlVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init (Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_control_video, this);
        mSeekBar = (SeekBar)findViewById(R.id.progress);
        mPlay = (ImageView)findViewById(R.id.play);
        mVideoView = (VideoView)findViewById(R.id.video_view);

        volumnController = new VolumnController(context);

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    }

    public void setVideoPath (String path) {
        mVideoPath = path;
    }

    private Runnable hideRunnable = new Runnable() {

        @Override
        public void run() {
            //showOrHide();
        }
    };

    public void playVideo() {
        mVideoView.setVideoPath(mVideoPath);
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                /*mVideoView.setVideoWidth(mp.getVideoWidth());
                mVideoView.setVideoHeight(mp.getVideoHeight());*/

                mVideoView.start();
                /*if (playTime != 0) {
                    mVideo.seekTo(playTime);
                }

                mHandler.removeCallbacks(hideRunnable);
                mHandler.postDelayed(hideRunnable, HIDE_TIME);
                mDurationTime.setText(formatTime(mVideo.getDuration()));*/
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(1);
                    }
                }, 0, 1000);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlay.setImageResource(R.drawable.video_btn_down);
                //mPlayTime.setText("00:00");
                mSeekBar.setProgress(0);
            }
        });
        mVideoView.setOnTouchListener(mTouchListener);
    }

    public VideoView getVideoView () {
        return mVideoView;
    }

    private float mLastMotionX;
    private float mLastMotionY;
    private int startX;
    private int startY;
    private int threshold;
    private boolean isClick = true;

    private OnTouchListener mTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final float x = event.getX();
            final float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    startX = (int) x;
                    startY = (int) y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mLastMotionX;
                    float deltaY = y - mLastMotionY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    // 声音调节标识
                    boolean isAdjustAudio = false;
                    if (absDeltaX > threshold && absDeltaY > threshold) {
                        if (absDeltaX < absDeltaY) {
                            isAdjustAudio = true;
                        } else {
                            isAdjustAudio = false;
                        }
                    } else if (absDeltaX < threshold && absDeltaY > threshold) {
                        isAdjustAudio = true;
                    } else if (absDeltaX > threshold && absDeltaY < threshold) {
                        isAdjustAudio = false;
                    } else {
                        return true;
                    }
                    if (isAdjustAudio) {
                        /*if (x < width / 2) {
                            if (deltaY > 0) {
                                lightDown(absDeltaY);
                            } else if (deltaY < 0) {
                                lightUp(absDeltaY);
                            }
                        } else {
                            if (deltaY > 0) {
                                volumeDown(absDeltaY);
                            } else if (deltaY < 0) {
                                volumeUp(absDeltaY);
                            }
                        }*/

                    } else {
                        /*if (deltaX > 0) {
                            forward(absDeltaX);
                        } else if (deltaX < 0) {
                            backward(absDeltaX);
                        }*/
                    }
                    mLastMotionX = x;
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(x - startX) > threshold
                            || Math.abs(y - startY) > threshold) {
                        isClick = false;
                    }
                    mLastMotionX = 0;
                    mLastMotionY = 0;
                    startX = (int) 0;
                    if (isClick) {
                        //showOrHide();
                    }
                    isClick = true;
                    break;

                default:
                    break;
            }
            return true;
        }

    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mVideoView.getCurrentPosition() > 0) {
                        //mPlayTime.setText(formatTime(mVideoView.getCurrentPosition()));
                        int progress = mVideoView.getCurrentPosition() * 100 / mVideoView.getDuration();
                        mSeekBar.setProgress(progress);
                        if (mVideoView.getCurrentPosition() > mVideoView.getDuration() - 100) {
                            //mPlayTime.setText("00:00");
                            mSeekBar.setProgress(0);
                        }
                        mSeekBar.setSecondaryProgress(mVideoView.getBufferPercentage());
                    } else {
                        //mPlayTime.setText("00:00");
                        mSeekBar.setProgress(0);
                    }

                    break;
                case 2:
                    //showOrHide();
                    break;

                default:
                    break;
            }
        }
    };
}
