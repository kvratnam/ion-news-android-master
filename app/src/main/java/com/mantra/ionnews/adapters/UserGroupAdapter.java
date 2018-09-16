package com.mantra.ionnews.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.interfaces.OnUserGroupClickListener;
import com.mantra.ionnews.models.responses.UserGroup;

import java.util.List;

/**
 * Created by TaNMay on 17/04/17.
 */

public class UserGroupAdapter extends RecyclerView.Adapter<UserGroupAdapter.ViewHolder> {

    private OnUserGroupClickListener onUserGroupClickListener;
    private List<UserGroup> userGroups;

    public UserGroupAdapter(OnUserGroupClickListener onUserGroupClickListener, List<UserGroup> userGroups) {
        super();
        this.onUserGroupClickListener = onUserGroupClickListener;
        this.userGroups = userGroups;
    }

    @Override
    public UserGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_group, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserGroupClickListener.onUserGroupSelected(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        UserGroup userGroup = userGroups.get(position);
        holder.item.setText(userGroup.getName());
        if (position == userGroups.size() - 1) holder.divider.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return userGroups.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item;
        public View divider;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.iug_item);
            divider = itemView.findViewById(R.id.iug_item_divider);
        }
    }
}
