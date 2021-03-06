/**
 * Generated by smali2java 1.0.0.558
 * Copyright (C) 2013 Hensence.com
 */

package com.mobcent.discuz.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appbyme.dev.R;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.constant.LocationProvider;
import com.mobcent.discuz.fragments.Discovery1Fragment;
import com.mobcent.discuz.fragments.Discovery2Fragment;
import com.mobcent.discuz.fragments.Discovery3Fragment;
import com.mobcent.discuz.fragments.Discuz1Fragment;
import com.mobcent.discuz.fragments.Discuz2Fragment;
import com.mobcent.discuz.fragments.Discuz3Fragment;
import com.mobcent.discuz.fragments.DiscuzFragment;
import com.mobcent.discuz.fragments.HomeFragment;
import com.mobcent.discuz.fragments.MeFragment;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;

import org.w3c.dom.Text;

public class HomeActivity extends FragmentActivity implements BaseIntentConstant, PlazaConstant, ConfigConstant, View.OnClickListener {
    private String TAG;
    private Fragment[] fragment = new Fragment[4];
    private String[] titles = new String[]{"首页","论坛","发现","我的"};
    private Fragment currentFragment;
    private Button mStateButton1;
    private Button mStateButton2;
    private Button mStateButton3;
    private Button mStateButton4;
    private int mState = -1;
    private View mStatePostButton;

    public HomeActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);
        LinearLayout tv = (LinearLayout) findViewById(R.id.bottomBox);
        mStateButton1 = (Button)tv.findViewById(R.id.first);
        mStateButton1.setOnClickListener(this);
        mStateButton1.setSelected(true);
        mStateButton2 = (Button)tv.findViewById(R.id.second);
        mStateButton2.setOnClickListener(this);
        mStateButton3 = (Button)tv.findViewById(R.id.third);
        mStateButton3.setOnClickListener(this);
        mStateButton4 = (Button)tv.findViewById(R.id.fourth);
        mStateButton4.setOnClickListener(this);
        mStatePostButton = tv.findViewById(R.id.nav_btn);
        mStatePostButton.setOnClickListener(this);
        addBackgroundFilter(mStateButton1, mStateButton2, mStateButton3, mStateButton4);

        LoginUtils.getInstance().init(this);
        LocationProvider.getInstance().init(this);
        fragment[0] = new HomeFragment();

        fragment[1] = new DiscuzFragment();
        String[] f1 = {"版块", "最新", "精华"};
        Fragment[] fg1 = {new Discuz1Fragment(), new Discuz2Fragment(), new Discuz3Fragment()};
        ((DiscuzFragment)fragment[1]).setTitles(f1);
        ((DiscuzFragment)fragment[1]).setFragments(fg1);

        String[] f2 = {"视界", "慈善", "动漫"};
        Fragment[] fg2 = {new Discovery1Fragment(), new Discovery2Fragment(), new Discovery3Fragment()};
        fragment[2] = new DiscuzFragment();
        ((DiscuzFragment)fragment[2]).setTitles(f2);
        ((DiscuzFragment)fragment[2]).setFragments(fg2);

        fragment[3] = new MeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment[0]).commit();
        currentFragment = fragment[0];
        switchState(0);
    }

    private void addBackgroundFilter(View view, View... views) {
        addBackgroundFilter(view);
        if (views != null ) {
            for (View v : views) {
                addBackgroundFilter(v);
            }
        }
    }

    private void addBackgroundFilter(View v) {
        PorterDuffColorFilter filter = new PorterDuffColorFilter(getResources().getColor(R.color.dz_skin_custom_main_color), PorterDuff.Mode.DST_OVER);
        Drawable drawable = v.getBackground();
        drawable.setColorFilter(filter);
    }

    private void switchState(int state) {
        if (mState == state) {
            return;
        }

        setHeaderTitle(titles[state]);

        mState = state;
        mStateButton1.setSelected(false);
        mStateButton2.setSelected(false);
        mStateButton3.setSelected(false);
        mStateButton4.setSelected(false);

        switch (mState) {
            case 0:
                mStateButton1.setSelected(true);
                break;

            case 1:
                mStateButton2.setSelected(true);
                break;

            case 2:
                mStateButton3.setSelected(true);
                break;

            case 3:
                mStateButton4.setSelected(true);
                break;

            default:
                break;
        }
        onTabChange(mState);
    }

    private void setHeaderTitle(String title) {
        TextView tv = (TextView) findViewById(R.id.nav_title);
        tv.setText(title);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.first:
                switchState(0);
                break;

            case R.id.second:
                switchState(1);
                break;

            case R.id.third:
                switchState(2);
                break;

            case R.id.fourth:
                switchState(3);
                break;

            case R.id.nav_btn:
                onPublic();
                break;

            default:
                break;
        }
    }

    private void onTabChange(int position) {
        if (currentFragment == fragment[position]) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (!fragment[position].isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment)
                    .add(R.id.container, fragment[position]).commit();
        } else {
            transaction.hide(currentFragment).show(fragment[position]).commit();
        }
        currentFragment = fragment[position];
    }

    private void onPublic() {
        if (!LoginUtils.getInstance().isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        final Dialog dialog = new Dialog(this, R.style.mc_forum_home_publish_dialog);
        final LayoutInflater in = LayoutInflater.from(this);
        View view = in.inflate(R.layout.publish_dialog, null);
        view.findViewById(R.id.mc_forum_cancle_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.mc_forum_publish_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(HomeActivity.this, PublishTopicActivity.class);
                intent.putExtra("Type", "0");
                startActivity(intent);
            }
        });

        view.findViewById(R.id.mc_forum_pic_topic_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(HomeActivity.this, PublishTopicActivity.class);
                intent.putExtra("Type", "1");
                startActivity(intent);
            }
        });

        view.findViewById(R.id.mc_forum_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(HomeActivity.this, PublishTopicActivity.class);
                intent.putExtra("Type", "2");
                startActivity(intent);
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }
}
