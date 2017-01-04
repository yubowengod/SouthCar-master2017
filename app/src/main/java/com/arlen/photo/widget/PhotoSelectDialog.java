package com.arlen.photo.widget;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.example.god.southcar.R;
import com.arlen.photo.photopickup.util.FileSizeUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Arlen on 2016/6/2 18:32.
 */
public class PhotoSelectDialog extends Dialog implements View.OnClickListener {

    public interface CropResultListener{
        void cropResult(Uri uri);
    }

    private Activity mActivity;
    private Uri mPhotoUri;
    private Uri mImageUri;
    private Uri mZoomUri;

    private CropResultListener mListener;

    private static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    private static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    private static final int CROP_RESULT = 3;

    public PhotoSelectDialog(Activity activity) {
        super(activity, R.style.photoDialog);
        this.mActivity = activity;

        initView();
        getWindow().setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        getWindow().setWindowAnimations(R.style.photoDialogAnimation); // 添加动画

        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        getWindow().setAttributes(lp);
    }
    
    private void initView(){
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.widget_select_photo, null);
        TextView takePhotoBtn = (TextView) contentView.findViewById(R.id.btn_take_photo);
        TextView btnPickPhoto = (TextView) contentView.findViewById(R.id.btn_pick_photo);
        TextView btnCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        takePhotoBtn.setOnClickListener(this);
        btnPickPhoto.setOnClickListener(this);
        btnCancel .setOnClickListener(this);

        setContentView(contentView);
    }

    public void setCropResultListener(CropResultListener listener){
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_take_photo:
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkSelfPermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
                    if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, 1);
                        return;
                    } else {
                        takePhoto();
                    }
                } else {
                    takePhoto();
                }
                    dismiss();
                break;
            case R.id.btn_pick_photo:
                pickPhoto();
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    public void takePhoto() {
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
            ContentValues values = new ContentValues();
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            values.put(MediaStore.Video.Media.TITLE, filename);
            mPhotoUri = mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            /** ----------------- */
            mActivity.startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(mActivity,"内存卡不存在",Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
        mImageUri = Uri.fromFile(outputImage);

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        mActivity.startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        int r = new Random().nextInt(Integer.MAX_VALUE);
        File file = new File(Environment.getExternalStorageDirectory(),
                "jh" + r + ".jpg");
        mZoomUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mZoomUri);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);//是否保存在bitmap中返回
        mActivity.startActivityForResult(intent, CROP_RESULT);
    }

    public void doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            if (data == null) {
               Toast.makeText(mActivity,"选择图片文件出错",Toast.LENGTH_SHORT).show();
                return;
            }
            mImageUri = data.getData();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String url= FileSizeUtil.getImageAbsolutePath(mActivity,mImageUri);
                mImageUri = Uri.fromFile(new File(url));
            }
            if (mImageUri == null) {
                Toast.makeText(mActivity,"选择图片文件出错",Toast.LENGTH_SHORT).show();
                return;
            }
            if(mListener != null) {
                mListener.cropResult(mImageUri);
            }
        } else if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {
            if(mListener != null) {
                mListener.cropResult(mPhotoUri);
            }
        }
    }
}
