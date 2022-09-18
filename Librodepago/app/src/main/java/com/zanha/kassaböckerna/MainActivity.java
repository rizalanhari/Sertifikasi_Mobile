package com.zanha.kassaböckerna;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView pemasukan, pengeluaran;
    private String username;
    private LineChart lineChart;
    private ArrayList<Cashflow> listCashflow;
    private DBHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("username");

        databaseHelper = new DBHelper(getApplicationContext());
        listCashflow = new ArrayList<>();
        pemasukan = (TextView)findViewById(R.id.total_pemasukan);
        pengeluaran = (TextView)findViewById(R.id.total_pengeluaran);

        total();
    }

    private List<Entry> getDataSet() {
        List<Entry> lineEntries = new ArrayList<Entry>();
        listCashflow.clear();
        listCashflow.addAll(databaseHelper.getAllCashflow(getIntent().getStringExtra("username")));
        System.out.println("size cashflow:" + listCashflow.size());
        for (int i=0; i<listCashflow.size(); i++){
            if (listCashflow.get(i).getJenis().equals("out")) {
                String tgl = listCashflow.get(i).getTgl();
                String[] part = tgl.split("-");

                if (part[1].equals("10")) {
                    lineEntries.add(new Entry(i, Float.parseFloat(listCashflow.get(i).getNominal())));
                    System.out.println("nominal cash:" + listCashflow.get(i).getNominal());
                }
            }
        }
        return lineEntries;
    }

    private List<Entry> getDataSet2() {
        List<Entry> lineEntries = new ArrayList<Entry>();
        listCashflow.clear();
        listCashflow.addAll(databaseHelper.getAllCashflow(getIntent().getStringExtra("username")));
        System.out.println("size cashflow:" + listCashflow.size());
        for (int i=0; i<listCashflow.size(); i++){
            if (listCashflow.get(i).getJenis().equals("in")) {
                String tgl = listCashflow.get(i).getTgl();
                String[] part = tgl.split("-");

                if (part[1].equals("10")) {
                    lineEntries.add(new Entry(i, Float.parseFloat(listCashflow.get(i).getNominal())));
                    System.out.println("nominal cash:" + listCashflow.get(i).getNominal());
                }
            }
        }
        return lineEntries;
    }

    private void total() {
        int in=0 , out = 0;
        List<Entry> lineEntries = new ArrayList<Entry>();
        listCashflow.clear();
        listCashflow.addAll(databaseHelper.getAllCashflow(getIntent().getStringExtra("username")));
        System.out.println("size cashflow:" + listCashflow.size());
        for (int i=0; i<listCashflow.size(); i++){
            if (listCashflow.get(i).getJenis().equals("out")) {
               out += Integer.parseInt(listCashflow.get(i).getNominal());
            }else{
                in += Integer.parseInt(listCashflow.get(i).getNominal());
            }
        }
        pemasukan.setText("Pemasukan: Rp." +in);
        pengeluaran.setText("Pengeluaran: Rp." +out);
    }

    public void toIncome(View v){
        Intent intent = new Intent(getApplicationContext(), PemasukanActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
    public void toOutcome(View v){
        Intent intent = new Intent(getApplicationContext(), PengeluaranActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
    public void toCashflow(View v){
        Intent intent = new Intent(getApplicationContext(), CashFlowActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
    public void toSetting(View v){
        Intent intent = new Intent(getApplicationContext(), PengaturanActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
