package com.example.library.view.panel;

import com.example.library.ResourceTable;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.text.Layout;
import ohos.app.Context;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.ResourceType;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.List;

public class PanelView extends StackLayout implements IPanelComponent {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001500, "MY_TAG_PanelView");
    public String panelName;
    private Component LayoutComponent =null;
    private int triggerViewId = 0;
    private boolean isToggle = true;
    private Context mContext;
    private ComponentContainer root;
    private boolean isShowState=false;

    public PanelView(Context context) {
        super(context);
        HiLog.info(LABEL,"PanelView 构造 1  is run");
    }

    public PanelView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initView(context, attrSet, "");
        HiLog.info(LABEL,"PanelView 构造 2  is run");
    }

    public PanelView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initView(context, attrSet, styleName);
        HiLog.info(LABEL,"PanelView 构造 3  is run");
    }
    @Override
    public void setPanelLayoutId(Component panelLayoutComponent) {
        this.LayoutComponent=panelLayoutComponent;
    }

    private void initView(Context context, AttrSet attrSet, String styleName) {
        mContext = context;
        panelName = attrSet.getAttr("panel_name").map(Attr::getStringValue).orElse("");
        triggerViewId = attrSet.getAttr("panel_trigger").map(Attr::getIntegerValue).orElse(-1);
        isToggle = attrSet.getAttr("panel_toggle").map(Attr::getBoolValue).orElse(false);
        HiLog.info(LABEL,"panel view run end------------------------------------------------>");

    }


    @Override
    public int getBindingTriggerViewId() {
        HiLog.info(LABEL, "PanelView  triggerViewId  " + triggerViewId);
        return triggerViewId;
    }


    @Override
    public boolean isTriggerViewCanToggle() {
        return isToggle;
    }

    @Override
    public boolean isShowing() {
        if (isShowState){
            isShowState=false;
        }else {
            isShowState=true;
        }
        return isShowState;
    }

    //子类绘制
    @Override
    public void assertComponent() throws Exception {
        HiLog.info(LABEL,"------>PanelView  assertComponent is run ");
        HiLog.info(LABEL,"------>PanelView  assertComponent triggerViewId  : "+triggerViewId);
        if (triggerViewId == -1) {
            throw new RuntimeException("PanelView -- you must set 'panel_layout' and panel_trigger by Integer id");
        }
        if (getChildCount() > 0) {
            throw new RuntimeException("PanelView -- you can't have any child! 3");
        }
        //默认实现 FrameLayout 恒为false，这里只是强调申明而已，可以不写。
        HiLog.info(LABEL,"------>PanelView  assertComponent triggerViewId  : "+triggerViewId);
        if (this instanceof Component) {
        } else {
            throw new RuntimeException("PanelView -- should be a view!");
        }
            addComponent(LayoutComponent);
    }

    public void onFinishFlate() {
        try {
            assertComponent();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }



}
