package com.metis.meishuquan.fragment.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.circle.ChatActivity;
import com.metis.meishuquan.view.circle.CircleChatListItemView;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/4/2015.
 */
public class ChatListFragment extends CircleBaseFragment {

    private ViewGroup rootView;
    private ListView listView;
    private ChatListAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //TODO: add listener
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //TODO: add listener
    }

    @Override
    public void timeToSetTitleBar() {
        if (getTitleBar() != null) {
            getTitleBar().setText("this is the chat list page");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("circle", "chat list onresume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_chatlistfragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.fragment_circle_chatlistfragment_listview);
        adapter = new ChatListAdapter();

        //TODO: fake data
        RongIMClient.Conversation conversation = new RongIMClient.Conversation();
        conversation.setTargetId("diwulechao2");
        conversation.setConversationTitle("diwulechao2");
        conversation.setSentTime(System.currentTimeMillis());
        conversation.setConversationType(RongIMClient.ConversationType.PRIVATE);
        adapter.data.add(conversation);

        conversation = new RongIMClient.Conversation();
        conversation.setTargetId("06eff743-5b49-492b-8128-edec65dfe9cb");
        conversation.setConversationTitle("diwugroup");
        conversation.setReceivedTime(System.currentTimeMillis());
        conversation.setConversationType(RongIMClient.ConversationType.DISCUSSION);
        adapter.data.add(conversation);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RongIMClient.Conversation conversation = adapter.data.get(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("title", conversation.getConversationTitle());
                intent.putExtra("targetId", conversation.getTargetId());
                intent.putExtra("type", conversation.getConversationType().toString());
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    class ChatListAdapter extends BaseAdapter {
        public List<RongIMClient.Conversation> data = new ArrayList<RongIMClient.Conversation>();

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new CircleChatListItemView(getActivity(), data.get(position));
            } else {
                ((CircleChatListItemView) convertView).setData(data.get(position));
            }
            return convertView;
        }
    }
}
