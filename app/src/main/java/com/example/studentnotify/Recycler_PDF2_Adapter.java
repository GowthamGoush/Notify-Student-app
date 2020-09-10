package com.example.studentnotify;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recycler_PDF2_Adapter extends RecyclerView.Adapter<Recycler_PDF2_Adapter.PDF_ViewHolder>{

    ArrayList<pdfDetails> pdfList;
    Activity mActivity;
    OnItemClickListener mListener;

    public class PDF_ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{

        public TextView pdfName;
        public CardView cardView;

        public PDF_ViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfName = itemView.findViewById(R.id.pdfName);
            cardView = itemView.findViewById(R.id.pdfCard);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem upload = menu.add(Menu.NONE,1,1,"Upload");
            MenuItem remove = menu.add(Menu.NONE,2,2,"Remove");
            MenuItem delete = menu.add(Menu.NONE,3,3,"Delete");

            upload.setOnMenuItemClickListener(this);
            remove.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null){

                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION){

                    switch (item.getItemId()) {

                        case 1 : mListener.onUploadClicked(position);
                                 return true;

                        case 2 : mListener.onRemoveClicked(position);
                                 return true;

                        case 3 : mListener.onDeleteClicked(position);
                                 return true;
                    }
                }
            }
            return false;
        }
    }

    public Recycler_PDF2_Adapter(ArrayList<pdfDetails> List, Activity activity) {
        pdfList = List;
        mActivity = activity;
    }

    @NonNull
    @Override
    public PDF_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdfcards,parent,false);
        PDF_ViewHolder pvh = new PDF_ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PDF_ViewHolder holder, final int position) {

        final pdfDetails pdfDetail = pdfList.get(position);

        String name = pdfDetail.getPdfName();

        holder.pdfName.setText(name);
/*
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mActivity,"Download",Toast.LENGTH_SHORT);
                String url = pdfDetail.getPdfUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mActivity.startActivity(Intent.createChooser(intent, "Browse with"));
                return false;
            }
        });

 */

    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public interface OnItemClickListener {
        void onUploadClicked(int position);
        void onRemoveClicked(int position);
        void onDeleteClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

}
