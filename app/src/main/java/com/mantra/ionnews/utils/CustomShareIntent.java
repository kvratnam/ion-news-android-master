package com.mantra.ionnews.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.mantra.ionnews.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by TaNMay on 06/04/17.
 */

public class CustomShareIntent {

    private static final String IMAGE_PATH = "";
    private static final String IMAGE_NAME = "temp.jpg";

    protected ProgressDialog mProgressDialog = null;

    private Context context;
    private String crawlUrl;
    private String text;
    private String imageUrl;
    private ImageView imageView = null;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            hideProgressDialog();
            prepareShareIntentBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            hideProgressDialog();
            prepareShareIntentBitmap(null);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            showProgressDialog(context.getString(R.string.loading));
        }
    };

    public CustomShareIntent(Context context, ImageView imageView, String crawlUrl, String text) {
        this.crawlUrl = crawlUrl;
        this.text = text;
        this.context = context;
        this.imageView = imageView;
    }

    public CustomShareIntent(Context context, String imageUrl, String crawlUrl, String text) {
        this.crawlUrl = crawlUrl;
        this.text = text;
        this.context = context;
        this.imageUrl = imageUrl;
    }

    private void prepareShareIntentBitmap(Bitmap bitmap) {
        Uri bmpUri = null;
        bmpUri = getLocalBitmapUri(bitmap);
        sendIntent(bmpUri);
    }

    public void share() {
        if (imageView != null) {
            prepareShareIntent();
        } else {
            if (imageUrl != null) {
                Picasso.with(context).load(imageUrl).into(target);
            } else {
                prepareShareIntentBitmap(null);
            }
        }
    }

    private void startShareIntent() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, text);
        share.setType("*/*");
        if (imageUrl != null) {
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + File.separator + IMAGE_PATH + IMAGE_NAME));
        }

        context.startActivity(share);
    }

    // Gets the image URI and setup the associated share intent to hook into the provider
    public void prepareShareIntent() {
        Uri bmpUri = null;
        bmpUri = getLocalBitmapUri(imageView); // see previous remote images section
        // Construct share intent as described above based on bitmap
        sendIntent(bmpUri);
    }

    private void sendIntent(Uri bmpUri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "Share Story"));
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        Bitmap bmp = null;
        if (imageView != null) {
            // Extract Bitmap from ImageView drawable
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            } else {
                return null;
            }
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        // Store image to default external storage directory
        Uri bmpUri = null;
        if (bmp == null)
            return null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(context, null, message, true, false, null);
        } else {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }
}
