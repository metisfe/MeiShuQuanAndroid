package com.metis.meishuquan.adapter.topline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.topline.RelatedRead;

import java.util.ArrayList;
import java.util.List;

/**
 * 相关阅读列表适配器
 * <p/>
 * Created by wangjin on 15/5/2.
 */
public class CommonAdapter extends BaseAdapter {
    private Context context;
    private List<RelatedRead> lstRelatedRead;

    public CommonAdapter(Context context, List<RelatedRead> lstRelatedRead) {
        this.context = context;
        if (lstRelatedRead == null) {
            this.lstRelatedRead = new ArrayList<RelatedRead>();
        }
        this.lstRelatedRead = lstRelatedRead;
    }

    public List<RelatedRead> getLstRelatedRead() {
        return lstRelatedRead;
    }

    @Override
    public int getCount() {
        return lstRelatedRead.size();
    }

    @Override
    public RelatedRead getItem(int i) {
        return lstRelatedRead.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstRelatedRead.get(i).getNewsId();
    }

    class ViewHolder {
        TextView tvTitle, tvSourse, tvReadCount, tvCommentCount;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_relation_read_list_item, null, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.id_tv_title);
            holder.tvSourse = (TextView) convertView.findViewById(R.id.id_tv_source);
            holder.tvReadCount = (TextView) convertView.findViewById(R.id.id_tv_read_count);
            holder.tvCommentCount = (TextView) convertView.findViewById(R.id.id_tv_comment_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //赋值
        RelatedRead relatedRead = lstRelatedRead.get(i);
        holder.tvTitle.setText(relatedRead.getTitle());
        holder.tvSourse.setText(relatedRead.getSource());
        holder.tvReadCount.setText("阅读（" + relatedRead.getPageViewCount() + ")");
        holder.tvCommentCount.setText("评论(" + relatedRead.getCommentCount() + ")");
        return convertView;
    }
}
