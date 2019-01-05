package com.mantra.ionnews.adapters;

import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.interfaces.OnProfileGridItemClickListener;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.models.responses.StoriesResponse;
import com.mantra.ionnews.utils.PicasoImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by TaNMay on 06/04/17.
 */

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {

    private List<Story> storyList;
    private List<StoriesResponse> storiesResponses;
    private OnProfileGridItemClickListener onProfileGridItemClickListener;
    private boolean isLikeView = false;
    public boolean isClickedFirstTime = true;
    String titleStr = null;



    public HomeFragmentAdapter(List<StoriesResponse> storiesResponses, List<Story> storyList, OnProfileGridItemClickListener onProfileGridItemClickListener) {
        super();
        this.storyList = storyList;
        this.storiesResponses = storiesResponses;
        this.onProfileGridItemClickListener = onProfileGridItemClickListener;
        isLikeView = storiesResponses == null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_layout, viewGroup, false);
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
        if (isLikeView) {
            story = storyList.get(position);
            titleStr = story.getTitle();
        } else {
            story = storiesResponses.get(position).getCategoryStories().get(0);
            titleStr = storiesResponses.get(position).getCategoryTitle();
            // viewHolder.title.setText(story.getTitle());
        }

        Typeface typeface = Typeface.createFromAsset(viewHolder.title.getContext().getAssets(), "OpenSans-Bold.ttf");
        viewHolder.title.setText(story.getTitle());
        viewHolder.title.setTypeface(typeface);

        viewHolder.listTitle.setText(titleStr);
        viewHolder.listTitle.setTypeface(typeface);
        viewHolder.details.setText(story.getContent());
        viewHolder.details.setTypeface(typeface);
        viewHolder.updateDate.setText(story.getUpdatedAt());
        viewHolder.updateDate.setTypeface(typeface);


        if(titleStr!=null)
        {
            if(titleStr.equalsIgnoreCase("automotive"))
            {
                viewHolder.headerImage.setImageResource(R.drawable.electronica);
            }
            else if(titleStr.replaceAll("\\s","").equalsIgnoreCase("businessnews"))
            {
                viewHolder.headerImage.setImageResource(R.drawable.business_news);
            }
            else if(titleStr.equalsIgnoreCase("processors"))
            {
                viewHolder.headerImage.setImageResource(R.drawable.processor);

            }
            else if(titleStr.replaceAll("\\s","").equalsIgnoreCase("newproducts"))
            {
                viewHolder.headerImage.setImageResource(R.drawable.newproduct);

            }
        }

      /*
        viewHolder.title1.setText(titleStr);
        viewHolder.title1.setTypeface(typeface);*/

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.image.setImageDrawable(new BitmapDrawable(viewHolder.image.getContext().getResources(), bitmap));
//                } else {
//                    viewHolder.image.setBackgroundDrawable(new BitmapDrawable(viewHolder.item.getContext().getResources(), bitmap));
//                }
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
            }
        };
        try
        {
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

       viewHolder.linearLayout1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               /*if(isClickedFirstTime)
               {
                   isClickedFirstTime = false;
                   viewHolder.getOptions.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                   viewHolder.storylinearLayout.setVisibility(View.VISIBLE);
                   *//* viewHolder.linearLayout1.setVisibility(View.GONE);
                  viewHolder.linearLayout2.setVisibility(View.VISIBLE);
                   viewHolder.updateDate.setVisibility(View.VISIBLE);*//*


               }
               else
               {
                   isClickedFirstTime = true;
                   viewHolder.getOptions.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                   viewHolder.storylinearLayout.setVisibility(View.GONE);
                    *//* viewHolder.linearLayout1.setVisibility(View.VISIBLE);
                 viewHolder.linearLayout2.setVisibility(View.GONE);
                   viewHolder.updateDate.setVisibility(View.GONE);*//*

               }*/


                   if(viewHolder.listTitle.getText().toString().equalsIgnoreCase("automotive"))
                   {
                       viewHolder.headerImage.setImageResource(R.drawable.electronica_hl);
                   }
                   else if(viewHolder.listTitle.getText().toString().replaceAll("\\s","").equalsIgnoreCase("businessnews"))
                   {
                       viewHolder.headerImage.setImageResource(R.drawable.business_news_hl);
                   }
                   else if(viewHolder.listTitle.getText().toString().equalsIgnoreCase("processors"))
                   {
                       viewHolder.headerImage.setImageResource(R.drawable.processor_hl);

                   }
                   else if(viewHolder.listTitle.getText().toString().replaceAll("\\s","").equalsIgnoreCase("newproducts"))
                   {
                       viewHolder.headerImage.setImageResource(R.drawable.newproduct_hl);

                   }




               viewHolder.storylinearLayout.setVisibility(View.VISIBLE);
               viewHolder.view.setVisibility(View.VISIBLE);
               viewHolder.updateDate.setVisibility(View.VISIBLE);
               viewHolder.listTitle.setTextColor(Color.parseColor("#ee0290"));


           }
       });

        viewHolder.closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(viewHolder.listTitle.getText().toString().equalsIgnoreCase("automotive"))
                {
                    viewHolder.headerImage.setImageResource(R.drawable.electronica);
                }
                else if(viewHolder.listTitle.getText().toString().replaceAll("\\s","").equalsIgnoreCase("businessnews"))
                {
                    viewHolder.headerImage.setImageResource(R.drawable.business_news);
                }
                else if(viewHolder.listTitle.getText().toString().equalsIgnoreCase("processors"))
                {
                    viewHolder.headerImage.setImageResource(R.drawable.processor);

                }
                else if(viewHolder.listTitle.getText().toString().replaceAll("\\s","").equalsIgnoreCase("newproducts"))
                {
                    viewHolder.headerImage.setImageResource(R.drawable.newproduct);

                }




                viewHolder.storylinearLayout.setVisibility(View.GONE);
                viewHolder.view.setVisibility(View.GONE);
                viewHolder.updateDate.setVisibility(View.GONE);
                viewHolder.listTitle.setTextColor(Color.parseColor("#000000"));


            }
        });



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
        if (isLikeView) {
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
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title,title1, news,listTitle,details,updateDate;
        public ImageView options, image;
        public LinearLayout storylinearLayout;
        public LinearLayout linearLayout1,linearLayout2;
        public  ImageView closeIcon,headerImage;
        public  View view;



        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ipg_image);
            headerImage = (ImageView) itemView.findViewById(R.id.ipg_image1);
            closeIcon = (ImageView) itemView.findViewById(R.id.expandable_icon2);
            title = (TextView) itemView.findViewById(R.id.ipg_title);
            listTitle = (TextView) itemView.findViewById(R.id.listTitle1);
            details = (TextView) itemView.findViewById(R.id.ipg_details);
            news = (TextView) itemView.findViewById(R.id.ipg_news);
            storylinearLayout = (LinearLayout) itemView.findViewById(R.id.story_layout);
            linearLayout1 = (LinearLayout) itemView.findViewById(R.id.L1);
            view = (View) itemView.findViewById(R.id.empty_view);
            updateDate = (TextView) itemView.findViewById(R.id.ipg_update_date);

           /* title1 = (TextView) itemView.findViewById(R.id.ipg_title1);
            updateDate = (TextView) itemView.findViewById(R.id.ipg_update_date);
            linearLayout2 = (LinearLayout) itemView.findViewById(R.id.L2);*/
        }
    }


}
