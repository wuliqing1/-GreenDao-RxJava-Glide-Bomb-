package android.wuliqing.com.lendphonesystemapp.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.dataBase.DBHelper;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

public class SettingFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener {
    private Preference change_theme_preference;
    private Preference clear_cache_Preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        change_theme_preference = findPreference("change_theme");
        clear_cache_Preference = findPreference("clear_cache");
        change_theme_preference.setOnPreferenceClickListener(this);
        clear_cache_Preference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == change_theme_preference) {

        } else if (preference == clear_cache_Preference) {
            showClearCacheDialog();
        }
        return true;
    }

    private void showClearCacheDialog() {
        MyDialogFragment.newInstance("", getString(R.string.exit_app_dialog_msg), new MyDialogFragment.DialogListener() {
            @Override
            public void onClickDialogOk() {
                DBHelper.getInstance().getPhoneNoteDao().deleteAll();
                DBHelper.getInstance().getLendPhoneNoteDao().deleteAll();
                ToastUtils.show(getActivity(), R.string.clear_database_success);
            }

            @Override
            public void onClickDialogCancel() {

            }
        }).show(getActivity().getFragmentManager(), "");
    }
}
