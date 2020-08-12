package com.example.studentnotify;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyCustomDialog3 extends DialogFragment{

    private static final String TAG = "MyCustomDialog";

    public interface OnInputSelected{
        void sendInput2(Boolean input);
    }
    public OnInputSelected mOnInputSelected;

    //widgets
    private TextView mActionOk,mActionCancel;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_my_custom3, container, false);
        mActionOk = view.findViewById(R.id.action_ok3);
        mActionCancel = view.findViewById(R.id.action_cancel3);

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean input = true;
                mOnInputSelected.sendInput2(input);
                getDialog().dismiss();
            }
        });

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
