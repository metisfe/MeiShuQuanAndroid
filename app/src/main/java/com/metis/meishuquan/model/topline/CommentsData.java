package com.metis.meishuquan.model.topline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/3/29.
 */
public class CommentsData {

    private List<Comment> hotComments;
    private List<Comment> newComments;

    public List<Comment> getHotComments() {
        if (hotComments ==null){
            hotComments = new ArrayList<Comment>();
        }
        return hotComments;
    }

    public void setHotComments(List<Comment> hotComments) {
        this.hotComments = hotComments;
    }

    public List<Comment> getNewComments() {
        if (newComments ==null){
            newComments = new ArrayList<Comment>();
        }
        return newComments;
    }

    public void setNewComments(List<Comment> newComments) {
        this.newComments = newComments;
    }
}
