package com.yffsc.myselectortool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yffsc.myselectortool.WheelSelector.DataPickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    List<Map<String, String>> data;
    Map<String, String> map;
    private void init(){
        data=new ArrayList<>();
        for(int i=0;i<5; i++){
            map=new HashMap<>();
            map.put("key:"+(i+3),"value:"+i);
            data.add(map);
        }

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPickerDialog.Builder builder = new DataPickerDialog.Builder(MainActivity.this);
                //List<String> data = Arrays.asList(new String[]{"a", "b", "c", "d", "e", "f", "g", "h","i","j","k","l","m"});
                DataPickerDialog dialog = builder.setMapData(data,null).setSelection("value:3","value:4")
                        .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemKey1, String itemValue1, int itemId1,String itemKey2, String itemValue2, int itemId2) {
                        Toast.makeText(MainActivity.this, itemKey1+"-" + itemValue1 + "-" + itemId1+"  ::  "+itemKey2+"-" + itemValue2 + "-" + itemId2, Toast.LENGTH_LONG).show();
                        Log.i("ii",itemKey1+"-" + itemValue1 + "-" + itemId1+"  ::  "+itemKey2+"-" + itemValue2 + "-" + itemId2);
                    }
                })
                        .setCyclen(true)
                        .create();
                dialog.show();
            }
        });
    }
}
