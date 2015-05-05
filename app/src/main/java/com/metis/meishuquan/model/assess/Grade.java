package com.metis.meishuquan.model.assess;

import java.io.Serializable;

/**
 * POJO:年级
 * Created by wj on 15/4/2.
 */
public class Grade implements Serializable {
    private int id;

    private String name;

    private boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Grade) {
            return this.getId() == ((Grade) o).getId();
        }
        return false;
    }
}
