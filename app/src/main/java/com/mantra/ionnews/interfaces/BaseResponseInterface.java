package com.mantra.ionnews.interfaces;

import com.mantra.ionnews.models.responses.Error;

/**
 * Created by TaNMay on 08/03/17.
 */

public interface BaseResponseInterface {

    void response(Object response, Error error);

}
