package com.mantra.ionnews.ui.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.interfaces.OnPopupDialogClickListener;

/**
 * Created by TaNMay on 10/05/17.
 */

public class NewsPopupDialogs {

    private PopupWindow popupWindow;

    private OnPopupDialogClickListener onPopupDialogClickListener;

    public NewsPopupDialogs(OnPopupDialogClickListener onPopupDialogClickListener) {
        this.onPopupDialogClickListener = onPopupDialogClickListener;
    }

    public void openHeaderOptionsPopup(View parent) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_header_menu_options, null);

        TextView backToTop = (TextView) layout.findViewById(R.id.phmo_back_to_top);
        backToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                onPopupDialogClickListener.onBackToTopClick();
            }
        });

        int valueInPixels = (int) context.getResources().getDimension(R.dimen.news_detail_menu_width);

        popupWindow = new PopupWindow(layout, valueInPixels, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(12f);
        }
        popupWindow.showAtLocation(parent, Gravity.TOP | Gravity.RIGHT, 8, 56);
    }
}
