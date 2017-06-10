package com.yalo.erunn.parking;

import android.app.AlertDialog;
import android.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by erunn on 2017-06-06.
 */

public class ParkingFragment extends Fragment {
    private TextView parkName;
    private TextView parkCount;
    private TextView parkFree;
    private TextView parkDays;
    private Button parkButton;
    private TextView parkPrice;

    private String name;
    private int quantity;
    private String free;
    private String days;
    private int price;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
            quantity = getArguments().getInt("quantity");
            free = getArguments().getString("free");
            days = getArguments().getString("days");



        }
    }

    public ParkingFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parkName = (TextView) view.findViewById(R.id.parkName);
        parkName.setText(name);

        parkCount = (TextView) view.findViewById(R.id.parkCount);
        parkCount.setText("주차가능대수: " + quantity);

        parkFree = (TextView) view.findViewById(R.id.parkFree);
        parkFree.setText("요금정보: "+ free);

        parkDays = (TextView) view.findViewById(R.id.parkDays);
        parkDays.setText("운영요일: "+ days);

        parkButton = (Button) view.findViewById(R.id.park_button);
        parkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                alert_confirm.setTitle(name);
                alert_confirm.setMessage("여기로 가시겠습니까?").setCancelable(false).setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();



            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parking, container, false);
    }
}
