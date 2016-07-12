package android.wuliqing.com.lendphonesystemapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.wuliqing.com.lendphonesystemapp.R;


public class MyDialogFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DialogListener mDialogListener;
    private String dialog_title;
    private String dialog_msg;

    public MyDialogFragment() {

    }

    public static MyDialogFragment newInstance(String title, String msg, DialogListener dialogListener) {
        MyDialogFragment fragment = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, msg);
        fragment.setArguments(args);
        fragment.setDialogListener(dialogListener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dialog_title = getArguments().getString(ARG_PARAM1);
            dialog_msg = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_view, null);
        final TextView title_view = (TextView) v.findViewById(R.id.dialog_title);
        final TextView message_view = (TextView) v.findViewById(R.id.dialog_msg);
        title_view.setText(dialog_title);
        message_view.setText(dialog_msg);
        final Button cancel_button = (Button) v.findViewById(R.id.dialog_cancel);
        final Button save_button = (Button) v.findViewById(R.id.dialog_ok);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.onClickDialogCancel();
                }
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.onClickDialogOk();
                }
            }
        });
        builder.setView(v);
        final Dialog dialog = builder.create();
        setCancelable(false);
        return builder.create();
    }

    public void setDialogListener(DialogListener dialogListener) {
        mDialogListener = dialogListener;
    }

    public interface DialogListener {
        void onClickDialogOk();

        void onClickDialogCancel();
    }
}
