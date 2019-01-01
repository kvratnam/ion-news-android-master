package com.mantra.ionnews.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.ui.activities.LoginActivity;

/**
 * Created by TaNMay on 30/03/17.
 */

public class UtilDialogs {

    public UtilDialogs() {
    }

    public void showSingleButtonDialog(final Context context, String title, String message, String btnText) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View infoDialogView = factory.inflate(
                R.layout.dialog_single_button, null);
        final AlertDialog infoDialog = new AlertDialog.Builder(context).create();
        infoDialog.setView(infoDialogView);
        if (title != null)
            ((TextView) infoDialogView.findViewById(R.id.tvDialogTitle)).setText(title);
        else
            infoDialogView.findViewById(R.id.tvDialogTitle).setVisibility(View.GONE);
        ((TextView) infoDialogView.findViewById(R.id.tvDialogMessage)).setText(message);
        ((TextView) infoDialogView.findViewById(R.id.btnSingle)).setText(btnText);
        infoDialogView.findViewById(R.id.btnSingle).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                infoDialog.dismiss();

                // change activity screen for forget password success
                Intent intent= new Intent(context,LoginActivity.class);
                context.startActivity(intent);
            }
        });
        infoDialog.show();
    }
}
