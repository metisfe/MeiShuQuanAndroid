package com.metis.meishuquan.model.topline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/3/29.
 */
public class CommentsData {

    private List<Comment> hostComments;
    private List<Comment> newComments;

    public List<Comment> getHostComments() {
        if (hostComments ==null){
            hostComments = new ArrayList<>();
        }
        return hostComments;
    }

    public void setHostComments(List<Comment> hostComments) {
        this.hostComments = hostComments;
    }

    public List<Comment> getNewComments() {
        if (newComments ==null){
            newComments = new ArrayList<>();
        }
        return newComments;
    }

    public void setNewComments(List<Comment> newComments) {
        this.newComments = newComments;
    }
}
