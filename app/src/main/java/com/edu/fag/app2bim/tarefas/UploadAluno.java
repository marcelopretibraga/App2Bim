package com.edu.fag.app2bim.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.edu.fag.app2bim.R;
import com.edu.fag.app2bim.Util.WSUtil;
import com.edu.fag.app2bim.Util.WSUtilAluno;
import com.edu.fag.app2bim.entity.Aluno;
import com.edu.fag.app2bim.entity.Equipe;
import com.edu.fag.app2bim.entity.Jogo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.serialization.PropertyInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UploadAluno extends AsyncTask<String, String, ArrayList<String>> {
    private ProgressDialog progress;
    private Context context;

    public UploadAluno(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setTitle("Upload de Dados");
        progress.setMessage("Aguarde Enviando registros de Aluno...");
        progress.setIcon(R.drawable.ic_person);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {

        List<Aluno> alunoList = Aluno.listAll(Aluno.class);

        //Defino o Type da Classe Jogo como Lista
        Type typeAluno = new TypeToken<List<Aluno>>(){}.getType();
        //Converte o Gson para a Lista de Jogo
        String alunoGson = new Gson().toJson(alunoList, typeAluno);

        List<PropertyInfo> parametros = new ArrayList<>();
        PropertyInfo property = new PropertyInfo();
        property.setName("istring");//Nome do Parametro
        property.setValue(alunoGson);//Aluno Convertido em Gson
        parametros.add(property);
        String resultadoAluno = WSUtilAluno.consomeWS("sendAluno", parametros);

        System.out.println("Resultado Aluno....."+resultadoAluno);

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        System.out.println("onPostExecute......");
        progress.setIndeterminate(false);
        progress.cancel();
    }
}
