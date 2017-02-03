package com.cadnunsdev.hombrenombradomail.core.asynctasks;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Tiago Silva on 02/02/2017.
 */

public class GetHtml extends AsyncTask<Void,String,Document> {

    private onSuccessGetHtml _onSucess;

    private GetHtml(){

    }


    public static GetHtml InstanceOf(){
        return new GetHtml();
    }

    public GetHtml setCallBackSuccess(onSuccessGetHtml _onSucess) {
        this._onSucess = _onSucess;
        return this;
    }


    public GetHtml setLink(String link) {
        this.link = link;
        return this;
    }

    @Override
    protected void onPostExecute(Document document) {
        if(document != null){
            _onSucess.act(document);
        }
    }

    public GetHtml  setSessionCookie(String sessionCookie) {
        this.sessionCookie = sessionCookie;
        return this;
    }

    private String link;
    private String sessionCookie;

    @Override
    protected Document doInBackground(Void... voids) {
        try {
            Connection connection = Jsoup.connect(link);

            connection.header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36)");

            connection.followRedirects(false);

            if(sessionCookie != null && sessionCookie.length() > 10){
                connection.cookie("session", sessionCookie);
            }

            Document html = connection.get();

            return html;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
