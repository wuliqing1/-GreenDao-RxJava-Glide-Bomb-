package android.wuliqing.com.lendphonesystemapp;

import android.wuliqing.com.lendphonesystemapp.mvpview.MainView;
import android.wuliqing.com.lendphonesystemapp.presenter.MainPresenter;

import junit.framework.TestCase;

import org.mockito.Mockito;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class MainPresenterTest extends TestCase {
    MainView mainView;
    MainPresenter mainPresenter;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainView = Mockito.mock(MainView.class);
        mainPresenter = Mockito.spy(new MainPresenter());
        mainPresenter.attach(mainView);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mainPresenter.detach();
    }

    public void testSyncLocalDataBaseAndNetWork() {

    }
}
