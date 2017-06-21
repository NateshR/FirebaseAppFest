package admin.complaintchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.complaintcheflib.util.BaseAppCompatActivity;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class MainActivity extends BaseAppCompatActivity {

    @BindView(R.id.tl_main)
    TabLayout tableLayout;
    @BindView(R.id.vp_main)
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tableLayout.setupWithViewPager(viewPager);
    }

    private class MyAdapter extends FragmentStatePagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ListFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "On-GOING";
                case 1:
                    return "COMPLETED";
                case 2:
                    return "REJECTED";
            }
            return null;
        }
    }
}
