package android.wuliqing.com.lendphonesystemapp;

import android.wuliqing.com.lendphonesystemapp.DataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.Utils.DataSyncFactory;
import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;
import android.wuliqing.com.lendphonesystemapp.mvpview.AddPhoneView;
import android.wuliqing.com.lendphonesystemapp.presenter.AddPhonePresenter;

import junit.framework.TestCase;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class AddPhonePresenterTest extends TestCase {
    AddPhoneView addPhoneView;
    AddPhonePresenter addPhonePresenter;
    DataSyncFactory dataSyncTools;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addPhoneView = Mockito.mock(AddPhoneView.class);
        addPhonePresenter = Mockito.spy(new AddPhonePresenter());
        dataSyncTools = Mockito.mock(DataSyncFactory.class);
        addPhonePresenter.setDataSyncTool(dataSyncTools);
        addPhonePresenter.attach(addPhoneView);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        addPhonePresenter.detach();
    }

    public void testAddPhone() {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                UpdateDataListener dataListener = (UpdateDataListener) arguments[0];
                dataListener.onResult(true);

                return null;
            }
        }).when(dataSyncTools).addData(Mockito.any(UpdateDataListener.class),
                Mockito.any(PhoneTableAction.class), Mockito.any(PhoneNote.class));

        PhoneNote phoneNote = new PhoneNote();
        addPhonePresenter.addPhone(phoneNote);
        Mockito.verify(addPhoneView).onShowLoading();
        Mockito.verify(addPhonePresenter).addPhone(phoneNote);
        Mockito.verify(addPhoneView).onHideLoading();
    }
}
