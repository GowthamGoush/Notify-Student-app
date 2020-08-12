package com.example.studentnotify;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyCustomDialog extends DialogFragment {

    private static final String TAG = "MyCustomDialog";

    public interface OnInputSelected{
        void sendInput(String input,String input1,String input2);
    }
    public OnInputSelected mOnInputSelected;

    //widgets
    private EditText mInput,mInput1,mInput2;
    private TextView mActionOk, mActionCancel;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_my_custom, container, false);
        mActionOk = view.findViewById(R.id.action_ok);
        mActionCancel = view.findViewById(R.id.action_cancel);
        mInput = view.findViewById(R.id.input);
        mInput1 = view.findViewById(R.id.input1);
        mInput2 = view.findViewById(R.id.input2);

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String input = mInput.getText().toString().trim();
                    String input1 = mInput1.getText().toString().trim();
                    String input2 = mInput2.getText().toString().trim();

                    if (!input.equals("") && !input.isEmpty()) {
                        mOnInputSelected.sendInput(input, input1, input2);
                    }
                    getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }

}
