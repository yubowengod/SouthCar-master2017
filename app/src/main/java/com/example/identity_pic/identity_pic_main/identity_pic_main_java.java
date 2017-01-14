package com.example.identity_pic.identity_pic_main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.god.southcar.R;
import com.example.identity_pic.identity_num.one_door.identity_1_activity;
import com.example.identity_pic.identity_num.two_door.identity_2_activity;
import com.example.identity_pic.identity_num.three_door.identity_3_activity;
import com.example.identity_pic.identity_num.four_door.identity_4_activity;
import com.example.test_activity;

import java.util.ArrayList;

/**
 * Created by GOD on 2016/10/19.
 */
public class identity_pic_main_java extends AppCompatActivity {

    private  ArrayList<String> leibie = new ArrayList<String>(){{add("第1类缝隙"); add("第2类缝隙");add("第3类缝隙");add("第4类缝隙");add("测试");}};

    private ArrayList<Integer> leibie_pic = new ArrayList<Integer>(){{add(R.mipmap.tittle_gap_1); add(R.mipmap.tittle_gap_2);add(R.mipmap.tittle_gap_3);add(R.mipmap.tittle_gap_4);add(R.mipmap.tittle_gap_test);}};


    public static String identity_pic_main_java_fengxileibie = "";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identity_pic_main);

        ListView listView = (ListView) findViewById(R.id.identity_pic_main_listview);

        listView.setAdapter(new identity_pic_main_java_adapter(identity_pic_main_java.this,leibie,leibie_pic));

//        listView.setAdapter(new identity_pic_main_java_adapter(identity_pic_main_java.this,null,leibie_pic));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                {
                    identity_pic_main_java_fengxileibie = "1";
                    Intent intent = new Intent(identity_pic_main_java.this,identity_1_activity.class);
                    startActivity(intent);
                }
                if (i==1)
                {
                    identity_pic_main_java_fengxileibie = "2";
                    Intent intent = new Intent(identity_pic_main_java.this,identity_2_activity.class);
                    startActivity(intent);
                }
                if (i==2)
                {
                    identity_pic_main_java_fengxileibie = "3";

                    Intent intent = new Intent(identity_pic_main_java.this,identity_3_activity.class);
                    startActivity(intent);
                }
                if (i==3)
                {
                    Intent intent = new Intent(identity_pic_main_java.this,identity_4_activity.class);
                    startActivity(intent);
                }
                if (i==4)
                {
                    identity_pic_main_java_fengxileibie = "5";
                    Intent intent = new Intent(identity_pic_main_java.this,test_activity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
