package android.wuliqing.com.lendphonesystemapp;

import android.wuliqing.com.lendphonesystemapp.dataBase.LendPhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.mvpview.LendPhoneListView;
import android.wuliqing.com.lendphonesystemapp.presenter.LendPhoneListPresenter;

import junit.framework.TestCase;

import org.mockito.Mockito;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class LendPhoneListPresenterTest extends TestCase {
    LendPhoneListView phoneListView;
    LendPhoneListPresenter phoneListPresenter;
    LendPhoneTableAction lendPhoneTableAction;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
//        MockitoAnnotations.initMocks(this);
        phoneListView = Mockito.mock(LendPhoneListView.class);
        phoneListPresenter = Mockito.spy(new LendPhoneListPresenter(1));
        lendPhoneTableAction = Mockito.spy(new LendPhoneTableAction(1));
        phoneListPresenter.setDatabaseAction(lendPhoneTableAction);
        phoneListPresenter.attach(phoneListView);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        phoneListPresenter.detach();
    }

    public void testOnFetchedPhoneList() {
        Mockito.doReturn(null).when(lendPhoneTableAction).query();
        phoneListPresenter.loadData(1);
        Mockito.verify(phoneListPresenter).loadData(1);
    }
}
