package com.example.library.view.content;

import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

import java.util.List;

public class DirectionalContentContainer extends DirectionalLayout implements IContentContainer, Component.TouchEventListener {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001500, "MY_TAG_LinearContentContainer");
    private int editTextId = 0;
    private int autoResetId = 0;
    private boolean autoResetByOnTouch = true;
    private ContentContainerImpl contentContainer;

    public DirectionalContentContainer(Context context) {
        super(context, null);
    }

    public DirectionalContentContainer(Context context, AttrSet attrSet) {
        super(context, attrSet, null);
        initView(context, attrSet);
    }

    public DirectionalContentContainer(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initView(context, attrSet);
    }

    private void initView(Context context, AttrSet attrSet) {
        editTextId = attrSet.getAttr("edit_view").map(Attr::getIntegerValue).orElse(-1);
        autoResetId = attrSet.getAttr("auto_reset_area").map(Attr::getIntegerValue).orElse(-1);
        autoResetByOnTouch = attrSet.getAttr("auto_reset_enable").map(Attr::getBoolValue).orElse(true);
        setOrientation(VERTICAL);

    }
    //---------鸿蒙中没有

    //添加view  onFinishFlate
    public void onFinishInflate() {
        //autoResetId
        contentContainer = new ContentContainerImpl(this, autoResetByOnTouch, editTextId, 0);
    }


    @Override
    public Component findTriggerView(int id) {
        return contentContainer.findTriggerView(id);

    }


    @Override
    public void layoutContainer(int l, int t, int r, int b, List<ContentScrollMeasurer> contentScrollMeasurers, int defaultScrollHeight, boolean canScrollOutsize, boolean reset) {
        contentContainer.layoutContainer(l, t, r, b, contentScrollMeasurers, defaultScrollHeight, canScrollOutsize, reset);
    }

    @Override
    public void changeContainerHeight(int targetHeight) {
        contentContainer.changeContainerHeight(targetHeight);
    }

    @Override
    public IInputAction getInputActionImpl() {
        HiLog.info(LABEL, "run this ");
        return contentContainer.getInputActionImpl();
    }

    @Override
    public IResetAction getResetActionImpl() {
        return contentContainer.getResetActionImpl();
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        boolean hookResult = getResetActionImpl().hookOnTouchEvent(touchEvent);
        return hookResult;
    }
}
