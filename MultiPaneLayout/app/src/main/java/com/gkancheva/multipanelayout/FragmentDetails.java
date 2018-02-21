package com.gkancheva.multipanelayout;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentDetails extends Fragment {

    Article mArticle;
    private TextView mTxtTitle, mTxtBody;
    private ScrollView mScrollView;
    private static String ARTICLE = "Article";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mArticle = savedInstanceState.getParcelable(ARTICLE);
        }
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mScrollView = (ScrollView)view.findViewById(R.id.scroll_view);
        mTxtTitle = (TextView)view.findViewById(R.id.title);
        mTxtBody = (TextView)view.findViewById(R.id.article_body);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if(args != null) {
            setContent((Article) args.getParcelable(ARTICLE));
        } else if(mArticle != null) {
            setContent(mArticle);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARTICLE, mArticle);
    }

    public void setContent(Article article) {
        mTxtTitle.setText(article.getTitle());
        mTxtBody.setText(article.getBody());
        mScrollView.scrollTo(0, 0);
        mArticle = article;
    }
}

