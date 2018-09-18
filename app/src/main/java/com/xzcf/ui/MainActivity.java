package com.xzcf.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.azhon.appupdate.manager.DownloadManager;
import com.xzcf.App;
import com.xzcf.R;
import com.xzcf.service.WebSocketService;
import com.xzcf.ui.adapters.MainViewPagerAdapter;
import com.xzcf.ui.fragments.HomeFragment;
import com.xzcf.ui.fragments.MineFragment;
import com.xzcf.ui.fragments.StockFragment;
import com.xzcf.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements HomeFragment.Listener, StockFragment.Listener, MineFragment.Listener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    public static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiUtils.setStatusBarTransparent(getWindow());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestPermission();
        checkLoginStatus();

        Intent intent = new Intent(this, WebSocketService.class);
        startService(intent);

        viewPager.setAdapter(new MainViewPagerAdapter(getFM()));

        bottomNavigationBar
                .setBarBackgroundColor(R.color.colorAccent)
                .setActiveColor(R.color.bottomNavItemTitleColor)
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_checked, R.string.home_title).setInactiveIconResource(R.mipmap.ic_home_normal))
                .addItem(new BottomNavigationItem(R.mipmap.ic_stock_checked, R.string.stock_title).setInactiveIconResource(R.mipmap.ic_stock_normal))
                .addItem(new BottomNavigationItem(R.mipmap.ic_info_checked, R.string.info_title).setInactiveIconResource(R.mipmap.ic_info_normal))
                .addItem(new BottomNavigationItem(R.mipmap.ic_mine_checked, R.string.mine_title).setInactiveIconResource(R.mipmap.ic_mine_normal))
                .setMode(BottomNavigationBar.MODE_FIXED)
                .initialise();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    public void onSearch() {
        startActivity(SearchActivity.startIntent(this));
    }

    @Override
    public void downloadApk(String apkUrl) {
        System.out.println("abcd: "+apkUrl);
        DownloadManager manager = DownloadManager.getInstance(this);
        manager.setApkName("appupdate.apk")
                .setApkUrl(apkUrl)
                .setDownloadPath(Environment.getExternalStorageDirectory() + "/AppUpdate")
                .setSmallIcon(R.mipmap.ic_launcher)
                //可设置，可不设置
//                .setConfiguration(configuration)
                .download();
    }


    private void requestPermission(){
        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ) {
            //进入到这里代表没有权限.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
        }
    }

    private void checkLoginStatus(){
        try{
            if (App.context().isLogged()) {
                startTimer();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
