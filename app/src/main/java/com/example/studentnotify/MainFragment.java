package com.example.studentnotify;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentnotify.ui.main.BlankFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainFragment extends Fragment implements MyCustomDialog2.OnInputSelected,MyCustomDialog3.OnInputSelected {

    private static final String TAG = "MainFragment";

    private FloatingActionButton fab2;
    private ListView listView;
    private ArrayList<pdfDetails> pdfDetailsList;
    private ArrayList<String> nameList;
    private ArrayAdapter<String> arrayAdapter;
    private RelativeLayout uploadLayout;
    private int pos;

    @Override
    public void sendInput(String input,String input1) {
        pdfDetailsList.add(new pdfDetails(input,input1));
        nameList.add(input);
        uploadLayout.setVisibility(View.GONE);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendInput2(Boolean input) {
        pdfDetailsList.remove(pos);
        nameList.remove(pos);
        arrayAdapter.notifyDataSetChanged();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        listView = view.findViewById(R.id.pdfListView);
        fab2 = view.findViewById(R.id.fab2);
        uploadLayout = view.findViewById(R.id.uploadLayout);
        uploadLayout.setVisibility(View.GONE);

        loadData();

        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nameList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position;
                pdfDetails pdfDetails = pdfDetailsList.get(pos);
                String url = pdfDetails.getPdfUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(Intent.createChooser(intent, "Browse with"));
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCustomDialog2 dialog2 = new MyCustomDialog2();
                dialog2.setTargetFragment(MainFragment.this, 1);
                dialog2.show(getFragmentManager(), "MyCustomDialog2");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MyCustomDialog3 dialog3 = new MyCustomDialog3();
                dialog3.setTargetFragment(MainFragment.this, 1);
                dialog3.show(getFragmentManager(), "MyCustomDialog3");
                pos = position;
                return true;
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Shared preferences2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json1 = gson.toJson(pdfDetailsList);
        String json2 = gson.toJson(nameList);
        editor.putString("PDF list", json1);
        editor.putString("PDF Name List", json2);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Shared preferences2", Context.MODE_PRIVATE);

        Gson gson = new Gson();

        String json1 = sharedPreferences.getString("PDF list", null);
        String json2 = sharedPreferences.getString("PDF Name List", null);

        Type type1 = new TypeToken<ArrayList<pdfDetails>>() {
        }.getType();
        pdfDetailsList = gson.fromJson(json1, type1);
        if (pdfDetailsList == null) {
            pdfDetailsList = new ArrayList<>();
        }

        Type type2 = new TypeToken<ArrayList<String>>() {
        }.getType();
        nameList = gson.fromJson(json2, type2);
        if (nameList == null) {
            nameList = new ArrayList<>();
            uploadLayout.setVisibility(View.VISIBLE);
        }
    }

}
