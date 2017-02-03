package com.cadnunsdev.hombrenombradomail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.cadnunsdev.hombrenombradomail.core.asynctasks.GetHtml;
import com.cadnunsdev.hombrenombradomail.core.asynctasks.onSuccessGetHtml;
import com.cadnunsdev.hombrenombradomail.core.dbentities.Email;
import com.cadnunsdev.hombrenombradomail.core.dbentities.Login;

import org.jsoup.nodes.Document;

public class EmailContentAct extends AppCompatActivity {

    private static final String EMAIL_BUNDLE_KEY = "email";
    private static final String LONGIN_BUNDLE_KEY = "login";
    private TextView tvAssunto;
    private Email email;
    private WebView webView;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_content);
        tvAssunto = (TextView)findViewById(R.id.email_content_assunto);
        email = (Email)getIntent().getSerializableExtra(EMAIL_BUNDLE_KEY);
        login = (Login)getIntent().getSerializableExtra(LONGIN_BUNDLE_KEY);
        webView = (WebView)findViewById(R.id.email_html);
        tvAssunto.setText(email.getTitulo());

        GetHtml getHtml = GetHtml
            .InstanceOf()
            .setLink(email.getLinkEmail())
            .setSessionCookie(login.getCookie())
            .setCallBackSuccess(new onSuccessGetHtml() {
                @Override
                public void act(Document document) {
                    String html = document.select("#viewMessage > div.msgBody").html();

                    html = html.length() == 0 ? document.body().html() : html;


                    webView.loadData(html,  "text/html", "UTF-8");
                }
            });
//       getHtml.execute();


    }

    public static void showActivity(Context ctx, Email email, Login login){
        Intent intent = new Intent(ctx,EmailContentAct.class);
        intent.putExtra(EMAIL_BUNDLE_KEY,email);
        intent.putExtra(LONGIN_BUNDLE_KEY,login);
        ctx.startActivity(intent);
    }
}
