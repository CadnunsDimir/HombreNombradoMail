package com.cadnunsdev.hombrenombradomail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cadnunsdev.hombrenombradomail.core.asynctasks.TryLoginOnSite;
import com.cadnunsdev.hombrenombradomail.core.dbentities.Login;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View _fragmentView;
    private Button _btnSubmit;
    private EditText _edtUserName;
    private EditText _edtPW;
    private String _mailBoxLink;
    private ListView _listViewLogins;
    private ArrayList<Login> _loginsSalvos;
    private ArrayAdapter _adapter;

    public LoginFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFrag newInstance(String param1, String param2) {
        LoginFrag fragment = new LoginFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _fragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        configEvents();
        return _fragmentView;
    }

    private void configEvents() {
        _btnSubmit = (Button)_fragmentView.findViewById(R.id.btnLoginSubmit);
        _edtUserName = (EditText)_fragmentView.findViewById(R.id.edtLoginUserName);
        _edtPW = (EditText)_fragmentView.findViewById(R.id.edtLoginPW);
        _listViewLogins = (ListView)_fragmentView.findViewById(R.id.lvwLoginsSalvos);
        _loginsSalvos = new ArrayList<>(Login.listAll(Login.class));
        _adapter = new ArrayAdapter(_fragmentView.getContext(),android.R.layout.simple_list_item_1,_loginsSalvos);
        _listViewLogins.setAdapter(_adapter);

        _listViewLogins.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Login login = (Login) adapterView.getItemAtPosition(i);
                try {
                    TryLoginOnSite worker = new TryLoginOnSite(new TryLoginOnSite.onSuccess() {
                        @Override
                        public void act(Document doc) {
                            _mailBoxLink = doc.select("#mainNavLinks > li:nth-child(3) > a").attr("href");
                            Toast.makeText(_fragmentView.getContext(), doc.select("#content-apps > h2").text(), Toast.LENGTH_LONG).show();
                        }
                    });
                    worker.setFormData(new TryLoginOnSite.FormData(login.getNomeUsuario(),login.getSenha()));
                    worker.execute();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        _listViewLogins.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Login login = (Login) adapterView.getItemAtPosition(i);
                login.delete();

                updateListaLogins();
                return false;
            }
        });

        _btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Login> findLogin = Login.find(Login.class,"nome_usuario = ?",_edtUserName.getText().toString());
                Login login = findLogin.size() > 0 ? findLogin.get(0) : new Login();
                login.setNomeUsuario(_edtUserName.getText().toString());
                login.setSenha(_edtPW.getText().toString());
                login.save();
                updateListaLogins();

                try {
                    TryLoginOnSite worker = new TryLoginOnSite(new TryLoginOnSite.onSuccess() {
                        @Override
                        public void act(Document doc) {
                            _mailBoxLink = doc.select("#mainNavLinks > li:nth-child(3) > a").attr("href");
                            Toast.makeText(_fragmentView.getContext(), doc.select("#content-apps > h2").text(), Toast.LENGTH_LONG).show();
                        }


                    });
                    worker.setFormData(new TryLoginOnSite.FormData(_edtUserName.getText().toString(),_edtPW.getText().toString()));
                    worker.execute();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateListaLogins() {
        _loginsSalvos.clear();
        _loginsSalvos.addAll(Login.listAll(Login.class));
        _adapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
