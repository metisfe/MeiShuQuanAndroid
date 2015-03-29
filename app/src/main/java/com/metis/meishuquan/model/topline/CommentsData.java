package com.metis.meishuquan.model.topline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/3/29.
 */
public class CommentsData {

    private List<Comments> hostComments;
    private List<Comments> newComments;

    public List<Comments> getHostComments() {
        if (hostComments ==null){
            hostComments = new ArrayList<>();
        }
        return hostComments;
    }

    public void setHostComments(List<Comments> hostComments) {
        this.hostComments = hostComments;
    }

    public List<Comments> getNewComments() {
        if (newComments ==null){
            newComments = new ArrayList<>();
        }
        return newComments;
    }

    public void setNewComments(List<Comments> newComments) {
        this.newComments = newComments;
    }
}
