package com.postmeapp;

import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private Bitmap bFoto = null;
    private Bitmap bDati = null;

    private String strUserName;

    private int[] tabIcons = {
            R.drawable.ic_camera_enhance_white_24dp,
            R.drawable.ic_chrome_reader_mode_white_24dp,
            R.drawable.ic_email_white_24dp};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            strUserName = extras.getString("user");

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
            setupViewPager(mViewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
            setupTabIcons(tabLayout);
        } else
            Toast.makeText(this, "No login credential passed",Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(new CapPic(), getString(R.string.capture_picture));
        mSectionsPagerAdapter.addFragment(new FillCard(),getString(R.string.data_collection));
        mSectionsPagerAdapter.addFragment(new SendCard(),getString(R.string.send_postcard));
        viewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void setupTabIcons(TabLayout tabLayout) {
        for(int i=0; i< tabIcons.length; i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }

    public Bitmap getDati() { return bDati; }

    public void setDati(Bitmap bDati) { this.bDati = bDati; }

    public Bitmap getFoto() { return bFoto; }

    public void setFoto(Bitmap bFoto) { this.bFoto = bFoto; }

    public String getUserName() { return strUserName; }

    public void setUserName(String strUserName) { this.strUserName = strUserName; }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Vector<Fragment> vFragment  = new Vector<>();
        Vector<String>   vFragTitle = new Vector<>();

        public SectionsPagerAdapter(FragmentManager fm) { super(fm); }
        
        public void addFragment(Fragment fragment, String strTitle) {
            vFragment.add(fragment);
            vFragTitle.add(strTitle);
        }

        @Override
        public Fragment getItem(int position){ return vFragment.get(position); }

        @Override
        public int getCount() { return vFragment.size(); }

        @Override
        public CharSequence getPageTitle(int position) { return vFragTitle.get(position); }
    }

}
