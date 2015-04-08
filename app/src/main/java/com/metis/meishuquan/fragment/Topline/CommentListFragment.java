package com.metis.meishuquan.fragment.Topline;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.login.LoginFragment;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.AllComments;
import com.metis.meishuquan.model.topline.Comment;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.metis.meishuquan.view.shared.DragListView;
import com.metis.meishuquan.view.topline.CommentInputView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论列表
 * <p/>
 * Created by wj on 15/3/27.
 */
public class CommentListFragment extends Fragment {

    private ViewGroup rootView;
    private DragListView listView;
    private TextView tvCommentCount;
    private Button btnBack, btnWriteComment, btnCommentlist, btnShare, btnPrivate;
    private CommentInputView commentInputView;


    private int newsId = 0;
    private int totalCommentCount = 0;
    private List<Comment> lstAllComments = new ArrayList<Comment>();
    private CommentsAdapter adapter;
    private FragmentManager fm;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Comment> result = (List<Comment>) msg.obj;
            switch (msg.what) {
                case DragListView.REFRESH:
                    listView.onRefreshComplete();
                    lstAllComments.clear();
                    lstAllComments.addAll(result);
                    break;
                case DragListView.LOAD:
                    listView.onLoadComplete();
                    lstAllComments.addAll(result);
                    break;
            }
            listView.setResultSize(result.size());
            adapter.notifyDataSetChanged();
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //接收数据
        Bundle args = this.getArguments();
        if (args != null) {
            newsId = args.getInt("newsId");
            totalCommentCount = args.getInt("totalCommentCount");
            //加载评论列表数据
            getData(newsId, DragListView.REFRESH);
        }

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_topline_comment_list, null, false);
        initView(rootView);
        initEvent();
        return rootView;
    }

    //初始化视图
    private void initView(ViewGroup rootView) {
        listView = (DragListView) rootView.findViewById(R.id.id_topline_comment_list);
        btnBack = (Button) rootView.findViewById(R.id.id_btn_back);
        btnCommentlist = (Button) rootView.findViewById(R.id.id_btn_commentlist);
        btnWriteComment = (Button) rootView.findViewById(R.id.id_btn_writecomment);
        btnShare = (Button) rootView.findViewById(R.id.id_btn_share);
        btnPrivate = (Button) rootView.findViewById(R.id.id_btn_private);
        tvCommentCount = (TextView) rootView.findViewById(R.id.id_tv_topline_info_comment_count);
        tvCommentCount.setText(String.valueOf(this.totalCommentCount));
        commentInputView = new CommentInputView(getActivity(), null, 0);
        fm = getActivity().getSupportFragmentManager();

        adapter = new CommentsAdapter(lstAllComments);
        listView.setAdapter(adapter);
    }

    public void initEvent() {
        listView.setOnRefreshListener(new DragListView.OnRefreshListener() {//列表刷新
            @Override
            public void onRefresh() {
                getData(newsId, DragListView.REFRESH);
            }
        });

        listView.setOnLoadListener(new DragListView.OnLoadListener() {//列表加载
            @Override
            public void onLoad() {
                getData(newsId, DragListView.LOAD);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {//返回
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(CommentListFragment.this);
                ft.commit();
            }
        });

        btnWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                String loginState = spu.getStringByKey(SharedPreferencesUtil.LOGIN_STATE);
                if (loginState != null && loginState.equals("已登录")) {
                    showOrHideCommentInputView(true);
                    Utils.showInputMethod(getActivity(), commentInputView.editText);
                } else {
                    LoginFragment loginFragment = new LoginFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.content_container, loginFragment);
                    ft.commit();
                }
            }
        });

        this.btnCommentlist.setOnClickListener(new View.OnClickListener() {//评论列表
            @Override
            public void onClick(View view) {//查看评论列表

            }
        });

        this.btnPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//收藏
                TopLineOperator topLineOperator = TopLineOperator.getInstance();
                topLineOperator.newsPrivate(0, newsId, 0, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Utils.alertMessageDialog("提示", "收藏成功！");
                        }
                    }
                });

            }
        });

        this.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SharePopupWindow(MainApplication.UIContext, rootView);
            }
        });
    }

    //显示或隐藏评论视图
    private void showOrHideCommentInputView(boolean isShow) {
        ViewGroup parent = (ViewGroup) getActivity().findViewById(R.id.ll_parent);
        TranslateAnimation translateAnimation = null;
        int yStart = -getActivity().getResources().getDisplayMetrics().heightPixels;
        int yEnd = 0;
        if (isShow) {
            if (commentInputView != null) {
                parent.addView(commentInputView);
            }
            translateAnimation = new TranslateAnimation(0, 0, yStart, yEnd);
        } else {
            if (commentInputView != null) {
                parent.removeView(commentInputView);
            }
            translateAnimation = new TranslateAnimation(0, 0, yEnd, yStart);
        }
        translateAnimation.setFillBefore(true);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setDuration(0);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        commentInputView.startAnimation(translateAnimation);
    }

    //加载评论列表数据
    private void getData(int newsId, final int type) {
        TopLineOperator topLineOperator = TopLineOperator.getInstance();
        topLineOperator.getCommentListByNewId(0, newsId, 0, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                AllComments commentsData = new AllComments();
                if (result != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    commentsData = gson.fromJson(json, new TypeToken<AllComments>() {
                    }.getType());
                    List<Comment> data = new ArrayList<Comment>();
                    if (commentsData != null) {
                        List<Comment> lstHostComments = commentsData.getData().getHostComments();
                        List<Comment> lstNewComments = commentsData.getData().getNewComments();

                        if (lstHostComments != null && lstHostComments.size() > 0) {
                            Comment commentGroup = new Comment();
                            commentGroup.setGroup("热门评论");
                            data.add(commentGroup);
                            data.addAll(lstHostComments);
                        }
                        if (lstNewComments != null && lstNewComments.size() > 0) {
                            Comment commentGroup = new Comment();
                            commentGroup.setGroup("最新评论");
                            data.add(commentGroup);
                            data.addAll(lstNewComments);
                        }
                    }
                    Message msg = handler.obtainMessage();
                    msg.what = type;
                    msg.obj = data;
                    handler.sendMessage(msg);
                }
            }
        });
    }


    private class CommentsAdapter extends BaseAdapter {
        private List<Comment> lstAllComments = new ArrayList<Comment>();
        private ViewHolder holder;

        public CommentsAdapter(List<Comment> lstAllComments) {
            this.lstAllComments = lstAllComments;
        }

        private class ViewHolder {
            SmartImageView portrait;
            TextView userName, source, notifyTime, content, tag;
            Button btn_support, btn_comment;

        }

        @Override
        public int getCount() {
            return lstAllComments.size();
        }

        @Override
        public Object getItem(int i) {
            return lstAllComments.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean isEnabled(int position) {
            if (lstAllComments.get(position).getGroup().equals("热门评论") || lstAllComments.get(position).getGroup().equals(("最新评论"))) {
                return false;
            }
            return super.isEnabled(position);
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            if (convertView == null) {
                holder = new ViewHolder();
                if (lstAllComments.get(i).getGroup().equals("热门评论") || lstAllComments.get(i).getGroup().equals(("最新评论"))) {
                    view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_topline_comment_list_item_tag, null);
                    holder.tag = (TextView) view.findViewById(R.id.id_tv_listview_tag);
                    holder.tag.setText(lstAllComments.get(i).getGroup());
                } else {
                    view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_comment_list_item, null);
                    //holder.portrait= (SmartImageView) view.findViewById(R.id.id_img_portrait);
                    holder.userName = (TextView) view.findViewById(R.id.id_username);
                    //holder.source= (TextView) view.findViewById(R.id.id_username);
                    holder.notifyTime = (TextView) view.findViewById(R.id.id_notifytime);
                    holder.content = (TextView) view.findViewById(R.id.id_textview_comment_content);
                    holder.btn_support = (Button) view.findViewById(R.id.id_btn_support);
                    holder.btn_comment = (Button) view.findViewById(R.id.id_btn_comment);
                    holder.btn_support.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TopLineOperator operator = TopLineOperator.getInstance();
                            operator.commentSurpot(0, newsId, lstAllComments.get(i).getId(), 0, 1);
                        }
                    });

                    holder.userName.setText(this.lstAllComments.get(i).getUser().getName());
                    //holder.source.setText(this.lstAllComments.get(i).getCommentDateTime());
                    String notifyTimeStr = this.lstAllComments.get(i).getCommentDateTime();
                    holder.notifyTime.setText(notifyTimeStr);
                    holder.content.setText(this.lstAllComments.get(i).getContent());
                }
                holder = (ViewHolder) view.getTag();
            }
            return view;
        }
    }
}
