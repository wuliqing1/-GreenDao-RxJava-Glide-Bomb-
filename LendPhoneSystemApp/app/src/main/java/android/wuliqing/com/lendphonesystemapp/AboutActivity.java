package android.wuliqing.com.lendphonesystemapp;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.wuliqing.com.lendphonesystemapp.swipeBack.SwipeBackActivity;
import android.wuliqing.com.lendphonesystemapp.utils.Util;

import cn.bmob.v3.update.BmobUpdateAgent;


public class AboutActivity extends SwipeBackActivity implements View.OnClickListener {
    TextView tvVersion;
    Button btCode;
    Button btBlog;
    Button btPay;
    Button btShare;
    Button btUpdate;
    Button btBug;

    @Override
    protected void detachPresenter() {

    }

    @Override
    protected void createPresenter() {

    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initWidgets() {
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText(String.format("当前版本: %s (Build %s)", Util.getVersion(this), Util.getVersionCode(this)));
        btCode = (Button) findViewById(R.id.bt_code);
        if (btCode != null) {
            btCode.setOnClickListener(this);
        }
        btBlog = (Button) findViewById(R.id.bt_blog);
        if (btBlog != null) {
            btBlog.setOnClickListener(this);
        }
        btPay = (Button) findViewById(R.id.bt_pay);
        if (btPay != null) {
            btPay.setOnClickListener(this);
        }
        btShare = (Button) findViewById(R.id.bt_share);
        if (btShare != null) {
            btShare.setOnClickListener(this);
        }
        btUpdate = (Button) findViewById(R.id.bt_update);
        if (btUpdate != null) {
            btUpdate.setOnClickListener(this);
        }
        btBug = (Button) findViewById(R.id.bt_bug);
        if (btBug != null) {
            btBug.setOnClickListener(this);
        }
    }

    @Override
    protected void initSwipeLayout() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_code:
//                goToHtml(getString(R.string.app_html));
                break;
            case R.id.bt_blog:
                goToHtml("http://blog.csdn.net/yq6073025");
                break;
            case R.id.bt_pay:
//                Util.copyToClipboard(getString(R.string.alipay), this);
                break;
            case R.id.bt_share:
//                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
//                sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt));
//                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_app)));
                break;
            case R.id.bt_bug:
//                goToHtml(getString(R.string.bugTableUrl));
                break;
            case R.id.bt_update:
                BmobUpdateAgent.update(this);
                break;
        }
    }

    private void goToHtml(String url) {
        Uri uri = Uri.parse(url);   //指定网址
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);           //指定Action
        intent.setData(uri);                            //设置Uri
        startActivity(intent);        //启动Activity
    }
}
