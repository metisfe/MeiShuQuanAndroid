package com.metis.meishuquan.view.common.delegate;

public interface DelegateImpl<T> {
    public T getSource ();
    public DelegateType getDelegateType ();
}