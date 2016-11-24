package movies.app.NetWork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import movies.app.Application.ApplicationClass;
import movies.app.Cachs.FileCach;

/**
 * Created by mohamed on 04/11/16.
 */

public class Networkconnection {
    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    public static final String Images_details="Images_details";
    public static final String Images_main="Images";

    public static void StartDownloadImage(String url,String id ,String type) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bitmap bitmap;


        if (url != null && url != "") {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
                if (bitmap != null) {
                    String image = encodingImage(bitmap);
                    FileCach fileCach = new FileCach(ApplicationClass.getInstance().getApplicationContext());
                    if(type.equals(Images_main)){
                        fileCach.insert(image, id);
                    }
                    else if(type.equals(Images_details))
                    {
                        fileCach.insert_images(id,id+String.valueOf(url.hashCode()),image);
                    }

                }
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d("Network","image failed Download");
                return;
            }

        }
        else {
            Log.d("Null","Photo Url is null");
            return;
        }
    }

    public static String  encodingImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[]bytes=byteArrayOutputStream.toByteArray();
        String image= Base64.encodeToString(bytes,Base64.DEFAULT);
        return image;
    }

}
