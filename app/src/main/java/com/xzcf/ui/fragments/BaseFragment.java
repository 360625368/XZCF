package com.xzcf.ui.fragments;

import android.support.v4.app.FragmentManager;

import me.yokeyword.fragmentation.SupportFragment;


public class BaseFragment extends SupportFragment {

    public FragmentManager getFM() {
        return getActivity().getSupportFragmentManager();
    }
}
