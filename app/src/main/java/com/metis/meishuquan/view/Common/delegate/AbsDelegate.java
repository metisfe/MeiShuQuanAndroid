package com.metis.meishuquan.view.Common.delegate;

/**
 * Created by WJ on 2015/6/1.
 */
public abstract class AbsDelegate<T> implements DelegateImpl<T> {

    private T mSource = null;

    public AbsDelegate (T t) {
        mSource = t;
    }

    @Override
    public T getSource() {
        return mSource;
    }
}
