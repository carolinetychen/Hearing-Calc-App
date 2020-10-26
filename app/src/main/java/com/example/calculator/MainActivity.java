package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Math.max;

public class MainActivity extends AppCompatActivity {
    private EditText[] leftInput, rightInput;
    private Button buttonCalc, buttonReset;
    private TextView leftPta, rightPta;
    private TextView leftResult, rightResult, bothResult;
    private TextView result, resultInfo;

    // Hearing value
    private int[] left, right;
    private Double left_avg, right_avg;
    private Double left_hh, right_hh, both_hh;

    // Result value
    private int result_index = 0;
    private String[] resultString = new String[] {
            "b230.0", "b230.1", "b230.2", "b230.3"
    };
    private String[] resultInfoString = new String[] {
            "未達下列基準",
            "雙耳整體障礙比率介於 50.0% 至 70.0%",
            "雙耳整體障礙比率介於 70.1% 至 90.0%",
            "雙耳整體障礙比率大於 90.1%"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // EditText init
        leftInput = new EditText[] {
                (EditText) findViewById(R.id.left500_num),
                (EditText) findViewById(R.id.left1k_num),
                (EditText) findViewById(R.id.left2k_num),
                (EditText) findViewById(R.id.left4k_num)
        };

        rightInput = new EditText[] {
                (EditText) findViewById(R.id.right500_num),
                (EditText) findViewById(R.id.right1k_num),
                (EditText) findViewById(R.id.right2k_num),
                (EditText) findViewById(R.id.right4k_num)
        };

        // Button init
        buttonCalc = (Button) findViewById(R.id.btn_calc);
        buttonReset = (Button) findViewById(R.id.btn_reset);

        buttonCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getEditText()) {
                    hearingCalc();
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEditText();
                resetTextView();
            }
        });

        // TextView init
        leftPta = (TextView) findViewById(R.id.left_pta);
        rightPta = (TextView) findViewById(R.id.right_pta);
        leftResult = (TextView) findViewById(R.id.left_result);
        rightResult = (TextView) findViewById(R.id.right_result);
        bothResult = (TextView) findViewById(R.id.both_result);
        result = (TextView) findViewById(R.id.result);
        resultInfo = (TextView) findViewById(R.id.result_info);

        // Value init
        left = new int[4];
        right = new int[4];
    }

    private boolean getEditText() {
        for (int i = 0; i < 4; i++) {
            String text = leftInput[i].getText().toString();
            if (text.equalsIgnoreCase("")) {
                leftInput[i].setError("請輸入整數");
                return false;
            } else {
                left[i] = Integer.parseInt(text);
            }

            text = rightInput[i].getText().toString();
            if (text.equalsIgnoreCase("")) {
                rightInput[i].setError("請輸入整數");
                return false;
            } else {
                right[i] = Integer.parseInt(text);
            }
        }
        return true;
    }

    private void resetEditText() {
        for (int i = 0; i < 4; i++) {
            leftInput[i].setText("");
            rightInput[i].setText("");
        }
    }

    private void resetTextView() {
        leftPta.setText("");
        rightPta.setText("");
        leftResult.setText("");
        rightResult.setText("");
        bothResult.setText("");
        result.setText("");
        resultInfo.setText("");
    }

    private void setHH() {
        leftPta.setText(String.valueOf(left_avg));
        rightPta.setText(String.valueOf(right_avg));

        leftResult.setText(String.valueOf(left_hh));
        rightResult.setText(String.valueOf(right_hh));
        bothResult.setText(String.valueOf(both_hh));
    }

    private void setResultString(int index) {
        result.setText(resultString[index]);
        resultInfo.setText(resultInfoString[index]);
    }

    private void hearingCalc() {
        left_avg = ptaCalc("left");
        right_avg = ptaCalc("right");

        left_hh = singleHHCalc(left_avg);
        right_hh = singleHHCalc(right_avg);

        if (left_hh < right_hh) {
            both_hh = bothHHCalc(left_hh, right_hh);
        } else {
            both_hh = bothHHCalc(right_hh, left_hh);
        }

        if (both_hh < 50) {
            result_index = 0;
        } else if (50 <= both_hh && both_hh <= 70.0) {
            result_index = 1;
        } else if (70 < both_hh && both_hh <= 90) {
            result_index = 2;
        } else {
            result_index = 3;
        }

        setHH();
        setResultString(result_index);
    }

    // pta = (500Hz + 1kHz + 2kHz + 4kHz) / 4
    private double ptaCalc(String side) {
        double pta = 0.0;
        double sum = 0.0;

        if (side.equals("left")) {
            for (int i = 0; i < 4; i++) {
                sum += left[i];
            }
        } else if (side.equals("right")) {
            for (int i = 0; i < 4; i++) {
                sum += right[i];
            }
        }
        pta = Math.round(sum * 10 / 4.0) / 10.0;
        return pta;
    }

    private double singleHHCalc(double pta) {
        double hh = 0.0;

        hh = Math.round((pta - 25) * 1.5 * 10) / 10.0;
        return hh;
    }

    private double bothHHCalc(double better_hh, double worse_hh) {
        double hh = 0.0;

        hh = Math.round((better_hh * 5 + worse_hh * 1) / 6.0 * 10) / 10.0;
        return hh;
    }
}