package com.mantra.ionnews.adapters;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.interfaces.OnProfileGridItemClickListener;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.models.responses.StoriesResponse;
import com.mantra.ionnews.ui.customui.SquareRelativeLayout;
import com.mantra.ionnews.utils.PicasoImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;


/**
 * Created by TaNMay on 06/04/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Story> storyList;
    private List<StoriesResponse> storiesResponses;
    private List<StoriesResponse> filteredStoriesResponses;
    private OnProfileGridItemClickListener onProfileGridItemClickListener;
    private boolean isLikeView = false;

    public SearchAdapter(List<StoriesResponse> storiesResponses, List<Story> storyList, OnProfileGridItemClickListener onProfileGridItemClickListener) {
        super();
        this.storyList = storyList;
        this.storiesResponses = storiesResponses;
        filteredStoriesResponses = storiesResponses;
        this.onProfileGridItemClickListener = onProfileGridItemClickListener;
        isLikeView = storiesResponses == null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileGridItemClickListener.onProfileGridItemClick(v, isLikeView);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Story story = null;
        String titleStr = null;
        if (isLikeView) {
            story = storyList.get(position);
            titleStr = story.getTitle();
        } else {
            story = storiesResponses.get(position).getCategoryStories().get(0);
            titleStr = storiesResponses.get(position).getCategoryTitle();
            viewHolder.news.setText(story.getTitle());
        }

        Typeface typeface = Typeface.createFromAsset(viewHolder.title.getContext().getAssets(), "OpenSans-Bold.ttf");
        viewHolder.title.setText(titleStr);
        viewHolder.title.setTypeface(typeface);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
  //              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.image.setImageDrawable(new BitmapDrawable(viewHolder.image.getContext().getResources(), bitmap));
  //             } else {
                    viewHolder.image.setBackgroundDrawable(new BitmapDrawable(viewHolder.item.getContext().getResources(), bitmap));
 //               }
                setUpTranslucentView(viewHolder.item, viewHolder.translucentView);
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
            }
        };
        try {
            if (story.getImage() != null && !story.getImage().isEmpty()) {
                /*OkHttpClient client = new OkHttpClient();
                Picasso.Builder builder = new Picasso.Builder(viewHolder.image.getContext());
                builder.downloader(new OkHttp3Downloader(client))
                        .build()
                        .load(story.getImage())
                        .into(viewHolder.image);*/

                //        Picasso.with(viewHolder.item.getContext())
                //              .load(story.getImage())
                //                .into(target);


                PicasoImageLoader.with(viewHolder.image.getContext())
                        .load(story.getImage())
                        .into(viewHolder.image);
                viewHolder.image.setTag(target);
            }
        }catch (Exception e)
        {

        }

    }

    private void setUpTranslucentView(final View mainView, final View translucentView) {
        ViewTreeObserver viewTreeObserver = mainView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        mainView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    else mainView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int viewHeight = mainView.getHeight();

                    ViewGroup.LayoutParams params = translucentView.getLayoutParams();
                    params.height = (int) (viewHeight * 0.25);
                    translucentView.setLayoutParams(params);
                    translucentView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
       /* if (isLikeView) {
            //added to handle null in notifiychange
            if (storyList != null) {
                return storyList.size();
            } else {
                return 0;
            }

        } else {
            if (storiesResponses != null) {
                return storiesResponses.size();
            } else {
                return 0;
            }
        }*/
        return storiesResponses.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {

        public SquareRelativeLayout item;
        public TextView title, news;
        public ImageView options, image;
        public View translucentView;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (SquareRelativeLayout) itemView.findViewById(R.id.ipg_entire_item);
            image = (ImageView) itemView.findViewById(R.id.ipg_image_search);
            title = (TextView) itemView.findViewById(R.id.ipg_title);
            news = (TextView) itemView.findViewById(R.id.ipg_news);
            options = (ImageView) itemView.findViewById(R.id.ipg_options);
            translucentView = itemView.findViewById(R.id.ipg_translucent_overlay);
        }
    }


   /*

   @Override
    public Filter getFilter() {

        return storiesFilter;
    }


    private Filter storiesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterPattern = constraint.toString().toLowerCase().trim();
            FilterResults results = new FilterResults();


            if (constraint == null || constraint.length() == 0) {
                results.values = filteredStoriesResponses;
                results.count = filteredStoriesResponses.size();

            } else {

                List<StoriesResponse> filteredList = new ArrayList<>();
                for (StoriesResponse item : storiesResponses) {
                    if (item.getCategoryTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
                results.values = filteredList;
            }


            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            storiesResponses = ( List<StoriesResponse>) results.values;
            notifyDataSetChanged();
        }



    };*/




}
