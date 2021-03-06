package android.wuliqing.com.lendphonesystemapp;

import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneListView;
import android.wuliqing.com.lendphonesystemapp.presenter.PhoneListPresenter;

import junit.framework.TestCase;

import org.mockito.Mockito;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class PhoneListPresenterTest extends TestCase {
    PhoneListView phoneListView;
    PhoneListPresenter phoneListPresenter;
    PhoneTableAction phoneTableAction;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
//        MockitoAnnotations.initMocks(this);
        phoneListView = Mockito.mock(PhoneListView.class);
        phoneListPresenter = Mockito.spy(new PhoneListPresenter());
        phoneTableAction = Mockito.mock(PhoneTableAction.class);
        phoneListPresenter.setDataBaseAction(phoneTableAction);
        phoneListPresenter.attach(phoneListView);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        phoneListPresenter.detach();
    }

    public void testOnFetchedPhoneList() {
        Mockito.doReturn(null).when(phoneTableAction).queryAll();
        phoneListPresenter.loadAllPhoneData();
        Mockito.verify(phoneListPresenter).loadAllPhoneData();
    }
}
