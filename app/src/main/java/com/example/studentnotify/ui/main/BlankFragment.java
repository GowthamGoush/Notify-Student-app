package com.example.studentnotify.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentnotify.MainFragment;
import com.example.studentnotify.MyCustomDialog;
import com.example.studentnotify.R;
import com.example.studentnotify.Recycler_Adapter;
import com.example.studentnotify.SubjectDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BlankFragment extends Fragment implements MyCustomDialog.OnInputSelected {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<SubjectDetails> Items;
    private View view;

    @Override
    public void sendInput(String input,String input1,String input2) {
        if(input1.isEmpty()){
            input1 = "Not available";
        }
        if(input2.isEmpty()){
            input2 = "Not available";
        }
        Items.add(new SubjectDetails(input,"Credits : "+input1,"Materials : "+input2,0,0));
        Snackbar.make(view,"1 item added",Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blank, container, false);

        loadData();

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        adapter = new Recycler_Adapter(Items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCustomDialog dialog = new MyCustomDialog();
                dialog.setTargetFragment(BlankFragment.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy>0 && fab.isShown())
                {
                    fab.hide();
                }
                if (dy<0)
                {
                    fab.show();
                }
            }
        });
        
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }

    private void saveData(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Items);
        editor.putString("task list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<SubjectDetails>>() {}.getType();
        Items = gson.fromJson(json, type);
        if (Items == null) {
            Items = new ArrayList<>();
        }
    }
}