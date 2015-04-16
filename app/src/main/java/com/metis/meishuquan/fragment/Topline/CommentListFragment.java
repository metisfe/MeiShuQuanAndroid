package com.metis.meishuquan.fragment.Topline;

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
import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.BlockTypeEnum;
import com.metis.meishuquan.model.enums.LoginStateEnum;
import com.metis.meishuquan.model.enums.PrivateResultEnum;
import com.metis.meishuquan.model.enums.SupportStepTypeEnum;
import com.metis.meishuquan.model.topline.AllComments;
import com.metis.meishuquan.model.topline.Comment;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.metis.meishuquan.view.shared.DragListView;
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
    private Button btnBack;
    private RelativeLayout rl_WriteComment, rl_Commentlist, rl_Share, rl_Private;
    private ImageView imgPrivate;
    private EditText editText;//评论或回复输入框
    private RelativeLayout rlSend, rlInput;//发送


    private int userId = MainApplication.userInfo.getUserId();
    private int newsId = 0;
    private int totalCommentCount = 0;
    private int childCommentId = -1;
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
        rl_Commentlist = (RelativeLayout) rootView.findViewById(R.id.id_rl_commentlist);//评论列表
        rl_WriteComment = (RelativeLayout) rootView.findViewById(R.id.id_rl_writecomment);//写评论
        rl_Share = (RelativeLayout) rootView.findViewById(R.id.id_rl_share);//分享
        rl_Private = (RelativeLayout) rootView.findViewById(R.id.id_rl_private);//收藏
        tvCommentCount = (TextView) rootView.findViewById(R.id.id_tv_topline_info_comment_count);//评论数
        imgPrivate = (ImageView) rootView.findViewById(R.id.id_img_favorite);//收藏图标

        //设置评论数
        if (totalCommentCount > 0) {
            this.tvCommentCount.setVisibility(View.VISIBLE);
            this.tvCommentCount.setText(String.valueOf(totalCommentCount));
        } else {
            this.tvCommentCount.setVisibility(View.GONE);
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

            }
        });

        this.rl_Private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//收藏
                if (MainApplication.userInfo.getAppLoginState() == LoginStateEnum.YES) {
                    if (!isPrivate) {
                        //收藏
                        TopLineOperator.getInstance().newsPrivate(userId, newsId, 0, PrivateResultEnum.PRIVATE, new ApiOperationCallback<ReturnInfo<String>>() {
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
                        TopLineOperator.getInstance().newsPrivate(userId, newsId, 0, PrivateResultEnum.CANCEL, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Toast.makeText(MainApplication.UIContext, "取消收藏", Toast.LENGTH_SHORT).show();
                                    isPrivate = false;
                                    imgPrivate.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_topline_unprivate));
                                }
                            }
                        });
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
                new SharePopupWindow(MainApplication.UIContext, rootView);
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

        rlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (childCommentId == -1) {
                    String content = editText.getText().toString();
                    if (!content.isEmpty()) {
                        CommonOperator.getInstance().publishComment(0, newsId, content, 0, BlockTypeEnum.TOPLINE, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
                                    hideInputView();
                                }
                            }
                        });
                    }
                } else {
                    String content = editText.getText().toString();
                    CommonOperator.getInstance().publishComment(0, newsId, content, childCommentId, BlockTypeEnum.TOPLINE, new ApiOperationCallback<ReturnInfo<String>>() {
                        @Override
                        public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                            if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                getData(newsId, DragListView.REFRESH);
                                hideInputView();
                            }
                        }
                    });
                }
            }
        });
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
            TextView tvSupportCount, tvAddOne;
            Button btnSupport;
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
            View view = convertView;
            if (convertView == null) {
                holder = new ViewHolder();
                Comment comment = this.lstAllComments.get(i);
                if (comment.getGroup().equals("热门评论") || comment.getGroup().equals(("最新评论"))) {
                    view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_topline_comment_list_item_tag, null);
                    holder.tag = (TextView) view.findViewById(R.id.id_tv_listview_tag);
                    holder.tag.setText(comment.getGroup());
                } else {
                    view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_comment_list_item, null);
                    initView(view, holder);
                    initEvent(comment, holder);
                    bindData(comment, holder);
                }
                holder = (ViewHolder) view.getTag();
            }
            return view;
        }

        private void initView(View view, ViewHolder holder) {
            //holder.portrait= (SmartImageView) view.findViewById(R.id.id_img_portrait);
            holder.userName = (TextView) view.findViewById(R.id.id_username);
            //holder.source= (TextView) view.findViewById(R.id.id_username);
            holder.notifyTime = (TextView) view.findViewById(R.id.id_notifytime);
            holder.content = (TextView) view.findViewById(R.id.id_textview_comment_content);
            holder.rl_support = (RelativeLayout) view.findViewById(R.id.id_rl_support);//顶
            holder.rl_reply = (RelativeLayout) view.findViewById(R.id.id_rl_reply);//回复
            holder.tvSupportCount = (TextView) view.findViewById(R.id.id_tv_support_count);
            holder.tvAddOne = (TextView) view.findViewById(R.id.id_tv_add_one);
            holder.btnSupport = (Button) view.findViewById(R.id.id_btn_support);
        }

        private void initEvent(final Comment comment, final ViewHolder holder) {
            //赞
            holder.rl_support.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = comment.getSupportCount();
                    Object supportCount = holder.tvSupportCount.getTag();
                    if (supportCount != null) {
                        int temp = (int) supportCount;
                        if (temp == count + 1) {
                            Toast.makeText(MainApplication.UIContext, "已顶", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    holder.tvAddOne.setVisibility(View.VISIBLE);
                    holder.tvAddOne.startAnimation(animation);
                    int addCount = count + 1;
                    holder.tvSupportCount.setText("(" + addCount + ")");
                    holder.tvSupportCount.setTag(count + 1);
                    holder.tvSupportCount.setTextColor(Color.RED);
                    holder.btnSupport.setBackground(getResources().getDrawable(R.drawable.icon_support));
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            holder.tvAddOne.setVisibility(View.GONE);
                        }
                    }, 1000);

                    //后台提交赞加1
                    if (userId == -1) {
                        return;
                    }
                    CommonOperator operator = CommonOperator.getInstance();
                    operator.supportOrStep(userId, comment.getId(), SupportStepTypeEnum.NewsComment, 1, new ApiOperationCallback<ReturnInfo<String>>() {
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
                    if (MainApplication.userInfo.getAppLoginState() == LoginStateEnum.YES) {
                        childCommentId = comment.getId();
                        showInputView();
                    } else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                    }
                }
            });
        }

        private void bindData(Comment comment, ViewHolder holder) {
            holder.userName.setText(comment.getUser().getName());
            //holder.source.setText(comment.getCommentDateTime());
            String notifyTimeStr = comment.getCommentDateTime();
            holder.notifyTime.setText(notifyTimeStr);
            holder.content.setText(comment.getContent());
            holder.tvSupportCount.setText("(" + comment.getSupportCount() + ")");
        }
    }

    private void hideInputView() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        rlInput.setVisibility(View.GONE);
    }

    private void showInputView() {
        //显示输入框
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        rlInput.setVisibility(View.VISIBLE);
        editText.setText("");
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }
}
