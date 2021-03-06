package com.metis.meishuquan.fragment.Topline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.BlockTypeEnum;
import com.metis.meishuquan.model.enums.LoginStateEnum;
import com.metis.meishuquan.model.enums.PrivateResultEnum;
import com.metis.meishuquan.model.enums.PrivateTypeEnum;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.model.topline.AllComments;
import com.metis.meishuquan.model.topline.Comment;
import com.metis.meishuquan.util.ActivityUtils;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.metis.meishuquan.view.shared.DragListView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论列表
 * <p/>
 * Created by wj on 15/3/27.
 */
public class CommentListFragment extends Fragment {

    private static final String CLASS_NAME=CommentListFragment.class.getSimpleName();

    private ViewGroup rootView;
    private DragListView listView;
    private TextView tvCommentCount;
    private Button btnBack;
    private RelativeLayout rl_WriteComment, rl_Commentlist, rl_Share, rl_Private;
    private ImageView imgPrivate;
    private EditText editText;//评论或回复输入框
    private RelativeLayout rlSend, rlInput;//发送


    private int newsId = 0;
    private int totalCommentCount = 0;
    private int childCommentId = -1;
    private int lastCommentId = 0;
    private boolean isPrivate = false;
    private List<Comment> lstAllComments = new ArrayList<Comment>();
    private CommentsAdapter adapter;
    private FragmentManager fm;
    private Animation animation;


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
            tvCommentCount.setText(adapter.getCount() + "");
        }
    };

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        int animId = R.anim.right_out;
        if (enter) {
            animId = R.anim.right_in;
        }
        return AnimationUtils.loadAnimation(getActivity(), animId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //接收数据
        Bundle args = this.getArguments();
        if (args != null) {
            newsId = args.getInt("newsId");
            totalCommentCount = args.getInt("totalCommentCount");
            //加载评论列表数据
            getData(newsId, 0, DragListView.REFRESH);
        }

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_topline_comment_list, null, false);
        initView(rootView);
        initEvent();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(CLASS_NAME); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(CLASS_NAME);
    }

    //初始化视图
    private void initView(ViewGroup rootView) {
        listView = (DragListView) rootView.findViewById(R.id.id_topline_comment_list);
        btnBack = (Button) rootView.findViewById(R.id.id_btn_back);
        rl_Commentlist = (RelativeLayout) rootView.findViewById(R.id.id_rl_commentlist);//评论列表
        rl_WriteComment = (RelativeLayout) rootView.findViewById(R.id.id_rl_writecomment);//写评论
        rl_Share = (RelativeLayout) rootView.findViewById(R.id.id_rl_share);//分享
        rl_Private = (RelativeLayout) rootView.findViewById(R.id.id_rl_private);//收藏
        tvCommentCount = (TextView) rootView.findViewById(R.id.id_tv_topline_info_comment_count);//评论数
        imgPrivate = (ImageView) rootView.findViewById(R.id.id_img_favorite);//收藏图标

        //设置评论数
        if (totalCommentCount > 0) {
            tvCommentCount.setVisibility(View.VISIBLE);
            tvCommentCount.setText(String.valueOf(totalCommentCount));
        } else {
            tvCommentCount.setVisibility(View.GONE);
        }

        editText = (EditText) rootView.findViewById(R.id.id_comment_edittext);
        rlSend = (RelativeLayout) rootView.findViewById(R.id.id_rl_send);
        rlInput = (RelativeLayout) rootView.findViewById(R.id.id_rl_input);

        fm = getActivity().getSupportFragmentManager();
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.support_add_one);
        adapter = new CommentsAdapter(lstAllComments);
        listView.setAdapter(adapter);
    }

    public void initEvent() {
        listView.setOnRefreshListener(new DragListView.OnRefreshListener() {//列表刷新
            @Override
            public void onRefresh() {
                getData(newsId, lastCommentId, DragListView.REFRESH);
            }
        });

        listView.setOnLoadListener(new DragListView.OnLoadListener() {//列表加载
            @Override
            public void onLoad() {
                lastCommentId = lstAllComments.get(lstAllComments.size() - 1).getId();
                getData(newsId, lastCommentId, DragListView.LOAD);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {//返回
            @Override
            public void onClick(View view) {
                hideInputView();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(CommentListFragment.this);
                ft.commit();
            }
        });

        rl_WriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.userInfo.getAppLoginState() == LoginStateEnum.YES) {
                    showInputView();
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        this.rl_Commentlist.setOnClickListener(new View.OnClickListener() {//评论列表
            @Override
            public void onClick(View view) {//查看评论列表
                hideInputView();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        this.rl_Private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//收藏
                if (MainApplication.userInfo.getAppLoginState() == LoginStateEnum.YES) {
                    if (!isPrivate) {
                        //收藏
                        CommonOperator.getInstance().favorite(MainApplication.userInfo.getUserId(), newsId, SupportTypeEnum.News, PrivateResultEnum.PRIVATE, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Toast.makeText(MainApplication.UIContext, "收藏成功", Toast.LENGTH_SHORT).show();
                                    isPrivate = true;
                                    imgPrivate.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_topline_private));
                                }
                            }
                        });
                    } else {
                        //取消收藏
//                        CommonOperator.getInstance().favorite(MainApplication.userInfo.getUserId(), newsId, SupportTypeEnum.News, PrivateResultEnum.CANCEL, new ApiOperationCallback<ReturnInfo<String>>() {
//                            @Override
//                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
//                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
//                                    Toast.makeText(MainApplication.UIContext, "取消收藏", Toast.LENGTH_SHORT).show();
//                                    isPrivate = false;
//                                    imgPrivate.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_topline_unprivate));
//                                }
//                            }
//                        });
                    }
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        this.rl_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//分享
                new SharePopupWindow(getActivity(), rootView);
            }
        });

        this.listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    hideInputView();

                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    hideInputView();
                }
                return false;
            }
        });

        //发送
        rlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (childCommentId == -1) {
                    String content = editText.getText().toString();
                    if (!content.isEmpty()) {
                        CommonOperator.getInstance().publishComment(MainApplication.userInfo.getUserId(), newsId, content, 0, BlockTypeEnum.TOPLINE, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
                                    hideInputView();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "请输入评论内容", Toast.LENGTH_SHORT).show();
                    }
                } else {//发表子评论
                    String content = editText.getText().toString();
                    if (!content.isEmpty()) {
                        CommonOperator.getInstance().publishComment(MainApplication.userInfo.getUserId(), newsId, content, childCommentId, BlockTypeEnum.TOPLINE, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    getData(newsId, lastCommentId, DragListView.REFRESH);
                                    hideInputView();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "请输入评论内容", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    //加载评论列表数据
    private void getData(int newsId, int lastCommentId, final int type) {
        TopLineOperator topLineOperator = TopLineOperator.getInstance();
        topLineOperator.getCommentListByNewId(0, newsId, lastCommentId, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                AllComments commentsData = new AllComments();
                if (result != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    Log.i(getClass().getSimpleName(), "评论数据：" + json);
                    commentsData = gson.fromJson(json, new TypeToken<AllComments>() {
                    }.getType());
                    List<Comment> data = new ArrayList<Comment>();
                    if (commentsData != null) {
                        List<Comment> lstHostComments = commentsData.getData().getHotComments();
                        List<Comment> lstNewComments = commentsData.getData().getNewComments();

                        for (int i = 0; i < lstHostComments.size(); i++) {
                            lstHostComments.get(i).setGroup("热门评论");
                        }

                        for (int i = 0; i < lstNewComments.size(); i++) {
                            lstNewComments.get(i).setGroup("最新评论");
                        }

                        data.addAll(lstHostComments);
                        data.addAll(lstNewComments);
                        adapter.lstHostComments = lstHostComments;
                        adapter.lstNewComments = lstNewComments;
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

        public List<Comment> lstHostComments = new ArrayList<Comment>();

        public List<Comment> lstNewComments = new ArrayList<Comment>();

        public CommentsAdapter(List<Comment> lstAllComments) {
            this.lstAllComments = lstAllComments;
        }


        private class ViewHolder {
            RelativeLayout rl_group, rl_content;
            TextView tvGroup;

            ImageView portrait;
            TextView userName, source, notifyTime, content, tag;
            TextView tvSupportCount, tvAddOne;
            ImageView btnSupport;
            RelativeLayout rl_support, rl_reply;

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
            ViewHolder holder = null;
            Comment comment = this.lstAllComments.get(i);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_comment_list_item, null);
                holder.rl_group = (RelativeLayout) convertView.findViewById(R.id.id_rl_group);
                holder.rl_content = (RelativeLayout) convertView.findViewById(R.id.id_rl_comment_content);
                holder.tvGroup = (TextView) convertView.findViewById(R.id.id_tv_listview_tag);
                holder.portrait = (ImageView) convertView.findViewById(R.id.id_img_portrait);
                holder.userName = (TextView) convertView.findViewById(R.id.id_username);
                holder.source = (TextView) convertView.findViewById(R.id.id_tv_region);
                holder.notifyTime = (TextView) convertView.findViewById(R.id.id_notifytime);
                holder.content = (TextView) convertView.findViewById(R.id.id_textview_comment_content);
                holder.rl_support = (RelativeLayout) convertView.findViewById(R.id.id_rl_support);//顶
                holder.rl_reply = (RelativeLayout) convertView.findViewById(R.id.id_rl_reply);//回复
                holder.tvSupportCount = (TextView) convertView.findViewById(R.id.id_tv_support_count);
                holder.tvAddOne = (TextView) convertView.findViewById(R.id.id_tv_add_one);
                holder.btnSupport = (ImageView) convertView.findViewById(R.id.id_btn_support);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (comment.getGroup().equals("热门评论") && lstHostComments.contains(comment)) {
                if (lstHostComments.indexOf(comment) == 0) {
                    holder.rl_group.setVisibility(View.VISIBLE);
                    holder.tvGroup.setText(comment.getGroup());
                } else {
                    holder.rl_group.setVisibility(View.GONE);
                }
            } else if (comment.getGroup().equals("最新评论") && lstNewComments.contains(comment)) {
                if (lstNewComments.indexOf(comment) == 0) {
                    holder.rl_group.setVisibility(View.VISIBLE);
                    holder.tvGroup.setText(comment.getGroup());
                } else {
                    holder.rl_group.setVisibility(View.GONE);
                }
            } else {
                holder.rl_group.setVisibility(View.GONE);
            }

            initEvent(comment, holder);
            bindData(comment, holder);

            return convertView;
        }

        private void initEvent(final Comment comment, final ViewHolder holder) {
            //赞
            holder.rl_support.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View view) {
                    int count = comment.getSupportCount();
                    Object supportCount = holder.tvSupportCount.getTag();
                    if (supportCount != null) {
                        int temp = (int) supportCount;
                        if (temp == count + 1) {
                            Toast.makeText(MainApplication.UIContext, "您已顶", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    holder.tvAddOne.setVisibility(View.VISIBLE);
                    holder.tvAddOne.startAnimation(animation);
                    int addCount = count + 1;
                    holder.tvSupportCount.setText("(" + addCount + ")");
                    holder.tvSupportCount.setTag(count + 1);
                    holder.tvSupportCount.setTextColor(Color.RED);
                    holder.btnSupport.setImageDrawable(getResources().getDrawable(R.drawable.icon_support));
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            holder.tvAddOne.setVisibility(View.GONE);
                        }
                    }, 1000);

                    //后台提交赞加1
                    CommonOperator operator = CommonOperator.getInstance();
                    operator.supportOrStep(MainApplication.userInfo.getUserId(), comment.getId(), SupportTypeEnum.NewsComment, 1, new ApiOperationCallback<ReturnInfo<String>>() {
                        @Override
                        public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                            if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                Log.i("supportOrStep", "赞成功");
                            }
                        }
                    });
                }
            });

            //回复
            holder.rl_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MainApplication.isLogin()) {
                        childCommentId = comment.getId();
                        editText.setText("//@" + comment.getUser().getName() + ":" + comment.getContent());
                        showInputView();
                    } else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                    }
                }
            });

            holder.portrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityUtils.startNameCardActivity(getActivity(), comment.getUser().getUserId());
                }
            });
        }

        private void bindData(final Comment comment, ViewHolder holder) {
            ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(comment.getUser().getAvatar(), holder.portrait,
                    ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.user_portrait_height), R.drawable.default_portrait_fang));
            holder.userName.setText(comment.getUser().getName());
            holder.source.setText(comment.getUser().getLocationAddress());
            String notifyTimeStr = comment.getTimeText();
            holder.notifyTime.setText(notifyTimeStr);
            holder.content.setText(comment.getContent());
            holder.tvSupportCount.setText("(" + comment.getSupportCount() + ")");
        }
    }

    private void hideInputView() {
        childCommentId = -1;
        editText.setText("");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        rlInput.setVisibility(View.GONE);
    }

    private void showInputView() {
        //显示输入框
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        rlInput.setVisibility(View.VISIBLE);

        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }
}
