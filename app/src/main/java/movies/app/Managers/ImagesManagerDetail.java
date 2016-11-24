package movies.app.Managers;

import android.content.Context;
import android.database.sqlite.SQLiteAbortException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import movies.app.Application.ApplicationClass;
import movies.app.Cachs.FileCach;
import movies.app.Cachs.MemoryCach;
import movies.app.NetWork.InternetConnection;
import movies.app.NetWork.Networkconnection;
import movies.app.R;

import static movies.app.Fragments.FragmentList.DDEFAULT;
import static movies.app.Fragments.FragmentList.FAV;
import static movies.app.NetWork.Networkconnection.Images_details;

/**
 * Created by mohamed on 04/11/16.
 */

public class ImagesManagerDetail {
    ExecutorService executorService;
    private final static int SystemCors=Runtime.getRuntime().availableProcessors();
    private final static int coresToThread=SystemCors-1;
    private static ImagesManagerDetail instince;
    MemoryCach memoryCach=new MemoryCach();
    Handler handler;
    Context context;
    String type;
    RelativeLayout relativeLayout;
    Map<ImageView ,String> imageViewMap= Collections.synchronizedMap(new WeakHashMap<ImageView,String>());
    String id;

    ImagesManagerDetail(){

    }

    public static ImagesManagerDetail getInstince() {
        if(instince==null){
            instince=new ImagesManagerDetail();
            instince.executorService= Executors.newFixedThreadPool(coresToThread);
            instince.handler=new Handler();

        }
        return instince;
    }

    public void diplayimage(Context context, ImageView imageView, ProgressBar progressBar,RelativeLayout relativeLayout,String url,String id){
        instince.relativeLayout=relativeLayout;
        this.type=type;
        instince.context=context;
       if(progressBar!=null){
           progressBar.setVisibility(View.VISIBLE);
       }
        imageViewMap.put(imageView,url);
        ThreadsofLooding(imageView,url,progressBar,id);

   }

    void ThreadsofLooding(ImageView imageView,String url,ProgressBar progressBar,String id){

        Hold hold=new Hold(imageView,url,progressBar,id);
        executorService.execute(new LoodingImageThread(hold));

    }

    class  LoodingImageThread implements Runnable {
        Hold hold;

        LoodingImageThread(Hold hold) {
            this.hold = hold;
        }

        Bitmap bitmap;
        @Override
        public void run() {


            try {

                    if (Imagereused(hold))
                        return;

                    bitmap= memoryCach.getBitmapFromMemCache(hold.id+String.valueOf(hold.url.hashCode()));
                    if (bitmap != null) {
                        handler.post(new displayimage(bitmap, hold));
                        return;
                    }

                    FileCach fileCach = new FileCach(ApplicationClass.getInstance().getApplicationContext());
                    String image = fileCach.get_image(hold.id+String.valueOf(hold.url.hashCode()));
                    bitmap = decodingImage(image);
                    if (bitmap != null) {
                        memoryCach.addToMemoryCache(hold.id+String.valueOf(hold.url.hashCode()), bitmap);
                        handler.post(new displayimage(bitmap, hold));
                        return;
                    } else{

                        if (InternetConnection.ifNetworkconnected()) {
                            Networkconnection.StartDownloadImage(hold.url,hold.id,Images_details);
                            fileCach = new FileCach(ApplicationClass.getInstance().getApplicationContext());
                            image = fileCach.get_image(hold.id+String.valueOf(hold.url.hashCode()));
                            bitmap = decodingImage(image);
                            if (bitmap != null) {
                                memoryCach.addToMemoryCache(hold.id+String.valueOf(hold.url.hashCode()), bitmap);
                            }
                        }
                    }
                    if (Imagereused(hold))
                        return;

                displayimage displayimage = new displayimage(bitmap, hold);
                handler.post(displayimage);
            }catch (SQLiteAbortException E){
                E.printStackTrace();
                displayimage displayimage = new displayimage(null, hold);
                handler.post(displayimage);

            }
            catch (Throwable throwable){
                displayimage displayimage = new displayimage(null, hold);
                handler.post(displayimage);
                if(throwable instanceof OutOfMemoryError){
                    memoryCach.clearCach();
                }
            }

        }
    }

    Bitmap decodingImage(String image){
        try {
            if(image!=null){
                byte []bytes= Base64.decode(image,Base64.DEFAULT);
                Bitmap bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                int width=relativeLayout.getWidth();
                int height=relativeLayout.getHeight();
                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width,
                        height, true);
                return newBitmap;

            }
            else {
                return null;
            }

        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }



    }

    class displayimage implements Runnable{


        Hold hold;
        Bitmap bitmap;
        displayimage(Bitmap bitmap,Hold hold){
            this.hold=hold;
            this.bitmap=bitmap;
        }
        @Override
        public void run() {
            if(hold.progressBar!=null){
                hold.progressBar.setVisibility(View.GONE);
            }
            if(bitmap!=null){
                hold.imageView.setImageBitmap(bitmap);
            }
            else {

                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.placeholder);

                hold.imageView.setImageBitmap(bitmap);

            }
        }
    }

    boolean Imagereused(Hold hold){

        String mapUrl=imageViewMap.get(hold.imageView);

        if(mapUrl==null||!mapUrl.equals(hold.id)){
            return false;

        }
        else {
            return false;

        }




    }

    public class Hold{
        String id;
        ImageView imageView;
        ProgressBar progressBar;
        String url;

        public Hold(ImageView imageView,String url,ProgressBar progressBar,String id){
            this.imageView=imageView;
            this.id=id;
            this.url=url;
            this.progressBar=progressBar;

        }
    }

}
