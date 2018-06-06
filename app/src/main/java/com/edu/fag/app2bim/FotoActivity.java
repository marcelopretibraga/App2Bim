package com.edu.fag.app2bim;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.fag.app2bim.entity.Foto;
import com.google.gson.annotations.Expose;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class FotoActivity extends AppCompatActivity {

    private final int CAMERA = 1;
    private File fotoCamera = null;
    private ImageView ivFoto;
    private TextView tvImagemBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_foto);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            //Para Resolver Problema com URI da Camera
            StrictMode.VmPolicy.Builder newbuilder =
                    new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(newbuilder.build());

            ivFoto = findViewById(R.id.ivFoto);
            tvImagemBase = findViewById(R.id.tvImagemBase);

            Foto foto = Foto.first(Foto.class);
            if (foto != null)
                tvImagemBase.setText(foto.getImagem());

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirCamera();
                }
            });
    }

    private void abrirCamera() {
        //Busca um diretorio padrao para gravar o Arquivo
        File diretorioFoto = this.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        //Cria o Arquivo Foto
        fotoCamera = new File(diretorioFoto, "foto.png");


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fotoCamera));//TO-DO

        //Esperando um Resultado
        startActivityForResult(intent, CAMERA);
    }

    //Metodo no qual recupero a Foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Verifico se o resultado eh o da camera
        //e o retorno foi obtido
        if (requestCode == CAMERA && resultCode == RESULT_OK){
            try{
                //
                FileInputStream fis = new FileInputStream(fotoCamera);
                Bitmap fotoBitmap = BitmapFactory.decodeStream(fis);
                ivFoto.setImageBitmap(fotoBitmap);
                //Verificacao da imagem
                if (fotoBitmap != null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //Reduzo a qualidade da imagem para 50%
                    fotoBitmap.compress(Bitmap.CompressFormat.JPEG,
                            50, baos);
                    //Jogo a Imagem comprimida em um Array de Byte
                    byte[] imagemComprimida = baos.toByteArray();

                    //Transformo em Base64
                    String imagemBase = Base64
                            .encodeToString(imagemComprimida,
                                    Base64.DEFAULT);

                    tvImagemBase.setText(imagemBase);

                    Foto foto = new Foto();
                    foto.setCodigo(1);
                    foto.setImagem(imagemBase);
                    foto.setPath("");
                    foto.setTipo(Bitmap.CompressFormat.JPEG.toString());
                    foto.save();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
