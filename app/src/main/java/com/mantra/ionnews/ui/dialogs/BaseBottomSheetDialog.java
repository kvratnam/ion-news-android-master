package com.mantra.ionnews.ui.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mantra.ionnews.R;

/**
 * Created by TaNMay on 21/03/17.
 */

public class BaseBottomSheetDialog extends BottomSheetDialogFragment {

    protected ProgressDialog mProgressDialog = null;

    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(getActivity(), null, message, true, false, null);
        } else {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    protected void showProgressBar(ProgressBar progressBarView) {
        progressBarView.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar(ProgressBar progressBarView) {
        progressBarView.setVisibility(View.GONE);
    }

    protected void showToastMessage(String message, boolean isLong) {
        Toast toast = null;

        if (isLong) toast = toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        else toast = toast.makeText(getContext(), message, Toast.LENGTH_SHORT);

        toast.show();
    }

    protected void showSnackbarMessage(View view, String message, boolean isLong, String action,
                                       View.OnClickListener onClickListener, boolean isDismissed) {
        Snackbar snackbar = null;

        if (isLong) snackbar = snackbar.make(view, message, Snackbar.LENGTH_LONG);
        else if (!isLong) snackbar = snackbar.make(view, message, Snackbar.LENGTH_SHORT);

        if (action != null && isDismissed == true) {
            final Snackbar finalSnackbar = snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
            snackbar = snackbar.setAction(action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalSnackbar.dismiss();
                }
            });
        } else if (action != null)
            snackbar = snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction(action, onClickListener);

        snackbar.show();
    }

    public void showNetworkErrorMessage(View view, View.OnClickListener mOnClickListener, Context context) {
        if (view != null) {
            Snackbar snackbar = Snackbar
                    .make(view, context.getString(R.string.unable_to_connect), Snackbar.LENGTH_INDEFINITE)
                    .setAction(context.getString(R.string.retry), mOnClickListener);
            snackbar.show();
        } else {
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    protected void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
