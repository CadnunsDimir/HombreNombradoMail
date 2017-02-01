package com.cadnunsdev.hombrenombradomail.core.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cadnunsdev.hombrenombradomail.R;
import com.cadnunsdev.hombrenombradomail.core.dbentities.Email;

import java.util.ArrayList;

/**
 * Created by Tiago Silva on 01/02/2017.
 */

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {

    private final ArrayList<Email> _emails;

    public EmailAdapter(ArrayList<Email> emails){
        _emails = emails;
    }

    @Override
    public EmailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mail_card_view,parent,false);
        EmailViewHolder viewHolder = new EmailViewHolder(view,_emails);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmailViewHolder holder, int position) {
        Email email = _emails.get(position);
        holder.bind(email);
    }

    @Override
    public int getItemCount() {
        return _emails.size();
    }
    public class EmailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView _mailAssunto;
        private final TextView _mailData;
        private final TextView _mailRemetente;
        private final ArrayList<Email> emails;
        private final Context _ctx;

        public EmailViewHolder(View itemView, ArrayList<Email> emails) {
            super(itemView);
            _mailAssunto = (TextView) itemView.findViewById(R.id.email_assunto);
            _mailData = (TextView) itemView.findViewById(R.id.email_data);
            _mailRemetente = (TextView) itemView.findViewById(R.id.email_remetente);
            itemView.setOnClickListener(this);
            _ctx = itemView.getContext();
            this.emails = emails;
        }

        public void bind(Email email) {
            _mailAssunto.setText(email.getTitulo());
            _mailData.setText(email.getData());
            _mailRemetente.setText(email.getEndEmail());
        }

        @Override
        public void onClick(View view) {
            Email email = _emails.get(getAdapterPosition());
            Toast.makeText(_ctx,email.getLinkEmail(), Toast.LENGTH_LONG).show();
        }
    }
}
