package android.wuliqing.com.lendphonesystemapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectPhotoActivity extends Activity {
    public static final String TAG = "SelectPhotoActivity";
    public static final String GET_PICTURE_FILE_PATH_KEY = "picture_file_path";
    public static final int REQUEST_PICTURE_CODE = 0x123;
    public static final int GET_PICTURE_REQUEST_CODE = 1;
    public static final int GET_CAPTURE_REQUEST_CODE = 2;
    public static final int GET_PICTURE_ZOOM_REQUEST_CODE = 3;

    private TextView mTextViewPicture;
    private TextView mTextViewCapture;
    private TextView mTextViewCancel;

    private String tempFileName;
    private String fileName;
    public static final String PHONE_PHOTO_DIR = "/lendphonesystem/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_picture_view);
        initIntent();
        initContentView();
    }

    private void initIntent() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String str = format.format(new Date());
        String filePath = Environment.getExternalStorageDirectory() + PHONE_PHOTO_DIR;
        checkFolderExists(filePath);
        fileName = filePath + str + ".png";
        tempFileName = filePath + str + ".temp.png";
        Log.i(TAG, "fileName=" + fileName);
    }

    private boolean checkFolderExists(String strFolder) {
        Log.i(TAG, "checkFolderExists() strFolder=" + strFolder);
        File file = new File(strFolder);
        if (!file.exists()) {
            if (file.mkdir()) {
                return true;
            } else
                return false;
        }
        return true;
    }

    private void initContentView() {
        mTextViewPicture = (TextView) findViewById(R.id.user_head_picture);
        mTextViewCapture = (TextView) findViewById(R.id.user_head_capture);
        mTextViewCancel = (TextView) findViewById(R.id.user_head_cancel);
        mTextViewPicture.setOnClickListener(mOnClickListener);
        mTextViewCapture.setOnClickListener(mOnClickListener);
        mTextViewCancel.setOnClickListener(mOnClickListener);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.user_head_picture:
                    goPicture();
                    break;
                case R.id.user_head_capture:
                    goCapture();
                    break;
                case R.id.user_head_cancel:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    protected void goPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, GET_PICTURE_REQUEST_CODE);
    }

    protected void goCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(tempFileName)));
        startActivityForResult(intent, GET_CAPTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case GET_PICTURE_REQUEST_CODE: {
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
            }
            break;
            // 如果是调用相机拍照时
            case GET_CAPTURE_REQUEST_CODE:
                startPhotoZoom(Uri.fromFile(new File(tempFileName)));
                break;
            // 取得裁剪后的图片
            case GET_PICTURE_ZOOM_REQUEST_CODE:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Log.i(TAG, "startPhotoZoom(" + uri.toString() + ")");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, GET_PICTURE_ZOOM_REQUEST_CODE);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            savePicture(photo);
        }
    }

    private void savePicture(Bitmap photo) {
        Log.i(TAG, "savePicture()");
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
//			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            photo.compress(Bitmap.CompressFormat.PNG, 80, out);
            out.flush();
            out.close();
//			CompressFormat format= Bitmap.CompressFormat.PNG;
//			int quality = 100;
//			OutputStream stream = new FileOutputStream(fileName);
//			photo.compress(format, quality, stream);

            Intent intent = new Intent();
            intent.putExtra(GET_PICTURE_FILE_PATH_KEY, fileName);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.main_enter, R.anim.sub_exit);
    }
}