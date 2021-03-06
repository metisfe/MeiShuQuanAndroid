package com.metis.meishuquan.fragment.login;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.model.login.Identity;
import com.metis.meishuquan.model.login.IdentityType;
import com.metis.meishuquan.model.login.UserRole;
import com.metis.meishuquan.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment:选择身份
 * Created by wj on 15/4/5.
 */
public class SelectIdFragment extends Fragment {
    private Button btnBack, btnNext;
    private ImageView imgStudent, imgTeacher, imgHuashi, imgParent, imgOther;
    private TextView tvStudent, tvTeacher, tvHuashi, tvParent, tvOther;
    private GridView gvData;
    private MyGridAdapter adapter;

    private FragmentManager fm;
    private IdTypeEnum idTypeEnum = null;
    private UserRole userRole;
    private Identity identity = null;

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
        Log.i("role_data", "角色信息：" + json);
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
        idTypeEnum = IdTypeEnum.STUDENT;
        if (this.userRole != null) {
            for (IdentityType type : userRole.getData()) {
                if (type.getName().equals("学生")) {
                    adapter = new MyGridAdapter(type.getChildLists());
                }
            }
        }
        this.gvData.setAdapter(adapter);
        setSelectedColorForTextView(tvStudent);
        setSelectedDrawable(imgStudent);
        setUnSelectedDrawable(IdTypeEnum.STUDENT);

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
                if (identity == null) {
                    Toast.makeText(MainApplication.UIContext, "请选择身份", Toast.LENGTH_SHORT).show();
                    return;
                }
                RegisterFragment registerFragment = new RegisterFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("selectedId", identity.getId());//若无子角色，则传父角色ID
                registerFragment.setArguments(bundle);

                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.id_rl_login_main, registerFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        imgStudent.setOnClickListener(new View.OnClickListener() {//学生
            @Override
            public void onClick(View view) {
                idTypeEnum = IdTypeEnum.STUDENT;
                setSelectedColorForTextView(tvStudent);
                setUnselectedColorForTextView(new TextView[]{tvTeacher, tvHuashi, tvParent, tvOther});
                setSelectedDrawable(imgStudent);
                setUnSelectedDrawable(IdTypeEnum.STUDENT);

                for (IdentityType type : userRole.getData()) {
                    if (type.getName().equals("学生")) {
                        identity = new Identity();
                        identity.setId(type.getId());
                        adapter = new MyGridAdapter(type.getChildLists());
                        gvData.setAdapter(adapter);
                    }
                }
            }
        });

        imgTeacher.setOnClickListener(new View.OnClickListener() {//老师
            @Override
            public void onClick(View view) {
                idTypeEnum = IdTypeEnum.TEACHER;
                setSelectedColorForTextView(tvTeacher);
                setUnselectedColorForTextView(new TextView[]{tvStudent, tvHuashi, tvParent, tvOther});
                setSelectedDrawable(imgTeacher);
                setUnSelectedDrawable(IdTypeEnum.TEACHER);
                for (IdentityType type : userRole.getData()) {
                    if (type.getName().equals("老师")) {
                        identity = new Identity();
                        identity.setId(type.getId());
                        adapter = new MyGridAdapter(type.getChildLists());
                        gvData.setAdapter(adapter);
                    }
                }
            }
        });

        imgHuashi.setOnClickListener(new View.OnClickListener() {//画室机构
            @Override
            public void onClick(View view) {
                idTypeEnum = IdTypeEnum.STUDIO;
                setSelectedColorForTextView(tvHuashi);
                setUnselectedColorForTextView(new TextView[]{tvTeacher, tvStudent, tvParent, tvOther});
                setSelectedDrawable(imgHuashi);
                setUnSelectedDrawable(IdTypeEnum.STUDIO);
                for (IdentityType type : userRole.getData()) {
                    if (type.getName().equals("画室/机构")) {
                        identity = new Identity();
                        identity.setId(type.getId());
                        adapter = new MyGridAdapter(type.getChildLists());
                        gvData.setAdapter(adapter);
                    }
                }
            }
        });

