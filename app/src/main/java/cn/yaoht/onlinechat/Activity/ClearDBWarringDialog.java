package cn.yaoht.onlinechat.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import cn.yaoht.onlinechat.R;

/**
 * Created by yaoht on 2015/12/27.
 * Project: OnlineChat
 */

/**
 * @author yaoht
 *         对话框
 *         清空数据的警示对话框
 */
public class ClearDBWarringDialog extends DialogFragment {

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (NoticeDialogListener) activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.warring))
                .setMessage(getString(R.string.clear_db_warring))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveClick(ClearDBWarringDialog.this);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativeClick(ClearDBWarringDialog.this);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        return builder.create();
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }
}
