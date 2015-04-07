package com.metis.meishuquan.fragment.circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.ChatActivity;
import com.metis.meishuquan.model.circle.Contact;
import com.metis.meishuquan.view.circle.CircleChatListItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wudi on 4/4/2015.
 */
public class ChatListFragment extends CircleBaseFragment {

    private ViewGroup rootView;
    private ListView listView;
    private ChatListAdapter adapter;

    @Override
    public void timeToSetTitleBar() {
        getTitleBar().setText("this is the chat list page");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_chatlistfragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.fragment_circle_chatlistfragment_listview);
        adapter = new ChatListAdapter();
        adapter.data.add(new Contact());
        adapter.data.add(new Contact());
        adapter.data.add(new Contact());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("title","diwulechao2");
                intent.putExtra("targetId","diwulechao2");
                intent.putExtra("type","private");
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    class ChatListAdapter extends BaseAdapter {
        public List<Contact> data = new ArrayList<Contact>();

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
                ((CircleChatListItemView)convertView).setData(data.get(position));
            }
            return convertView;
        }
    }
}
