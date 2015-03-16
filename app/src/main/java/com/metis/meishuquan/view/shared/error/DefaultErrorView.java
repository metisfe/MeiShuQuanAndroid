package com.metis.meishuquan.view.shared.error;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by wudi on 3/15/2015.
 */

public class DefaultErrorView extends ExtendableErrorView
{
    private ImageView button;
    private TextView text;

    public DefaultErrorView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    public DefaultErrorView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public DefaultErrorView(Context context)
    {
        super(context);
        init(context);
    }

    private void init(Context context)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_shared_error_defaulterrorview, this, true);
        this.button = (ImageView) this.findViewById(R.id.view_shared_error_defaultnodatcaerrorview_button);
        this.text = (TextView) this.findViewById(R.id.view_shared_error_defaultnodatcaerrorview_text);
        this.inflateHeader();
    }

    @Override
    public void setErrorType(ErrorType errorType)
    {
        this.errorType = errorType;
    }

    @Override
    public void bind()
    {
        OnClickListener refreshListener = new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                errorlistener.OnErrorRefresh();
            }
        };
        switch (this.errorType)
        {
            case NetworkNotAvailable:
                this.text.setText("please check network or click to refresh");
                this.button.setImageResource(R.drawable.view_shared_errorview_reload_selector);
                this.button.setOnClickListener(refreshListener);
                break;
            case NoData:
                this.text.setText("no data");
                this.button.setImageResource(R.drawable.view_shared_errorview_reload_selector);
                this.button.setOnClickListener(refreshListener);
                break;
            case NoMyFollow:
                this.text.setText("error1");
                this.button.setImageResource(R.drawable.no_data_error_image);
                this.button.setOnClickListener(null);
                break;
            case NoMyActivity:
                this.text.setText("error2");
                this.button.setImageResource(R.drawable.no_data_error_image);
                this.button.setOnClickListener(null);
                break;
            case NoMyTimeline:
                this.text.setText("error3");
                this.button.setImageResource(R.drawable.no_data_error_image);
                this.button.setOnClickListener(null);
                break;
            case NoTaImages:
                this.text.setText("error4");
                this.button.setImageResource(R.drawable.no_gallery_error_image);
                this.button.setOnClickListener(null);
                break;
            default:
                break;
        }
    }

    @Override
    protected void inflateHeader()
    {
        this.headerGroup = (ViewGroup) this.findViewById(R.id.view_shared_error_defaultnodatcaerrorview_header);
    }

}
