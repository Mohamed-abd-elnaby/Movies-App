package movies.app.Cachs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import movies.app.Model.ModelDetails;
import movies.app.Model.ModelFav;
import movies.app.Model.ModelUrl;

/**
 * Created by mohamed on 01/11/16.
 */
public class FileCach extends SQLiteOpenHelper{

    public static final String All="mohamed";
    public static final String Images ="Images";
    public static final String setting="setting";
    private static final String id="1";
    SQLiteDatabase mydatabase;
    public FileCach(Context context) {
        super(context,context.getFilesDir().getPath().toString()+"/movies.db",null,3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void creat(){
        try {
            mydatabase=this.getWritableDatabase();
            mydatabase.execSQL("create table if not exists "+All+"(id number primary key,image BLOB," +
                    "overview TEXT," +
                    "release_date TEXT," +
                    "original_title TEXT," +
                    "popularity TEXT," +
                    "vote_count TEXT," +
                    "vote_average TEXT," +
                    "fav TEXT )");
            mydatabase.close();
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            mydatabase.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
            mydatabase.close();
        }
    }

   public void insert(String image,String id){
       creat();
       try {
           ContentValues allvalues=new ContentValues();
           allvalues.put("image",image);
           allvalues.put("id",id);
           allvalues.put("fav","0");
           ContentValues idvalue=new ContentValues();
           idvalue.put("id",id);
           mydatabase=this.getReadableDatabase();
           long result= mydatabase.insert(All,null,allvalues);
           mydatabase.close();
           Log.d("insert",""+result);
       }catch (SQLiteAbortException ex){
           ex.printStackTrace();
           mydatabase.close();
           return;
       }
       catch (Exception ex){
           ex.printStackTrace();
           mydatabase.close();
           return;
       }




    }

    public String Getimage(String id){
        creat();
        try {
            mydatabase=this.getReadableDatabase();
            Cursor row=mydatabase.rawQuery("select image from '"+All+"' where id='"+id+"'",null);
            if(row.getCount()>0){
                row.moveToFirst();
                String image=row.getString(row.getColumnIndex("image"));
                mydatabase.close();
                return image;

            }
            else {
                mydatabase.close();
                return null;
            }
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            mydatabase.close();
            return null;

        }

        catch (Exception ex){
            ex.printStackTrace();
            mydatabase.close();
            return null;
        }

    }

   public boolean update(String id, String fav){
       try {

           ContentValues contentValues=new ContentValues();
           contentValues.put("fav",fav);
           mydatabase=this.getReadableDatabase();
           mydatabase.update(All,contentValues,"id="+id,null);
           mydatabase.close();

           return true;

       }catch (SQLiteAbortException ex){
           ex.printStackTrace();
           mydatabase.close();

           return false;
       }
       catch (Exception ex){
           ex.printStackTrace();
           mydatabase.close();

           return false;
       }


   }

    public boolean updateall(String id, String overview,String release_date,String original_title,String popularity,String vote_count,String vote_average){
        try {

            ContentValues contentValues=new ContentValues();
            contentValues.put("overview",overview);
            contentValues.put("release_date",release_date);
            contentValues.put("original_title",original_title);
            contentValues.put("popularity",popularity);
            contentValues.put("vote_count",vote_count);
            contentValues.put("vote_average",vote_average);
            mydatabase=this.getReadableDatabase();
            mydatabase.update(All,contentValues,"id="+id,null);
            mydatabase.close();

            return true;
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            mydatabase.close();

            return false;
        }
        catch (Exception ex){
            ex.printStackTrace();
            mydatabase.close();

            return false;
        }

    }

    public ModelDetails getdata(String id){
        try {
            mydatabase = this.getReadableDatabase();
            Cursor row = mydatabase.rawQuery("select id,overview,release_date,original_title,popularity,vote_count,vote_average from '" + All + "' where id='" + id + "'", null);
            row.moveToFirst();
            if (row.getCount() > 0&&(row.getString(row.getColumnIndex("overview")))!=null) {
                row.moveToFirst();
                String overview = row.getString(row.getColumnIndex("overview"));
                String release_date = row.getString(row.getColumnIndex("release_date"));
                String original_title = row.getString(row.getColumnIndex("original_title"));
                String popularity = row.getString(row.getColumnIndex("popularity"));
                String vote_count = row.getString(row.getColumnIndex("vote_count"));
                String vote_average = row.getString(row.getColumnIndex("vote_average"));
                ModelDetails modelDetails = new ModelDetails();
                modelDetails.setOverview(overview);
                modelDetails.setRelease_date(release_date);
                modelDetails.setOriginal_title(original_title);
                modelDetails.setPopularity(popularity);
                modelDetails.setVote_average(vote_average);
                modelDetails.setVote_count(vote_count);
                mydatabase.close();
                return modelDetails;
            } else {
                mydatabase.close();
                return null;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            mydatabase.close();

            return null;
        }
    }

    public String getFav(String id){
        try {
            mydatabase=this.getReadableDatabase();
            Cursor row=mydatabase.rawQuery("select fav from '"+All+"' where id='"+id+"'",null);
            if(row.getCount()>0){
                row.moveToFirst();
                String idfav=row.getString(row.getColumnIndex("fav"));
                mydatabase.close();
                return idfav;

            }
            else {
                mydatabase.close();
                return "no";
            }
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            mydatabase.close();
            return "no";

        }

        catch (Exception ex){
            ex.printStackTrace();
            mydatabase.close();
            return null;
        }

    }

    public List<ModelFav> getAllFav(){

        List<ModelFav>ListFav=new ArrayList<ModelFav>();
        try{

            mydatabase=this.getReadableDatabase();
            Cursor row=mydatabase.rawQuery("select id,image from '"+All+"' where fav=1",null);
            if(row.getCount()>0){
                row.moveToFirst();
                for(int i=0;i<row.getCount();i++){
                    String id=row.getString(row.getColumnIndex("id"));
                    String image=row.getString(row.getColumnIndex("image"));
                    ModelFav modelFav=new ModelFav();
                    modelFav.setId(id);
                    modelFav.setImage(image);
                    ListFav.add(modelFav);
                    row.moveToNext();
                }
                mydatabase.close();



            }
            return ListFav;

        }catch (Exception ex){

            ex.printStackTrace();
            mydatabase.close();
            return null;

        }

    }

    public void creat_table_setting(){
        try {
            mydatabase=this.getWritableDatabase();
            mydatabase.execSQL("create table if not exists "+setting+"(id number primary key,Year TEXT,TypeSearch Text,Language TEXT,NumberPage TEXT,SizeImage TEXT)");
            ContentValues contentValues=new ContentValues();
            contentValues.put("id",id);
            contentValues.put("Year","2016");
            contentValues.put("TypeSearch","popularity.desc");
            contentValues.put("Language","en-US");
            contentValues.put("NumberPage","1");
            contentValues.put("SizeImage","185");
            mydatabase.insert(setting,null,contentValues);
            mydatabase.close();
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            mydatabase.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
            mydatabase.close();
        }
    }

    public boolean updateSetting_year(String year){
        try {

            ContentValues contentValues=new ContentValues();
            contentValues.put("Year",year);
            mydatabase=this.getReadableDatabase();
            mydatabase.update(setting,contentValues,"id="+id,null);
            return true;
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            return false;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateSetting_type(String type){
        try {

            ContentValues contentValues=new ContentValues();
            contentValues.put("TypeSearch",type);
            mydatabase=this.getReadableDatabase();
            mydatabase.update(setting,contentValues,"id="+id,null);
            return true;
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            return false;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateSetting_language(String language){
        try {

            ContentValues contentValues=new ContentValues();
            contentValues.put("Language",language);
            mydatabase=this.getReadableDatabase();
            mydatabase.update(setting,contentValues,"id="+id,null);
            return true;
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            return false;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateSetting_numberPage(String number_page){
        try {

            ContentValues contentValues=new ContentValues();
            contentValues.put("NumberPage",number_page);
            mydatabase=this.getReadableDatabase();
            mydatabase.update(setting,contentValues,"id="+id,null);
            return true;
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            return false;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateSetting_SizeImage(String SizeImage){
        try {

            ContentValues contentValues=new ContentValues();
            contentValues.put("SizeImage",SizeImage);
            mydatabase=this.getReadableDatabase();
            mydatabase.update(setting,contentValues,"id="+id,null);
            return true;
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            return false;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public ModelUrl getsetting(){
        try {

            mydatabase=this.getReadableDatabase();
            Cursor row = mydatabase.rawQuery("select TypeSearch,Language,NumberPage,SizeImage,Year from '" + setting + "'", null);
            ModelUrl modelUrl=new ModelUrl();
            if(row.getCount()>0){
                row.moveToFirst();
                modelUrl.setTypeOfSearch(row.getString(row.getColumnIndex("TypeSearch")));
                modelUrl.setLanguage(row.getString(row.getColumnIndex("Language")));
                modelUrl.setPages(row.getString(row.getColumnIndex("NumberPage")));
                modelUrl.setSizeOfImage(row.getString(row.getColumnIndex("SizeImage")));
                modelUrl.setYear(row.getString(row.getColumnIndex("Year")));
            }
            mydatabase.close();
            return modelUrl;

        }catch (Exception ex){
            ex.printStackTrace();
            mydatabase.close();
            return null;
        }
    }

    public void creat_images_tables(){
        try {
            mydatabase=this.getWritableDatabase();
            mydatabase.execSQL("create table if not exists "+Images+"(id_image number primary key,id TEXT,image BLOB)");
            mydatabase.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void insert_images(String id,String id_image,String image){
        try {
            ContentValues allvalues=new ContentValues();
            allvalues.put("id_image",id_image);
            allvalues.put("id",id);
            allvalues.put("image",image);
            mydatabase=this.getReadableDatabase();
            long result= mydatabase.insert(Images,null,allvalues);
            mydatabase.close();
            Log.d("insert",""+result);
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            mydatabase.close();
            return;
        }
        catch (Exception ex){
            ex.printStackTrace();
            mydatabase.close();
            return;
        }

    }

    public String get_image(String id_image){
        try {
            mydatabase=this.getReadableDatabase();
            Cursor row=mydatabase.rawQuery("select image from '"+Images+"' where id_image='"+id_image+"'",null);
            if(row.getCount()>0){
                row.moveToFirst();
                String image=row.getString(row.getColumnIndex("image"));
                mydatabase.close();
                return image;

            }
            else {
                mydatabase.close();
                return null;
            }
        }catch (SQLiteAbortException ex){
            ex.printStackTrace();
            mydatabase.close();
            return null;

        }

        catch (Exception ex){
            ex.printStackTrace();
            mydatabase.close();
            return null;
        }
    }


}