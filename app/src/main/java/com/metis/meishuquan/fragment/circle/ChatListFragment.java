package com.metis.meishuquan.fragment.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.circle.ChatActivity;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.ViewUtils;
import com.metis.meishuquan.view.circle.CircleChatListItemView;
import com.metis.meishuquan.view.circle.PopupAddWindow;

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
        ChatManager.SetOnReceivedListener(new ChatManager.OnReceivedListener() {
            @Override
            public void onReceive(RongIMClient.Message message) {
                refreshList();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ChatManager.RemoveOnReceivedListener();
    }

    @Override
    public void timeToSetTitleBar() {
        getTitleBar().setText("消息");
        getTitleBar().setRightButton("Add", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupAddWindow addWindow = new PopupAddWindow(getActivity(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).removeAllAttachedView();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //select friend
                        StartFriendPickFragment startFriendPickFragment = new StartFriendPickFragment();
                        Bundle args = new Bundle();
                        args.putString("fromtype", "privateconfig");
                        args.putString("title", "消息");
                        startFriendPickFragment.setArguments(args);

                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                        ft.add(R.id.content_container, startFriendPickFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddFriendFragment addFriendFragment = new AddFriendFragment();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                        ft.add(R.id.content_container, addFriendFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScanQRCodeFragment scanQRCodeFragment = new ScanQRCodeFragment();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                        ft.add(R.id.content_container, scanQRCodeFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });

                ((MainActivity) getActivity()).addAttachView(addWindow);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        if (this.listView != null && adapter != null && MainApplication.rongClient != null) {
            List<RongIMClient.Conversation> clist = MainApplication.rongClient.getConversationList();
            if (clist != null) adapter.data = clist;
            ViewUtils.delayExecute(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            }, 50);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_chatlistfragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.fragment_circle_chatlistfragment_listview);
        adapter = new ChatListAdapter();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RongIMClient.Conversation conversation = adapter.data.get(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("title", ChatManager.getConversationTitle(conversation));
                intent.putExtra("targetId", conversation.getTargetId());
                intent.putExtra("type", conversation.getConversationType().toString());
                getActivity().startActivity(intent);
            }
        });

        if (MainApplication.rongClient != null) {
            adapter.data = MainApplication.rongClient.getConversationList();
        } else {
            //TODO: tell user to login
        }

        listView.setAdapter(adapter);
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
