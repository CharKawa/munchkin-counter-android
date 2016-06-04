package com.datarockets.mnchkn.activities.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.datarockets.mnchkn.R;
import com.datarockets.mnchkn.activities.BaseActivity;
import com.datarockets.mnchkn.activities.result.GameResultActivity;
import com.datarockets.mnchkn.adapters.PlayerListAdapter;
import com.datarockets.mnchkn.fragments.players.PlayerFragment;
import com.datarockets.mnchkn.models.Player;
import com.datarockets.mnchkn.utils.LogUtil;

import java.util.List;

public class DashboardActivity extends BaseActivity implements DashboardView,
        View.OnClickListener, AdapterView.OnItemClickListener, PlayerFragment.PlayerFragmentCallback {

    public static final String TAG = LogUtil.makeLogTag(DashboardActivity.class);

    DashboardPresenter presenter;
    Toolbar toolbar;
    ListView lvPlayerList;
    PlayerListAdapter lvPlayerListAdapter;
    Button btnNextStep;
    PlayerFragment playerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DashboardPresenterImpl(this, this);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnNextStep = (Button) findViewById(R.id.btn_next_step);
        btnNextStep.setOnClickListener(this);
        lvPlayerList = (ListView) findViewById(R.id.lv_player_list);
        lvPlayerList.setOnItemClickListener(this);
        playerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_player);
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.trackWithProperties("Current activity", "Activity name", TAG);
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_finish_game:
                showConfirmFinishGameDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishGame() {
        presenter.setGameFinished();
        Intent intent = new Intent(this, GameResultActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setItems(List<Player> players) {
        lvPlayerListAdapter = new PlayerListAdapter(this, players);
        lvPlayerList.setAdapter(lvPlayerListAdapter);
        lvPlayerList.setSelection(0);
        lvPlayerList.setItemChecked(0, true);
        playerFragment.loadPlayerScores(lvPlayerListAdapter.getItem(0), 0);
    }

    @Override
    public void showConfirmFinishGameDialog() {
        AlertDialog.Builder confirmFinishGameDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_finish_game_title)
                .setMessage(R.string.dialog_finish_game_message)
                .setPositiveButton(R.string.button_yes, (dialog, which) -> {
                    finishGame();
                })
                .setNegativeButton(R.string.button_no, (dialog, which) -> {
                    dialog.dismiss();
                });
        confirmFinishGameDialog.create().show();
    }


    @Override
    public void updatePlayerData(Player player, int position) {
        Player playerToUpdate = lvPlayerListAdapter.getItem(position);
        playerToUpdate.levelScore = player.levelScore;
        playerToUpdate.strengthScore = player.strengthScore;
        lvPlayerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_step:
                int position = lvPlayerList.getCheckedItemPosition();
                if (position == lvPlayerList.getCount() - 1) {
                    lvPlayerList.setItemChecked(0, true);
                    lvPlayerList.setSelection(0);
                    position = 0;
                } else {
                    position++;
                    lvPlayerList.setItemChecked(position, true);
                    lvPlayerList.setSelection(position);
                }
                playerFragment.loadPlayerScores(lvPlayerListAdapter.getItem(position), position);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        lvPlayerList.setItemChecked(position, true);
        playerFragment.loadPlayerScores(lvPlayerListAdapter.getItem(position), position);
    }

    @Override
    public void onScoreChanged(Player player, int index) {
        presenter.updatePlayerListItem(player, index);
        presenter.insertStep(player);
    }

}
