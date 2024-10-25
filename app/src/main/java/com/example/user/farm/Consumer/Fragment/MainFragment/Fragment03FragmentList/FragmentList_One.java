package com.example.user.farm.Consumer.Fragment.MainFragment.Fragment03FragmentList;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.farm.BlueTooth.SelectActivity;
import com.example.user.farm.Consumer.Actitvity.AccountActivity;
import com.example.user.farm.Consumer.Actitvity.BluetoothActivity;
import com.example.user.farm.Consumer.Actitvity.DealActivity;
import com.example.user.farm.Consumer.Actitvity.HelpActivity;
import com.example.user.farm.Consumer.Actitvity.HistoryActivity;
import com.example.user.farm.Consumer.Actitvity.LoginActivity;
import com.example.user.farm.Consumer.Actitvity.QrcodeActivity;
import com.example.user.farm.Consumer.Actitvity.ScanActivity;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.History;
import com.example.user.farm.SharePreference.Login;


/**
 * Created by Solinari on 2016/12/31.
 */

public class FragmentList_One extends Fragment implements View.OnClickListener {
    public static final int PERMISSION_REQUEST = 200;
    RelativeLayout bluetooth;
    RelativeLayout shipping;
    RelativeLayout receipt;
    RelativeLayout qrcode;

    LinearLayout history;
    LinearLayout account;
    LinearLayout help;
    LinearLayout logout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_list3__one, container, false);

        bluetooth = (RelativeLayout) view.findViewById(R.id.bluetooth);
        shipping = (RelativeLayout) view.findViewById(R.id.shipping);
        receipt = (RelativeLayout) view.findViewById(R.id.receipt);
        qrcode = (RelativeLayout) view.findViewById(R.id.qrcode);

        history = (LinearLayout) view.findViewById(R.id.history);
        account = (LinearLayout) view.findViewById(R.id.account);
        help = (LinearLayout) view.findViewById(R.id.help);
        logout = (LinearLayout) view.findViewById(R.id.logout);

        bluetooth.setOnClickListener(this);
        shipping.setOnClickListener(this);
        receipt.setOnClickListener(this);
        qrcode.setOnClickListener(this);

        history.setOnClickListener(this);
        account.setOnClickListener(this);
        help.setOnClickListener(this);
        logout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent deal = new Intent(getActivity(), DealActivity.class);
        switch (v.getId()) {
            case R.id.shipping:
                deal.putExtra("pos", 0);
                startActivity(deal);
                break;
            case R.id.receipt:
                deal.putExtra("pos", 1);
                startActivity(deal);
                break;
            case R.id.bluetooth:
                startActivity(new Intent(getActivity(), SelectActivity.class));
                break;
            case R.id.qrcode:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
                } else
                    startActivity(new Intent(getActivity(), ScanActivity.class));
                break;
            case R.id.history:
                startActivity(new Intent(getActivity(), HistoryActivity.class));
                break;
            case R.id.account:
                startActivity(new Intent(getActivity(), AccountActivity.class));
                break;
            case R.id.help:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            case R.id.logout:
                History.clear(getActivity());
                Login.logout(getActivity());
                final Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                    Toast.makeText(getActivity(), "logout", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
