package com.mantra.ionnews.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.mantra.ionnews.R;
import com.mantra.ionnews.adapters.UserGroupAdapter;
import com.mantra.ionnews.interfaces.OnUserGroupClickListener;
import com.mantra.ionnews.interfaces.OnUserGroupDialogClickListener;
import com.mantra.ionnews.models.responses.UserGroup;

import java.util.List;

/**
 * Created by TaNMay on 24/05/17.
 */

public class UserGroupDialog implements OnUserGroupClickListener {

    private Context context;
    private OnUserGroupDialogClickListener onUserGroupDialogClickListener;

    private Dialog dialog;
    private RecyclerView userGroupsRv;
    private RecyclerView.Adapter userGroupAdapter;

    private List<UserGroup> userGroups;

    public UserGroupDialog(Context context, OnUserGroupDialogClickListener onUserGroupDialogClickListener) {
        this.context = context;
        this.onUserGroupDialogClickListener = onUserGroupDialogClickListener;
    }

    public void showUserGroupDropdown(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_select_user_group);
        dialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.transparent));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(4f);
        }

        userGroupsRv = (RecyclerView) dialog.findViewById(R.id.dsug_user_group_rv);
        RecyclerView.LayoutManager avatarsLayoutManager = new LinearLayoutManager(context);
        userGroupAdapter = new UserGroupAdapter(this, userGroups);

        userGroupsRv.setHasFixedSize(true);
        userGroupsRv.setLayoutManager(avatarsLayoutManager);
        userGroupsRv.setAdapter(userGroupAdapter);

        dialog.show();
    }

    @Override
    public void onUserGroupSelected(View view) {
        int position = userGroupsRv.getChildAdapterPosition(view);
        onUserGroupDialogClickListener.onUserGroupSelected(userGroups.get(position).getName(), userGroups.get(position).getId());
        dialog.dismiss();
    }
}
