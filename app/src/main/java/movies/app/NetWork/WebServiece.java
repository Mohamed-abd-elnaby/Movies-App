package movies.app.NetWork;

import android.content.Context;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import movies.app.Application.ApplicationClass;
import movies.app.Cachs.FileCach;
import movies.app.Interfaces.FragmentLisener;
import movies.app.Model.ModelUrl;
import movies.app.Model.ModelWebservice;

/**
 * Created by mohamed on 16/11/16.
 */

public class WebServiece extends Thread{
    ArrayList<ModelWebservice> Listmodel=new ArrayList<>();
    String type;
    FragmentLisener fragmentLisener;

    ModelUrl modelUrl;
    FileCach fileCach;
    Handler handler;

   public WebServiece (){
       handler=new Handler();
       fileCach=new FileCach(ApplicationClass.getInstance().getApplicationContext());
       modelUrl=fileCach.getsetting();


   }
    public void setFragmentLisener(FragmentLisener fragmentLisener) {
        this.fragmentLisener = fragmentLisener;
    }

    URL preparingURL(){
        try {
            String link="http://api.themoviedb.org/3/discover/movie?api_key="+modelUrl.getApi_key()+"&language="+modelUrl.getLanguage()+"&sort_by="+modelUrl.getTypeOfSearch()+"&include_adult=false&include_video=false&page="+modelUrl.getPages()+"&year="+modelUrl.getYear();

            URL url=new URL(link);
            return url;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

    }

    String StartConnect(URL url){

        if(url!=null){
            String result="";
            HttpURLConnection httpURLConnection=null;
            InputStream inputStream;
            StringBuilder stringBuilder;
            BufferedReader bufferedReader;

            try {
                httpURLConnection=(HttpURLConnection)url.openConnection();
                inputStream=httpURLConnection.getInputStream();
                bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                if (inputStream!=null){
                    String line;
                    stringBuilder=new StringBuilder();
                    while ((line=bufferedReader.readLine())!=null){
                        stringBuilder.append(line+"/n");
                    }
                    inputStream.close();
                    result=stringBuilder.toString();
                }

                return result;

            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
                httpURLConnection.disconnect();
                return null;
            }

            catch (Exception ex){
                httpURLConnection.disconnect();
                ex.printStackTrace();
                return null;
            }
        }
        else
            return null;


    }

    void parsing(String result){
        try {

            Listmodel=new ArrayList<ModelWebservice>();
            JSONObject dataobject=new JSONObject(result);
            JSONArray results=dataobject.getJSONArray("results");

            for(int i=0;i<results.length();i++){
                JSONObject dataindex=results.getJSONObject(i);
                if(dataindex.getString("poster_path")!="null"&&dataindex.getString("id")!=null&&dataindex.getString("id")!="null"){
                    String poster_path=modelUrl.getPhoto_url()+modelUrl.getSizeOfImage()+dataindex.getString("poster_path");
                    String id=dataindex.getString("id");

                    ModelWebservice modelWebservice=new ModelWebservice();
                    modelWebservice.setPoster_path(poster_path);
                    modelWebservice.setId(id);
                    Listmodel.add(modelWebservice);
                }
                }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    boolean saveToFile(){

        ObjectOutputStream objectOutputStream=null;
        FileOutputStream fileOutputStream=null;
        try{
            fileOutputStream= ApplicationClass.getInstance().getApplicationContext().openFileOutput(type, Context.MODE_PRIVATE);
            objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(Listmodel);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
            return true;
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
            try {
                objectOutputStream.close();
                fileOutputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;

        }
        catch (Exception ex){
            ex.printStackTrace();
            try {
                objectOutputStream.close();
                fileOutputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    ArrayList<ModelWebservice> readFromFile(){
        FileInputStream fileInputStream=null;
        ObjectInputStream objectInputStream=null;

        try {
            Log.d("readfrom file","yes");
            fileInputStream=ApplicationClass.getInstance().getApplicationContext().openFileInput(type);
            objectInputStream=new ObjectInputStream(fileInputStream);
            ArrayList<ModelWebservice>list=(ArrayList<ModelWebservice>)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return list;
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
            if(objectInputStream!=null){
                try {
                    objectInputStream.close();
                    fileInputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            Log.d("readfrom file","creached");

            return null;
        }catch (Exception ex){
            ex.printStackTrace();
            if(objectInputStream!=null){
                try {
                    objectInputStream.close();
                    fileInputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            Log.d("readfrom file","creached");

            return null;
        }


    }

    public  void mangeWebservies(){
        try {
            type=modelUrl.getTypeOfSearch()+modelUrl.getYear()+modelUrl.getLanguage()+modelUrl.getPages()+modelUrl.getSizeOfImage();
            Listmodel=readFromFile();
            if(Listmodel!=null&&Listmodel.size()>0) {
                Log.d("webservice", "list will get from file cached ");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        fragmentLisener.onWebServieceFinshed(Listmodel);

                    }
                });
            }
            else{
                Log.d("webservice","list wiil get from internet ");
                if(InternetConnection.ifNetworkconnected()){
                    String result=StartConnect(preparingURL());
                    parsing(result);
                    saveToFile();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            fragmentLisener.onWebServieceFinshed(Listmodel);
                        }
                    });
                }
                else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            fragmentLisener.onWebServieceFinshed(null);

                            Toast.makeText(ApplicationClass.getInstance().getApplicationContext(),"no Internet Connection",Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }


        }
       catch (Exception ex){
           ex.printStackTrace();
       }
    }

    @Override
    public void run() {

        mangeWebservies();
    }
}
