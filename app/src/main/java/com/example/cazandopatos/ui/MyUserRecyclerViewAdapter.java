package com.example.cazandopatos.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cazandopatos.R;
import com.example.cazandopatos.models.User;

import java.util.List;


public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;

    public MyUserRecyclerViewAdapter(List<User> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        int pos = position + 1;
        holder.tv_position.setText(pos + "º");
        holder.tv_ducks.setText(String.valueOf(mValues.get(position).getDucks()));
        holder.tv_nick.setText(mValues.get(position).getNick());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tv_position;
        public final TextView tv_ducks;
        public final TextView tv_nick;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_position = view.findViewById(R.id.textViewPosition);
            tv_ducks =  view.findViewById(R.id.textViewDucks);
            tv_nick =  view.findViewById(R.id.textViewNick);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tv_nick.getText() + "'";
        }
    }
}