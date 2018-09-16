package com.mantra.ionnews.interfaces;

import com.mantra.ionnews.models.responses.Error;

/**
 * Created by TaNMay on 10/04/17.
 */

public interface OnLikeStoryResponseListener {

    void likeStoryResponse(String message, Error error, int index);

}
