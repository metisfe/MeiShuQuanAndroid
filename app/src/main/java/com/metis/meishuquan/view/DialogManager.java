package com.metis.meishuquan.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;

public class DialogManager
{
    private static DialogManager instance;

    private InfoDialog dialog;

    private DialogManager()
    {
    }

    public static DialogManager getInstance()
    {
        if (instance == null)
        {
            instance = new DialogManager();
        }

        return instance;
    }

    public void showDialog(boolean isShowProgress, String text)
    {
        if (dialog == null)
        {
            dialog = new InfoDialog(MainApplication.MainActivity, R.style.Dialog);
        }
        dialog.updateInfo(isShowProgress, text);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void dismissDialog()
    {
        if (dialog != null)
        {
            dialog.dismiss();
        }
    }

    public void resetDialog()
    {
        dialog = null;
    }

    public class InfoDialog extends Dialog
    {
        private View progressBar;
        private TextView infoTextView;

        public InfoDialog(Context context)
        {
            super(context);
            init(context);
        }

        public InfoDialog(Context context, int theme)
        {
            super(context, theme);
            init(context);
        }

        private void init(Context context)
        {
            this.setContentView(R.layout.info_dialog_layout);
            this.progressBar = this.findViewById(R.id.loading_progressbar);
            this.infoTextView = (TextView) this.findViewById(R.id.loaded_message_textview);
        }

        public void updateInfo(boolean isShowProgress, String text)
        {
            this.progressBar.setVisibility(isShowProgress ? View.VISIBLE : View.GONE);
            this.infoTextView.setText(text);
        }
    }
}