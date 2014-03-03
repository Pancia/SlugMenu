package com.dayzerostudio.utils.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.dayzerostudio.slugguide.slugmenu.R;
import com.dayzerostudio.slugguide.slugmenu.application.MyGoogleAnalytics;

public class OptOutGADialog extends DialogFragment {

    private static String TAG = OptOutGADialog.class.getSimpleName();

    public OptOutGADialog() {
        setRetainInstance(true);
    }

    public static OptOutGADialog newInstance() {
        return new OptOutGADialog();
    }

    public void show(FragmentManager manager) {
        super.show(manager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.optout_googleanalytics_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("Opt out of Google Analytics?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        MyGoogleAnalytics.setOptOutPreference(true);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyGoogleAnalytics.setOptOutPreference(false);
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

}
