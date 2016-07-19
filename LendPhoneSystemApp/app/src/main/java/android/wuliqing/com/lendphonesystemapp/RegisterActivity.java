package android.wuliqing.com.lendphonesystemapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.wuliqing.com.lendphonesystemapp.mvpview.RegisterView;
import android.wuliqing.com.lendphonesystemapp.presenter.RegisterPresenter;
import android.wuliqing.com.lendphonesystemapp.swipeBack.SwipeBackActivity;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends SwipeBackActivity implements RegisterView {
    public static final int REGISTER_REQUEST_CODE = 46;
    public static final String REGISTER_FLAG_KEY = "register_flag_key";
    private EditText mNameView;
    private EditText mPasswordView;
    private EditText mAgainPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private RegisterPresenter mRegisterPresenter;

    @Override
    protected void detachPresenter() {
        mRegisterPresenter.detach();
    }

    @Override
    protected void createPresenter() {
        mRegisterPresenter = new RegisterPresenter();
        mRegisterPresenter.attach(this);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initWidgets() {
        mNameView = (EditText) findViewById(R.id.register_name);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mAgainPasswordView = (EditText) findViewById(R.id.register_again_password);
        mAgainPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.register_sign_in_button);
        assert mEmailSignInButton != null;
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        mToolbar.setTitle(R.string.action_register);
    }

    private void attemptRegister() {
        mNameView.setError(null);
        mPasswordView.setError(null);
        mAgainPasswordView.setError(null);

        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String againPassword = mAgainPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(againPassword) && !isPasswordValid(againPassword)) {
            mAgainPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mAgainPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(againPassword)
                && !password.equals(againPassword)) {
            mAgainPasswordView.setError(getString(R.string.error_not_same_password));
            focusView = mAgainPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mRegisterPresenter.register(name, password);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
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
    public void onRegisterResult(boolean result) {
        if (result) {
            ToastUtils.show(this, getString(R.string.register_success));
            Intent intent = new Intent();
            intent.putExtra(REGISTER_FLAG_KEY, true);
//            String name = mNameView.getText().toString();
//            String password = mPasswordView.getText().toString();

            setResult(RESULT_OK, intent);
            finishActivity();
        } else {
            showProgress(false);
        }
    }

    @Override
    protected void initSwipeLayout() {

    }
}

