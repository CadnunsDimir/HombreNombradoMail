package com.cadnunsdev.hombrenombradomail.core;

import com.cadnunsdev.hombrenombradomail.core.dbentities.Login;

/**
 * Created by Tiago Silva on 01/02/2017.
 */

public class LoginManager {
    private static Login _loggedUser;

    public static void login(Login login, String mailBoxLink, String cookie) {
        login.setLinkCaixaEntrada(mailBoxLink);
        login.setCookie(cookie);
        login.save();
        _loggedUser = login;
    }

    public static Login getLoggedUser(){
        return _loggedUser;
    }
}
