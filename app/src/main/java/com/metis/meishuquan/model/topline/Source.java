package com.metis.meishuquan.model.topline;

/**
 * 数据来源
 *
 * Created by WJ on 2015/3/24.
 */
public class Source {
    private int id;
    private String title;
    private String goal;

    public Source(int id, String title, String goal) {
        this.id = id;
        this.title = title;
        this.goal = goal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    @Override
    public String toString() {
        return "Source{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", goal='" + goal + '\'' +
                '}';
    }
}
