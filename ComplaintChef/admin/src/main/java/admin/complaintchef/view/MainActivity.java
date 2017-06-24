package admin.complaintchef.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import admin.complaintchef.R;
import common.complaintcheflib.view.BaseAppCompatActivity;
import common.complaintcheflib.view.ListFragment;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class MainActivity extends BaseAppCompatActivity {

    TabLayout tableLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = (TabLayout) findViewById(R.id.tl_main);
        viewPager = (ViewPager) findViewById(R.id.vp_main);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tableLayout.setupWithViewPager(viewPager);
    }

    private class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ListFragment.newInstance(ListFragment.LIST_TYPE.PENDING);
                case 1:
                    return ListFragment.newInstance(ListFragment.LIST_TYPE.ACCEPTED);
                case 2:
                    return ListFragment.newInstance(ListFragment.LIST_TYPE.DECLINED);
                default:
                    throw new RuntimeException("Type Not Supported");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "On-GOING";
                case 1:
                    return "ACCEPTED";
                case 2:
                    return "REJECTED";
            }
            return null;
        }
    }
}
