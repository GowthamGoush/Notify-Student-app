package com.example.studentnotify;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyCustomDialog4 extends DialogFragment {

    private static final String TAG = "MyCustomDialog";

    public interface OnInputSelected{
        void sendInput(String input,String input1,boolean isTeacher);
    }
    public OnInputSelected mOnInputSelected;

    //widgets
    private EditText mInput,mInput1;
    private TextView mActionSign, mActionCancel;
    private CheckBox checkBox1,checkBox2;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_my_custom4, container, false);
        mActionSign = view.findViewById(R.id.action_ok);
        mActionCancel = view.findViewById(R.id.action_cancel);
        mInput = view.findViewById(R.id.inputName);
        mInput1 = view.findViewById(R.id.inputId);
        checkBox1 = view.findViewById(R.id.checkTeacher);
        checkBox2 = view.findViewById(R.id.checkStudent);



        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        mActionSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input = mInput.getText().toString().trim();
                String input1 = mInput1.getText().toString().trim();

                if (!input.equals("") && !input.isEmpty()) {
                    if(checkBox1.isChecked()) {
                        mOnInputSelected.sendInput(input, input1,true);
                    }
                    else if(checkBox2.isChecked()){
                        mOnInputSelected.sendInput(input, input1,false);
                    }
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
            mOnInputSelected = (OnInputSelected) context;
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }

}
