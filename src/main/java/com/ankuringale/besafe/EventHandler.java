package com.ankuringale.besafe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

public class EventHandler extends RecyclerView.Adapter<EventHandler.MyViewHolder> {
    private static List<Disaster> mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView text,date;
        public MyViewHolder(View v) {
            super(v);
            text = v.findViewById(R.id.textvieww);
            date = v.findViewById(R.id.dateView);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = getAdapterPosition();
                    if(p!=RecyclerView.NO_POSITION){
                        Disaster d = mDataset.get(p);
                        Intent i = new Intent(context,FullStoryActivity.class);
                        i.putExtra("url",d.getStoryLink());
                        context.startActivity(i);
                    }
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventHandler(List<Disaster> myDataset , Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventHandler.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new vie
        View v =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_view
                                , parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.text.setText(mDataset.get(position).getTitle());
        holder.date.setText(mDataset.get(position).getDate());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

