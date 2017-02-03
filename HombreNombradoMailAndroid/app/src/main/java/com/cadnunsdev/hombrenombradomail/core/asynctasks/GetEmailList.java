package com.cadnunsdev.hombrenombradomail.core.asynctasks;

import android.os.AsyncTask;

import com.cadnunsdev.hombrenombradomail.core.dbentities.Email;
import com.cadnunsdev.hombrenombradomail.core.dbentities.Login;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tiago Silva on 01/02/2017.
 */

public class GetEmailList extends AsyncTask<Void,String,List<Email>> {

    private Login _login;
    private Callback _callback;

    private GetEmailList(){

    }

    public static GetEmailList InstanceOf(){
        return new GetEmailList();
    }

    public  GetEmailList loginData(Login login){
        _login = login;
        return this;
    }
    public  GetEmailList callback(Callback cb){
        _callback = cb;
        return this;
    }


    @Override
    protected List<Email> doInBackground(Void... voids) {



        try {
            Connection connection = Jsoup
                    .connect(_login.getLinkCaixaEntrada())
                    .cookie("session", _login.getCookie());

            Document html = connection.get();
            Connection.Response response = connection.response();

            List<Email> emails = new ArrayList<>();
            Elements tbody = html.select("#listMessages > tbody > tr");
            for (Element el : tbody) {
                Email email = new Email();
                email.setEndEmail(el.children().get(1).text());
                email.setTitulo(el.children().get(2).text());
                email.setData(el.children().get(3).text());

                //{{msgId}}
                String url = _login.getLinkCaixaEntrada().replace("INBOX","MSGUSRINBOX")+"?";
                String link = url +"txtAction=VIEWMSG&txtMessageUID={{msgId}}&txtListPage=1&txtSortField=1&txtSortDirection=1";

                String emailId = el.children().get(1).attr("onclick").split("'")[1];
//                emailId = URLEncoder.encode(emailId, "UTF-8");

                email.setLinkEmail(link.replace("{{msgId}}",emailId));
                emails.add(email);
            }
            return emails;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Email> emails) {
        if(emails != null && _callback != null){
            _callback.sucesso(emails);
        }
    }

    public interface Callback{
        void sucesso(List<Email> emails);
    }
}
