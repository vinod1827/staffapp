package com.kleverowl.staffapp.components.numberpicker.Listener;

import android.util.Log;
import com.kleverowl.staffapp.components.numberpicker.Interface.LimitExceededListener;


/**
 * Created by travijuu on 26/05/16.
 */
public class DefaultLimitExceededListener implements LimitExceededListener {

    public void limitExceeded(int limit, int exceededValue) {

        String message = String.format("NumberPicker cannot set to %d because the limit is %d.", exceededValue, limit);
        Log.v(this.getClass().getSimpleName(), message);
    }
}
