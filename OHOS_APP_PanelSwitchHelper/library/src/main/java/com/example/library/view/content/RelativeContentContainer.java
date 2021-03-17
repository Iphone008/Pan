package com.example.library.view.content;


import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
import ohos.agp.components.*;
import ohos.agp.text.Layout;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

import java.util.List;

public class RelativeContentContainer extends DependentLayout implements IContentContainer,Component.TouchEventListener {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001700, "MY_TAG_RelativeContentContainer");
    private int editTextid = 0;
    private int autoResetid = 0;
    private boolean autoResetByOnTouch = true;
    private ContentContainerImpl contentContainer;

    public RelativeContentContainer(Context context) {
        super(context);
        setTouchEventListener(this);
    }
    public RelativeContentContainer(Context context, AttrSet attrSet) {
        super(context, attrSet, "");
        HiLog.info(LABEL, "RelativeContentContainer run 1 ");
        setTouchEventListener(this);
        initView(context, attrSet);
    }

    public RelativeContentContainer(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        HiLog.info(LABEL, "RelativeContentContainer run 2 ");
        setTouchEventListener(this);
        initView(context, attrSet);
    }
    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        HiLog.info(LABEL,"getResetActionImpl  is run ");
        boolean hookResult = getResetActionImpl().hookOnTouchEvent(touchEvent);
        return  hookResult;
    }

    //获取当前Containner 数据的
    private void initView(Context context, AttrSet attrSet) {
        HiLog.info(LABEL, "initView run start ");
        editTextid = attrSet.getAttr("edit_view").get().getIntegerValue();
        autoResetid = attrSet.getAttr("auto_reset_area").get().getIntegerValue();
        autoResetByOnTouch = attrSet.getAttr("auto_reset_enable").get().getBoolValue();
        HiLog.info(LABEL, "RelativeContentContainer  获取的自定义属性  \n editTextid " + editTextid + "\n autoResetid " + autoResetid + "\n  autoResetByOnTouch " + autoResetByOnTouch);
    }

    public void onFinishFlate() {
        HiLog.info(LABEL, "onFinishFlate is run end ");
        contentContainer = new ContentContainerImpl(this, autoResetByOnTouch, editTextid, autoResetid);
//        TextField editText = getInputActionImpl().getFullScreenPixelInputView();
//        addComponent(editText,0,new LayoutConfig(1,1));
    }


    @Override
    public Component findTriggerView(int id) {
        HiLog.info(LABEL, "findTriggerView is run end ");
        return contentContainer.findTriggerView(id);
    }

    @Override
    public void layoutContainer(int l, int t, int r, int b, List<ContentScrollMeasurer> contentScrollMeasurers, int defaultScrollHeight, boolean canScrollOutsize, boolean reset) {
        HiLog.info(LABEL, "RelativeContentContainer run layoutContainer");
        contentContainer.layoutContainer(l, t, r, b, contentScrollMeasurers, defaultScrollHeight, canScrollOutsize, reset);
    }

    @Override
    public void changeContainerHeight(int targetHeight) {
        HiLog.info(LABEL, "changeContainerHeight run layoutContainer");
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
