package com.edu.fag.app2bim.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.fag.app2bim.R;

import java.util.List;

public class AdapterBluetooth extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<BluetoothDevice> itens;
    private String nullo;

    public AdapterBluetooth(Context context, List<BluetoothDevice> itens, String nullo){
        //Itens que preencheram o listview
        this.itens = itens;
        this.nullo = nullo;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    public int getCount(){
        return itens.size();
    }

    public BluetoothDevice getItem(int position){
        return itens.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View view, ViewGroup parent){
        BluetoothDevice item = (BluetoothDevice) itens.get(position);
        view = mInflater.inflate(R.layout.item_bluetooth, null);
        TextView tvBluetooth = (TextView) view.findViewById(R.id.tvBluetooth);
        if (item != null) {
            tvBluetooth.setText(item.getName() + " - " + item.getAddress());
        }else {
            tvBluetooth.setText(nullo);
        }
        return view;
    }
}