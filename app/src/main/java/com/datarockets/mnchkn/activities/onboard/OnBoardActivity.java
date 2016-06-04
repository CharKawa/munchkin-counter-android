package com.datarockets.mnchkn.activities.onboard;

import android.content.Intent;
import android.os.Bundle;

import com.chyrta.onboarder.OnboarderActivity;
import com.chyrta.onboarder.OnboarderPage;
import com.datarockets.mnchkn.R;
import com.datarockets.mnchkn.activities.players.PlayersListActivity;

import java.util.ArrayList;
import java.util.List;

public class OnboardActivity extends OnboarderActivity implements OnboardView {

    OnboardPresenter presenter;
    List<OnboarderPage> onboarderPages;
    OnboarderPage onboarderPageOne, onboarderPageTwo, onboarderPageThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new OnboardPresenterImpl(this, this);
        presenter.checkIsUserSeenOnboarding();
        onboarderPages = new ArrayList<>();
        onboarderPageOne = new OnboarderPage(
                R.string.onboarder_page1_title,
                R.string.onboarder_page1_description,
                R.drawable.ic_munchkin);
        onboarderPageOne.setBackgroundColor(R.color.card_general);
        onboarderPageTwo = new OnboarderPage(
                R.string.onboarder_page2_title,
                R.string.onboarder_page2_description,
                R.drawable.ic_infinite);
        onboarderPageTwo.setBackgroundColor(R.color.card_light);
        onboarderPageThree = new OnboarderPage(
                R.string.onboarder_page3_title,
                R.string.onboarder_page3_description,
                R.drawable.ic_dice);
        onboarderPageThree.setBackgroundColor(R.color.card_corner);
        onboarderPages.add(onboarderPageOne);
        onboarderPages.add(onboarderPageTwo);
        onboarderPages.add(onboarderPageThree);
        setOnboardPagesReady(onboarderPages);
    }

    @Override
    public void onSkipButtonPressed() {
        super.onSkipButtonPressed();
    }

    @Override
    public void onFinishButtonPressed() {
        openPlayersActivity();
    }

    @Override
    public void openPlayersActivity() {
        presenter.setOnboardingSeen();
        Intent intent = new Intent(this, PlayersListActivity.class);
        startActivity(intent);
        finish();
    }

}