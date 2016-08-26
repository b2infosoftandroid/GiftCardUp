package com.b2infosoft.giftcardup.volly;

/**
 * Created by rajesh on 7/24/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageRequest;
import com.b2infosoft.giftcardup.R;
import com.mikhaellopez.circularimageview.CircularImageView;

public class LruBitmapCache extends LruCache<String, Bitmap>
        implements ImageCache {

    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    public LruBitmapCache(Context ctx) {
        this(getCacheSize(ctx));
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

    // Returns a cache size equal to approximately three screens worth of images.
    public static int getCacheSize(Context ctx) {
        final DisplayMetrics displayMetrics = ctx.getResources().
                getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;
        // 4 bytes per pixel
        final int screenBytes = screenWidth * screenHeight * 4;

        return screenBytes * 3;
    }

    /*
    RequestQueue mRequestQueue; // assume this exists.
    ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(LruBitmapCache.getCacheSize()));
    */

    public static void loadCacheImage(Context context, final ImageView imageView, String imageUrl, String tag) {
        Cache cache = MySingleton.getInstance(context).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(imageUrl);
        if (entry != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(entry.data, 0, entry.data.length);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage());
            }
        } else {
            cache.invalidate(imageUrl, true);
            ImageRequest request = new ImageRequest(imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.e("ERROR VOLLY 1", error.getMessage() + "");
                            imageView.setImageResource(R.drawable.not_find);
                        }
                    });
            request.setTag(tag);
            MySingleton.getInstance(context).addToRequestQueue(request);
        }
    }

    public static void loadCacheImage(Context context, final CircularImageView imageView, String imageUrl, String tag) {
        Cache cache = MySingleton.getInstance(context).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(imageUrl);
        if (entry != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(entry.data, 0, entry.data.length);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage());
            }
        } else {
            cache.invalidate(imageUrl, true);
            ImageRequest request = new ImageRequest(imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.e("ERROR VOLLY 1", error.getMessage() + "");
                            imageView.setImageResource(R.drawable.not_find);
                        }
                    });
            request.setTag(tag);
            MySingleton.getInstance(context).addToRequestQueue(request);
        }
    }
}
