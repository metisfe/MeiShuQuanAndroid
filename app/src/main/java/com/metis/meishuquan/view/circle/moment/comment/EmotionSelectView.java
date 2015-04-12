package com.metis.meishuquan.view.circle.moment.comment;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.metis.meishuquan.R;


public class EmotionSelectView extends LinearLayout implements OnClickListener, OnItemClickListener
{
    private GridView emotionGridView;
    private ImageView imageViewDelete;

    private CommentEmotionSelectGridAdapter gridAdapter;
    private EmotionEditText bindedEditText;

    public EmotionSelectView(Context context)
    {
        super(context);
        init();
    }

    public EmotionSelectView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public void init()
    {
        inflate(getContext(), R.layout.view_comment_emotionselectionview, this);
        emotionGridView = (GridView) findViewById(R.id.view_comment_emotionselectionview_gridview);
        imageViewDelete = (ImageView) findViewById(R.id.view_comment_emotionselectionview_imageview_delete);

        emotionGridView.setOnItemClickListener(this);
        imageViewDelete.setOnClickListener(this);

        gridAdapter = new CommentEmotionSelectGridAdapter(getContext(), CommentUtility.EmotionNames);
        emotionGridView.setAdapter(gridAdapter);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.view_comment_emotionselectionview_imageview_delete:
                if (bindedEditText != null)
                {
                    int cursorIndex = bindedEditText.getSelectionStart();
                    if (cursorIndex == 0)
                    {
                        return;
                    }

                    String content = bindedEditText.getText().toString();
                    if (TextUtils.isEmpty(content))
                    {
                        return;
                    }

                    String contentBeforeCursor = content.substring(0, cursorIndex);
                    String contentAfterCursor = content.substring(cursorIndex, content.length());
                    String lastCharacterBeforeCursor = content.substring(cursorIndex - CommentUtility.EmotionStringEnd.length(), cursorIndex);
                    int indexOfLastEmotionStart = contentBeforeCursor.lastIndexOf(CommentUtility.EmotionStringStart);

                    if (CommentUtility.EmotionStringEnd.equals(lastCharacterBeforeCursor) && indexOfLastEmotionStart >= 0)
                    {
                        bindedEditText.setText(contentBeforeCursor.substring(0, indexOfLastEmotionStart) + contentAfterCursor);
                        bindedEditText.setSelection(indexOfLastEmotionStart);
                    }
                    else
                    {
                        bindedEditText.setText(contentBeforeCursor.substring(0, cursorIndex - 1) + contentAfterCursor);
                        bindedEditText.setSelection(cursorIndex - 1);
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        String text = CommentUtility.EmotionNames.get(arg2);

        if (bindedEditText != null && !TextUtils.isEmpty(text))
        {
            int start = bindedEditText.getSelectionStart();
            CharSequence content = bindedEditText.getText().insert(start, text);
            bindedEditText.setText(content);
            bindedEditText.setSelection(start + text.length());
        }
    }

    public void setEditText(EmotionEditText editText)
    {
        bindedEditText = editText;
    }
}