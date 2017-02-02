package com.cadnunsdev.hombrenombradomail.core.dbentities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

/**
 * Created by Tiago Silva on 31/01/2017.
 */

public class Login extends SugarRecord implements Serializable{
    private String nomeUsuario;
    private String senha;
    private String cookie;
    private String linkCaixaEntrada;

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getLinkCaixaEntrada() {
        return linkCaixaEntrada;
    }

    public void setLinkCaixaEntrada(String linkCaixaEntrada) {
        this.linkCaixaEntrada = linkCaixaEntrada;
    }

    @Override
    public String toString() {
        return nomeUsuario;
    }
}
