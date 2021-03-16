package com.example.library.interfaces.listener;

import com.example.library.interfaces.listener.IListener.IOnFocusChange;
import com.example.library.interfaces.listener.IListener.OnTextFieldChangeListener;
import ohos.agp.components.Component;

public class OnEditFocusChangeListenerBuilder implements OnTextFieldChangeListener {


    private OnTextFieldChangeListener mOnEditFocusChangeListener;
    private IOnFocusChange monFocusChange=null ;

    @Override
    public void onFocusChange(Component mComponent, boolean hasFocus) {
        monFocusChange.Ionfucation(mComponent,hasFocus);
    }

    public void  onFocusChange(IOnFocusChange iOnFocusChange){
         this.monFocusChange=iOnFocusChange;
    }
}


