package com.xson.common.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.umeng.analytics.MobclickAgent;
import com.xson.common.utils.L;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final int RC_PERMISSION_STORAGE = 110;
    private boolean isAttached;
    private ArrayList<DispatchTouchEventListener> dispatchTouchEventListeners;

    /**
     * @return contentViewId   布局Id
     */
    public abstract int getContentViewId();

    /**
     * @param savedInstanceState onCreate()中的参数,Bundle类型
     */
    protected abstract void initAllMembersView(Bundle savedInstanceState);

    public interface DispatchTouchEventListener {
        void onDispatchTouchEvent(MotionEvent event);
    }

    public void initIntent(Intent intent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ((BaseApp)getApplication()).watch(this);
        ButterKnife.reset(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initIntent(getIntent());
        ButterKnife.inject(this, this);
        initAllMembersView(savedInstanceState);
    }


    public Context getContext() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttached = false;
    }

    public boolean isAttached() {
        return isAttached;
    }

    public boolean isDetached() {
        return !isAttached;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (dispatchTouchEventListeners != null) {
            for (DispatchTouchEventListener l : dispatchTouchEventListeners) {
                l.onDispatchTouchEvent(ev);
            }
        }
        //fix PhotoViewAttacher bug
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            L.e(e);
            return true;
        }
    }

    public void addDispatchTouchEventListener(DispatchTouchEventListener l) {
        if (dispatchTouchEventListeners == null)
            dispatchTouchEventListeners = new ArrayList<>();
        dispatchTouchEventListeners.add(l);
    }

    public void removeDispatchTouchEventListener(DispatchTouchEventListener l) {
        if (dispatchTouchEventListeners == null)
            return;
        dispatchTouchEventListeners.remove(l);
    }

}