        imgParent.setOnClickListener(new View.OnClickListener() {//家长
            @Override
            public void onClick(View view) {
                idTypeEnum = IdTypeEnum.PARENT;
                setSelectedColorForTextView(tvParent);
                setUnselectedColorForTextView(new TextView[]{tvTeacher, tvHuashi, tvStudent, tvOther});
                setSelectedDrawable(imgParent);
                setUnSelectedDrawable(IdTypeEnum.PARENT);
                for (IdentityType type : userRole.getData()) {
                    if (type.getName().equals("家长")) {
                        identity = new Identity();
                        identity.setId(type.getId());
                        adapter = new MyGridAdapter(type.getChildLists());
                        gvData.setAdapter(adapter);
                    }
                }
            }
        });

        imgOther.setOnClickListener(new View.OnClickListener() {//其他
            @Override
            public void onClick(View view) {
                idTypeEnum = IdTypeEnum.OTHER;
                setSelectedColorForTextView(tvOther);
                setUnselectedColorForTextView(new TextView[]{tvTeacher, tvHuashi, tvParent, tvStudent});
                setSelectedDrawable(imgOther);
                setUnSelectedDrawable(IdTypeEnum.OTHER);
                for (IdentityType type : userRole.getData()) {
                    if (type.getName().equals("爱好者")) {
                        identity = new Identity();
                        identity.setId(type.getId());
                        adapter = new MyGridAdapter(type.getChildLists());
                        gvData.setAdapter(adapter);
                    }
                }
            }
        });

        gvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                if (view == null || position < 0) {
                    return;
                }
                for (int i = 0; i < parent.getCount(); i++) {
                    View v = parent.getChildAt(i);
                    MyGridAdapter.ViewHolder holder = (MyGridAdapter.ViewHolder) v.getTag();
                    if (position == i) {//当前选中的Item改变背景颜色
                        setSelectedColorForTextView(holder.textView);
                        identity = adapter.getItem(i);
                    } else {
                        setUnselectedColorForTextView(new TextView[]{holder.textView});
                    }
                }
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

    private void setSelectedDrawable(ImageView img) {
        switch (idTypeEnum) {
            case STUDENT:
                img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_select_id_1));
                break;
            case TEACHER:
                img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_select_id_2));
                break;
            case STUDIO:
                img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_select_id_3));
                break;
            case PARENT:
                img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_select_id_4));
                break;
            case OTHER:
                img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_select_id_5));
                break;
        }
    }

    private void setUnSelectedDrawable(IdTypeEnum selectType) {
        switch (selectType) {
            case STUDENT:
                imgTeacher.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_2));
                imgHuashi.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_3));
                imgParent.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_4));
                imgOther.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_5));
                break;
            case TEACHER:
                imgStudent.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_1));
                imgHuashi.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_3));
                imgParent.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_4));
                imgOther.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_5));
                break;
            case STUDIO:
                imgStudent.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_1));
                imgTeacher.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_2));
                imgParent.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_4));
                imgOther.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_5));
                break;
            case PARENT:
                imgStudent.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_1));
                imgTeacher.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_2));
                imgHuashi.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_3));
                imgOther.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_5));
                break;
            case OTHER:
                imgStudent.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_1));
                imgTeacher.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_2));
                imgHuashi.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_3));
                imgParent.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_unselect_id_4));
                break;
        }

    }

    class MyGridAdapter extends BaseAdapter {
        private List<Identity> identity;
        private ViewHolder holder;

        public void setIdentity(List<Identity> identity) {
            this.identity = identity;
        }

        MyGridAdapter(List<Identity> identity) {
            if (identity == null) {
                identity = new ArrayList<Identity>();
            }
            this.identity = identity;
        }

        class ViewHolder {
            TextView textView;
        }

        @Override
        public int getCount() {
            return identity.size();
        }

        @Override
        public Identity getItem(int i) {

            return identity.get(i);
        }

        @Override
        public long getItemId(int i) {
            return identity.get(i).getId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.assess_user_role_gridview_item, null, false);
                holder = new ViewHolder();
                holder.textView = (TextView) view.findViewById(R.id.id_user_role_textview);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            Identity id = identity.get(i);
            holder.textView.setText(id.getName());
            return view;
        }
    }

}
