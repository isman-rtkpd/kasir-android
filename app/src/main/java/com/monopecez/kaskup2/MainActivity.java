package com.monopecez.kaskup2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static int[] qtyList, harga;
    static int menuSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] name = new String[] {"Kupat", "Kari", "Tahu"};
        harga = new int[]{10000, 20000, 200};

        menuSize = harga.length;

        setContentView(R.layout.activity_main);

        ConstraintLayout mainConstraintLayout = findViewById(R.id.mainConstraintLayout);

        ConstraintLayout temp = new ConstraintLayout(this);

        qtyList = new int[menuSize];
        for(int i = 0; i < menuSize; i++){
            if (i == 0){
                temp = buildBlock(R.id.kupatset, name[i], harga[i], true, i);
            } else {
                temp = buildBlock(temp.getId(), name[i], harga[i], true, i);
            }
            mainConstraintLayout.addView(temp);
            qtyList[i] = 0;

        }

    }

    private void calculateTotal(){
        TextView totalText = (TextView) findViewById(R.id.totalharga);
        int total = 0;
        for (int i = 0; i < menuSize; i++){
            total = total + (qtyList[i] * harga[i]);
        }
        totalText.setText(String.valueOf(total));
    }

    private ConstraintLayout buildBlock(int beforeComponent, String menuNameInput, int harga, boolean isEven, int idx){
        float dpCoeficient = this.getResources().getDisplayMetrics().density;

        int background = 0;
        if (isEven) {
            background = 15263976; // "#e8e8e8";
        } else {
            background = 0; // "#ffffff";
        }
        ConstraintLayout outLayout = new ConstraintLayout(this);
        int layoutId = View.generateViewId();

        ConstraintLayout.LayoutParams outLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        outLayoutParams.topToBottom = beforeComponent;
        outLayout.setLayoutParams(outLayoutParams);
        outLayout.setBackgroundColor(background);
        outLayout.setId(layoutId);


        // TEXT VIEW
        TextView menuName = new TextView(this);
        menuName.setText(menuNameInput);
        menuName.setTypeface(null, Typeface.BOLD);
        menuName.setId(View.generateViewId());
        menuName.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        menuName.setGravity(Gravity.CENTER);

        ConstraintLayout.LayoutParams menuNameParams = new ConstraintLayout.LayoutParams((int) (dpCoeficient * 128 * 1.15 ), 0);
        menuNameParams.verticalBias = .857f;
        menuNameParams.topToTop = outLayout.getId();
        menuNameParams.bottomToBottom = outLayout.getId();
        menuNameParams.setMarginStart(16);

        // BUTTON KURANG
        Button menuKurang = new Button(this);
        menuKurang.setText("-");
        menuKurang.setId(View.generateViewId());

        ConstraintLayout.LayoutParams menuKurangParams = new ConstraintLayout.LayoutParams((int) (dpCoeficient * 46), (int) (dpCoeficient* 46));
        menuKurangParams.setMarginStart(8);

        // BUTTON TAMBAH
        Button menuTambah = new Button(this);
        menuTambah.setText("+");
        menuTambah.setId(View.generateViewId());

        ConstraintLayout.LayoutParams menuTambahParams = new ConstraintLayout.LayoutParams((int) (dpCoeficient * 46), (int) (dpCoeficient* 46));
        menuTambahParams.setMarginStart(8);


        // TEXT VIEW TOTAL
        TextView menuTotal = new TextView(this);
        menuTotal.setText("0 | 0");
        menuTotal.setTypeface(null, Typeface.BOLD);
        menuTotal.setId(View.generateViewId());

        ConstraintLayout.LayoutParams menuTotalParams = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        menuTotalParams.setMarginStart(8);
        menuTotalParams.topToTop = outLayout.getId();
        menuTotalParams.bottomToBottom = outLayout.getId();

        menuNameParams.leftToLeft = outLayout.getId();
        menuKurangParams.leftToRight = menuName.getId();
        menuTambahParams.leftToRight = menuKurang.getId();
        menuTotalParams.rightToRight = outLayout.getId();
        menuTotalParams.setMarginEnd(20);

        menuName.setLayoutParams(menuNameParams);
        menuKurang.setLayoutParams(menuKurangParams);
        menuTambah.setLayoutParams(menuTambahParams);
        menuTotal.setLayoutParams(menuTotalParams);

        outLayout.addView(menuName);
        outLayout.addView(menuKurang);
        outLayout.addView(menuTambah);
        outLayout.addView(menuTotal);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(outLayout);
        cs.applyTo(outLayout);

        menuTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = qtyList[idx] + 1;
                qtyList[idx] = qty;
                menuTotal.setText(qty + " | " + (qty*harga));
                calculateTotal();
            }
        });

        menuKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = qtyList[idx];
                if (qty >= 1){
                    qty--;
                }
                qtyList[idx] = qty;
                menuTotal.setText(qty + " | " + (qty*harga));
                calculateTotal();
            }
        });

        return outLayout;
    }
}