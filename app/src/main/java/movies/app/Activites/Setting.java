package movies.app.Activites;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.ContactsContract;
import android.support.v4.app.AppLaunchChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import movies.app.Application.ApplicationClass;
import movies.app.Cachs.FileCach;
import movies.app.Model.ModelUrl;
import movies.app.R;

/**
 * Created by mohamed on 19/11/16.
 */

public class Setting extends Activity {

    ArrayList<String>typylist,yearlist;
    ArrayAdapter<String> typeadapter,yearadapter;
    public ImageButton update_typesearch,update_page_number,update_language,update_sizeofimage,update_year;
    public EditText language,pagenumber,sizeofimage;
    FileCach fileCach;


    Spinner type_search,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        setTitle("Setting");
        intionlizecommponent();
        insertItems();
        try {
            fileCach=new FileCach(ApplicationClass.getInstance().getApplicationContext());
            ModelUrl modelUrl=fileCach.getsetting();
            int typepostion=-1;
            int yearpostion=-1;
            for(int i=0;i<typylist.size();i++){
                if(typylist.get(i).equals(modelUrl.getTypeOfSearch())){
                    typepostion=i;
                }
            }
            if(typepostion!=-1){
                type_search.setSelection(typepostion);
            }
            for(int i=0;i<yearlist.size();i++){
                if(yearlist.get(i).equals(modelUrl.getYear())){
                    yearpostion=i;
                }
            }
            if(yearpostion!=-1){
                year.setSelection(yearpostion);
            }
            language.setText(modelUrl.getLanguage());
            pagenumber.setText(modelUrl.getPages());
            sizeofimage.setText(modelUrl.getSizeOfImage());
            intionlizelisener();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    void intionlizelisener(){
        update_page_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=pagenumber.getText().toString();
                if(fileCach.updateSetting_numberPage(number))
                    Toast.makeText(Setting.this,"PageNumber is updated successfuly",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Setting.this,"PageNumber is updated Failed",Toast.LENGTH_LONG).show();

            }
        });
        update_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lang=language.getText().toString();
                if(fileCach.updateSetting_language(lang))
                    Toast.makeText(Setting.this,"Language is updated Successfuly",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Setting.this,"Language is updated Failed",Toast.LENGTH_LONG).show();


            }
        });
        update_typesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=type_search.getSelectedItem().toString();
                if(fileCach.updateSetting_type(type))
                    Toast.makeText(Setting.this,"Type is updated Successfuly",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Setting.this,"Type is updated Failed",Toast.LENGTH_LONG).show();


            }
        });
        update_sizeofimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size=sizeofimage.getText().toString();
                if(fileCach.updateSetting_SizeImage(size))
                    Toast.makeText(Setting.this,"SizeOfImage is updated Successfuly",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Setting.this,"SizeOfImage is updated Failed",Toast.LENGTH_LONG).show();


            }
        });
        update_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value=year.getSelectedItem().toString();
                if(fileCach.updateSetting_year(value))
                    Toast.makeText(Setting.this,"Year is updated Successfuly",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Setting.this,"Year is updated Failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    void intionlizecommponent(){
        year=(Spinner)findViewById(R.id.year);
        update_year=(ImageButton)findViewById(R.id.update_year);
        typylist=new ArrayList<>();
        yearlist=new ArrayList<>();
        typeadapter=new ArrayAdapter<String>(Setting.this,android.R.layout.simple_list_item_1,typylist);
        yearadapter=new ArrayAdapter<String>(Setting.this,android.R.layout.simple_list_item_1,yearlist);
        update_language=(ImageButton)findViewById(R.id.update_language);
        update_page_number=(ImageButton)findViewById(R.id.update_pagenumber);
        update_sizeofimage=(ImageButton)findViewById(R.id.update_sizeofimage);
        update_typesearch=(ImageButton)findViewById(R.id.update_type);
        type_search=(Spinner) findViewById(R.id.type_of_search);
        sizeofimage=(EditText)findViewById(R.id.sizeofimage);
        language=(EditText)findViewById(R.id.language);
        pagenumber=(EditText)findViewById(R.id.page_number);
    }

    void insertItems(){

        typylist.add("popularity.asc");
        typylist.add("popularity.desc");
        typylist.add("release_date.asc");
        typylist.add("release_date.desc");
        typylist.add("vote_average.asc");
        typylist.add("vote_average.desc");
        typylist.add("vote_count.asc");
        typylist.add("vote_count.desc");

        type_search=(Spinner)findViewById(R.id.type_of_search);
        type_search.setAdapter(typeadapter);
        Calendar calendar = Calendar.getInstance();
        int yearcount = calendar.get(Calendar.YEAR);

        for(int i=100;i>0;i--){
            String value=String.valueOf(yearcount);
            yearlist.add(value);
            yearcount=yearcount-1;

        }
        year.setAdapter(yearadapter);

    }


}
