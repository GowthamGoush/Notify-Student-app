package com.example.studentnotify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.Frag1_Viewholder> {

    ArrayList<SubjectDetails> Frag1List;

    public class Frag1_Viewholder extends RecyclerView.ViewHolder {

        public TextView textView1, textView2, textView3, textView4,textView5;
        public RelativeLayout expandableLayout;
        public CardView cardView;
        public ImageView imageView1, imageView2, imageView;

        public Frag1_Viewholder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.content_text1);
            textView2 = itemView.findViewById(R.id.content_text2);
            textView3 = itemView.findViewById(R.id.content_text3);
            textView4 = itemView.findViewById(R.id.attendPercent1);
            textView5 = itemView.findViewById(R.id.atdTitle);
            expandableLayout = itemView.findViewById(R.id.fragment1_content);
            cardView = itemView.findViewById(R.id.frag1_card);
            imageView = itemView.findViewById(R.id.frag_image);
            imageView1 = itemView.findViewById(R.id.frag_attend);
            imageView2 = itemView.findViewById(R.id.frag_bunk);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubjectDetails item = Frag1List.get(getAdapterPosition());
                    item.setExpanded(!item.getExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Frag1List.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });

            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Frag1List.get(getAdapterPosition()).setAttended(Frag1List.get(getAdapterPosition()).getAttended() + 1);
                    int Num = Frag1List.get(getAdapterPosition()).getAttended();
                    int dNum = Frag1List.get(getAdapterPosition()).getBunked() + Frag1List.get(getAdapterPosition()).getAttended();
                    int percent = Num * 100 / dNum;
                    textView4.setText(percent + "%  (" + Num + "/" + dNum + ")");
                }
            });

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Frag1List.get(getAdapterPosition()).setBunked(Frag1List.get(getAdapterPosition()).getBunked() + 1);
                    int Num = Frag1List.get(getAdapterPosition()).getAttended();
                    int dNum = Frag1List.get(getAdapterPosition()).getBunked() + Frag1List.get(getAdapterPosition()).getAttended();
                    int percent = Num * 100 / dNum;
                    textView4.setText(percent + "%  (" + Num + "/" + dNum + ")");
                }
            });

        }
    }

    public Recycler_Adapter(ArrayList<SubjectDetails> List) {
        Frag1List = List;
    }

    @NonNull
    @Override
    public Frag1_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjectcards, parent, false);
        Frag1_Viewholder fvh = new Frag1_Viewholder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull Frag1_Viewholder holder, int position) {

        SubjectDetails Item = Frag1List.get(position);

        holder.textView1.setText(Item.getSubject());
        holder.textView2.setText(Item.getContent());
        holder.textView3.setText(Item.getDescription());

        boolean isExpanded = Frag1List.get(position).getExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.imageView1.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        holder.imageView2.setVisibility(isExpanded ? View.GONE : View.VISIBLE);

        if (Item.getAttended() + Item.getBunked() != 0) {
            int Num = Item.getAttended();
            int dNum = Item.getAttended() + Item.getBunked();
            int percent = Num * 100 / dNum;
            holder.textView4.setText(percent + "%  (" + Num + "/" + dNum + ")");
        }

        holder.textView4.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        holder.textView5.setVisibility(isExpanded ? View.GONE : View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return Frag1List.size();
    }
}
