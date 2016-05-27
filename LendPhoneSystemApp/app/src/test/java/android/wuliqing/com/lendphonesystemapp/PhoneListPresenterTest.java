package android.wuliqing.com.lendphonesystemapp;

import android.wuliqing.com.lendphonesystemapp.DataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.Utils.DataSyncFactory;
import android.wuliqing.com.lendphonesystemapp.listeners.DataListener;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneListView;
import android.wuliqing.com.lendphonesystemapp.presenter.PhoneListPresenter;

import junit.framework.TestCase;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class PhoneListPresenterTest extends TestCase {
    PhoneListView phoneListView;
    PhoneListPresenter phoneListPresenter;
    DataSyncFactory dataSyncTools;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
//        MockitoAnnotations.initMocks(this);
        phoneListView = Mockito.mock(PhoneListView.class);
        phoneListPresenter = Mockito.spy(new PhoneListPresenter());
        dataSyncTools = Mockito.mock(DataSyncFactory.class);
        phoneListPresenter.setDataSyncTool(dataSyncTools);
        phoneListPresenter.attach(phoneListView);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        phoneListPresenter.detach();
    }

    public void testOnFetchedPhoneList() {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                DataListener dataListener = (DataListener)arguments[0];
                List<PhoneNote> result = new ArrayList<PhoneNote>();
                dataListener.onComplete(result);

                return result;
            }
        }).when(dataSyncTools).loadData(Mockito.any(DataListener.class), Mockito.any(PhoneTableAction.class));

        phoneListPresenter.onFetchedPhoneList();
        Mockito.verify(phoneListView).onShowLoading();
        Mockito.verify(phoneListPresenter).loadData();
        Mockito.verify(phoneListView).onHideLoading();
    }
}
