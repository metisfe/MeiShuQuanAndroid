package com.metis.meishuquan.fragment.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.enums.IdType;
import com.metis.meishuquan.model.login.IdentityType;
import com.metis.meishuquan.model.login.UserRole;
import com.metis.meishuquan.util.SharedPreferencesUtil;

/**
 * Fragment:选择身份
 * Created by wj on 15/4/5.
 */
public class SelectIdFragment extends Fragment {
    private Button btnBack, btnNext;
    private ImageView imgStudent, imgTeacher, imgHuashi, imgParent, imgOther;
    private TextView tvStudent, tvTeacher, tvHuashi, tvParent, tvOther;
    private GridView gvData;

    private FragmentManager fm;
    private IdType idType = null;
    private UserRole userRole;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //从缓存中读取身份信息
        getData();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_select_id, null, false);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void getData() {
        SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
        String json = spu.getStringByKey(SharedPreferencesUtil.USER_ROLE);
        Gson gson = new Gson();
        UserRole userRole = gson.fromJson(json, new TypeToken<UserRole>() {
        }.getType());
        if (userRole != null) {
            this.userRole = userRole;
        }
    }

    private void initView(ViewGroup rootView) {
        btnBack = (Button) rootView.findViewById(R.id.id_btn_select_id_back);
        btnNext = (Button) rootView.findViewById(R.id.id_btn_select_id_next);
        imgStudent = (ImageView) rootView.findViewById(R.id.id_img_select_id_student);
        imgTeacher = (ImageView) rootView.findViewById(R.id.id_img_select_id_teacher);
        imgHuashi = (ImageView) rootView.findViewById(R.id.id_img_select_id_huashi);
        imgParent = (ImageView) rootView.findViewById(R.id.id_img_select_id_parent);
        imgOther = (ImageView) rootView.findViewById(R.id.id_img_select_id_other);
        tvStudent = (TextView) rootView.findViewById(R.id.id_tv_select_id_student);
        tvTeacher = (TextView) rootView.findViewById(R.id.id_tv_select_id_teacher);
        tvHuashi = (TextView) rootView.findViewById(R.id.id_tv_select_id_huashi);
        tvParent = (TextView) rootView.findViewById(R.id.id_tv_select_id_parent);
        tvOther = (TextView) rootView.findViewById(R.id.id_tv_select_id_other);
        gvData = (GridView) rootView.findViewById(R.id.id_select_id_gridview);

        fm = getActivity().getSupportFragmentManager();

    }

    private void initEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {//返回
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(SelectIdFragment.this);
                //TODO:加载动画
                ft.commit();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {//下一步
            @Override
            public void onClick(View view) {
                if (idType == null) {
                    Toast.makeText(MainApplication.UIContext, "请选择身份", Toast.LENGTH_SHORT).show();
                    return;
                }
                RegisterFragment registerFragment = new RegisterFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("idType", idType.getVal());
                registerFragment.setArguments(bundle);

                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.content_container, registerFragment);
                //TODO:动画
                ft.commit();
            }
        });

        imgStudent.setOnClickListener(new View.OnClickListener() {//学生
            @Override
            public void onClick(View view) {
                idType = IdType.Student;
                setSelectedColorForTextView(tvStudent);
                setUnselectedColorForTextView(new TextView[]{tvTeacher, tvHuashi, tvParent, tvOther});
            }
        });

        imgTeacher.setOnClickListener(new View.OnClickListener() {//老师
            @Override
            public void onClick(View view) {
                idType = IdType.Teacher;
                setSelectedColorForTextView(tvTeacher);
                setUnselectedColorForTextView(new TextView[]{tvStudent, tvHuashi, tvParent, tvOther});
            }
        });

        imgHuashi.setOnClickListener(new View.OnClickListener() {//画室机构
            @Override
            public void onClick(View view) {
                idType = IdType.HuaShi;
                setSelectedColorForTextView(tvHuashi);
                setUnselectedColorForTextView(new TextView[]{tvTeacher, tvStudent, tvParent, tvOther});
            }
        });

        imgParent.setOnClickListener(new View.OnClickListener() {//家长
            @Override
            public void onClick(View view) {
                idType = IdType.Parent;
                setSelectedColorForTextView(tvParent);
                setUnselectedColorForTextView(new TextView[]{tvTeacher, tvHuashi, tvStudent, tvOther});
            }
        });

        imgOther.setOnClickListener(new View.OnClickListener() {//其他
            @Override
            public void onClick(View view) {
                idType = IdType.Other;
                setSelectedColorForTextView(tvOther);
                setUnselectedColorForTextView(new TextView[]{tvTeacher, tvHuashi, tvParent, tvStudent});
            }
        });
    }

    private void setSelectedColorForTextView(TextView tv) {
        tv.setTextColor(Color.rgb(255, 83, 99));
    }

    private void setUnselectedColorForTextView(TextView[] tvs) {
        for (TextView textView : tvs) {
            textView.setTextColor(Color.rgb(160, 160, 160));
        }
    }

}
