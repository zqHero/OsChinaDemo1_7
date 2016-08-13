package com.hero.zhaoq.oschinademo1_7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.hero.zhaoq.oschinademo1_7.ui.Main;

/**
 * 应用程序的  启动界面   启动类
 * 显示欢迎界面   并跳转到   主界面
 */
public class AppStart extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = View.inflate(this,R.layout.start,null);
        setContentView(view);

        //添加动画  给主界面
        addAnimation(view);

        //TODO 版本兼容  问题
    }

    /**
     * 添加动画
     * @param view
     */
    private void addAnimation(final View view) {
        //渐变  展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.4f,1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        //给动画  设置监听事件   动画执行完  调到新的界面
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束  跳转到新的界面
                redirectTo();
            }
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
        Intent intent = new Intent(this,Main.class);
        startActivity(intent);
        finish();
    }
}
