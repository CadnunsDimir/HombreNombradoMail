package com.cadnunsdev.hombrenombradomail;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

public class EmailFrag extends Fragment {
    private String _mailBoxLink;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_email_frag);
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
////
////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
////        });
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        super.onCreateView(inflater, container, savedInstanceState);
        final View rootview = inflater.inflate(R.layout.activity_email_frag, container, false);
        ConfigInnerElements(rootview);
        return rootview;
    }

    private void ConfigInnerElements(final View rootview) {
        Button btn = (Button)rootview.findViewById(R.id.btnTesteHtml);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    TryLoginOnSite worker = new TryLoginOnSite(new TryLoginOnSite.onSuccess() {
                        @Override
                        public void act(Document doc) {
                            _mailBoxLink = doc.select("#mainNavLinks > li:nth-child(3) > a").attr("href");
                            Toast.makeText(rootview.getContext(), doc.select("#content-apps > h2").text(), Toast.LENGTH_LONG).show();
                        }
                    });
                    worker.setFormData(new TryLoginOnSite.FormData("", ""));
                    worker.execute();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private static class TryLoginOnSite extends AsyncTask<Void,String,Document>{

        private final onSuccess _onSucess;
        private String _sessionCookie;
        private FormData _formData;

        public void setFormData(FormData formData) {
            this._formData = formData;
        }


        public interface onSuccess{
            void act(Document doc);
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
                _onSucess.act(document);
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
}
