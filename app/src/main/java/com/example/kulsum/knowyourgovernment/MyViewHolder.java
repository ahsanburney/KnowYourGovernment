package com.example.kulsum.knowyourgovernment;



import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView officeDetail;
    public TextView officialDetail;


    public MyViewHolder(View view) {
        super(view);
        officeDetail = (TextView) view.findViewById(R.id.block1);
        officialDetail = (TextView) view.findViewById(R.id.block2);

    }
}
