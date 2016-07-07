package android.wuliqing.com.lendphonesystemapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.wuliqing.com.lendphonesystemapp.fragment.MyDialogFragment;
import android.wuliqing.com.lendphonesystemapp.listeners.UpLoadDataListener;
import android.wuliqing.com.lendphonesystemapp.model.MyUser;
import android.wuliqing.com.lendphonesystemapp.mvpview.UserView;
import android.wuliqing.com.lendphonesystemapp.presenter.UserPresenter;
import android.wuliqing.com.lendphonesystemapp.transformations.CropCircleTransformation;
import android.wuliqing.com.lendphonesystemapp.utils.MyTextUtils;

import com.bumptech.glide.Glide;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.other.CropHelper;

import java.io.File;

import cn.bmob.v3.BmobUser;

/**
 * A login screen that offers login via email/password.
 */
public class UserActivity extends BaseToolBarActivity implements UserView {
    public static final int USER_REQUEST_CODE = 47;
    public static final String USER_FLAG_KEY = "user_flag_key";
    private ImageView mPhoto;
    private EditText mNameView;
    private EditText mDepartmentView;
    private EditText mPositionView;
    private ProgressBar mProgressView;
    private View mLoginFormView;
    private UserPresenter mUserPresenter;
    private Uri outUri;

    @Override
    protected void detachPresenter() {
        mUserPresenter.detach();
    }

    @Override
    protected void createPresenter() {
        mUserPresenter = new UserPresenter();
        mUserPresenter.attach(this);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidgets() {
        mPhoto = (ImageView) findViewById(R.id.user_photo);
        mNameView = (EditText) findViewById(R.id.user_name);
        mPositionView = (EditText) findViewById(R.id.user_position);
        mDepartmentView = (EditText) findViewById(R.id.user_department);
        mPositionView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.save || id == EditorInfo.IME_NULL) {
                    save();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.user_save_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        mLoginFormView = findViewById(R.id.user_form);
        mProgressView = (ProgressBar) findViewById(R.id.user_progress);
        mPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });

        updateView();
    }

    private void updateView() {
        MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
        if (myUser != null) {
            mNameView.setText(myUser.getUsername());
            mPositionView.setText(myUser.getPosition());
            mDepartmentView.setText(myUser.getDepartment());
            if (!TextUtils.isEmpty(myUser.getPhoto_url())) {
                Glide.with(this)
                        .load(myUser.getPhoto_url())
                        .placeholder(R.drawable.ic_account_circle_60pt_2x)
                        .error(R.drawable.ic_account_circle_60pt_2x)
                        .crossFade()
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(mPhoto);
            }
        }
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        mToolbar.setTitle(R.string.title_activity_update);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit_account) {
            showDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        MyDialogFragment.newInstance("", getString(R.string.exit_account_message), new MyDialogFragment.DialogListener() {
            @Override
            public void onClickDialogOk() {
                BmobUser.logOut(UserActivity.this);
                finishActivityWithData();
            }

            @Override
            public void onClickDialogCancel() {

            }
        }).show(getFragmentManager(), "");
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
                        Crop.pickCapture(UserActivity.this, outUri);
                        break;
                    case 1:
                        Crop.pickImage(UserActivity.this);
                        break;
                    case 2: {
                        mUserPresenter.setBmobFile(null);
                        mPhoto.setImageResource(R.drawable.ic_account_circle_60pt_2x);
                    }
                    break;
                }
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setModal(true);
        listPopupWindow.setWidth(300);
        listPopupWindow.setAnchorView(mPhoto);
        listPopupWindow.show();
    }

    private void save() {
        mNameView.setError(null);
        mDepartmentView.setError(null);
        mPositionView.setError(null);

        final String name = mNameView.getText().toString();
        final String department = mDepartmentView.getText().toString();
        final String position = mPositionView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(department)) {
            mDepartmentView.setError(getString(R.string.error_field_required));
            focusView = mDepartmentView;
            cancel = true;
        }

        if (TextUtils.isEmpty(position)) {
            mPositionView.setError(getString(R.string.error_field_required));
            focusView = mPositionView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            if (mUserPresenter.getBmobFile() != null && mUserPresenter.getBmobFile().exists()) {
                mUserPresenter.addPicToNetWork(new UpLoadDataListener<String>() {
                    @Override
                    public void onComplete(String result) {
                        mUserPresenter.update(result, name, department, position);
                    }

                    @Override
                    public void onProgress(int value) {
                        mProgressView.setProgress(value);
                    }

                    @Override
                    public void onError() {
                        showProgress(false);
                    }
                });
            } else {
                mUserPresenter.update(null, name, department, position);
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CAPTURE && resultCode == RESULT_OK) {
            beginCrop(outUri);
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        String fileName = MyTextUtils.getDateStringForName() + "_cropped";
        File file = new File(getCacheDir(), fileName);
        Uri destination = Uri.fromFile(file);
        mUserPresenter.setBmobFile(file);
        Crop.of(source, destination).asSquare().withMaxSize(300, 300).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_account_circle_60pt_2x)
                    .error(R.drawable.ic_account_circle_60pt_2x)
                    .crossFade()
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(mPhoto);
//            mPhoto.setImageURI(uri);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateResult(boolean result) {
        if (result) {
            finishActivityWithData();
        } else {
            showProgress(false);
        }
    }

    private void finishActivityWithData() {
        Intent intent = new Intent();
        intent.putExtra(USER_FLAG_KEY, true);
        setResult(RESULT_OK, intent);
        finishActivity();
    }
}

