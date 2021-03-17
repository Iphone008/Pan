package com.example.library.view.content;

import com.example.library.interfaces.ContentScrollMeasurerBuilder;
import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
import ohos.aafwk.ability.delegation.IAbilityDelegator;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.StackLayout;
import ohos.agp.components.TextField;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

import java.util.List;


public class StackContentContainer extends StackLayout implements IContentContainer {
    private int editTextId = 0;
    private int autoResetId = 0;
    private boolean autoResetByOnTouch=true;
    private  ContentContainerImpl contentContainer;
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD002000, "MY_TAG_StackContentContainer");

    public StackContentContainer(Context context) {
        this(context,null);
    }

    public StackContentContainer(Context context, AttrSet attrSet) {
        this(context, attrSet,"");
        initView(context,attrSet,"");
    }

    public StackContentContainer(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initView(context, attrSet, styleName);
    }

    private void initView(Context context, AttrSet attrSet,String defStyleRes){

        editTextId=attrSet.getAttr("edit_text").get().getIntegerValue();
        autoResetId=attrSet.getAttr("auto_reset_area").get().getIntegerValue();
        autoResetByOnTouch=attrSet.getAttr("auto_reset_enable").get().getBoolValue();
        new TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                boolean onTouchTrue =component.isTouchFocusable();
                boolean hookResult = getResetActionImpl().hookDispatchTouchEvent(touchEvent, onTouchTrue);
                return hookResult || onTouchTrue;
            }
        };
    }

    public void onFinishFlate(){
        contentContainer = new ContentContainerImpl(this,autoResetByOnTouch, editTextId, autoResetId);
        TextField editText = getInputActionImpl().getFullScreenPixelInputView();
//        addComponent(editText,0,new LayoutConfig(1,1));
    }


    @Override
    public Component findTriggerView(int id) {
        return contentContainer.findTriggerView(id);
    }


    @Override
    public void layoutContainer(int l, int t, int r, int b, List<ContentScrollMeasurer> contentScrollMeasurers, int defaultScrollHeight, boolean canScrollOutsize, boolean reset) {
        HiLog.info(LABEL,"run  contentScrollMeasurers "+contentScrollMeasurers.size());
        contentContainer.layoutContainer(l, t, r, b, contentScrollMeasurers, defaultScrollHeight, canScrollOutsize,reset);
    }

    @Override
    public void changeContainerHeight(int targetHeight) {
        contentContainer.changeContainerHeight(targetHeight);
    }


    @Override
    public IInputAction getInputActionImpl() {
        return contentContainer.getInputActionImpl();
    }

    @Override
    public IResetAction getResetActionImpl() {
        return contentContainer.getResetActionImpl();
    }
}
