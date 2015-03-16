package com.metis.meishuquan.view.shared;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.framework.util.TextureRender;
import com.metis.meishuquan.util.Utils;

import java.net.URL;

/**
 * Created by wudi on 3/15/2015.
 */
public class BaseDialog extends Dialog
{
    public BaseDialog(Context context, int theme)
    {
        super(context, theme);
        this.setCancelable(false);
    }

    public BaseDialog(Context context)
    {
        super(context);
        this.setCancelable(false);
    }

    public boolean getCheckBoxState()
    {
        return this.getCheckBoxState();
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder
    {
        public interface OnCheckBoxListener
        {
            public void checkedOperation();
        };

        private Context context;
        private int imageResource;
        private URL imageUrl;
        private boolean checkBoxDefaultState;
        private boolean title1BoldAndBig;
        private String checkBoxText;
        private String title1;
        private String title2;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;

        private ImageView icon;
        private TextView maintitleTextView;
        private TextView subtitleTextView;
        private TextView titleDivideTextView;
        private View contentLayout;
        private TextView contentTextView;
        private View checkLayout;
        private CheckBox checkBox;
        private TextView checkBoxTextView;
        private Button positiveButton, negativeButton;
        private TextView buttonDivideTextView;
        private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;
        private OnCheckBoxListener onCheckBoxListener;

        public Builder(Context context)
        {
            this.context = context;
        }

        public void setCancelable(boolean flag)
        {
            this.setCancelable(flag);
        }

        public Builder setImageResource(URL imageUrl)
        {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setImageResource(int imageResource)
        {
            this.imageResource = imageResource;
            return this;
        }

        public Builder setCheckBox(boolean checkBoxDefaultState, String text, OnCheckBoxListener onCheckBoxListener)
        {
            this.checkBoxDefaultState = checkBoxDefaultState;
            this.checkBoxText = text;
            this.onCheckBoxListener = onCheckBoxListener;
            return this;
        }

        public boolean getCheckBoxState()
        {
            return this.checkLayout.isShown()&&this.checkBox.isChecked();
        }

        public Builder setMessage(String message)
        {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message)
        {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title)
        {
            this.title1 = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title)
        {
            this.title1 = title;
            return this;
        }

        public Builder setTitle(int title1, int title2)
        {
            this.title1 = (String) context.getText(title1);
            this.title2 = (String) context.getText(title2);
            return this;
        }

        public Builder setTitleBoldAndBig()
        {
            this.title1BoldAndBig=true;
            return this;
        }

        public Builder setTitle(String title1, String title2)
        {
            this.title1 = title1;
            this.title2 = title2;
            return this;
        }

        public Builder setView(View v)
        {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener)
        {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener)
        {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener)
        {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener)
        {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Create the custom dialog
         */
        public BaseDialog create()
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final BaseDialog dialog = new BaseDialog(context, R.style.Dialog);

            View layout = inflater.inflate(R.layout.view_shared_basedialog, null);

            //set the dialog's image
            this.icon = (ImageView) layout.findViewById(R.id.view_shared_basedialog_icon);

            if (this.imageResource > 0 || this.imageUrl != null)
            {
                this.icon.setVisibility(View.VISIBLE);
                if (this.imageResource > 0)
                {
                    this.icon.setBackgroundResource(this.imageResource);
                }
                else
                {
                    TextureRender.getInstance().setBitmap(this.imageUrl, this.icon, R.drawable.no_data_error_image);
                }
            }
            else
            {
                this.icon.setVisibility(View.GONE);
            }

            // set check box's text and default value
            this.checkLayout = layout.findViewById(R.id.view_shared_basedialog_checkbox_layout);
            this.checkBox = (CheckBox) layout.findViewById(R.id.view_shared_basedialog_checkbox);
            this.checkBoxTextView = (TextView) layout.findViewById(R.id.view_shared_basedialog_checkbox_text);

            if (!TextUtils.isEmpty(this.checkBoxText))
            {
                this.checkLayout.setVisibility(View.VISIBLE);
                this.checkBoxTextView.setText(this.checkBoxText);
                this.checkBox.setChecked(checkBoxDefaultState);
            }
            else
            {
                this.checkLayout.setVisibility(View.GONE);
            }

            // set the dialog main title and sub title
            this.maintitleTextView = (TextView) layout.findViewById(R.id.view_shared_basedialog_maintitle_textview);
            this.subtitleTextView = (TextView) layout.findViewById(R.id.view_shared_basedialog_subtitle_textview);
            this.titleDivideTextView = (TextView) layout.findViewById(R.id.view_shared_basedialog_titledivide_textview);
            if (!TextUtils.isEmpty(title1))
            {
                this.maintitleTextView.setText(title1);
                if (this.title1BoldAndBig)
                {
                    this.maintitleTextView.setTypeface(null, Typeface.BOLD);
                    this.maintitleTextView.setTextSize(17);
                }

                this.maintitleTextView.setVisibility(View.VISIBLE);
            }
            else
            {
                this.maintitleTextView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(title2))
            {
                this.subtitleTextView.setText(title2);
                this.subtitleTextView.setVisibility(View.VISIBLE);
            }
            else
            {
                this.subtitleTextView.setVisibility(View.GONE);
            }
            this.titleDivideTextView.setVisibility(View.GONE);

            // set the confirm button
            this.positiveButton = ((Button) layout.findViewById(R.id.view_shared_basedialog_positivebutton));
            if (!TextUtils.isEmpty(positiveButtonText))
            {
                this.positiveButton.setText(positiveButtonText);
                this.positiveButton.setVisibility(View.VISIBLE);
                if (positiveButtonClickListener != null)
                {
                    this.positiveButton.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            if (checkBox.isChecked()&&onCheckBoxListener!=null)
                            {
                                onCheckBoxListener.checkedOperation();
                            }
                        }
                    });
                }
            }
            else
            {
                // if no confirm button just set the visibility to GONE
                this.positiveButton.setVisibility(View.GONE);
            }

            // set the cancel button
            this.negativeButton = ((Button) layout.findViewById(R.id.view_shared_basedialog_negativebutton));
            if (!TextUtils.isEmpty(negativeButtonText))
            {
                this.negativeButton.setText(negativeButtonText);
                this.negativeButton.setVisibility(View.VISIBLE);
                if (negativeButtonClickListener != null)
                {
                    this.negativeButton.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }
            else
            {
                // if no confirm button just set the visibility to GONE
                this.negativeButton.setVisibility(View.GONE);
            }

            // set button's background
            this.buttonDivideTextView = (TextView) layout.findViewById(R.id.view_shared_basedialog_buttondivide_textview);
            if (!TextUtils.isEmpty(negativeButtonText) && !TextUtils.isEmpty(negativeButtonText))
            {
                this.buttonDivideTextView.setVisibility(View.VISIBLE);
                this.positiveButton.setBackgroundResource(R.drawable.view_shared_corner_round_rightbottom);
                this.negativeButton.setBackgroundResource(R.drawable.view_shared_corner_round_leftbottom);
            }
            else
            {
                this.buttonDivideTextView.setVisibility(View.GONE);
                this.positiveButton.setBackgroundResource(R.drawable.view_shared_corner_round_bottom);
                this.negativeButton.setBackgroundResource(R.drawable.view_shared_corner_round_bottom);
            }

            // set the content message
            this.contentLayout = layout.findViewById(R.id.view_shared_basedialog_content_layout);
            if (!TextUtils.isEmpty(message))
            {
                this.contentTextView = ((TextView) layout.findViewById(R.id.view_shared_basedialog_content_textview));
                this.contentTextView.setText(message);
                this.contentTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
            }
            else if (contentView != null)
            {
                // if no message set
                // add the contentView to the dialog body
                ((ViewGroup) this.contentLayout).removeAllViews();
                ((ViewGroup) this.contentLayout).addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            else
            {
                this.contentLayout.setVisibility(View.GONE);
            }

            int[] params = Utils.getLayoutParamsForHeroImage();
            dialog.setContentView(layout, new ViewGroup.LayoutParams(params[0]*4/5, ViewGroup.LayoutParams.MATCH_PARENT));
            return dialog;
        }
    }
}