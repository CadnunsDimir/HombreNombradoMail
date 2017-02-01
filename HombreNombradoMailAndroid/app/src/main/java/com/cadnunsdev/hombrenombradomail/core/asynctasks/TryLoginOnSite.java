package com.cadnunsdev.hombrenombradomail.core.asynctasks;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Tiago Silva on 31/01/2017.
 */

public class TryLoginOnSite extends AsyncTask<Void,String,Document> {

    private final onSuccess _onSucess;
    private String _sessionCookie;
    private FormData _formData;

    public void setFormData(FormData formData) {
        this._formData = formData;
    }


    public interface onSuccess{
        void act(Document doc, String sessionCookie);
    }

    public static class FormData{
        public final String _login;
        public final String _senha;

        public FormData(String login, String senha){
            _login = login;
            _senha = senha;
        }
    }

    public TryLoginOnSite(onSuccess onSuccess){
        _onSucess = onSuccess;
    }


    @Override
    protected void onPostExecute(Document document) {
        if(document != null){
            _onSucess.act(document,_sessionCookie.toString());
        }
    }

    @Override
    protected Document doInBackground(Void... voids) {
        try {
            Connection connection = Jsoup.connect("https://apps.jw.org/T_LOGIN2")
                    .data(new HashMap<String, String>(){
                        {
                            this.put("txtUserName",_formData._login);
                            this.put("txtPassword", _formData._senha);
                        }
                    });

            Document html = connection.post();
            Connection.Response response = connection.response();
            _sessionCookie =  response.cookie("session");

            return html;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
