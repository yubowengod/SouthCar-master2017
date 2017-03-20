package com.example.identity_dc110v.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlen.photo.photopickup.presenter.PhotoPresenter;
import com.bumptech.glide.Glide;
import com.example.god.southcar.R;
import com.example.identity_dc110v.ksoap.identity_dc110v_test_mul;
import com.example.identity_pic.identity_num.one_door.identity_1_activity;
import com.example.identity_pic.identity_num.one_door.identity_num_listview_main.identity_num_listview_main_activity2;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by GOD on 2017/1/18.
 */
public class identity_dc110v_main extends AppCompatActivity {
    private int flag = 1;
    private static final int REQUEST_CODE = 0;
    private Button btn_r;
    private Button btn_m;
    private Button btn_l;
    private ImageView avatar;
    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    };
    public static PhotoPresenter mPhotoPresenter;
    private ExecutorService executorService;
    ArrayList<String> pic_path=new ArrayList<>();
    private TextView textView;

    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 2171) {
                //只要在主线程就可以处理ui
                ((TextView) identity_dc110v_main.this.findViewById(msg.arg1)).setText((String) msg.obj);
            }
        }
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identity_dc110v_main_ui);
        executorService = Executors.newFixedThreadPool(5);//开启5个线程，其实根据你的情况，一般不会超过8个
        textView=(TextView)findViewById(R.id.identity_num_listview_main_listview_item_name);
        textView.setText("DC110V识别");
        avatar=(ImageView)findViewById(R.id.identity_num_listview_main_listview_item_avatar);
        Glide.with(identity_dc110v_main.this).load(identity_1_activity.no1[0]).fitCenter().skipMemoryCache(true).into(avatar);

        btn_m=(Button)findViewById(R.id.identity_num_listview_main_listview_item_btn_mid);

        btn_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgSelConfig config = new ImgSelConfig.Builder(loader)
                        // 是否多选
                        .multiSelect(false)
                        // 确定按钮背景色
                        .btnBgColor(Color.GRAY)
                        // 确定按钮文字颜色
                        .btnTextColor(Color.BLUE)
                        // 使用沉浸式状态栏
                        .statusBarColor(Color.parseColor("#3F51B5"))
                        // 返回图标ResId
                        .backResId(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material)
                        .title("图片")
                        .titleColor(Color.WHITE)
                        .titleBgColor(Color.parseColor("#3F51B5"))
                        .cropSize(1, 1, 200, 200)
                        .needCrop(false)
                        // 第一个是否显示相机
                        .needCamera(true)
                        // 最大选择图片数量
                        .maxNum(9)
                        .build();
                ImgSelActivity.startActivity(identity_dc110v_main.this, config, REQUEST_CODE);
            }
        });

        btn_r=(Button)findViewById(R.id.identity_num_listview_main_listview_item_btn_right);
        btn_r.setText("检测");
        btn_l=(Button)findViewById(R.id.identity_num_listview_main_listview_item_btn_left);

        btn_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag==1){
                    Toast.makeText(identity_dc110v_main.this, "此项为第一项", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = ProgressDialog.show(identity_dc110v_main.this, "标记检测中", "请稍候...", true);
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        identity_dc110v_test_mul.getImageromSdk(pic_path);
                        try
                        {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (identity_dc110v_test_mul.return_true_flag.size()==pic_path.size())
                                    {
                                        dialog.dismiss();
                                        textView.setText("DC110V正常标记");
                                        identity_dc110v_test_mul.return_true_flag.clear();
                                    }
                                }
                            });
                        }
                        catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            Glide.with(identity_dc110v_main.this).load(pathList.get(0)).fitCenter().skipMemoryCache(true).into(avatar);
            identity_1_activity.no1[0] = pathList.get(0);
            pic_path.add(pathList.get(0));
        }
    }
}
