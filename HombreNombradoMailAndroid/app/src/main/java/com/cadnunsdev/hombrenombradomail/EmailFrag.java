package com.cadnunsdev.hombrenombradomail;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cadnunsdev.hombrenombradomail.core.LoginManager;
import com.cadnunsdev.hombrenombradomail.core.adapters.EmailAdapter;
import com.cadnunsdev.hombrenombradomail.core.asynctasks.GetEmailList;
import com.cadnunsdev.hombrenombradomail.core.dbentities.Email;
import com.cadnunsdev.hombrenombradomail.core.dbentities.Login;
import com.cadnunsdev.hombrenombradomail.core.notifications.NotificationAppManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmailFrag extends Fragment {

    private RecyclerView recycleView;
    private ArrayList<Email> _emails;
    private EmailAdapter adapter;

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
        _emails = new ArrayList<>();

        Login login = LoginManager.getLoggedUser();
        if(login!= null)
            _emails.addAll(Email.find(Email.class,"login_id = ?",login.getId().toString()));

        recycleView = (RecyclerView)rootview.findViewById(R.id.recycleViewEmails);
        adapter = new EmailAdapter(_emails);
        
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootview.getContext());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(adapter);


        btn.setText("listar emails do site");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Login login = LoginManager.getLoggedUser();

                if(login == null){
                    Toast.makeText(rootview.getContext(),"Realizar o login", Toast.LENGTH_LONG).show();
                    NotificationAppManager.gerarNotificacaoTeste(rootview.getContext(),"Realizar o login","Realizar o login");
                    return;
                }



                GetEmailList.InstanceOf()
                    .loginData(login)
                    .callback(new GetEmailList.Callback() {
                        @Override
                        public void sucesso(List<Email> emails) {

                            for (Email em : emails) {
                                em.setLoginId(login.getId());
                                if(Email.find(Email.class,"login_id = ? and titulo = ? and data = ? and end_email = ?", login.getId().toString(),em.getTitulo(),em.getData(),em.getEndEmail()).size() == 0){
                                    em.save();
                                }
                            }



                            _emails.clear();
                            _emails.addAll(Email.find(Email.class,"login_id = ?",login.getId().toString()));
                            adapter.notifyDataSetChanged();
                            Toast.makeText(rootview.getContext(),"emails: "+ emails.size(), Toast.LENGTH_LONG).show();
                        }
                    }).execute();
            }
        });
    }



}
