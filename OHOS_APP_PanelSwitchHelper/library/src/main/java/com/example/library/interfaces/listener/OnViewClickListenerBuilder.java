package com.example.library.interfaces.listener;


import com.example.library.interfaces.listener.IListener.IOnClickBefore;
import com.example.library.interfaces.listener.IListener.OnComponentClickListener;
import ohos.agp.components.Component;


public class OnViewClickListenerBuilder implements OnComponentClickListener {
    public IOnClickBefore mIOnClickBefore = null;

    @Override
    public void onClickBefore(Component component) {
        mIOnClickBefore.IonClickBefore(component);
    }

    public void onClickBefore(IOnClickBefore iOnClickBefore) {
        this.mIOnClickBefore = iOnClickBefore;
    }

}


