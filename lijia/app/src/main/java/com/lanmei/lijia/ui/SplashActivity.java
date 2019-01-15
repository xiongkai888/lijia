package com.lanmei.lijia.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.GuideViewPagerAdapter;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.SharedAccount;
import com.xson.common.utils.DeviceUtils;
import com.xson.common.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页、启动页
 */
public class SplashActivity extends AppCompatActivity  {

    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private Button mSkipIv;//立即体验
    private ImageView mLaunchIv;//启动图片
    int[] guide = new int[]{R.drawable.guidance1,
            R.drawable.guidance2,
            R.drawable.guidance3};
    boolean isNoFirstLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_splash);
        isNoFirstLogin = SharedAccount.getInstance(this).isNoFirstLogin();
        if (!isNoFirstLogin) {//第一次进入该应用
            initViewPager();
        } else {
            mLaunchIv = (ImageView) findViewById(R.id.launch_iv);
            mLaunchIv.setVisibility(View.VISIBLE);
        }
        initIMI();
    }


    public final static int REQUEST_READ_PHONE_STATE = 1;
    private void initIMI() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
            LiJiaApp.pcode = DeviceUtils.getIMEI(this);
            SharedAccount.getInstance(this).savePcode(LiJiaApp.pcode);//防止手动杀掉应用是LiJiaApp.pcode == "";
            if (isNoFirstLogin){
                handHome();
            }
        }
    }


    private void handHome(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    LiJiaApp.pcode = DeviceUtils.getIMEI(this);
                    SharedAccount.getInstance(this).savePcode(LiJiaApp.pcode);
                    if (isNoFirstLogin){
                        handHome();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void enterHomeActivity() {
        if (CommonUtils.isLogin(this)) {
            IntentUtil.startActivity(this, MainActivity.class);
        }
        finish();
    }

    private void initViewPager() {
        vp = (ViewPager) findViewById(R.id.vp_guide);
        mSkipIv = (Button) findViewById(R.id.skip_tv);
        mSkipIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedAccount.getInstance(SplashActivity.this).setNoFirstLogin(true);
                enterHomeActivity();
            }
        });
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    mSkipIv.setVisibility(View.VISIBLE);
                } else {
                    mSkipIv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 初始化引导页视图列表
        views = new ArrayList<View>();
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.guid_view, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageResource(guide[i]);
            views.add(view);
        }
        vp = (ViewPager) findViewById(R.id.vp_guide);
        // 初始化adapter
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
    }
}
