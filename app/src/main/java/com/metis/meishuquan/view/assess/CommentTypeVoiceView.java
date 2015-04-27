package com.metis.meishuquan.view.assess;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;
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

    private ImageView imgPortrait;
    private TextView tvCommentUser, tvReply, tvReplyUser, tvContent, tvCommentTime;
    private Button btnPlayVoice;
    private String path = "";

    public void setAssessComment(AssessComment assessComment) {
        this.assessComment = assessComment;
        initData(assessComment);
    }

    public CommentTypeVoiceView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_assess_reply_comment_type_voice, this);
        initView(this);
        intEvent();
    }

    private void initView(CommentTypeVoiceView commentTypeVoiceView) {
        this.imgPortrait = (ImageView) commentTypeVoiceView.findViewById(R.id.id_img_comment_user_portrait);
        this.tvCommentUser = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_comment_user_name);
        this.tvReply = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_reply);
        this.tvReplyUser = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_reply_user_name);
        this.tvContent = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_comment_content);
        this.tvCommentTime = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_comment_createtime);
        this.btnPlayVoice = (Button) commentTypeVoiceView.findViewById(R.id.id_btn_play_voice);
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
                if (path.isEmpty()) {
                    String url = assessComment.getImgOrVoiceUrl().get(0).getVoiceUrl();
                    DownloadTask downloadTask = new DownloadTask(context, url);
                    downloadTask.execute();
                } else {
                    PlayerManager.getInstance(context).start(path);
                }
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
            if (!url.isEmpty()) {
                DownloadUtil downloadUtil = new DownloadUtil();
                String fileName = SystemClock.currentThreadTimeMillis() + ".mp3";
                result = downloadUtil.downFile(url, "", fileName);
                path = DownloadUtil.downloadPath + fileName;
            }
            return result;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            Toast.makeText(context, "下载完毕播放", Toast.LENGTH_SHORT).show();
            Log.i("path", path);
            PlayerManager.getInstance(context).start(path);
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
}
