package com.sunland.securitycheck.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunland.netmodule.Global;
import com.sunland.securitycheck.GlideApp;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.V_config;
import com.sunland.securitycheck.bean.i_check_person_list.TSummitPersion;
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
        myViewHolder.tv_lxdh.setText(info.getLxrdh());
        myViewHolder.tv_rylx.setText(info.getPeopleTypeStr());
        GlideApp.with(context).asBitmap()
                .load("http://" + Global.ip + ":" + Global.port + info.getIcon())
                .placeholder(R.drawable.pass_profile)
                .error(R.drawable.pass_profile)
                .into(myViewHolder.iv_icon);

        myViewHolder.tv_hc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", sfzh);
                bundle.putString("yhdm", V_config.YHDM);
                bundle.putString("jysfzh", V_config.JYSFZH);
                bundle.putString("jyxm", V_config.JYXM);
                bundle.putString("jybmbh", V_config.JYBMBH);
                bundle.putString("lbr", "02");
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
        ImageView iv_icon;
        TextView tv_lxdh;
        TextView tv_rylx;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.name);
            tv_num = itemView.findViewById(R.id.paper_num);
            tv_yxq = itemView.findViewById(R.id.yxq);
            tv_gj = itemView.findViewById(R.id.gj);
            rl_container = itemView.findViewById(R.id.name_container);
            tv_hc = itemView.findViewById(R.id.hc);
            iv_icon = itemView.findViewById(R.id.icon);
            tv_rylx = itemView.findViewById(R.id.rylx);
            tv_lxdh = itemView.findViewById(R.id.lxdh);

        }
    }

    public interface OnItemClickedListener {
        void onClicked();
    }
}
