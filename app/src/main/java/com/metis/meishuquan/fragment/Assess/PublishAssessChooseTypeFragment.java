package com.metis.meishuquan.fragment.assess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.assess.Channel;
import com.metis.meishuquan.model.assess.ChannelAndGradeData;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment:发布Assess选择类别
 * <p/>
 * Created by wj on 15/4/2.
 */
public class PublishAssessChooseTypeFragment extends Fragment {
    private FragmentManager fm;
    private Button btnBack;
    private GridView gridView;
    private AssessOperator assessOperator;
    private List<Channel> lstChannel = new ArrayList<Channel>();
    private MyGridAdapter gridAdapter;
    private Channel selectedChannel;
    private OnSeletedChannelListener onSeletedChannelListener;

    public void setOnSeletedChannelListener(OnSeletedChannelListener onSeletedChannelListener) {
        this.onSeletedChannelListener = onSeletedChannelListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getChannelDataFromCache();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assess_publish_choose_channel, null);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        fm = getActivity().getSupportFragmentManager();
        this.btnBack = (Button) rootView.findViewById(R.id.id_btn_back);
        this.gridView = (GridView) rootView.findViewById(R.id.id_gridview_assess_choose_channel);

        this.gridAdapter = new MyGridAdapter(lstChannel);
        this.gridView.setAdapter(gridAdapter);
    }

    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(PublishAssessChooseTypeFragment.this);
                ft.commit();
            }
        });

        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedChannel = gridAdapter.getItem(i);
                onSeletedChannelListener.setChannel(selectedChannel);
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(PublishAssessChooseTypeFragment.this);
                ft.commit();
            }
        });
    }

    private void getChannelDataFromCache() {
        SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
        String json = spu.getStringByKey(SharedPreferencesUtil.ASSESS_LIST_FILTER_DATA);
        Gson gson = new Gson();
        if (!json.equals("")) {
            ChannelAndGradeData data = gson.fromJson(json, new TypeToken<ChannelAndGradeData>() {
            }.getType());
            this.lstChannel = data.getData().getChannelList();
        }
    }

    class MyGridAdapter extends BaseAdapter {
        private List<Channel> channels;
        private ViewHolder holder;

        MyGridAdapter(List<Channel> channels) {
            if (channels == null) {
                channels = new ArrayList<Channel>();
            }
            this.channels = channels;
        }

        class ViewHolder {
            ImageView imgView;
            TextView textView;
        }

        @Override
        public int getCount() {
            return channels.size();
        }

        @Override
        public Channel getItem(int i) {

            return channels.get(i);
        }

        @Override
        public long getItemId(int i) {
            return channels.get(i).getChannelId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.assess_channel_gridview_item, null, false);
                holder = new ViewHolder();
                holder.imgView = (ImageView) view.findViewById(R.id.id_assess_channel_img);
                holder.textView = (TextView) view.findViewById(R.id.id_assess_channel_textview);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            Channel channel = channels.get(i);
            ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(channels.get(i).getThumbnails(), holder.imgView, ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.user_portrait_height)));
            holder.textView.setText(channel.getChannelName());
            return view;
        }
    }

    interface OnSeletedChannelListener {
        void setChannel(Channel channel);
    }
}
