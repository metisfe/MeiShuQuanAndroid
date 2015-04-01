package com.metis.meishuquan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.assess.AllCity;
import com.metis.meishuquan.model.assess.City;
import com.metis.meishuquan.model.assess.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/3/31.
 */
public class ExpandeAdapter extends BaseExpandableListAdapter {

    private static final String HOT="热门";
    private static final String PROVINCE="省份";
    private Context context;
    private LayoutInflater mInflater = null;
    private AllCity mData = null;


    public ExpandeAdapter(Context context, AllCity mData) {
        this.context = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mData = mData;
        changeData();
    }

    private void changeData(){
        if (mData.getData().size()>0){
            List<Province> all=new ArrayList<>();
            List<Province> lstHotCity=new ArrayList<>();
            List<Province> lstProvince=new ArrayList<>();
            List<Province> temp=mData.getData();

            Province p=new Province();
            p.setGroupName(HOT);
            lstHotCity.add(p);
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).isHotCity()){
                    lstHotCity.add(temp.get(i));
                }
            }
            Province p2=new Province();
            p2.setGroupName(PROVINCE);
            lstProvince.add(p2);
            for (int i = 0; i < temp.size(); i++) {
                if (!temp.get(i).isHotCity()){
                    lstProvince.add(temp.get(i));
                }
            }
            all.addAll(lstHotCity);
            all.addAll(lstProvince);

            this.mData.getData().clear();
            this.mData.setData(all);
        }
    }

    @Override
    public int getGroupCount() {
        if (mData.getData().size() > 0) {
            return mData.getData().size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mData.getData() != null && mData.getData().size() > 0) {
            int count=mData.getData().get(groupPosition).getCityList().size();
            return count;
        }
        return 0;
    }

    @Override
    public Province getGroup(int groupPosition) {
        if (mData.getData() != null && mData.getData().size() > 0) {
            return mData.getData().get(groupPosition);
        }
        return null;
    }

    @Override
    public City getChild(int groupPosition, int childPosition) {
        if (mData.getData() != null && mData.getData().size() > 0 && mData.getData().get(groupPosition).getCityList().size() > 0) {
            return mData.getData().get(groupPosition).getCityList().get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        long id=mData.getData().get(groupPosition).getProvinceId();
        return id;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        long id=mData.getData().get(groupPosition).getCityList().get(childPosition).getCodeid();
        return id;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        GroupViewHolder holder = new GroupViewHolder();
        if (mData.getData().get(i).getGroupName().equals(HOT)||mData.getData().get(i).getGroupName().equals(PROVINCE)){
            convertView = mInflater.inflate(R.layout.fragment_topline_comment_list_item_tag, null);
            convertView.setBackgroundColor(Color.rgb(230,232,237));
            holder.mTag= (TextView) convertView.findViewById(R.id.id_tv_listview_tag);
            holder.mTag.setTextColor(Color.rgb(255,83,99));
            holder.mTag.setText(mData.getData().get(i).getGroupName());
        }else{
            convertView = mInflater.inflate(R.layout.fragment_assess_city_list_group_item, null);
            holder.mGroupName = (TextView) convertView.findViewById(R.id.id_tv_city_group_name);
            holder.mGroupName.setTextColor(Color.rgb(127,126,127));
            holder.mIsExpand = (TextView) convertView.findViewById(R.id.id_tv_city_group_tag);

            holder.mGroupName.setText(mData.getData().get(i).getName());
            holder.mIsExpand.setText(">");
        }
        return convertView;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_assess_city_list_child_item, null);
        }
        ChildViewHolder holder = new ChildViewHolder();
        holder.mChildName = (TextView) convertView.findViewById(R.id.id_assess_city_child_item);
        holder.mChildName.setText(((City) getChild(i, i2)).getCityName());
        return convertView;
    }

    class GroupViewHolder {
        TextView mGroupName, mIsExpand,mTag;
    }

    class ChildViewHolder {
        TextView mChildName;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}
