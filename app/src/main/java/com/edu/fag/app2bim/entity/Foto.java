package com.edu.fag.app2bim.entity;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Foto extends SugarRecord implements Serializable {

    private int codigo; //codigo
    private String path;
    private String tipo;
    private String imagem;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
