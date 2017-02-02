package com.cadnunsdev.hombrenombradomail.core.asynctasks;

import org.jsoup.nodes.Document;

/**
 * Created by Tiago Silva on 02/02/2017.
 */

public interface onSuccessGetLogin {
    void act(Document doc, String sessionCookie);

}