package com.example.hpprobook.photogram;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public List<Comments> commentsList;
    public List<User> userList;
    public Context context;

    public CommentsRecyclerAdapter(List<Comments> commentsList, List<User> userList){

        this.commentsList = commentsList;
        this.userList = userList;

    }

    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(com.example.hpprobook.blogapp.R.layout.comment_list_item, parent, false);
        context = parent.getContext();
        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        String commentMessage = commentsList.get(position).getMessage();
        holder.setComment_message(commentMessage);
        String commentUsername = userList.get(position).getName();
        holder.setComment_username(commentUsername);

    }


    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message;
        private TextView comment_username;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setComment_message(String message){

            comment_message = mView.findViewById(com.example.hpprobook.blogapp.R.id.comment_message);
            comment_message.setText(message);

        }
        public void setComment_username(String name){
            comment_username = mView.findViewById(com.example.hpprobook.blogapp.R.id.comment_username);
            comment_username.setText(name);

        }

    }

}
