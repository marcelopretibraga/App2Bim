package com.edu.fag.app2bim.tarefas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.edu.fag.app2bim.R;
import com.edu.fag.app2bim.Util.WSUtil;
import com.edu.fag.app2bim.entity.Equipe;
import com.edu.fag.app2bim.entity.Jogo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;


public class DownloadEquipe extends AsyncTask<String, String, ArrayList<String>> {
    private ProgressDialog progress;
    private Context context;

    public DownloadEquipe(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("On PreExecute......");
        progress = new ProgressDialog(context);
        progress.setTitle("Importar Dados");
        progress.setMessage("Aguarde Baixando registros de Equipes...");
        progress.setIcon(R.drawable.ic_person);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        List<PropertyInfo> parametros = new ArrayList<>();
        PropertyInfo property = new PropertyInfo();
        property.setName("cdEquipe");
        property.setValue(1);
        parametros.add(property);
        String equipe = WSUtil.consomeWS("getEquipe", parametros);
        //Converte a String em Gson para um Objeto
        Equipe eq = new Gson().fromJson(equipe, Equipe.class);
        //eq.save(); //salvar com o Sugar

        //Salvar as equipes
        System.out.println(eq.toString());

        //Simulo uma progressao
        for (int i=0; i<10; i++){
            //Aqui estou chamando a progressao
            publishProgress(new String[]{"Progresso de "+i+" de 10."});
            try {
                Thread.sleep(1500);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        System.out.println("Resultado Equipes....."+equipe);

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

        DownloadJogo downloadJogo = new DownloadJogo(context);
        downloadJogo.executeOnExecutor
                (AsyncTask.THREAD_POOL_EXECUTOR, new String[]{});


    }
}
