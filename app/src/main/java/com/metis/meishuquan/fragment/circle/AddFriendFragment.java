package com.metis.meishuquan.fragment.circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.QrScanActivity;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.circle.ContactListItemView;

/**
 * Created by wudi on 4/12/2015.
 */
public class AddFriendFragment extends Fragment {
    public static final int QR_REQUEST_CODE = 101;

    private ViewGroup rootView;
    private CircleTitleBar titleBar;
    private View searchView;
    private ContactListItemView item1, item2;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QR_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            String userPhone = (String) data.getExtras().get(QrScanActivity.KEY_RESULT);
            Log.i("QR_RESULT", userPhone);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_addfriendfragment, container, false);

        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_circle_addfriendfragment_titlebar);

        titleBar.setText("增加朋友");
        titleBar.setLeftButton("", R.drawable.btn_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.searchView = rootView.findViewById(R.id.fragment_circle_addfriendfragment_search);
        this.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnlineFriendSearchFragment onlineFriendSearchFragment = new OnlineFriendSearchFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, onlineFriendSearchFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        this.item1 = (ContactListItemView) rootView.findViewById(R.id.fragment_circle_addfriendfragment_scan);
        this.item2 = (ContactListItemView) rootView.findViewById(R.id.fragment_circle_addfriendfragment_contactbook);

        this.item1.setNormalMode("fakeid", "扫一扫", "扫描二维码名片", null, R.drawable.fragment_circle_addfriendfragment_scan, true);
        this.item2.setNormalMode("fakeid", "通讯录好友", "增加或邀请通讯录中的好友", null, R.drawable.fragment_circle_addfriendfragment_book, true);

        //扫一扫
        this.item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), QrScanActivity.class), QR_REQUEST_CODE);
//                ScanQRCodeFragment scanQRCodeFragment = new ScanQRCodeFragment();
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
//                ft.add(R.id.content_container, scanQRCodeFragment);
//                ft.addToBackStack(null);
//                ft.commit();
            }
        });

        //通讯录好友
        this.item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendMatchFragment friendMatchFragment = new FriendMatchFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, friendMatchFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return rootView;
    }

    private void finish() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
