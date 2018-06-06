package com.edu.fag.app2bim;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.edu.fag.app2bim.entity.Jogo;

import java.util.List;

public class JogoListActivity extends AppCompatActivity {

    private ListView lvJogos;
    private ArrayAdapter<Jogo> adapterJogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvJogos = findViewById(R.id.lvJogos);

        List<Jogo> jogoList = Jogo.listAll(Jogo.class);
        adapterJogo = new ArrayAdapter<Jogo>(
                this, R.layout.item_bluetooth,
                jogoList);

        lvJogos.setAdapter(adapterJogo);

    }

}
