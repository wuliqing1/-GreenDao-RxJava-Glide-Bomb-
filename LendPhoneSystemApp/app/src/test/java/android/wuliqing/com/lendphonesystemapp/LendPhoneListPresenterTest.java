package android.wuliqing.com.lendphonesystemapp;

import android.wuliqing.com.lendphonesystemapp.DataBase.LendPhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.Utils.DataSyncFactory;
import android.wuliqing.com.lendphonesystemapp.listeners.DataListener;
import android.wuliqing.com.lendphonesystemapp.mvpview.LendPhoneListView;
import android.wuliqing.com.lendphonesystemapp.presenter.LendPhoneListPresenter;

import junit.framework.TestCase;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import zte.phone.greendao.LendPhoneNote;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class LendPhoneListPresenterTest extends TestCase {
    LendPhoneListView phoneListView;
    LendPhoneListPresenter phoneListPresenter;
    DataSyncFactory dataSyncTools;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
//        MockitoAnnotations.initMocks(this);
        phoneListView = Mockito.mock(LendPhoneListView.class);
        phoneListPresenter = Mockito.spy(new LendPhoneListPresenter());
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
                List<LendPhoneNote> result = new ArrayList<LendPhoneNote>();
                dataListener.onComplete(result);

                return result;
            }
        }).when(dataSyncTools).loadData(Mockito.any(DataListener.class), Mockito.any(LendPhoneTableAction.class));

        phoneListPresenter.onFetchedLendPhoneList(1);
        Mockito.verify(phoneListView).onShowLoading();
        Mockito.verify(phoneListPresenter).loadData(1);
        Mockito.verify(phoneListView).onHideLoading();
    }
}
