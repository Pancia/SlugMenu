package com.dayzerostudio.utils.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.dayzerostudio.slugguide.slugmenu.R;

public class FeedBackDialog extends DialogFragment {

    private static String TAG = FeedBackDialog.class.getSimpleName();

    public FeedBackDialog() {
        setRetainInstance(true);
    }

    public static FeedBackDialog newInstance() {
        return new FeedBackDialog();
    }

    public void show(FragmentManager manager) {
        super.show(manager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        EditText message = null;
        RadioGroup typeOfFeedback = null;
        View view = getActivity().getLayoutInflater().inflate(R.layout.feedback_dialog_fragment, null);
        if (view != null) {
            message = (EditText) view.findViewById(R.id.feedback_dialog_edittext_message);
            typeOfFeedback = (RadioGroup) view.findViewById(R.id.feedback_dialog_radiogroup);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final RadioGroup finalTypeOfFeedback = typeOfFeedback;
        final EditText finalMessage = message;
        builder.setView(view)
                .setTitle("Send Feedback?")
                .setPositiveButton("Send!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        Intent sendIntent = new Intent(Intent.ACTION_SENDTO,
                                Uri.fromParts("mailto", "anthony.dayzerostudio@gmail.com", null));
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT,
                                getOption(finalTypeOfFeedback != null
                                        ? finalTypeOfFeedback.getCheckedRadioButtonId()
                                        : 0));
                        try {
                            //noinspection ConstantConditions
                            sendIntent.putExtra(Intent.EXTRA_TEXT, finalMessage.getText().toString());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            dialog.cancel();
                            return;
                        }
                        startActivity(Intent.createChooser(sendIntent, "Send feedback using:"));
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

    private String getOption(int id) {
        switch (id) {
            case R.id.feedback_dialog_radiobutton_bug:
                return "Bug";
            case R.id.feedback_dialog_radiobutton_feedback:
                return "Feedback";
            case R.id.feedback_dialog_radiobutton_suggestion:
                return "Suggestion";
            default:
                return "General Feedback";
        }
    }

}
