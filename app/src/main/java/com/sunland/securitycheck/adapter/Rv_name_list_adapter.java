package com.sunland.securitycheck.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunland.securitycheck.R;
import com.sunland.securitycheck.bean.TSummitPersion;
import com.sunland.securitycheck.utils.UtilsString;

import java.util.List;

public class Rv_name_list_adapter extends RecyclerView.Adapter<Rv_name_list_adapter.MyViewHolder> {

    private List<TSummitPersion> dataSet;
    private Context context;
    private LayoutInflater inflater;

    public Rv_name_list_adapter(Context context, List<TSummitPersion> dataSet) {
        super();
        this.context = context;
        this.dataSet = dataSet;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public Rv_name_list_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.rv_item_name_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Rv_name_list_adapter.MyViewHolder myViewHolder, int i) {
        final TSummitPersion info = dataSet.get(i);
        myViewHolder.tv_name.setText(info.getPaperName());
        final String sfzh = info.getSfzh();
        myViewHolder.tv_num.setText(sfzh);
        myViewHolder.tv_yxq.setText(info.getYxq());
        myViewHolder.tv_gj.setText(info.getNationStr());

        myViewHolder.tv_hc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", sfzh);
                Intent intent = new Intent();
                intent.setAction("com.sunland.intent.action.QUERY_ID");
                intent.putExtra("bundle", bundle);
                if (UtilsString.checkId(sfzh).equals("")) {
                    Toast.makeText(context, "此人身份证号为无效格式", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "未安装核查应用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_container;
        TextView tv_name;
        TextView tv_num;
        TextView tv_yxq;
        TextView tv_gj;
        TextView tv_hc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.name);
            tv_num = itemView.findViewById(R.id.paper_num);
            tv_yxq = itemView.findViewById(R.id.yxq);
            tv_gj = itemView.findViewById(R.id.gj);
            rl_container = itemView.findViewById(R.id.name_container);
            tv_hc = itemView.findViewById(R.id.hc);
        }
    }

    public interface OnItemClickedListener {
        void onClicked();
    }
}
