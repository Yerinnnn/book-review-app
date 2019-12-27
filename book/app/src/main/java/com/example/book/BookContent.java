package com.example.book;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.FragmentManager;


public class BookContent extends AppCompatActivity {
    private static final String TAG = "BookContent";
//    private Memo memo_Main;
//    public List<Memo> list = new ArrayList<>();

    public byte[] bCover;
    public String bScore;
    public String bTitle;
    public String bWriter;
    public String bPublisher;
    public String bStartDate;
    public String bFinishDate;
    public String bComment;

    public Bundle bundle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_content);

        bCover = getIntent().getByteArrayExtra("bCover");
        bScore = getIntent().getStringExtra("bScore");
        bTitle = getIntent().getStringExtra("bTitle");
        bWriter = getIntent().getStringExtra("bWriter");
        bPublisher = getIntent().getStringExtra("bPublisher");
        bStartDate = getIntent().getStringExtra("bStartDate");
        bFinishDate = getIntent().getStringExtra("bFinishDate");
        bComment = getIntent().getStringExtra("bComment");


        //TabLayout
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("책 정보"));
        tabs.addTab(tabs.newTab().setText("메모"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어답터설정
        final ViewPager viewPager = findViewById(R.id.viewpager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(pagerAdapter);

        //탭메뉴를 클릭하면 해당 프래그먼트로 변경-싱크화
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int NumOfTabs;

        public PagerAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.NumOfTabs = numOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    book_content tab1 = new book_content();

                    bundle = new Bundle(8);

                    Log.d(TAG, "94 bCover: " + bCover + "**********************************************************************************");

                    bundle.putByteArray("bCover", bCover);
                    bundle.putString("bScore", bScore);
                    bundle.putString("bTitle", bTitle);
                    bundle.putString("bWriter", bWriter);
                    bundle.putString("bPublisher", bPublisher);
                    bundle.putString("bStartDate", bStartDate);
                    bundle.putString("bFinishDate", bFinishDate);
                    bundle.putString("bComment", bComment);

                    tab1.setArguments(bundle);

                    return tab1;
                case 1:
                    memo_list tab2 = new memo_list();

                    bundle = new Bundle(1);

                    bundle.putByteArray("bCover", bCover);
                    bundle.putString("bScore", bScore);
                    bundle.putString("bTitle", bTitle);
                    bundle.putString("bWriter", bWriter);
                    bundle.putString("bPublisher", bPublisher);
                    bundle.putString("bStartDate", bStartDate);
                    bundle.putString("bFinishDate", bFinishDate);
                    bundle.putString("bComment", bComment);

                    tab2.setArguments(bundle);

                    Log.d(TAG, "114 bTitle: " + bTitle + "===============================================================================");

                    return tab2;
                default:
                    return null;
            }
        }


        @Override
        public int getCount() {
            return NumOfTabs;
        }
    }

}
