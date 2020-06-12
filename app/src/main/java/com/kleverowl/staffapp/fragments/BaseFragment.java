package com.kleverowl.staffapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import com.kleverowl.staffapp.listeners.PageChangeListener;


/**
 * @Created by Vinod Kulkarni
 * @Date on 6/2/2016.
 */
public class BaseFragment extends Fragment {
    protected Context mContext;
    protected Activity mActivity;

    protected PageChangeListener pageChangeListener;
    public static Handler mHandler;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mActivity = getActivity();
    }

    public void setHandler(Handler handler){
         this.mHandler = handler;
    }
    public void setPageChangeListener(PageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }
}
