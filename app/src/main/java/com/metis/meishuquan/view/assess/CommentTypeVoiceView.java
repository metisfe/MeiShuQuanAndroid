package com.metis.meishuquan.view.assess;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.metis.meishuquan.R;
import com.metis.meishuquan.manager.common.MediaManager;
import com.metis.meishuquan.manager.common.PlayerManager;
import com.metis.meishuquan.model.assess.AssessComment;
import com.metis.meishuquan.util.DownloadUtil;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.shared.DragListView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 评论带语音视图
 * <p/>
 * Created by wangjin on 15/4/23.
 */
public class CommentTypeVoiceView extends RelativeLayout {
    private Context context;
    private AssessComment assessComment;

    private CommentTypeVoiceView commentTypeVoiceView;
    private ImageView imgPortrait;
    private TextView tvCommentUser, tvReply, tvReplyUser, tvContent, tvCommentTime;
    private FrameLayout btnPlayVoice;
    private TextView tvVoiceTime;
    private View viewAnim;

    private String path = "";

    private int mMinVoiceWidth;
    private int mMaxVoiceWidth;


    public void setAssessComment(AssessComment assessComment) {
        this.assessComment = assessComment;
        initData(assessComment);
    }

    public CommentTypeVoiceView(Context context) {
        super(context);
        this.context = context;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mMaxVoiceWidth = (int) (outMetrics.widthPixels * 0.7f);
        mMinVoiceWidth = (int) (outMetrics.widthPixels * 0.15f);

        commentTypeVoiceView = (CommentTypeVoiceView) LayoutInflater.from(context).inflate(R.layout.layout_assess_reply_comment_type_voice, this);
        initView(commentTypeVoiceView);
        intEvent();
    }

    private void initView(CommentTypeVoiceView commentTypeVoiceView) {
        this.imgPortrait = (ImageView) commentTypeVoiceView.findViewById(R.id.id_img_comment_user_portrait);
        this.tvCommentUser = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_comment_user_name);
        this.tvReply = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_reply);
        this.tvReplyUser = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_reply_user_name);
        this.tvContent = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_comment_content);
        this.tvCommentTime = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_comment_createtime);
        this.btnPlayVoice = (FrameLayout) commentTypeVoiceView.findViewById(R.id.id_btn_play_voice);
        this.viewAnim = commentTypeVoiceView.findViewById(R.id.id_view_anim);
        this.tvVoiceTime = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_recorder_length);
    }

    private void initData(final AssessComment assessComment) {
        if (assessComment != null) {
            ImageLoaderUtils.getImageLoader(this.context).
                    displayImage(assessComment.getUser().getAvatar(),
                            this.imgPortrait,
                            ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.user_portrait_height)));

            this.tvCommentUser.setText(assessComment.getUser().getName());//评论人
            if (assessComment.getReplyUser() == null || assessComment.getReplyUser().getName().isEmpty()) {
                this.tvReply.setVisibility(View.GONE);
                this.tvReplyUser.setVisibility(View.GONE);
            }
            this.tvReplyUser.setText(assessComment.getReplyUser().getName());//被回复人
            this.tvCommentTime.setText(assessComment.getCommentDateTime());//评论时间

            ViewGroup.LayoutParams lp = this.btnPlayVoice.getLayoutParams();
            lp.width = (int) (mMinVoiceWidth + (mMaxVoiceWidth / 60f + assessComment.getTime()));
            this.tvVoiceTime.setText(Math.round(assessComment.getTime()) + "\"");
        }
    }


    private void intEvent() {
        this.tvCommentUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "进入" + assessComment.getUser().getName() + "的个人主页", Toast.LENGTH_SHORT).show();
            }
        });

        this.tvReplyUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "进入" + assessComment.getReplyUser().getName() + "的个人主页", Toast.LENGTH_SHORT).show();
            }
        });

        //播放语音
        this.btnPlayVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (assessComment.getImgOrVoiceUrl() == null || assessComment.getImgOrVoiceUrl().size() == 0) {
                    Toast.makeText(context, "播放失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = assessComment.getImgOrVoiceUrl().get(0).getVoiceUrl();
                DownloadTask downloadTask = new DownloadTask(context, url);
                downloadTask.execute();
            }
        });
    }

    class DownloadTask extends AsyncTask<Void, Integer, Integer> {
        private Context context;
        private String url;

        DownloadTask(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            int result = 0;
            if (path != "") {
                return result;
            }
            if (!url.isEmpty()) {
                DownloadUtil downloadUtil = new DownloadUtil();
                String fileName = SystemClock.currentThreadTimeMillis() + ".amr";
                result = downloadUtil.downFile(url, "/msqdownload/", fileName);
                path = DownloadUtil.downloadPath + "/msqdownload/" + fileName;
                Log.e("path", path);
            }
            return result;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            Log.i("path", path);
            if (viewAnim != null) {
                viewAnim.setBackgroundResource(R.drawable.adj);
                viewAnim = null;
            }
            if (viewAnim == null) {
                viewAnim = commentTypeVoiceView.findViewById(R.id.id_view_anim);
            }
            viewAnim.setBackgroundResource(R.drawable.play_anim);
            final AnimationDrawable animation = (AnimationDrawable) viewAnim.getBackground();
            animation.start();
//            PlayerManager.getInstance(context).start(path);

            MediaManager.playSound(path, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    animation.stop();
                    viewAnim.setBackgroundResource(R.drawable.adj);
                }
            });
            Toast.makeText(context, "下载完毕播放", Toast.LENGTH_SHORT).show();
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
}
