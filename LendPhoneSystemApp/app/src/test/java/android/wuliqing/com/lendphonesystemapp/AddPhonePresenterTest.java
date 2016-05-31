package android.wuliqing.com.lendphonesystemapp;

import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.mvpview.AddPhoneView;
import android.wuliqing.com.lendphonesystemapp.presenter.AddPhonePresenter;

import junit.framework.TestCase;

import org.mockito.Mockito;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class AddPhonePresenterTest extends TestCase {
    AddPhoneView addPhoneView;
    AddPhonePresenter addPhonePresenter;
    PhoneTableAction phoneTableAction;
    PhoneNote phoneNote;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addPhoneView = Mockito.mock(AddPhoneView.class);
        addPhonePresenter = Mockito.spy(new AddPhonePresenter());
        phoneTableAction = Mockito.spy(new PhoneTableAction());
        phoneNote = Mockito.spy(new PhoneNote());
        addPhonePresenter.attach(addPhoneView);
        addPhonePresenter.setDataBaseAction(phoneTableAction);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        addPhonePresenter.detach();
    }

    public void testAddPhone() {
        Mockito.doReturn(true).when(addPhonePresenter).addPhoneTable(phoneNote);
        addPhonePresenter.addPhone(phoneNote);
        Mockito.verify(addPhonePresenter).addPhoneTable(phoneNote);
        Mockito.verify(phoneTableAction, Mockito.never()).add(phoneNote);
        Mockito.verify(addPhoneView,Mockito.never()).onShowLoading();
        Mockito.verify(addPhonePresenter).addPhone(phoneNote);
        Mockito.verify(addPhoneView,Mockito.never()).onHideLoading();
    }
}
