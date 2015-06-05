package com.metis.meishuquan.fragment.circle;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CCircleRelateMeDetail;
import com.metis.meishuquan.model.circle.CRelatedCircleModel;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.circle.moment.comment.EmotionTextView;
import com.metis.meishuquan.view.shared.DragListView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论我的
 * <p/>
 * Created by wangjin on 15/6/5.
 */
public class CircleCommentMeFragment extends Fragment {

    private static final String TITLE = "评论我的";

    private Button btnBack;
    private TextView tvTitle;
    private DragListView listView;

    private List<CCircleRelateMeDetail> list = new ArrayList<CCircleRelateMeDetail>();
    private CommentMeAdapter adapter;
    private FragmentManager fm;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_circle_comment_me_list, null, false);
        initView(rootView);
        initEvent();
        getData(0, DragListView.REFRESH);
        return rootView;
    }

    private void initView(View rootView) {
        this.btnBack = (Button) rootView.findViewById(R.id.id_btn_back);
        this.tvTitle = (TextView) rootView.findViewById(R.id.id_tv_title);
        this.tvTitle.setText(TITLE);
        this.listView = (DragListView) rootView.findViewById(R.id.fragment_circle_comment_me_list);

        this.fm = getActivity().getSupportFragmentManager();
        adapter = new CommentMeAdapter();
        listView.setAdapter(adapter);
    }

    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.popBackStack();
            }
        });

        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(0, DragListView.REFRESH);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {
            @Override
            public void onLoad() {
                int momentLastId = list != null && list.size() > 0 ? list.get(list.size() - 1).id : 0;
                getData(momentLastId, DragListView.LOAD);
            }
        });

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void getData(int lastId, final int mode) {
        CircleOperator.getInstance().getCommentMeData(lastId, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if (result != null && result.isSuccess()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    ReturnInfo<CRelatedCircleModel> cRelatedCircleModel = gson.fromJson(json, new TypeToken<ReturnInfo<CRelatedCircleModel>>() {
                    }.getType());

                    List<CCircleRelateMeDetail> result_list = cRelatedCircleModel.getData().commentList;

                    switch (mode) {
                        case DragListView.REFRESH:
                            listView.onRefreshComplete();
                            list.clear();
                            list.addAll(result_list);
                            break;
                        case DragListView.LOAD:
                            listView.onLoadComplete();
                            list.addAll(result_list);
                            break;
                    }
                    listView.setResultSize(result_list.size());
                    adapter.notifyDataSetChanged();
                } else if (result != null && !result.isSuccess()) {
                    Log.i("comment_me", new Gson().toJson(result));
                }
            }
        });
    }

    class CommentMeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CCircleRelateMeDetail getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            CCircleRelateMeDetail relateMeDetail = list.get(i);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_circle_comment_me_list_item, null, false);
                holder.imgHeaderView = (ImageView) convertView.findViewById(R.id.id_img_portrait);
                holder.tvName = (TextView) convertView.findViewById(R.id.id_username);
                holder.tvRole = (TextView) convertView.findViewById(R.id.id_tv_role);
                holder.tvTime = (TextView) convertView.findViewById(R.id.id_createtime);
                holder.tvDevice = (TextView) convertView.findViewById(R.id.tv_device);
                holder.tvCommentContent = (EmotionTextView) convertView.findViewById(R.id.id_tv_content);

                holder.ll_weibo_content = (LinearLayout) convertView.findViewById(R.id.id_ll_not_circle);
                holder.imgWeiboPic = (ImageView) convertView.findViewById(R.id.id_img_for_not_circle);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.id_tv_title);
                holder.tvContent = (TextView) convertView.findViewById(R.id.id_tv_info);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //头像
            ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(relateMeDetail.lastComment.user.avatar, holder.imgHeaderView);
            holder.tvName.setText(relateMeDetail.lastComment.user.name);
            holder.tvTime.setText(relateMeDetail.getTimeText());
            holder.tvRole.setText("");//角色
            holder.tvDevice.setText("");//来源

            //最后一条评论内容
            holder.tvCommentContent.append("回复");
            SpannableString at = new SpannableString("@" + relateMeDetail.user.name);
            ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
            at.setSpan(span, 0, at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainApplication.UIContext, "进入个人主页", Toast.LENGTH_SHORT).show();
                }
            };
            at.setSpan(clickableSpan, 0, at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvCommentContent.append(at);
            holder.tvCommentContent.append(":" + relateMeDetail.lastComment.content);

            //原微博
            if (relateMeDetail.relayImgUrl != null) {
                ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(relateMeDetail.relayImgUrl, holder.imgWeiboPic);
            } else {
                holder.imgWeiboPic.setBackgroundResource(R.drawable.default_portrait_fang);
            }
            holder.tvTitle.setText("@" + relateMeDetail.user.name);
            holder.tvContent.setText(relateMeDetail.content);

            return convertView;
        }

        class ViewHolder {
            private ImageView imgHeaderView;
            private TextView tvName;
            private TextView tvRole;
            private TextView tvTime;
            private TextView tvDevice;
            private EmotionTextView tvCommentContent;

            /*原微博*/
            private LinearLayout ll_weibo_content;
            private ImageView imgWeiboPic;
            private TextView tvTitle;
            private TextView tvContent;
        }
    }

}
