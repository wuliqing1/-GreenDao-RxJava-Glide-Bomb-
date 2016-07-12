package android.wuliqing.com.lendphonesystemapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Toast;
import android.wuliqing.com.lendphonesystemapp.fragment.MyDialogFragment;
import android.wuliqing.com.lendphonesystemapp.listeners.UpLoadDataListener;
import android.wuliqing.com.lendphonesystemapp.model.PhoneNodeWrap;
import android.wuliqing.com.lendphonesystemapp.mvpview.AddPhoneView;
import android.wuliqing.com.lendphonesystemapp.permission.PermissionListener;
import android.wuliqing.com.lendphonesystemapp.permission.PermissionManager;
import android.wuliqing.com.lendphonesystemapp.presenter.EditPhonePresenter;
import android.wuliqing.com.lendphonesystemapp.transformations.CropCircleTransformation;
import android.wuliqing.com.lendphonesystemapp.utils.MyTextUtils;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import com.bumptech.glide.Glide;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.other.BitmapUtil;
import com.soundcloud.android.crop.other.CropFileUtils;
import com.soundcloud.android.crop.other.CropHelper;

import java.io.File;
import java.util.List;

import zte.phone.greendao.PhoneNote;

public class EditPhoneActivity extends BaseToolBarActivity implements AddPhoneView, MyDialogFragment.DialogListener {
    public static final int ADD_PHONE_REQUEST_CODE = 222;
    public static final int EDIT_PHONE_REQUEST_CODE = 223;
    private static final int REQUEST_CODE_EXTERNAL = 8127;
    public static final String UPDATE_PHONE_RESULT_KEY = "update_phone_result_key";
    public static final String DELETE_PHONE_RESULT_KEY = "delete_phone_result_key";
    public static final String EDIT_PHONE_ID_RESULT_KEY = "edit_phone_id_key";
    public static final String EDIT_PHONE_DATA = "edit_phone_data";
    private PhoneNodeWrap mPhoneNodeWrap;
    private EditPhonePresenter mAddPhonePresenter = new EditPhonePresenter();
    private ImageView mAdd_phone_photo_view;
    private AutoCompleteTextView mAdd_phone_name_view;
    private EditText mAdd_phone_number_view;
    private AutoCompleteTextView mAdd_phone_project_view;
    private ProgressDialog mProgressDialog;
    private Uri outUri;
    private PermissionManager helper;

    @Override
    protected void detachPresenter() {
        mAddPhonePresenter.detach();
    }

    @Override
    protected void createPresenter() {
        mAddPhonePresenter.attach(this);
        mAddPhonePresenter.queryPhoneNameAndProjectName();
        if (mPhoneNodeWrap != null) {
//            mAddPhonePresenter.queryPhoneWithID(phone_id);
            updateUi();
        }
    }

    @Override
    protected void initIntentData(Bundle savedInstanceState) {
        mPhoneNodeWrap = getIntent().getParcelableExtra(EDIT_PHONE_DATA);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_add_phone;
    }

