
package com.example.bulldogtipper;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class TipActivity extends Activity {

    private static final int DEFAUTL_TIP = 15;
    private static final int MAX_NUMBER_PICKER_VALUE = 20;
    private static final int MIN_NUMBER_PICKER_VALUE = 1;

    private EditText etBillAmount;
    private TextView tvTipPct;
    private TextView tvTipAmount;
    private TextView tvTotalAmount;
    private TextView tvTotalAmountEach;
    private SeekBar sbTipPct;
    private NumberPicker npNumPeople;


    private final NumberFormat decimalFormatter = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        // Bind UI elements
        etBillAmount = findViewById(R.id.etBillAmount);
        tvTipPct = findViewById(R.id.tvTipPct);
        tvTipAmount = findViewById(R.id.tvTipAmount);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvTotalAmountEach =  findViewById(R.id.tvTotalAmountEach);
        sbTipPct = findViewById(R.id.sbTipPct);
        npNumPeople = findViewById(R.id.npNumPeople);


        setupSeekBarListener();

        setupEditorActionListener();

        setupNumberPickerListener();
    }

    /**Setup Tip Percentage (SeekBar) listener**/
    private void setupSeekBarListener() {
        sbTipPct.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar bar)
            {
                try {
                    double bill = Double.parseDouble(etBillAmount.getText().toString());
                    int tipPct = bar.getProgress();
                    int splitNum = npNumPeople.getValue();

                    calculateTip(bill, tipPct, splitNum);
                } catch (NumberFormatException e) {
                    Log.i(INPUT_SERVICE, "Invalid bill amount: ", e);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar bar)
            {

            }

            @Override
            public void onProgressChanged(SeekBar bar, int paramInt, boolean paramBoolean)
            {
                tvTipPct.setText("" + paramInt + "%");
            }
        });
        sbTipPct.setProgress(DEFAUTL_TIP);
    }

    /**Setup Total Amount (EditText) OnEditorActionListener**/
    private void setupEditorActionListener() {
        etBillAmount.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        double bill = Double.parseDouble(v.getText().toString());
                        int tipPct = sbTipPct.getProgress();
                        int splitNum = npNumPeople.getValue();

                        calculateTip(bill, tipPct, splitNum);
                    } catch (NumberFormatException e) {
                        Log.i(INPUT_SERVICE, "Invalid bill amount: ", e);
                    }
                }
                return false;
            }
        });
    }

    /**Setup Number of People (NumberPicker) Listener**/
    private void setupNumberPickerListener() {
        // Initialize NumberPicker input control
        npNumPeople.setMaxValue(MAX_NUMBER_PICKER_VALUE);
        npNumPeople.setMinValue(MIN_NUMBER_PICKER_VALUE);

        npNumPeople.setOnValueChangedListener(new OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                try {
                    double bill = Double.parseDouble(etBillAmount.getText().toString());
                    int tipPct = sbTipPct.getProgress();

                    calculateTip(bill, tipPct, newVal);

                } catch (NumberFormatException e) {
                    Log.i(INPUT_SERVICE, "Invalid bill amount: ", e);
                }

            }
        });
    }

    /**Calculate the tip amount base on bill and tip percentage and update the UI**/
    private void calculateTip(double bill, int tipPct, int splitNum) {
        double tip = (bill * tipPct / 100);
        double totalAmount = bill + tip;

        tvTipAmount.setText("$" + decimalFormatter.format(tip));
        tvTotalAmount.setText("$" + decimalFormatter.format(totalAmount + tip));
        tvTotalAmountEach.setText("$" + decimalFormatter.format((totalAmount + tip)/splitNum));
    }

}
