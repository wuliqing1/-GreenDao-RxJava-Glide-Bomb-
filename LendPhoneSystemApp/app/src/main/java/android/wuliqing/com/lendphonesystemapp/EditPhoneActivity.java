package android.wuliqing.com.lendphonesystemapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.wuliqing.com.lendphonesystemapp.mvpview.AddPhoneView;
import android.wuliqing.com.lendphonesystemapp.presenter.AddPhonePresenter;
import android.wuliqing.com.lendphonesystemapp.utils.MyTextUtils;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.other.BitmapUtil;
import com.soundcloud.android.crop.other.CropFileUtils;
import com.soundcloud.android.crop.other.CropHelper;

import java.io.File;
import java.util.List;

import zte.phone.greendao.PhoneNote;

public class EditPhoneActivity extends BaseToolBarActivity implements AddPhoneView, MyDialogFragment.DialogListener {
    public static final int ADD_PHONE_REQUEST_CODE = 222;
    public static final String ADD_PHONE_RESULT_KEY = "add_phone_result_key";
    private AddPhonePresenter mAddPhonePresenter = new AddPhonePresenter();
    private ImageView mAdd_phone_photo_view;
    private AutoCompleteTextView mAdd_phone_name_view;
    private EditText mAdd_phone_number_view;
    private AutoCompleteTextView mAdd_phone_project_view;
    private ProgressDialog mProgressDialog;
    private Uri outUri;

    @Override
    protected void detachPresenter() {
        mAddPhonePresenter.detach();
    }

    @Override
    protected void createPresenter() {
        mAddPhonePresenter.attach(this);
        mAddPhonePresenter.queryPhoneNameAndProjectName();
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
                    case 2:{
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
        mToolbar.setTitle(R.string.add_phone_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_phone_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_phone:
                addPhone();
                break;
        }
        return super.onOptionsItemSelected(item);
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
            phoneNote.setPhone_number(Integer.valueOf(phone_number));
            phoneNote.setProject_name(project_name);
            mProgressDialog.show();
            if (mAddPhonePresenter.getBmobFile() != null && mAddPhonePresenter.getBmobFile().exists()) {
                mAddPhonePresenter.addPicToNetWork(this, new UpLoadDataListener<String>() {
                    @Override
                    public void onComplete(String result) {
                        phoneNote.setPhone_photo_url(result);
                        mAddPhonePresenter.addPhone(phoneNote);
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
            } else {
                mAddPhonePresenter.addPhone(phoneNote);
            }
        }
    }

    @Override
    public void onResult(boolean result) {
        mProgressDialog.dismiss();
        if (result) {
            ToastUtils.show(this, R.string.add_phone_success);
            Intent intent = new Intent();
            intent.putExtra(ADD_PHONE_RESULT_KEY, true);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            ToastUtils.show(this, R.string.add_phone_error);
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
                .show(getFragmentManager(), "");
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
    protected void finishActivity() {
        onBackPressed();
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
            mAdd_phone_photo_view.setImageURI(uri);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
