package com.cadnunsdev.hombrenombradomail.core.dbentities;

/**
 * Created by Tiago Silva on 01/02/2017.
 */

public class Email {
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEndEmail() {
        return endEmail;
    }

    public void setEndEmail(String endEmail) {
        this.endEmail = endEmail;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLinkEmail() {
        return linkEmail;
    }

    public void setLinkEmail(String linkEmail) {
        this.linkEmail = linkEmail;
    }

    private String data;
    private String endEmail;
    private String titulo;
    private String linkEmail;
}
