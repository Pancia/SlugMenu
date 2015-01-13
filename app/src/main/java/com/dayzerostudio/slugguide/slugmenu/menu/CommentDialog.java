package com.dayzerostudio.slugguide.slugmenu.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.dayzerostudio.slugguide.slugmenu.R;

public class CommentDialog extends DialogFragment {
    private final static String TAG = CommentDialog.class.getSimpleName();

    public CommentDialog() {} //is actually used on rotation

    public void show(FragmentManager manager) {
        super.show(manager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.displaymenu_dialog_fragment, null);

        builder.setView(view)
                .setTitle("Comment:")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
