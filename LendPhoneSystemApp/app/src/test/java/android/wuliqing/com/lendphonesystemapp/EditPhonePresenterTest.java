package android.wuliqing.com.lendphonesystemapp;

import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.mvpview.AddPhoneView;
import android.wuliqing.com.lendphonesystemapp.presenter.EditPhonePresenter;

import junit.framework.TestCase;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class EditPhonePresenterTest extends TestCase {
    AddPhoneView addPhoneView;
    EditPhonePresenter addPhonePresenter;
    PhoneTableAction phoneTableAction;
    PhoneNote phoneNote;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addPhoneView = Mockito.mock(AddPhoneView.class);
        addPhonePresenter = Mockito.spy(new EditPhonePresenter());
        phoneTableAction = Mockito.spy(new PhoneTableAction());
        phoneNote = Mockito.spy(new PhoneNote());
        addPhonePresenter.attach(addPhoneView);
//        addPhonePresenter.setDataBaseAction(phoneTableAction);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        addPhonePresenter.detach();
    }

    public void testQueryPhoneNameAndProjectName() {
        List<PhoneNote> phoneNotes = new ArrayList<>();
        List<String> list = new ArrayList<>();
        Mockito.doReturn(phoneNotes).when(phoneTableAction).queryAll();
        addPhonePresenter.queryPhoneNameAndProjectName();
        Mockito.verify(addPhoneView).onQueryPhoneNameResult(list);
        Mockito.verify(addPhoneView).onQueryProjectNameResult(list);
    }
}
