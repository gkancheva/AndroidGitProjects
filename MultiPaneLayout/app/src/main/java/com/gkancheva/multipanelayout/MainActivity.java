package com.gkancheva.multipanelayout;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements FragmentTitles.OnArticleSelectedListener {
    private static String ARTICLE = "Article";
    private FragmentTitles mFragmentTitles;
    private FragmentDetails mFragmentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.news_letter));

        int screenOrientation = getResources().getConfiguration().orientation;

        if(screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if(savedInstanceState != null) {
                return;
            }
            mFragmentTitles = new FragmentTitles();
            mFragmentTitles.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mFragmentTitles)
                    .commit();
        }
    }

    @Override
    public void onArticleSelected(Article article) {
        mFragmentDetails = (FragmentDetails)getFragmentManager()
                        .findFragmentById(R.id.fragment_details);
        if(mFragmentDetails != null && mFragmentDetails.isInLayout()) {
            mFragmentDetails.setContent(article);
        } else {
            mFragmentDetails = new FragmentDetails();
            Bundle args = new Bundle();
            args.putParcelable(ARTICLE, article);
            mFragmentDetails.setArguments(args);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, mFragmentDetails);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
