package android.wuliqing.com.lendphonesystemapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.wuliqing.com.lendphonesystemapp.R;


public class InputDialogFragment extends DialogFragment {
    private int res_id;
    private String title_string;
    private String hint_string;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    public static InputDialogFragment newInstance(int id, String title, String hint) {
        InputDialogFragment fragment = new InputDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, 0);
        args.putString(ARG_PARAM2, title);
        args.putString(ARG_PARAM3,hint);
        fragment.setArguments(args);
        return fragment;
    }

    public InputDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            res_id = getArguments().getInt(ARG_PARAM1);
            title_string = getArguments().getString(ARG_PARAM2);
            hint_string = getArguments().getString(ARG_PARAM3);
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_input_dialog, null);
//        final TextView title = (TextView) v.findViewById(R.id.input_dialog_title);
//        title.setText(title_string);
        final EditText text = (EditText) v.findViewById(R.id.input_dialog_edit);
//        if (!TextUtils.isEmpty(title_string)) {
//            text.setHint(title_string);
//        }
        if (!TextUtils.isEmpty(hint_string)) {
            text.setText(hint_string);
            text.setSelection(hint_string.length());
        }
        final Button cancel_button = (Button) v.findViewById(R.id.input_dialog_cancel);
        final Button ok_button = (Button) v.findViewById(R.id.input_dialog_ok);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = text.getText().toString();
                if (!TextUtils.isEmpty(str)){
                    if (null != mListener){
                        mListener.onFragmentInteraction(str, res_id);
                    }
                }
                dismiss();
            }
        });
        builder.setView(v);
//        final Dialog dialog = builder.create();
        setCancelable(false);
        return builder.create();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_input_dialog, container, false);
//    }

    private OnFragmentInteractionListener mListener;
    public void setListener(OnFragmentInteractionListener listener){
        mListener = listener;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String text, int id);
    }

}
