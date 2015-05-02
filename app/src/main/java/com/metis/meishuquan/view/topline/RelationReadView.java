package com.metis.meishuquan.view.topline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.topline.RelatedRead;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/5/2.
 */
public class RelationReadView extends RelativeLayout {
    private Context mContext;
    private ListView listView;
    private CommonAdapter commonAdapter;
    private List<RelatedRead> lstRelatedRead;

    public RelationReadView(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_relation_read, this);
        initView();
        initEvent();
    }

    public void setLstRelatedRead(List<RelatedRead> lstRelatedRead) {
        this.lstRelatedRead = lstRelatedRead;
    }

    private void initView() {
        this.listView = (ListView) this.findViewById(R.id.id_lv_relation);
        this.commonAdapter = new CommonAdapter(mContext, lstRelatedRead);
        this.listView.setAdapter(commonAdapter);

    }

    private void initEvent() {
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    /**
     * 相关阅读列表适配器
     */
    class CommonAdapter extends BaseAdapter {
        private Context context;
        private List<RelatedRead> lstRelatedRead;

        CommonAdapter(Context context, List<RelatedRead> lstRelatedRead) {
            this.context = context;
            if (lstRelatedRead == null) {
                this.lstRelatedRead = new ArrayList<RelatedRead>();
            }
            this.lstRelatedRead = lstRelatedRead;
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
//            holder.tvReadCount.setText("");
//            holder.tvCommentCount.setText("");
            return convertView;
        }
    }
}
