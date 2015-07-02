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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.metis.meishuquan.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Beak on 2015/6/26.
 */
public class ControlVideoView extends RelativeLayout {

    private SeekBar mSeekBar;
    private ImageView mPlay;
    private VideoView mVideoView;
    private TextView mDurationTime, mPlayTime;
    private ImageView mFullScreenBtn = null;
    private RelativeLayout mControlContainer = null;


    private String mVideoPath = null;

    private int playTime = 0;

    private static final int HIDE_TIME = 5000;

    private AudioManager mAudioManager;

    private OnPlayEndListener mEndListener = null;

    private VolumnController volumnController;

    private int orginalLight;

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play:
                    if (mVideoView.isPlaying()) {
                        pause();
                    } else {
                        playVideo();
                    }
                    break;
            }
        }
    };

    private boolean isFullScreen = false;

//    private MediaController mController = null;

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
        mDurationTime = (TextView)findViewById(R.id.duration);
        mPlayTime = (TextView)findViewById(R.id.current);
        mFullScreenBtn = (ImageView)findViewById(R.id.full_screen);
        mControlContainer = (RelativeLayout)findViewById(R.id.control_container);

        volumnController = new VolumnController(context);

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        mPlay.setOnClickListener(mOnClickListener);
        mFullScreenBtn.setOnClickListener(mOnClickListener);

//        mController = new MediaController(context);
//        mController.hide();
//        mController.setVisibility(GONE);
        //mVideoView.setMediaController(mController);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mVideoView.seekTo(mVideoView.getDuration() / 100 * seekBar.getProgress());
            }
        });

    }

    public void setFullScreenClickListener (OnClickListener listener) {
        mFullScreenBtn.setOnClickListener(listener);
    }

    public boolean isFullScreen () {
        return isFullScreen;
    }

    public void setFullScreen (boolean fullScreen) {
        isFullScreen = fullScreen;
        mFullScreenBtn.setImageResource(fullScreen ? R.drawable.ic_arrow_collapse : R.drawable.ic_arrow_expand);
        /*if (isFullScreen) {
            int screenWid = getContext().getResources().getDisplayMetrics().widthPixels;
            int screenHei = getContext().getResources().getDisplayMetrics().heightPixels;
            this.setLayoutParams(new ViewGroup.LayoutParams(screenWid, screenHei));
        } else {
            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }*/
    }

    public void setVideoPath (String path) {
        mVideoPath = path;
    }

    private Runnable hideRunnable = new Runnable() {

        @Override
        public void run() {
            showOrHide();
        }
    };

    public void pause () {
        mVideoView.pause();
        playTime = mVideoView.getCurrentPosition();
        mPlay.setImageResource(R.drawable.ic_play);
    }

    public void playVideo() {
        mVideoView.setVideoPath(mVideoPath);
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                /*mVideoView.setVideoWidth(mp.getVideoWidth());
                mVideoView.setVideoHeight(mp.getVideoHeight());*/

                mVideoView.start();

                mPlay.setImageResource(R.drawable.ic_pause);
                if (playTime != 0) {
                    mVideoView.seekTo(playTime);
                }

                mHandler.removeCallbacks(hideRunnable);
                mHandler.postDelayed(hideRunnable, HIDE_TIME);
                mDurationTime.setText(formatTime(mVideoView.getDuration()));
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
                mHandler.removeCallbacks(hideRunnable);
                mPlay.setImageResource(R.drawable.ic_play);
                mPlayTime.setText("00:00");
                mSeekBar.setProgress(0);
                if (mEndListener != null) {
                    mEndListener.onEnd();
                }
            }
        });
//        mVideoView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showOrHide();
//                Toast.makeText(view.getContext(), "onClick", Toast.LENGTH_SHORT).show();
//            }
//        });
        mVideoView.setOnTouchListener(mTouchListener);
    }

    public VideoView getVideoView () {
        return mVideoView;
    }

    @SuppressLint("SimpleDateFormat")
    private String formatTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
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
//                case MotionEvent.ACTION_DOWN:
//                    mLastMotionX = x;
//                    mLastMotionY = y;
//                    startX = (int) x;
//                    startY = (int) y;
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    float deltaX = x - mLastMotionX;
//                    float deltaY = y - mLastMotionY;
//                    float absDeltaX = Math.abs(deltaX);
//                    float absDeltaY = Math.abs(deltaY);
//
//                    boolean isAdjustAudio = false;
//                    if (absDeltaX > threshold && absDeltaY > threshold) {
//                        if (absDeltaX < absDeltaY) {
//                            isAdjustAudio = true;
//                        } else {
//                            isAdjustAudio = false;
//                        }
//                    } else if (absDeltaX < threshold && absDeltaY > threshold) {
//                        isAdjustAudio = true;
//                    } else if (absDeltaX > threshold && absDeltaY < threshold) {
//                        isAdjustAudio = false;
//                    } else {
//                        return true;
//                    }
//                    if (isAdjustAudio) {
//                        /*if (x < width / 2) {
//                            if (deltaY > 0) {
//                                lightDown(absDeltaY);
//                            } else if (deltaY < 0) {
//                                lightUp(absDeltaY);
//                            }
//                        } else {
//                            if (deltaY > 0) {
//                                volumeDown(absDeltaY);
//                            } else if (deltaY < 0) {
//                                volumeUp(absDeltaY);
//                            }
//                        }*/
//
//                    } else {
//                        /*if (deltaX > 0) {
//                            forward(absDeltaX);
//                        } else if (deltaX < 0) {
//                            backward(absDeltaX);
//                        }*/
//                    }
//                    mLastMotionX = x;
//                    mLastMotionY = y;
//                    break;
                case MotionEvent.ACTION_UP:
                    showOrHide();
//                    if (Math.abs(x - startX) > threshold
//                            || Math.abs(y - startY) > threshold) {
//                        isClick = false;
//                    }
//                    mLastMotionX = 0;
//                    mLastMotionY = 0;
//                    startX = (int) 0;
//                    if (isClick) {
//                        showOrHide();
//                    }
//                    isClick = true;
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
                        mPlayTime.setText(formatTime(mVideoView.getCurrentPosition()));
                        int progress = mVideoView.getCurrentPosition() * 100 / mVideoView.getDuration();
                        mSeekBar.setProgress(progress);
                        if (mVideoView.getCurrentPosition() > mVideoView.getDuration() - 100) {
                            mPlayTime.setText("00:00");
                            mSeekBar.setProgress(0);
                        }
                        mSeekBar.setSecondaryProgress(mVideoView.getBufferPercentage());
                    } else {
                        mPlayTime.setText("00:00");
                        mSeekBar.setProgress(0);
                    }

                    break;
                case 2:
                    showOrHide();
                    break;

                default:
                    break;
            }
        }
    };



    private void showOrHide () {
        if (mControlContainer.getVisibility() == VISIBLE) {
            mControlContainer.setVisibility(GONE);
            mHandler.removeCallbacks(hideRunnable);
        } else {
            mControlContainer.setVisibility(VISIBLE);
            mHandler.postDelayed(hideRunnable, 3000);
        }

    }

    public void setOnPlayEndListener (OnPlayEndListener listener) {
        mEndListener = listener;
    }

    public static interface OnPlayEndListener {
        public void onEnd ();
    }
}
