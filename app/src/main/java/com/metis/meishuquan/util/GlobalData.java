package com.metis.meishuquan.util;

import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.ui.SelectedTabType;

/**
 * Created by wudi on 3/15/2015.
 */
public class GlobalData {
    private static GlobalData instance = new GlobalData();
    public static int AssessIndex = 0;
    public static CCircleDetailModel moment; //for data between moment list and moment detail

    private SelectedTabType tabTypeSelected;
    private int[] titleBarTypeSelected = new int[] { -1, -1, -1, -1, -1 };

    public static GlobalData getInstance()
    {
        return instance;
    }

    public void resetTabTypeSelected()
    {
        for (int i = 0; i < titleBarTypeSelected.length; i++)
        {
            titleBarTypeSelected[i] = -1;
        }
    }

    public SelectedTabType getTabTypeSelected()
    {
        return tabTypeSelected;
    }

    public void setTabTypeSelected(SelectedTabType tabTypeSelected)
    {
        this.tabTypeSelected = tabTypeSelected;
    }

    public int getTitleBarTypeSelected()
    {

        return titleBarTypeSelected[getIndexCompat ()];
    }

    public void setTitleBarTypeSelected(int titleBarTypeSelected)
    {
        //this.titleBarTypeSelected[tabTypeSelected.ordinal()] = titleBarTypeSelected;
        this.titleBarTypeSelected[getIndexCompat ()] = titleBarTypeSelected;
    }

    private int getIndexCompat () {
        int index = tabTypeSelected.ordinal();
        if (index >= titleBarTypeSelected.length) {
            index = titleBarTypeSelected.length - 1;
        }
        return index;
    }
}
