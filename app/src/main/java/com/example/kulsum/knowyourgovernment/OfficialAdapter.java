package com.example.kulsum.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private static final String TAG = "OfficialAdapter";
    private List<Official> list;
    private MainActivity mainAct;
    String side;

    public OfficialAdapter(List<Official> list, MainActivity ma) {
        this.list = list;
        mainAct = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_list, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Official official = list.get(position);

        holder.officeDetail.setText(official.getOfficialDetails());
        if(official.getSide()!=null&&official.getSide()!=""){
            side = "("+official.getSide()+")";
        }
        else
        { side = "";}
        Log.d(TAG, "getside: " + side);
        holder.officialDetail.setText(official.getOfficialName()+" "+side);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
