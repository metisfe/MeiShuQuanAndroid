package com.metis.meishuquan.fragment.act;

import android.os.Bundle;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.MultiListViewFragment;

/**
 * Created by WJ on 2015/5/12.
 */
public class PKSwitchFragment extends SpecFragment {

    private static PKSwitchFragment sFragment = new PKSwitchFragment();

    public static PKSwitchFragment getInstance () {
        return sFragment;
    }

    @Override
    public String[] getStringArray() {
        String[] array = getResources().getStringArray(R.array.act_filter_2);
        return array;
    }
}
