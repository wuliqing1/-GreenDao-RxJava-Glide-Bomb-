package android.wuliqing.com.lendphonesystemapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.wuliqing.com.lendphonesystemapp.permission.PermissionListener;
import android.wuliqing.com.lendphonesystemapp.permission.PermissionManager;

import com.soundcloud.android.crop.other.CropHandler;
import com.soundcloud.android.crop.other.CropHelper;
import com.soundcloud.android.crop.other.CropParams;

public class SelectPhotoActivity extends Activity implements CropHandler {
    public static final int SELECT_PHOTO_REQUEST_CODE = 0x123;
    public static final String RETURN_PHOTO_DATA_KEY = "return_photo_data_key";
    private TextView mTextViewPicture;
    private TextView mTextViewCapture;
    private TextView mTextViewCancel;
    private CropParams mCropParams;
    private PermissionManager mPermissionManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_picture_view);
        mCropParams = new CropParams(this);
        initContentView();
        initPermissionManager();
    }

    private void initPermissionManager() {
        mPermissionManager = PermissionManager.with(this)
                //添加权限请求码
                .addRequestCode(SelectPhotoActivity.SELECT_PHOTO_REQUEST_CODE)
                //设置权限，可以添加多个权限
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //设置权限监听器
                .setPermissionsListener(new PermissionListener() {

                    @Override
                    public void onGranted() {
                        //当权限被授予时调用
                        Toast.makeText(SelectPhotoActivity.this, "Camera Permission granted",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDenied() {
                        //用户拒绝该权限时调用
                        Toast.makeText(SelectPhotoActivity.this, "Camera Permission denied",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onShowRationale(String[] permissions) {
                        //当用户拒绝某权限时并点击`不再提醒`的按钮时，下次应用再请求该权限时，需要给出合适的响应（比如,给个展示对话框来解释应用为什么需要该权限）
                        Snackbar.make(mTextViewPicture, "需要相机权限去拍照", Snackbar.LENGTH_INDEFINITE)
                                .setAction("ok", new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //必须调用该`setIsPositive(true)`方法
                                        mPermissionManager.setIsPositive(true);
                                        mPermissionManager.request();
                                    }
                                }).show();
                    }
                })
                //请求权限
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SELECT_PHOTO_REQUEST_CODE:
                mPermissionManager.onPermissionResult(permissions, grantResults);
                break;
        }
    }

    private void initContentView() {
        mTextViewPicture = (TextView) findViewById(R.id.phone_head_picture);
        mTextViewCapture = (TextView) findViewById(R.id.phone_head_capture);
        mTextViewCancel = (TextView) findViewById(R.id.phone_head_cancel);
        mTextViewPicture.setOnClickListener(mOnClickListener);
        mTextViewCapture.setOnClickListener(mOnClickListener);
        mTextViewCancel.setOnClickListener(mOnClickListener);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.phone_head_picture: {
                    mCropParams.enable = true;
                    mCropParams.compress = false;
                    Intent intent = CropHelper.buildGalleryIntent(mCropParams);
                    startActivityForResult(intent, CropHelper.REQUEST_CROP);
                }
                break;
                case R.id.phone_head_capture: {
                    mCropParams.enable = true;
                    mCropParams.compress = false;
                    Intent intent = CropHelper.buildCameraIntent(mCropParams);
                    startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
                }
                break;
                case R.id.phone_head_cancel:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CropHelper.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        CropHelper.clearCacheDir();
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.main_enter, R.anim.sub_exit);
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        // Original or Cropped uri
        if (!mCropParams.compress) {
//            Bitmap photo = BitmapUtil.decodeUriAsBitmap(this, uri);
            Intent intent = new Intent();
            intent.putExtra(RETURN_PHOTO_DATA_KEY, uri);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onCompressed(Uri uri) {
//        mImageView.setImageBitmap(BitmapUtil.decodeUriAsBitmap(this, uri));
        Toast.makeText(this, "onCompressed! " + uri.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "Crop canceled!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailed(String message) {
        Toast.makeText(this, "Crop failed: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleIntent(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }
}