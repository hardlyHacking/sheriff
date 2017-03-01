package com.gaming.dogeoh.sheriff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String mComputeKey = "hasComputed";
    private static final String mNameKey = "names";
    private static final String mScoreKey = "score";
    private String[] mNames;
    private Score mScore;
    private boolean mHasComputed;

    private void createNameListeners() {
        final TableLayout totalsTable = (TableLayout) findViewById(R.id.total_table);
        final TableLayout valuesTable = (TableLayout) findViewById(R.id.table);
        for (int i = 1; i < valuesTable.getChildCount(); i++) {
            final int playerNum = i - 1;
            final TableRow row = (TableRow) valuesTable.getChildAt(i);
            final EditText editText = (EditText) row.getChildAt(0);

            final TableRow totalRow = (TableRow) totalsTable.getChildAt(playerNum);
            final TextView name = (TextView) totalRow.getChildAt(0);
            name.setText(mNames[playerNum]);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    final String newName = editable.toString();
                    name.setText(newName);
                    mNames[playerNum] = newName;
                }
            });
        }
    }

    private void createValueListeners() {
        final TableLayout table = (TableLayout) findViewById(R.id.table);
        for (int i = 1; i < table.getChildCount(); i++) {
            final int playerNum = i - 1;
            final TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 1; j < row.getChildCount(); j++) {
                final int resourceType = j - 1;
                final EditText cell = (EditText) row.getChildAt(j);
                cell.setText(Integer.toString(mScore.getScore(playerNum, resourceType)), TextView.BufferType.EDITABLE);
                cell.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        try {
                            final short val = Short.parseShort(editable.toString());
                            mScore.setScore(playerNum, resourceType, val);
                        } catch (NumberFormatException e) {
                            mScore.setScore(playerNum, resourceType, (short) 0);
                        }
                        mHasComputed = false;
                    }
                });
            }
        }
    }

    private void displayScores() {
        final Integer[] finalScores = mScore.calculateTotals();
        final TableLayout totalsTable = (TableLayout) findViewById(R.id.total_table);
        for (short i = 0; i < totalsTable.getChildCount(); i++) {
            final TableRow row = (TableRow) totalsTable.getChildAt(i);
            final TextView total = (TextView) row.getChildAt(1);
            total.setText(Integer.toString(finalScores[i]));
            final TextView name = (TextView) row.getChildAt(0);
            row.setVisibility(name.getText().length() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void setHeaderLabels() {
        ((TextView) findViewById(R.id.apple_label)).setText(R.string.apples);
        ((TextView) findViewById(R.id.bread_label)).setText(R.string.bread);
        ((TextView) findViewById(R.id.cheese_label)).setText(R.string.cheese);
        ((TextView) findViewById(R.id.chicken_label)).setText(R.string.chicken);
        ((TextView) findViewById(R.id.spice_label)).setText(R.string.spice);
        ((TextView) findViewById(R.id.mead_label)).setText(R.string.mead);
        ((TextView) findViewById(R.id.silk_label)).setText(R.string.silk);
        ((TextView) findViewById(R.id.crossbow_label)).setText(R.string.crossbow);
        ((TextView) findViewById(R.id.cash_label)).setText(R.string.cash);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            mNames = new String[Score.NUM_PLAYERS];
            mScore = new Score();
        } else {
            mHasComputed = savedInstanceState.getBoolean(mComputeKey);
            mNames = new String[] {"", "", "", "", ""};
            mScore = savedInstanceState.getParcelable(mScoreKey);
        }

        setHeaderLabels();
        createValueListeners();
        createNameListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calculate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_calculate && !mHasComputed) {
            displayScores();
            mHasComputed = true;
        }
        return id == R.id.action_calculate || super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(mComputeKey, mHasComputed);
        savedInstanceState.putStringArray(mNameKey, mNames);
        savedInstanceState.putParcelable(mScoreKey, mScore);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mHasComputed = savedInstanceState.getBoolean(mComputeKey);
        mNames = savedInstanceState.getStringArray(mNameKey);
        mScore = savedInstanceState.getParcelable(mScoreKey);
    }
}
