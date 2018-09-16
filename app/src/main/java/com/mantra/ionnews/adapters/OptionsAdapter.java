package com.mantra.ionnews.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.interfaces.OnOptionsClickListener;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.utils.AppConstants;

import static com.mantra.ionnews.utils.ConstantClass.ADS_FOOTER_MENU_OPTIONS;
import static com.mantra.ionnews.utils.ConstantClass.ADS_FOOTER_MENU_OPTION_ICONS;
import static com.mantra.ionnews.utils.ConstantClass.NEWS_FOOTER_MENU_OPTIONS;
import static com.mantra.ionnews.utils.ConstantClass.NEWS_FOOTER_MENU_OPTION_ICONS;

/**
 * Created by TaNMay on 06/04/17.
 */

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    private OnOptionsClickListener onOptionsClickListener;
    private Story story;
    private boolean isAd;

    public OptionsAdapter(OnOptionsClickListener onOptionsClickListener, Story story, boolean isAd) {
        super();
        this.onOptionsClickListener = onOptionsClickListener;
        this.story = story;
        this.isAd = isAd;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news_footer_menu_option, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsClickListener.onOptionClick(v, isAd);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (isAd) {
            viewHolder.icon.setImageDrawable(ContextCompat.getDrawable(viewHolder.icon.getContext(), ADS_FOOTER_MENU_OPTION_ICONS[position]));
            viewHolder.item.setText(ADS_FOOTER_MENU_OPTIONS[position]);
        } else {
            if (position == 0) {
                if (story.getIslike() != null && story.getIslike() == 1) {
                    viewHolder.item.setText(AppConstants.VAL_FOOTER_MENU_LIKED);
                    viewHolder.icon.setImageDrawable(ContextCompat.getDrawable(viewHolder.icon.getContext(), R.drawable.ic_like));
                } else {
                    viewHolder.item.setText(AppConstants.VAL_FOOTER_MENU_LIKE);
                    viewHolder.icon.setImageDrawable(ContextCompat.getDrawable(viewHolder.icon.getContext(), R.drawable.ic_unlike));
                }
            } else {
                viewHolder.icon.setImageDrawable(ContextCompat.getDrawable(viewHolder.icon.getContext(), NEWS_FOOTER_MENU_OPTION_ICONS[position]));
                viewHolder.item.setText(NEWS_FOOTER_MENU_OPTIONS[position]);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (isAd) return ADS_FOOTER_MENU_OPTIONS.length;
        else return NEWS_FOOTER_MENU_OPTIONS.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.infmo_item);
            icon = (ImageView) itemView.findViewById(R.id.infmo_icon);
        }
    }
}