package com.dayzerostudio.utils.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.dayzerostudio.slugguide.slugmenu.R;
import com.dayzerostudio.slugguide.slugmenu.activities.DisplayMenuActivity;

public class GetInternetDialog extends DialogFragment {

    private static String TAG = GetInternetDialog.class.getSimpleName();

    public GetInternetDialog() {
        setRetainInstance(true);
    }

    public static GetInternetDialog newInstance() {
        return new GetInternetDialog();
    }

    public void show(FragmentManager manager) {
        super.show(manager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.getinternet_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("Go Online")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                        dialog.dismiss();
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

    @Override
    public void dismiss() {
        super.dismiss();
        ((DisplayMenuActivity)getActivity()).setIsShowingGetInternetDialog(false);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        ((DisplayMenuActivity)getActivity()).setIsShowingGetInternetDialog(false);
    }

}

