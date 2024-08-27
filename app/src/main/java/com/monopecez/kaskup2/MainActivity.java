package com.monopecez.kaskup2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] name = new String[] {"Kupat", "Kari"};
        int[] harga = new int[]{10000, 20000};

        setContentView(R.layout.activity_main);

        ConstraintLayout mainConstraintLayout = findViewById(R.id.mainConstraintLayout);

        ConstraintLayout block1 = buildBlock(R.id.ph16, "ASFFF", 200, true);

        mainConstraintLayout.addView(block1);

    }

    private ConstraintLayout buildBlock(int beforeComponent, String menuNameInput, int harga, boolean isEven){
        float dpCoeficient = this.getResources().getDisplayMetrics().density;

        String background = "";
        if (isEven) {
            background = "#e8e8e8";
        } else {
            background = "#ffffff";
        }
        ConstraintLayout outLayout = new ConstraintLayout(this);
        int layoutId = View.generateViewId();

        ConstraintLayout.LayoutParams outLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        outLayoutParams.topToBottom = beforeComponent;
        outLayout.setLayoutParams(outLayoutParams);
        outLayout.setId(layoutId);


        // TEXT VIEW
        TextView menuName = new TextView(this);
        menuName.setText(menuNameInput);
        menuName.setId(View.generateViewId());
        menuName.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

        ConstraintLayout.LayoutParams menuNameParams = new ConstraintLayout.LayoutParams((int) dpCoeficient * 128, 0);
        menuNameParams.verticalBias = .857f;
        menuNameParams.setMarginStart(16);

        // BUTTON KURANG
        Button menuKurang = new Button(this);
        menuKurang.setText("-");
        menuKurang.setId(View.generateViewId());

        ConstraintLayout.LayoutParams menuKurangParams = new ConstraintLayout.LayoutParams((int) dpCoeficient * 46, (int) dpCoeficient* 46);
        menuKurangParams.setMarginStart(8);

        // BUTTON TAMBAH
        Button menuTambah = new Button(this);
        menuTambah.setText("+");
        menuTambah.setId(View.generateViewId());

        ConstraintLayout.LayoutParams menuTambahParams = new ConstraintLayout.LayoutParams((int) dpCoeficient* 46, (int)  dpCoeficient* 46);
        menuTambahParams.setMarginStart(8);


        // TEXT VIEW TOTAL
        TextView menuTotal = new TextView(this);
        menuTotal.setText("0 | 0");
        menuTotal.setId(View.generateViewId());

        ConstraintLayout.LayoutParams menuTotalParams = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        menuTotalParams.setMarginStart(8);



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
        cs.connect(menuName.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        cs.connect(menuKurang.getId(), ConstraintSet.START, menuName.getId(), ConstraintSet.END);
        cs.connect(menuTambah.getId(), ConstraintSet.START, menuKurang.getId(), ConstraintSet.END);
        cs.connect(menuTotal.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        cs.connect(menuTotal.getId(), ConstraintSet.START, menuTambah.getId(), ConstraintSet.END);
        cs.applyTo(outLayout);


        return outLayout;

    }
}