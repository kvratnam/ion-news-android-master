package com.mantra.ionnews.interfaces;

import android.widget.ImageView;

/**
 * Created by TaNMay on 06/04/17.
 */

public interface OnNewsFlipItemClickListener {

    void onNewsItemClick(int position, ImageView imageView);

    void onShareClick(int position, ImageView imageView);

    void onLikeClick(int position);

    void onBackClick();

    void onPaginationIndexFlip();

    void onHeaderMenuClick(int position);

    void onFooterMenuClick(int position);

    void onLastItem();
}
