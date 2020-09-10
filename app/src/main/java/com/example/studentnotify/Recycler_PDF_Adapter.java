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

public class Recycler_PDF_Adapter extends RecyclerView.Adapter<Recycler_PDF_Adapter.PDF_ViewHolder> {

    ArrayList<pdfDetails> pdfList;
    Activity mActivity;

    public class PDF_ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener , MenuItem.OnMenuItemClickListener{

        public TextView pdfName;
        public CardView cardView;

        public PDF_ViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfName = itemView.findViewById(R.id.pdfName);
            cardView = itemView.findViewById(R.id.pdfCard);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int pos = getAdapterPosition();

            if(pos != RecyclerView.NO_POSITION && item.getItemId() == 1){
                pdfDetails pdfDetail = pdfList.get(pos);
                String url = pdfDetail.getPdfUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mActivity.startActivity(Intent.createChooser(intent, "Browse with"));
                return true;
            }

            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem download = menu.add(Menu.NONE,1,1,"Download");

            download.setOnMenuItemClickListener(this);
        }
    }

    public Recycler_PDF_Adapter(ArrayList<pdfDetails> List, Activity activity) {
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

        /*holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mActivity,"Download",Toast.LENGTH_SHORT);
                String url = pdfDetail.getPdfUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mActivity.startActivity(Intent.createChooser(intent, "Browse with"));
                return true;
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

}
