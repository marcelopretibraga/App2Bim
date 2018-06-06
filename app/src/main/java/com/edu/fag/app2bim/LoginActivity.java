package com.edu.fag.app2bim;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.fag.app2bim.Adapter.AdapterBluetooth;
import com.edu.fag.app2bim.Util.ByteUtil;
import com.edu.fag.app2bim.Util.WSUtil;
import com.edu.fag.app2bim.tarefas.DownloadEquipe;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etSenha;
    private TextView tvDadosRecebidos;
    private CheckBox cbLembrar;
    private Button btLogin, btCamera;
    private ListView lvPareados;

    private BluetoothAdapter mBtAdapter;
    private AdapterBluetooth adapterPareados;
    private List<BluetoothDevice> pareadosList = new ArrayList<>();
    private BluetoothDevice bluetoothDevice = null;
    private BluetoothSocket btSocket = null;
    // SPP UUID service - isso deve funcionar para a maioria dos dispositivos
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Boolean stopThread = false;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String texto = "";
    private int countByte = 0;
    private byte[] myBuffer = new byte[13];

    private final static String NAMESPACE = "http://ws.wsintegrabolao.com.br/";
    private final static String URL = "http://54.233.136.203:8080/WSIntegraBolao/funcoes";
    private final static String SOAP_ACTION = "http://ws.wsintegrabolao.com.br/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Inicializa o SugarOrm
        SugarContext.init(this);
        findComponentes();

        SharedPreferences preferences = getSharedPreferences("APP2BIM", MODE_PRIVATE);
        etUsuario.setText(preferences.getString("USER", ""));
        //verificaPermissoes();
        //Buscar o Adapter Default (padrao) do Bluetooth
        //mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        //exibeDevicesPareados();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,
                        JogoListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void verificaPermissoes() {
        //Adapter de Bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Habilitando Bluetooth...", Toast.LENGTH_SHORT).show();
            mBluetoothAdapter.enable();
        }
    }

    private void exibeDevicesPareados() {
        // Retorna a instancia de Bluetooth com os Devices pareados
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        // Se tem algumama coisa adiciono na lista
        boolean devicesPareados = false;
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                devicesPareados = true;
                //Adiciona os pareados na Lista
                if (!pareadosList.contains(device)) {
                    pareadosList.add(device);
                    //O adapter foi alterado entao recarrega o mesmo
                    adapterPareados.notifyDataSetChanged();
                }
            }
        }
        if (!devicesPareados) {
            pareadosList.add(bluetoothDevice);
        }
    }

    
    private void findComponentes() {
        etUsuario = findViewById(R.id.etUsuario);
        etSenha = findViewById(R.id.etSenha);
        cbLembrar = findViewById(R.id.cbLembrar);
        btLogin = findViewById(R.id.btLogin);
        lvPareados = findViewById(R.id.lvPareados);
        tvDadosRecebidos = findViewById(R.id.tvDadosRecebidos);
        btCamera = findViewById(R.id.btCamera);

        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        FotoActivity.class);
                startActivity(intent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                //DownloadBolao.start();
                DownloadEquipe downloadEquipe = new DownloadEquipe(LoginActivity.this);
                downloadEquipe.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{});

            }
        });
        //carregaPareados();

    }

    private void carregaPareados() {
        //Instancio para ficar com nenhum dispositivo na lista
        //disponiveisList.add(bluetoothDevice);
        //adapterDisponiveis = new AdapterBluetooth(this, disponiveisList, getResources().getText(R.string.tvNenhumDispDisp).toString());
        adapterPareados  = new AdapterBluetooth(this, pareadosList, getResources().getText(R.string.tvNPareados).toString());
        lvPareados.setAdapter(adapterPareados);

        lvPareados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                realizaConexaoBluetooth((BluetoothDevice) lvPareados.getItemAtPosition(pos));
                return true;
            }
        });

    }
    //Obsoleto
    Thread DownloadBolao = new Thread( new Runnable() {
        @Override
        public void run() {
            WSUtil.consomeWS("getClassificacaoList", null);
            List<PropertyInfo> parametros = new ArrayList<>();
            PropertyInfo property = new PropertyInfo();
            property.setName("cdEquipe");
            property.setValue(1);
            parametros.add(property);
            WSUtil.consomeWS("getEquipe", parametros);
        }
    });

    private void realizaConexaoBluetooth(BluetoothDevice deviceSel) {
        if (deviceSel != null) {
            //cancelDiscovery(); //Cancela a Busca de Dispositivos Disponiveis
            stopThread = false;
            try {
                //A função device.createRfcommSocketToServiceRecord(MY_UUID) abre m conexão Entre o dispositivo e o módulo
                btSocket = deviceSel.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                //É iniciada a saída d dados do dispositivo
                btSocket.connect();
                Thread.sleep(700);
                //Se der tudo certo na hora da conexão, irá aparecer a tela do controle
                if (btSocket != null) {
                    //Global.btSocket = btSocket;
                    Toast.makeText(getApplicationContext(), "Conexão Bluetooth Realizada com Sucesso", Toast.LENGTH_LONG).show();
                    /*
                    ivConectado.setImageResource(R.drawable.ic_desconectado);
                    ivConectado.setVisibility(View.VISIBLE);
                    tvBluetoothCon.setText(this.getString(R.string.tvConectado));
                    tvBluetoothCon.setTextColor(Color.parseColor("#FFFF7B00"));
                    alerta.dismiss();
                    pingBluetooth = new PingBluetooth();
                    pingBluetooth.start();
                    */
                    ReadBluetooth readBluetooth = new ReadBluetooth();
                    readBluetooth.start();

                }
            } catch (Exception e) {
                e.getMessage();
                Toast.makeText(this, "Ocorreu um erro inesperado ao realizar a conexão. Favor verifique se o Dispositivo está Ligado e Pronto para receber conexões!", Toast.LENGTH_LONG).show();
                //Log.d(Global.TAG, e.getMessage());
            }
        }
    }

    private class ReadBluetooth extends Thread {
        public void run() {
            while (!stopThread) {
                if (btSocket != null) {
                    try {
                        inputStream = btSocket.getInputStream();
                        if (inputStream != null) {
                            byte[] bytRec = new byte[1];
                            inputStream.read(bytRec, 0, 1);
                            System.out.println("Posicao (" + countByte + ") :" + ByteUtil.toInt(bytRec[0]));
                            texto += ByteUtil.toInt(bytRec[0]) + " , ";
                            myBuffer[countByte] = bytRec[0];
                            //O recebimento e feito byte a byte
                            //monto um buffer de 12 bytes no caso e passo pro decodificaPacote (regra de negocio)
                            if (countByte == 12) {
                                decodificaPacote(myBuffer);
                                countByte = -1;
                            }
                            countByte++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //passa na tela o que esta sendo recebido
                                    tvDadosRecebidos.setText(texto);
                                }
                            });
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void decodificaPacote(byte[] myBuffer) {
        //aqui e tratado a parte logica da aplicacao
        //se o recebido na posicao 1 for 175 por exemplo - Pega os dados da matrix

        /*if (!vPingSucess && ByteUtil.toInt(myBuffer[1]) == Global.CMD_PING){
            vPingSucess = true;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivConectado.setImageResource(R.drawable.ic_conectado);
                    ivConectado.setVisibility(View.VISIBLE);
                    tvBluetoothCon.setTextColor(Color.GREEN);
                }
            });

        }else if (!vPingSucess){
            countPing++;
            vMandaPing = true;
            if (countPing == 5){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ivConectado.setImageResource(R.drawable.ic_desconectado);
                        ivConectado.setVisibility(View.VISIBLE);
                        Mensagem.exibirMensagem(MainActivity.this, "Não foi possível conectar com o Módulo LDM. Favor verique se o mesmo está Ativo." ,3);
                        stopThread = true;
                    }
                });
            }
        }*/
    }

    private void login() {

        SharedPreferences.Editor editor = getSharedPreferences("APP2BIM", MODE_PRIVATE).edit();
        editor.putString("USER", etUsuario.getText().toString());
        editor.commit();

        //Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
        //startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
