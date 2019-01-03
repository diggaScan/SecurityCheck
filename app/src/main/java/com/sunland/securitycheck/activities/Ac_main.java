package com.sunland.securitycheck.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.sunland.securitycheck.DataModel;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.adapter.OnItemClickedListener;
import com.sunland.securitycheck.adapter.VpAdapter_main;
import com.sunland.securitycheck.customView.BannerIndicator;

import java.util.Arrays;

import butterknife.BindView;

public class Ac_main extends Ac_base {

    @BindView(R.id.page_indicator)
    public BannerIndicator bi_indicator;
    @BindView(R.id.pager)
    public ViewPager vp_pager;
    private final int num_per_page = 6;
    private int backPressed_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ac_main);
        showNavIcon(false);
        setToolbarTitle("峰会安保辖区人员安检");
        initView();
    }

    private void initView() {
        int location_nums = DataModel.conference_locs.length;
        int page_num = location_nums % 6 == 0 ? location_nums / 6 : location_nums / 6 + 1;
        bi_indicator.setItem_nums(page_num);
        bi_indicator.setDotsColor(Color.WHITE);
        bi_indicator.setMovingDotColor(getResources().getColor(R.color.colorPrimary));

        Integer[] icons = {R.drawable.ic_g20, R.drawable.ic_b20, R.drawable.ic_xhabq, R.drawable.ic_hlbk, R.drawable.ic_xixi, R.drawable.ic_xsjc,
                R.drawable.ic_sjq, R.drawable.ic_jzzd, R.drawable.ic_bingjiang};
        VpAdapter_main adapter = new VpAdapter_main(this, Arrays.asList(DataModel.conference_locs),
                Arrays.asList(icons), page_num, num_per_page);
        adapter.setOnItemClickListener(new OnItemClickedListener() {
            @Override
            public void onClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("title", DataModel.conference_locs[position]);
                bundle.putString("area", DataModel.AREA_CODE[position]);
                hop2Activity(Ac_check.class, bundle);
            }
        });
        vp_pager.setAdapter(adapter);
        vp_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                bi_indicator.setCurrentPosition(i, v);

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (vp_pager.getCurrentItem() != 0) {
            vp_pager.setCurrentItem(0);
        } else {
            quitApp();
        }
    }

    public void quitApp() {
        backPressed_time++;
        if (backPressed_time != 1) {
            finish();
        } else {
            Toast.makeText(this, "再按一次，退出应用", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressed_time--;
                }
            }, 2500);
        }
    }
}