    @Override
    protected void initWidgets() {
        mAdd_phone_photo_view = (ImageView) findViewById(R.id.add_phone_photo_view);
        mAdd_phone_name_view = (AutoCompleteTextView) findViewById(R.id.add_phone_name_view);
        mAdd_phone_number_view = (EditText) findViewById(R.id.add_phone_number_view);
        mAdd_phone_project_view = (AutoCompleteTextView) findViewById(R.id.add_phone_project_view);
        mAdd_phone_photo_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(EditPhoneActivity.this, SelectPhotoActivity.class);
//                startActivityForResult(intent, SelectPhotoActivity.SELECT_PHOTO_REQUEST_CODE);
//                overridePendingTransition(R.anim.sub_enter,
//                        R.anim.main_exit);
                showPopWindow();
            }
        });
        initProgressDialog();
    }

    private void showPopWindow() {
        requestPermission();
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                new String[]{getString(R.string.select_capture_title),
                        getString(R.string.select_picture_title),
                        getString(R.string.select_delete_title)});
        listPopupWindow.setAdapter(arrayAdapter);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        outUri = CropHelper.generateUri();
                        Crop.pickCapture(EditPhoneActivity.this, outUri);
                        break;
                    case 1:
                        Crop.pickImage(EditPhoneActivity.this);
                        break;
                    case 2: {
                        mAddPhonePresenter.setBmobFile(null);
                        mAdd_phone_photo_view.setImageResource(R.drawable.ic_camera_48pt_3x);
                    }
                    break;
                }
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setModal(true);
        listPopupWindow.setWidth(300);
        listPopupWindow.setAnchorView(mAdd_phone_photo_view);
        listPopupWindow.show();
    }

    public void requestPermission() {
        helper = PermissionManager.with(this)
                .addRequestCode(EditPhoneActivity.REQUEST_CODE_EXTERNAL)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissionsListener(new PermissionListener() {

                    @Override
                    public void onGranted() {
                        ToastUtils.show(EditPhoneActivity.this, getString(R.string.get_permission_success));
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.show(EditPhoneActivity.this, getString(R.string.get_permission_error));
                    }

                    @Override
                    public void onShowRationale(String[] permissions) {
                        //当用户拒绝某权限时并点击`不再提醒`的按钮时，下次应用再请求该权限时，需要给出合适的响应（比如,给个展示对话框来解释应用为什么需要该权限）
                        Snackbar.make(mAdd_phone_photo_view, getString(R.string.need_permission_msg), Snackbar.LENGTH_INDEFINITE)
                                .setAction("ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //必须调用该`setIsPositive(true)`方法
                                        helper.setIsPositive(true);
                                        helper.request();
                                    }
                                }).show();
                    }
                })
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_EXTERNAL:
                helper.onPermissionResult(permissions, grantResults);
                break;
        }
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        mProgressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        mProgressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//        mProgressDialog.setIcon(R.drawable.ic_launcher);// 设置提示的title的图标，默认是没有的
//        mProgressDialog.setTitle("提示");
        mProgressDialog.setMax(100);
        mProgressDialog.setMessage(getResources().getString(R.string.up_data_message));
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (mPhoneNodeWrap != null) {
            mToolbar.setTitle(R.string.edit_phone_title);
        } else {
            mToolbar.setTitle(R.string.add_phone_title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_phone_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteMenu = menu.findItem(R.id.action_delete_phone);
        if (mPhoneNodeWrap != null) {
            deleteMenu.setVisible(true);
        } else {
            deleteMenu.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_phone:
                addPhone();
                break;
            case R.id.action_delete_phone:
                showDeletePhoneDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeletePhoneDialog() {
        if (mPhoneNodeWrap == null) {
            return;
        }
        MyDialogFragment.newInstance("", getString(R.string.phone_delete_dialog_msg, mPhoneNodeWrap.getPhone_name()),
                new MyDialogFragment.DialogListener() {
                    @Override
                    public void onClickDialogOk() {
                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        mProgressDialog.setMessage(getString(R.string.delete_message));
                        mProgressDialog.show();
                        mAddPhonePresenter.deletePhone(mPhoneNodeWrap);
                    }

                    @Override
                    public void onClickDialogCancel() {

                    }
                }).show(getSupportFragmentManager(), "");
    }

    private boolean isInvalidString(String phone_name, String phone_number) {
        if (TextUtils.isEmpty(phone_name)) {
            ToastUtils.showSnackBar(mAdd_phone_name_view, R.string.add_phone_name_invalid);
            return true;
        }
        if (TextUtils.isEmpty(phone_number) || !MyTextUtils.isNumeric(phone_number)) {
            ToastUtils.showSnackBar(mAdd_phone_number_view, R.string.add_phone_number_invalid);
            return true;
        }
        return false;
    }

    private void addPhone() {
        final PhoneNote phoneNote = new PhoneNote();
        String phone_name = mAdd_phone_name_view.getText().toString();
        String phone_number = mAdd_phone_number_view.getText().toString();
        String project_name = mAdd_phone_project_view.getText().toString();
        if (!isInvalidString(phone_name, phone_number)) {
            phoneNote.setPhone_name(phone_name);
            phoneNote.setPhone_number(Long.valueOf(phone_number));
            phoneNote.setProject_name(project_name);
            if (mPhoneNodeWrap != null) {
                phoneNote.setBmob_phone_id(mPhoneNodeWrap.getBmob_phone_id());
                phoneNote.setPhone_photo_url(mPhoneNodeWrap.getPhone_photo_url());
            }

            if (mAddPhonePresenter.getBmobFile() != null && mAddPhonePresenter.getBmobFile().exists()) {
                upDataWithFile(phoneNote);
            } else {
                upData(phoneNote);
            }
        }
    }

    private void upData(PhoneNote phoneNote) {
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (mPhoneNodeWrap != null) {
            mProgressDialog.setMessage(getString(R.string.update_message));
            mProgressDialog.show();
            mAddPhonePresenter.updatePhone(phoneNote, null);
        } else {
            mProgressDialog.show();
            mAddPhonePresenter.addPhone(phoneNote);
        }
    }

    private void upDataWithFile(final PhoneNote phoneNote) {
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
        mAddPhonePresenter.addPicToNetWork(this, new UpLoadDataListener<String>() {
            @Override
            public void onComplete(String result) {
                String old_url = phoneNote.getPhone_photo_url();
                phoneNote.setPhone_photo_url(result);
                if (!TextUtils.isEmpty(phoneNote.getBmob_phone_id())) {
                    mAddPhonePresenter.updatePhone(phoneNote, old_url);
                } else {
                    mAddPhonePresenter.addPhone(phoneNote);
                }
            }

            @Override
            public void onProgress(int value) {
                mProgressDialog.setProgress(value);
            }

            @Override
            public void onError() {
                mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onResult(boolean result, String id) {
        mProgressDialog.dismiss();
        if (result) {
            if (mPhoneNodeWrap != null) {
                ToastUtils.show(this, R.string.update_phone_success);
            } else {
                ToastUtils.show(this, R.string.add_phone_success);
            }

            Intent intent = new Intent();
            intent.putExtra(UPDATE_PHONE_RESULT_KEY, true);
            intent.putExtra(EDIT_PHONE_ID_RESULT_KEY, id);
            setResult(RESULT_OK, intent);
            finish();
        } else {
//            ToastUtils.show(this, R.string.add_phone_error);
        }
    }

    @Override
    public void onDeleteResult(boolean result, String id) {
        mProgressDialog.dismiss();
        if (result) {
            ToastUtils.show(this, R.string.delete_phone_success);
            Intent intent = new Intent();
            intent.putExtra(DELETE_PHONE_RESULT_KEY, true);
            intent.putExtra(EDIT_PHONE_ID_RESULT_KEY, id);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        String phone_name = mAdd_phone_name_view.getText().toString();
        String phone_number = mAdd_phone_number_view.getText().toString();
        if (!TextUtils.isEmpty(phone_name) && !TextUtils.isEmpty(phone_number)) {
            showMyDialog();
            return;
        }
        super.onBackPressed();
    }

    private void showMyDialog() {
        MyDialogFragment.newInstance("", getResources().getString(R.string.add_phone_alert_dialog), this)
                .show(getSupportFragmentManager(), "");
    }

    @Override
    public void onQueryPhoneNameResult(List<String> list) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(EditPhoneActivity.this,
                        android.R.layout.simple_dropdown_item_1line, list);

        mAdd_phone_name_view.setAdapter(adapter);
    }

    @Override
    public void onQueryProjectNameResult(List<String> list) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(EditPhoneActivity.this,
                        android.R.layout.simple_dropdown_item_1line, list);

        mAdd_phone_project_view.setAdapter(adapter);
    }

    @Override
    public void onQueryPhone(PhoneNote phoneNote) {
//        if (phoneNote != null) {
//            updateUi();
//        }
    }

    private void updateUi() {
        if (mPhoneNodeWrap == null) {
            return;
        }
        mAdd_phone_name_view.setText(mPhoneNodeWrap.getPhone_name());
        mAdd_phone_number_view.setText(String.valueOf(mPhoneNodeWrap.getPhone_number()));
        mAdd_phone_project_view.setText(mPhoneNodeWrap.getProject_name());
        if (mPhoneNodeWrap.getPhone_photo_url() != null) {
            Glide.with(this)
                    .load(mPhoneNodeWrap.getPhone_photo_url())
                    .placeholder(R.drawable.ic_camera_48pt_3x)
                    .error(R.drawable.ic_camera_48pt_3x)
                    .crossFade()
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(mAdd_phone_photo_view);
        }
    }

    @Override
    public void onClickDialogOk() {
        addPhone();
    }

    @Override
    public void onClickDialogCancel() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CAPTURE && resultCode == RESULT_OK) {
            beginCrop(outUri);
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        } else if (requestCode == SelectPhotoActivity.SELECT_PHOTO_REQUEST_CODE) {
            setPhotoData(result);
        }
    }

    private void setPhotoData(Intent result) {
        try {
            Uri uri = result
                    .getParcelableExtra(SelectPhotoActivity.RETURN_PHOTO_DATA_KEY);
            if (uri != null) {
                Bitmap photo = BitmapUtil.decodeUriAsBitmap(this, uri);
                if (photo != null) {
                    mAdd_phone_photo_view.setImageBitmap(photo);
                    String path = CropFileUtils.getSmartFilePath(this, uri);
                    File file = new File(path);
                    mAddPhonePresenter.setBmobFile(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void beginCrop(Uri source) {
        String fileName = MyTextUtils.getDateStringForName() + "_cropped";
        File file = new File(getCacheDir(), fileName);
        Uri destination = Uri.fromFile(file);
        mAddPhonePresenter.setBmobFile(file);
        Crop.of(source, destination).asSquare().withMaxSize(300, 300).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_camera_48pt_3x)
                    .error(R.drawable.ic_camera_48pt_3x)
                    .crossFade()
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(mAdd_phone_photo_view);
//            mAdd_phone_photo_view.setImageURI(uri);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
