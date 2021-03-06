package com.jolpai.doctorsdiary.View.helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.jolpai.doctorsdiary.Model.Worker.Database.entity.Comments;
import com.jolpai.doctorsdiary.R;
import com.jolpai.doctorsdiary.Model.Worker.Others.MyDateFormat;

import java.util.List;



/**
 * Created by User on 1/22/2017.
 */

public class RecyclerAdapter_Comment extends RecyclerView.Adapter<RecyclerAdapter_Comment.View_Holder> {

        private List<Comments> list;
        private Context context;

        public RecyclerAdapter_Comment(List<Comments> list, Context context) {
            this.list = list;
            this.context = context;
        }


        public class View_Holder extends RecyclerView.ViewHolder{

            RippleView llFooterDate;
            TextView
                    textCommentDate,
                    textComment,
                    textCommentersName;
            View view;

            public View_Holder (View itemView){
                super(itemView);
                textCommentDate =(TextView) itemView.findViewById(R.id.textCommentDate);
                textComment=(TextView) itemView.findViewById(R.id.textComment);
                textCommentersName=(TextView) itemView.findViewById(R.id.textCommentersName);
                view=itemView;
            }
        }

        @Override
        public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate the layout, initialize the View Holder
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_commenters, parent, false);
            View_Holder holder = new View_Holder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(final View_Holder holder, int position) {
            //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
            final Comments comments = list.get(position);

            holder.textCommentDate.setText(MyDateFormat.getDDMMYYTT(comments.getDate()));
            holder.textComment.setText(comments.getComment());
            holder.textCommentersName.setText(comments.getPersonsName());
            holder.view.setTag(comments);


        }

        @Override
        public int getItemCount() {
            //returns the number of elements the RecyclerView will display
            return list.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

}
