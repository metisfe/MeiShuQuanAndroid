package com.metis.meishuquan.activity.circle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.circle.CCircleDefaultChatRoomModel;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class ChatRoomListActivity extends BaseActivity {

    public static final String TITLE = "志愿答疑群组";

    private ListView listView;
    private ChatRoomListAdapter adapter;
    private List<CCircleDefaultChatRoomModel> lstDefaultChatRomm = new ArrayList<CCircleDefaultChatRoomModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_list);

        String jsonData = SharedPreferencesUtil.getInstanse(ChatRoomListActivity.this).getStringByKey(SharedPreferencesUtil.DEFAULT_CHATROOM);
        if (jsonData.equals("")) {
            //获取数据
            getData();
        } else {
            //获得序列化对象
            ReturnInfo<List<CCircleDefaultChatRoomModel>> data = new Gson().fromJson(jsonData, new TypeToken<ReturnInfo<List<CCircleDefaultChatRoomModel>>>() {
            }.getType());
            this.lstDefaultChatRomm.addAll(data.getData());
        }

        //初始化控件及绑定数据
        this.listView = (ListView) this.findViewById(R.id.id_lv_chat_room);
        this.adapter = new ChatRoomListAdapter(lstDefaultChatRomm);
        this.listView.setAdapter(adapter);

        //列表点击事件
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 启动聊天室
                Intent intent = new Intent(ChatRoomListActivity.this, ChatActivity.class);
                intent.putExtra("title", lstDefaultChatRomm.get(i).getDiscussionName());
                intent.putExtra("targetId", lstDefaultChatRomm.get(i).getDiscussionId());
                intent.putExtra("type", RongIMClient.ConversationType.CHATROOM.toString());
                startActivity(intent);

//                RongIM.getInstance().startChatroom(ChatRoomListActivity.this, lstDefaultChatRomm.get(i).getDiscussionId(), lstDefaultChatRomm.get(i).getDiscussionName());
            }
        });
    }

    @Override
    public String getTitleCenter() {
        return TITLE;
    }

    private void getData() {
        CircleOperator.getInstance().getDefaultChatRomm(new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if (result != null && result.isSuccess()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);

                    //缓存至本地
                    SharedPreferencesUtil.getInstanse(ChatRoomListActivity.this).update(SharedPreferencesUtil.DEFAULT_CHATROOM, json);

                    //获得序列化对象
                    ReturnInfo<List<CCircleDefaultChatRoomModel>> data = gson.fromJson(json, new TypeToken<ReturnInfo<List<CCircleDefaultChatRoomModel>>>() {
                    }.getType());

                    if (data != null) {
                        lstDefaultChatRomm.addAll(data.getData());
                        adapter.setLstDefaultChatRomm(lstDefaultChatRomm);
                        adapter.notifyDataSetChanged();
                    }
                } else if (result != null && !result.isSuccess()) {
                    if (e != null)
                        Log.e("defaultChatRoom", e.toString());
                    else if (result.getMessage() != null)
                        Log.i("defaultChatRoom", result.getMessage());
                }
            }
        });
    }

    class ChatRoomListAdapter extends BaseAdapter {
        private List<CCircleDefaultChatRoomModel> lstDefaultChatRomm;

        public ChatRoomListAdapter(List<CCircleDefaultChatRoomModel> lstDefaultChatRomm) {
            this.lstDefaultChatRomm = lstDefaultChatRomm;
        }

        public void setLstDefaultChatRomm(List<CCircleDefaultChatRoomModel> lstDefaultChatRomm) {
            this.lstDefaultChatRomm = lstDefaultChatRomm;
        }

        @Override
        public int getCount() {
            return lstDefaultChatRomm.size();
        }

        @Override
        public CCircleDefaultChatRoomModel getItem(int i) {
            return lstDefaultChatRomm.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHoler holer = null;
            CCircleDefaultChatRoomModel chatRoomModel = lstDefaultChatRomm.get(position);
            if (convertView == null) {
                holer = new ViewHoler();
                convertView = LayoutInflater.from(ChatRoomListActivity.this).inflate(R.layout.activity_chat_room_list_item, null, false);
                holer.imgChatRoom = (ImageView) convertView.findViewById(R.id.id_img_chat_room);
                holer.tvChatRoomName = (TextView) convertView.findViewById(R.id.id_tv_chat_room_name);
                convertView.setTag(holer);
            } else {
                holer = (ViewHoler) convertView.getTag();
            }

            //绑定数据
            if (!chatRoomModel.getDiscussionImage().isEmpty()) {
                ImageLoaderUtils.getImageLoader(ChatRoomListActivity.this).displayImage(chatRoomModel.getDiscussionImage(), holer.imgChatRoom);
            }
            holer.tvChatRoomName.setText(chatRoomModel.getDiscussionName());

            return convertView;
        }

        class ViewHoler {
            ImageView imgChatRoom;
            TextView tvChatRoomName;
        }

    }
}
