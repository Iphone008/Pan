package com.example.library.view.content;

import com.example.library.interfaces.ContentScrollMeasurerBuilder;
import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
import ohos.agp.components.*;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

import java.util.List;

public class DirectionalContentContainer extends DirectionalLayout implements IContentContainer {
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
        initView(context, attrSet, "");
    }

    public DirectionalContentContainer(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initView(context, attrSet, styleName);
    }


    private void initView(Context context, AttrSet attrSet, String styleName) {
        editTextId = attrSet.getAttr("edit_view").get().getIntegerValue();
        autoResetId = attrSet.getAttr("auto_reset_area").get().getIntegerValue();
        autoResetByOnTouch = attrSet.getAttr("auto_reset_enable").get().getBoolValue();
        setOrientation(VERTICAL);
        contentContainer = new ContentContainerImpl(this, autoResetByOnTouch, editTextId, autoResetId);
        new TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                HiLog.info(LABEL,"initView  LinearContainer  is run");
                return true;
            }
        };
    }
    //---------鸿蒙中没有

    //添加view  onFinishFlate
    public void onFinishInflate() {
        contentContainer = new ContentContainerImpl(this, autoResetByOnTouch, editTextId, autoResetId);
        TextField editText = getInputActionImpl().getFullScreenPixelInputView();
        if (editText != null) {
            ComponentContainer.LayoutConfig config = new ComponentContainer.LayoutConfig(1, 1);
//            addComponent(editText, 0, config);
        }
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
}
