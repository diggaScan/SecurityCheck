package com.sunland.securitycheck.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunland.securitycheck.R;

import java.util.ArrayList;
import java.util.List;

public class VpAdapter_main extends PagerAdapter {

    private Context context;
    private List<String> names;
    private List<Integer> icons;
    private int page_num;
    private int num_per_page;
    private LayoutInflater inflater;
    private List<View> views;
    private OnItemClickedListener listener;

    public VpAdapter_main(Context context, List<String> names, List<Integer> icons, int page_num, int num_per_page) {
        super();
        this.context = context;
        this.names = names;
        this.icons = icons;
        this.page_num = page_num;
        this.num_per_page = num_per_page;
        inflater = LayoutInflater.from(context);
        initContent();
    }

    public void setOnItemClickListener(OnItemClickedListener listener) {
        this.listener = listener;
    }

    private void initContent() {
        views = new ArrayList<>();
        for (int i = 0; i < page_num; i++) {
            final int num = i;
            View view = inflater.inflate(R.layout.vp_item__view, null);
            GridView gridView = view.findViewById(R.id.grid);
            List<String> grid_names = new ArrayList<>();
            List<Integer> grid_icons = new ArrayList<>();
            for (int a = 0; a < num_per_page; a++) {
                if ((a + i * num_per_page) >= names.size())
                    break;
                int index = a + i * num_per_page;
                grid_names.add(names.get(index));
                grid_icons.add(icons.get(index));
            }

            GvAdapter_vp adapter = new GvAdapter_vp(inflater, grid_names, grid_icons, page_num);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (listener != null) {
                        listener.onClick(position + num * num_per_page);
                    }
                }
            });
            views.add(view);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return page_num;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    class GvAdapter_vp extends BaseAdapter {

        private LayoutInflater inflater;
        private List<String> names;
        private List<Integer> icons;
        private int page_num;
        private Resources res;

        public GvAdapter_vp(LayoutInflater inflater, List<String> names, List<Integer> icons, int page_num) {
            super();
            this.inflater = inflater;
            this.names = names;
            this.icons = icons;
            this.page_num = page_num;
            res = context.getResources();
        }

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView != null) {
                GvViewHolder holder = (GvViewHolder) convertView.getTag();
                holder.textView.setText(names.get(position));
                holder.imageView.setImageDrawable(res.getDrawable(icons.get(position)));
                return convertView;
            } else {
                View view = inflater.inflate(R.layout.gv_item_view, null);
                GvViewHolder holder = new GvViewHolder(view);
                holder.textView.setText(names.get(position));
                holder.imageView.setImageDrawable(res.getDrawable(icons.get(position)));
                view.setTag(holder);
                return view;
            }
        }
    }

    class GvViewHolder {
        public TextView textView;
        public ImageView imageView;

        GvViewHolder(View itemView) {
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


}
