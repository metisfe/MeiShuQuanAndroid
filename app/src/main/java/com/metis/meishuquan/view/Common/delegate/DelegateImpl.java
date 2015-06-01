package com.metis.meishuquan.view.Common.delegate;

public interface DelegateImpl<T> {
    public T getSource ();
    public DelegateType getDelegateType ();
}