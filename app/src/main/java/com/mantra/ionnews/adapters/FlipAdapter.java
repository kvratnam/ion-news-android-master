package com.mantra.ionnews.adapters;

/**
 * Created by rajat on 30/03/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.mantra.ionnews.R;
import com.mantra.ionnews.interfaces.OnNewsFlipItemClickListener;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;

import static com.mantra.ionnews.utils.AppConstants.VAL_ADS;
import static com.mantra.ionnews.utils.ConstantClass.MAX_LINES_NEWS_DESC;

public class FlipAdapter extends BaseAdapter {

    private static final String TAG = FlipAdapter.class.getName() + " ==>";
    ViewHolder holder;
    private Context context;
    private LayoutInflater inflater;
    private Callback callback;
    private List<Story> stories = new ArrayList<Story>();
    private String categoryName;
    private OnNewsFlipItemClickListener onNewsFlipItemClickListener;
    private int perPage;

    public FlipAdapter(Context context, List<Story> stories, String categoryName,
                       OnNewsFlipItemClickListener onNewsFlipItemClickListener, int perPage) {
        this.stories = stories;
        this.context = context;
        this.categoryName = categoryName;
        this.onNewsFlipItemClickListener = onNewsFlipItemClickListener;
        this.perPage = perPage;
        inflater = LayoutInflater.from(context);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return stories.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return stories.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_news_detail, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.footerShare.setTag(position);
        holder.footerLike.setTag(position);
        holder.headerMenu.setTag(position);
        holder.footerMenu.setTag(position);

        holder.headerTitle.setTag(position);
        holder.titleTv.setTag(position);
        holder.hashtagsTv.setTag(position);
        holder.descriptionTv.setTag(position);
        holder.footerNoOfLikesTv.setTag(position);
        holder.sourceTv.setTag(position);
        holder.timeTv.setTag(position);
        holder.newsIv.setTag(position);

        if (position == stories.size() - 1) {
            onNewsFlipItemClickListener.onLastItem();
        }

        if (stories.get(position).getType() != null && stories.get(position).getType().equalsIgnoreCase(VAL_ADS)) {
            holder.footerNoOfLikesTv.setVisibility(View.INVISIBLE);
            holder.footerLike.setVisibility(View.INVISIBLE);
            holder.footerShare.setVisibility(View.INVISIBLE);
        } else {
            holder.footerNoOfLikesTv.setVisibility(View.VISIBLE);
            holder.footerLike.setVisibility(View.VISIBLE);
            holder.footerShare.setVisibility(View.VISIBLE);

            if (stories.get(position).getIslike() != null)
                setStoryLike(position);
        }

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsFlipItemClickListener.onNewsItemClick(position, holder.newsIv);
            }
        });

        Typeface headerTitleTypeface = Typeface.createFromAsset(holder.headerTitle.getContext().getAssets(), "OpenSans-Bold.ttf");
        Typeface hashtagsTitleTypeface = Typeface.createFromAsset(holder.headerTitle.getContext().getAssets(), "OpenSans-Regular.ttf");
        Typeface titleTypeface = Typeface.createFromAsset(holder.titleTv.getContext().getAssets(), "SourceSerifPro-Bold.ttf");
        Typeface descTypeface = Typeface.createFromAsset(context.getAssets(), "SourceSerifPro-Regular.ttf");

        holder.headerTitle.setTypeface(headerTitleTypeface);
        holder.titleTv.setTypeface(titleTypeface);
        holder.descriptionTv.setTypeface(descTypeface);
        holder.hashtagsTv.setTypeface(hashtagsTitleTypeface);

        holder.headerBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsFlipItemClickListener.onBackClick();
            }
        });

        if (position == (perPage - 3)) {
            onNewsFlipItemClickListener.onPaginationIndexFlip();
        }


        holder.headerTitle.setText(categoryName);
        String formattedHashtags = Util.getSingleFormattedHashtag(stories.get((int) holder.hashtagsTv.getTag()).getTags(), categoryName);
        if (formattedHashtags==null){
            holder.hashtagsTv.setVisibility(View.INVISIBLE);
        } else {
            holder.hashtagsTv.setVisibility(View.VISIBLE);
            holder.hashtagsTv.setText(formattedHashtags);
        }
        holder.hashtagsTv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));

        if (stories.get((int) holder.descriptionTv.getTag()).getContent() != null && !stories.get((int) holder.descriptionTv.getTag()).getContent().isEmpty())
            setNewsContent(holder.descriptionTv, (int) holder.descriptionTv.getTag());
        else
            setNewsContent(holder.descriptionTv, "");

        if (stories.get((int) holder.titleTv.getTag()).getTitle() != null && !stories.get((int) holder.titleTv.getTag()).getTitle().isEmpty())
            holder.titleTv.setText(stories.get((int) holder.titleTv.getTag()).getTitle().trim());

        if (stories.get((int) holder.footerNoOfLikesTv.getTag()).getLikeCount() != null && stories.get((int) holder.footerNoOfLikesTv.getTag()).getLikeCount() > 1)
            holder.footerNoOfLikesTv.setText(stories.get((int) holder.footerNoOfLikesTv.getTag()).getLikeCount() + " likes");
        else if (stories.get((int) holder.footerNoOfLikesTv.getTag()).getLikeCount() != null && stories.get((int) holder.footerNoOfLikesTv.getTag()).getLikeCount() == 1)
            holder.footerNoOfLikesTv.setText(stories.get((int) holder.footerNoOfLikesTv.getTag()).getLikeCount() + " like");

        if (stories.get((int) holder.sourceTv.getTag()).getCrawlUrl() != null && !stories.get((int) holder.sourceTv.getTag()).getCrawlUrl().isEmpty()) {
            String[] source = stories.get((int) holder.sourceTv.getTag()).getCrawlUrl().split("/");
            holder.sourceTv.setText(source[2]);
        } else {
            holder.sourceTv.setText("");
        }

        if (stories.get((int) holder.timeTv.getTag()).getCreatedAt() != null && !stories.get((int) holder.timeTv.getTag()).getCreatedAt().isEmpty()) {
            String originalDate = stories.get((int) holder.timeTv.getTag()).getCreatedAt();
            String finalDate = getFinalDateToDisplay(originalDate);
            holder.timeTv.setText(finalDate);
        }

        Picasso.with(this.context).cancelRequest(holder.newsIv);
        if (stories.get((int) holder.newsIv.getTag()).getImage() != null && !stories.get((int) holder.newsIv.getTag()).getImage().isEmpty()) {
            holder.newsIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_story_item_background));

            OkHttpClient client = new OkHttpClient();
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttp3Downloader(client))
                    .build()
                    .load(stories.get((int) holder.newsIv.getTag()).getImage())
                    .placeholder(R.drawable.ic_story_item_background)
                    .error(R.drawable.ic_story_item_background)
                    .into(holder.newsIv);
            holder.newsIv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            holder.newsIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_story_item_background));
        }


        return convertView;
    }

    private String getFinalDateToDisplay(String originalDate) {
        String getFinalDateToDisplay = "";
        Date newsDate = Util.getDateFromIst(originalDate);
        Date currentDate = Util.getCurrentDate();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long monthsInMilli = daysInMilli * 30;
        long yearsInMilli = daysInMilli * 365;

        long differenceInMillis = Util.differenceInDates(currentDate, newsDate);

        long differenceInMins = differenceInMillis / minutesInMilli;
        if (differenceInMins > 60) {

            long differenceInHours = differenceInMillis / hoursInMilli;
            if (differenceInHours > 24) {

                long differenceInDays = differenceInMillis / daysInMilli;
                if (differenceInDays > 30) {

                    long differenceInMonths = differenceInMillis / monthsInMilli;
                    if (differenceInMonths > 11) {

                        long differenceInYears = differenceInMillis / yearsInMilli;
                        getFinalDateToDisplay = differenceInYears + "y";
                    } else {
                        getFinalDateToDisplay = differenceInMonths + "m";
                    }
                } else {
                    getFinalDateToDisplay = differenceInDays + "d";
                }
            } else {
                getFinalDateToDisplay = differenceInHours + "hr";
            }
        } else {
            getFinalDateToDisplay = differenceInMins + "min";
        }


//        int yearFromDate =Util.getTimeInYearsFromDate(date);
//        int currentYear = Util.getCurrentYear();
//        if (yearFromDate < currentYear) {
//            int diffInYear = currentYear - yearFromDate;
//            getFinalDateToDisplay = diffInYear + "y";
//        } else {
//            int monthFromDate = Util.getTimeInMonthsFromDate(date);
//            int currentMonth = Util.getTimeInMonthsFromDate(date);
//            if (monthFromDate < currentMonth) {
//
//            }
//        }
        return getFinalDateToDisplay;
    }

    public void setStoryLike(int position) {
        if (stories.get(position).getIslike() == 1) {
            holder.footerLike.setImageDrawable(ContextCompat.getDrawable(holder.footerLike.getContext(), R.drawable.ic_like));
        } else {
            holder.footerLike.setImageDrawable(ContextCompat.getDrawable(holder.footerLike.getContext(), R.drawable.ic_unlike));
        }
    }

    private void setNewsContent(final TextView descriptionTv, int index) {
//        ViewTreeObserver observer = descriptionTv.getViewTreeObserver();
//        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int maxLines = (int) descriptionTv.getHeight() / descriptionTv.getLineHeight();
//                descriptionTv.setMaxLines(maxLines);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    descriptionTv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                } else {
//                    descriptionTv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
        String content = stories.get(index).getContent();

        if (isLargerText(descriptionTv, content)) {
            descriptionTv.setLines(MAX_LINES_NEWS_DESC);
            descriptionTv.setText(content.trim());
        } else {
            descriptionTv.setLines(getNumberOfLines(descriptionTv, content));
            descriptionTv.setText(content.trim());
        }
//            }
//        });
    }

    private void setNewsContent(final TextView descriptionTv, String text) {
        descriptionTv.setLines(1);
        descriptionTv.setText(text);
    }

    private int getNumberOfCharactersInOneLine(TextView textView) {
        TextPaint paint = textView.getPaint();
        int wordwidth = (int) paint.measureText("a", 0, 1);
        int screenwidth = context.getResources().getDisplayMetrics().widthPixels;
        int num = screenwidth / wordwidth;
        return num;
    }

    private boolean isLargerText(TextView text, String newText) {
        int charLengthOfTextView = getNumberOfCharactersInOneLine(text);
        boolean isLargerText = newText.length() > charLengthOfTextView * 3;
        return isLargerText;
    }

    private int getNumberOfLines(TextView text, String newText) {
        int charLengthOfTextView = getNumberOfCharactersInOneLine(text);
        double numOfLines = newText.length() / charLengthOfTextView;
        int absoluteNoOfLines = (int) Math.ceil(numOfLines);
        return absoluteNoOfLines;
    }

    public interface Callback {
        void onPageRequested(int page);
    }

    public class ViewHolder implements OnClickListener {
        TextView titleTv, descriptionTv, headerTitle, sourceTv, timeTv, footerNoOfLikesTv, hashtagsTv;
        ImageView newsIv, headerBack, headerMenu, footerLike, footerShare, footerMenu;

        public ViewHolder(View convertView) {
            titleTv = (TextView) convertView.findViewById(R.id.ind_title);
            descriptionTv = (TextView) convertView.findViewById(R.id.inp_description);
            timeTv = (TextView) convertView.findViewById(R.id.inp_time);
            sourceTv = (TextView) convertView.findViewById(R.id.inp_source);
            newsIv = (ImageView) convertView.findViewById(R.id.ind_image);
            headerTitle = (TextView) convertView.findViewById(R.id.ind_header_title);
            headerBack = (ImageView) convertView.findViewById(R.id.ind_header_back);
            headerMenu = (ImageView) convertView.findViewById(R.id.ind_header_options_menu);
            footerShare = (ImageView) convertView.findViewById(R.id.ind_share);
            footerLike = (ImageView) convertView.findViewById(R.id.ind_like);
            footerMenu = (ImageView) convertView.findViewById(R.id.ind_menu);
            footerNoOfLikesTv = (TextView) convertView.findViewById(R.id.ind_no_of_likes);
            hashtagsTv = (TextView) convertView.findViewById(R.id.ind_hashtags);

            footerLike.setOnClickListener(this);
            footerShare.setOnClickListener(this);
            footerMenu.setOnClickListener(this);
            headerMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ind_like:
                    if (stories.get(0).getIslike() != null) {
                        if (stories.get((int) v.getTag()).getIslike() == 1) {
                            footerLike.setImageDrawable(ContextCompat.getDrawable(holder.footerLike.getContext(), R.drawable.ic_unlike));
                            stories.get((int) v.getTag()).setIslike(0);
                        } else {
                            footerLike.setImageDrawable(ContextCompat.getDrawable(holder.footerLike.getContext(), R.drawable.ic_like));
                            stories.get((int) v.getTag()).setIslike(1);
                        }
                        onNewsFlipItemClickListener.onLikeClick((int) v.getTag());
                    }
                    break;
                case R.id.ind_share:
                    onNewsFlipItemClickListener.onShareClick((int) v.getTag(), holder.newsIv);
                    break;
                case R.id.ind_header_options_menu:
                    onNewsFlipItemClickListener.onHeaderMenuClick((int) v.getTag());
                    break;
                case R.id.ind_menu:
                    onNewsFlipItemClickListener.onFooterMenuClick((int) v.getTag());
                    break;
            }
        }
    }
}
