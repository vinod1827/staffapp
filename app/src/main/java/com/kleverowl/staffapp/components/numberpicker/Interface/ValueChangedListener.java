package com.kleverowl.staffapp.components.numberpicker.Interface;


import com.kleverowl.staffapp.components.numberpicker.Enums.ActionEnum;

/**
 * Created by travijuu on 19/12/16.
 */

public interface ValueChangedListener {

    void valueChanged(int value, ActionEnum action);
}
