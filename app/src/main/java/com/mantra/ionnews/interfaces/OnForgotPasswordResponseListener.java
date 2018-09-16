package com.mantra.ionnews.interfaces;

import com.mantra.ionnews.models.responses.Error;

/**
 * Created by TaNMay on 10/04/17.
 */

public interface OnForgotPasswordResponseListener {

    void forgotPasswordResponse(String message, Error error);

}
