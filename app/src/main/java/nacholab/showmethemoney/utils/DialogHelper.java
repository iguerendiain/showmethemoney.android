package nacholab.showmethemoney.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import nacholab.showmethemoney.R;

public class DialogHelper {

    public interface ConfirmationListener{
        void onConfirmationDialogYes();
        void onConfirmationDialogNo();
    }

    public static void showConfirmationDialog(Context context, String msg, final ConfirmationListener listener){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        listener.onConfirmationDialogYes();
                        return;
                    case DialogInterface.BUTTON_NEGATIVE:
                        listener.onConfirmationDialogNo();
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setMessage(msg)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener)
                .show();
    }

    public static void showInformationDialog(Context context, int msgRes, int btnRes, final DialogInterface.OnClickListener listener){
        showInformationDialog(
                context,
                context.getResources().getString(msgRes),
                context.getResources().getString(btnRes),
                listener
        );
    }

    public static void showInformationDialog(Context context, String msg, String btn, final DialogInterface.OnClickListener listener){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        if (listener!=null){
                            listener.onClick(dialog, which);
                        }
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setMessage(msg)
                .setNeutralButton(btn, dialogClickListener)
                .show();
    }
}
