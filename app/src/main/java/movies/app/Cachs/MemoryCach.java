package movies.app.Cachs;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;


/**
 * Created by mohamed on 01/11/16.
 */

public class MemoryCach {



    private final static int maxmemory=(int)Runtime.getRuntime().maxMemory()/1024;
    private final static int cachsize=maxmemory/8;

    private LruCache<String, Bitmap> memorycach = new LruCache<String, Bitmap>(cachsize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {

            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
        }
    };

    public void addToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memorycach.put(key, bitmap);

        }

    }

    public Bitmap getBitmapFromMemCache(String key) {

        return memorycach.get(key);
    }

    public void clearCach(){
        memorycach.evictAll();
    }


}
