package com.github.rodrigohenriques.mvp.sample.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rodrigohenriques.mvp.sample.R;
import com.github.rodrigohenriques.mvp.sample.domain.entities.Episode;
import com.github.rodrigohenriques.mvp.sample.presenter.EpisodesPresenter;
import com.github.rodrigohenriques.mvp.sample.presenter.view.EpisodesView;
import com.github.rodrigohenriques.mvp.sample.recyclerview.DividerItemDecoration;
import com.github.rodrigohenriques.mvp.sample.recyclerview.EpisodesRecyclerViewAdapter;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectExtra;

public class SeasonActivity extends RoboActionBarActivity implements EpisodesView, EpisodesRecyclerViewAdapter.OnItemClickListener {

    @Inject EpisodesPresenter mPresenter;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.toolbar_layout) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.season_picture) ImageView mImageViewSeasonPicture;
    @Bind(R.id.season_header) ImageView mImageViewHeader;
    @Bind(R.id.season_rating) TextView mTextViewRating;
    @Bind(R.id.recyclerview) RecyclerView mRecyclerView;

    @BindColor(android.R.color.transparent) int mColorTransparent;
    @BindColor(R.color.title_color) int mColorTitle;

    @InjectExtra(value = "serie", optional = true) String mSerie = "game-of-thrones";
    @InjectExtra(value = "season", optional = true) int mSeason = 1;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        ButterKnife.bind(this);

        mCollapsingToolbarLayout.setTitle("Season 1");
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(mColorTitle);
        mCollapsingToolbarLayout.setExpandedTitleColor(mColorTransparent);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));

        mPresenter.loadData(mSerie, mSeason);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
    }

    @Override
    public void onItemClick(Episode episode) {
        Snackbar.make(mRecyclerView, episode.getTitle(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        mProgressDialog = ProgressDialog.show(this, "Processando", "Aguarde...", true);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showEmptyListTextView() {
        // TODO: 10/24/15
    }

    @Override
    public void showItems(List<Episode> episodes) {
        EpisodesRecyclerViewAdapter episodesRecyclerViewAdapter = new EpisodesRecyclerViewAdapter(SeasonActivity.this, episodes);
        episodesRecyclerViewAdapter.setOnItemClickListener(SeasonActivity.this);
        mRecyclerView.setAdapter(episodesRecyclerViewAdapter);
    }

    @Override
    public void showError(String error) {
        AlertDialog dialog = new AlertDialog.Builder(SeasonActivity.this)
                .setTitle("Atenção")
                .setMessage(error)
                .setPositiveButton(R.string.action_close, null)
                .create();

        dialog.show();
    }

    @Override
    public void showSeasonPicture(String seasonPictureUrl) {
        Picasso.with(SeasonActivity.this)
                .load(seasonPictureUrl)
                .placeholder(R.drawable.season_picture_placeholder)
                .into(mImageViewSeasonPicture);
    }

    @Override
    public void showSeasonBanner(String seasonBannerUrl) {
        Picasso.with(SeasonActivity.this)
                .load(seasonBannerUrl)
                .placeholder(R.drawable.season_header_placeholder)
                .into(mImageViewHeader);
    }

    @Override
    public void showSeasonRating(String rating) {
        mTextViewRating.setText(rating);
    }
}
