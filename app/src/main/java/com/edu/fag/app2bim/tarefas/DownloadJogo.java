package com.edu.fag.app2bim.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.edu.fag.app2bim.R;
import com.edu.fag.app2bim.Util.WSUtil;
import com.edu.fag.app2bim.entity.Equipe;
import com.edu.fag.app2bim.entity.Jogo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.serialization.PropertyInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DownloadJogo extends AsyncTask<String, String, ArrayList<String>> {
    private ProgressDialog progress;
    private Context context;

    public DownloadJogo(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setTitle("Importar Dados");
        progress.setMessage("Aguarde Baixando registros de Jogos...");
        progress.setIcon(R.drawable.ic_person);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        String jogosGson = WSUtil.consomeWS("getJogoList", null);
        //Defino o Type da Classe Jogo como Lista
        Type typeJogos = new TypeToken<List<Jogo>>(){}.getType();
        //Converte o Gson para a Lista de Jogo
        List<Jogo> jogoList = new Gson().fromJson(jogosGson, typeJogos);
        //Apenas um Objeto
        //Equipe eq = new Gson().fromJson(equipe, Equipe.class);

        //Salvar os Registros (List inteira)
        Jogo.saveInTx(jogoList);

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        System.out.println("onProgressUpdate......");
        if (values != null)
            progress.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        System.out.println("onPostExecute......");
        progress.setIndeterminate(false);
        progress.cancel();
    }
}
