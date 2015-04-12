package com.metis.meishuquan.view.circle.moment.comment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommentEmotionSelectGridAdapter extends BaseAdapter
{
    protected Context context;
    protected LayoutInflater inflater;
    protected List<String> emotionWordList = new ArrayList<String>();

    public CommentEmotionSelectGridAdapter(Context context, List<String> emotionWords)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        if (emotionWords != null && emotionWords.size() > 0)
        {
            this.emotionWordList = emotionWords;
        }
    }

    @Override
    public int getCount()
    {
        return this.emotionWordList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.emotionWordList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        EmotionSelectionGridItem gridItem = null;
        if (convertView == null)
        {      
            gridItem = new EmotionSelectionGridItem(context);
        }
        else
        {
            gridItem = (EmotionSelectionGridItem) convertView;
        }

        String name = (String) getItem(position);
        gridItem.setEmotionName(name);
        
        return gridItem;
    }
}
