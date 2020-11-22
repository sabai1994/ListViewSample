package com.example.listviewsample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.listviewsample.Model.LanguageInfo;
import com.example.listviewsample.Utility.DbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ListView toDoList;
    FloatingActionButton fab;
    ArrayList<LanguageInfo> arrayList = new ArrayList<>();
    EditText txtLangName;
    EditText txtId;
    EditText txtDescription;
    EditText txtReleaseDate;
    Button btnSave;
    Button btnCancel;
    Button btnDelete;
    AlertDialog alertDialog;
    Integer tmpPosition=-9;
    LanguageAdapter adapter;
    LanguageInfo info = new LanguageInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toDoList = (ListView) findViewById(R.id.toDoList);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        /*
        String[] nameList ={"java","android","C#","C++","php","python"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item,nameList);
        toDoList.setAdapter(adapter);
        */
        //GenerateList();

        GetAllData();


        adapter = new LanguageAdapter(this,arrayList);
        toDoList.setAdapter(adapter);
        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //showpopup
                tmpPosition = position;
                showPopupDialog(arrayList.get(position));
                Toast.makeText(MainActivity.this,arrayList.get(position).getName(),Toast.LENGTH_LONG).show();
            }
        });
        fab.setOnClickListener(this);
    }
    public void GetAllData(){
        DbHelper dbHelper = new DbHelper(MainActivity.this);
        try {
            arrayList = dbHelper.GetAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void GenerateList(){
        for(int i=0;i<15;i++){
            LanguageInfo info = new LanguageInfo();
            info.setId(i+1);
            info.setName("Language"+(i+1));
            Date d = new Date();
           info.setReleaseDate(d);
            info.setDescription(info.getName()+" "+"Lorem spum");
            arrayList.add(info);

        }
    }
    private  void showPopupDialog(LanguageInfo info){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
       LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.language_view,null);
        builder.setView(view);

        alertDialog = builder.create();
        alertDialog.show();
        //Showing
        txtId = (EditText) view.findViewById(R.id.txtId);
        txtLangName = (EditText) view.findViewById(R.id.txtLangName);
        txtDescription = (EditText) view.findViewById(R.id.txtDesc);
        txtReleaseDate = (EditText) view.findViewById(R.id.txtReleaseDate);

        if(info != null){
txtId.setText(String.valueOf(info.getId()));
txtLangName.setText(info.getName());
txtDescription.setText(info.getDescription());
txtReleaseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(info.getReleaseDate()) );
        }
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button)view.findViewById(R.id.btnCancel);
        btnDelete = (Button)view.findViewById(R.id.btnDelete);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.btnSave:
            //Code to Insert
            String name = txtLangName.getText().toString();
            String desc = txtDescription.getText().toString();
            String releaseDate = txtReleaseDate.getText().toString();
            int id = Integer.parseInt(txtId.getText().toString());

            if(tmpPosition >= 0){
               info= arrayList.get(tmpPosition);
            }
            info.setId(id);
            info.setName(name);
            info.setDescription(desc);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
           try{
               info.setReleaseDate(df.parse(releaseDate));
           }catch (ParseException e)
           {
               e.printStackTrace();
           }
           //Insert
            DbHelper db = new DbHelper(this);
            if(tmpPosition <0){
                //arrayList.add(info);
                id =db.Insert(info);
                info.setId(id);
                arrayList.add(info);
            }
            else{
                db.Update(info);
            }
           adapter.notifyDataSetChanged();
           tmpPosition=-9;
           alertDialog.hide();
            break;
        case R.id.btnCancel:
            alertDialog.cancel();
            break;
        case R.id.fab:
            tmpPosition =-9;
            showPopupDialog(null);break;
        case R.id.btnDelete:
            DbHelper dbdel = new DbHelper(this);
            int deleteId = Integer.parseInt(txtId.getText().toString());
            dbdel.delete(info);
            if(deleteId <0){
                return;
            }
            else{
                if(tmpPosition >=0){
                    arrayList.remove(arrayList.get(tmpPosition));
                    adapter.notifyDataSetChanged();
                }
            }
            alertDialog.hide();
            tmpPosition=-9;
            break;
    }
    }

    public void onClick_Old(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                //Code to Insert
                String name = txtLangName.getText().toString();
                String desc = txtDescription.getText().toString();
                String releaseDate = txtReleaseDate.getText().toString();
                int id = Integer.parseInt(txtId.getText().toString());
                if(arrayList == null || arrayList.size()==0){
                    id=1;
                }
                else if(id<0){
                    id+=arrayList.get(arrayList.size()-1).getId();
                }
                LanguageInfo info = new LanguageInfo();
                if(tmpPosition >= 0){
                    info= arrayList.get(tmpPosition);
                }
                info.setId(id);
                info.setName(name);
                info.setDescription(desc);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                try{
                    info.setReleaseDate(df.parse(releaseDate));
                }catch (ParseException e)
                {
                    e.printStackTrace();
                }
                //Add to list if current position is less than 0
                if(tmpPosition <0){
                    arrayList.add(info);
                }
                adapter.notifyDataSetChanged();
                tmpPosition=-9;
                alertDialog.hide();
                break;
            case R.id.btnCancel:
                alertDialog.cancel();
                break;
            case R.id.fab:
                tmpPosition =-9;
                showPopupDialog(null);break;
            case R.id.btnDelete:
                int deleteId = Integer.parseInt(txtId.getText().toString());
                if(deleteId <0){
                    return;
                }
                else{
                    if(tmpPosition >=0){
                        arrayList.remove(arrayList.get(tmpPosition));
                        adapter.notifyDataSetChanged();
                    }
                }
                alertDialog.hide();
                tmpPosition=-9;
                break;
        }
    }
}
