package com.example.library.view.panel;

import com.example.library.Constants;
import com.example.library.interfaces.ComponentAssertion;
import ohos.agp.components.*;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.Pair;

import java.util.*;


public class PanelContainer extends DirectionalLayout implements ComponentAssertion {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001400, "MY_TAG_PanelContainer");
    public Map<Integer, IPanelComponent> panelComponentMap = new HashMap<>();

    private LayoutConfig layoutParams;

    private List<Component> paenllayoutComponent=new ArrayList<>();

    public PanelContainer(Context context) {
        super(context);
    }

    public PanelContainer(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initView(context, attrSet);
    }

    public PanelContainer(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initView(context, attrSet);
    }

    public void initView(Context context, AttrSet attrSet) {
        HiLog.info(LABEL, "初始化 PanelContainer ");

    }


    @Override
    public void assertComponent() throws Exception {
        HiLog.info(LABEL,"当前view的 子类有  "+getChildCount());
        for (int i = 0; i < getChildCount(); i++) {
            IPanelComponent panel = null;
            getComponentAt(i);
            HiLog.info(LABEL, "assertComponent  id  " + getComponentAt(i).getId());
            if (getComponentAt(i) instanceof IPanelComponent) {
                panel = (IPanelComponent) getComponentAt(i);
            } else {
                throw new RuntimeException("PanelContainer -- PanelContainer's child should be IPanelView");
            }
            panelComponentMap.put(panel.getBindingTriggerViewId(), panel);
            ((Component) panel).setVisibility(Component.HIDE);
            panel.setPanelLayoutId(paenllayoutComponent.get(i));
            panel.onFinishFlate();
        }
    }


    public IPanelComponent getPanelView(int panelId) {
        HiLog.info(LABEL, "getPanelView:  is run ");
        return (IPanelComponent) panelComponentMap.get(panelId);
    }

    public int getPanelId(IPanelComponent panel) {
        String valueId = panel.getBindingTriggerViewId() + "";
        if (!valueId.equals("")) {
            HiLog.info(LABEL,"--->getPanelId()-->   panel.getBindingTriggerViewId() <---"+panel.getBindingTriggerViewId());
            return panel.getBindingTriggerViewId();
        } else {
            HiLog.info(LABEL,"--->getPanelId()-->  panel.getBindingTriggerViewId() <---"+0);
            return Constants.getInstance().PANEL_KEYBOARD;
        }
    }

    //显示
    public Pair<Integer, Integer> showPanel(int panelId, Pair<Integer, Integer> size) {
        HiLog.info(LABEL, "showPanel:  is run ");
        HiLog.info(LABEL, "showPanel:  size  "+size.toString());
        IPanelComponent panel = (IPanelComponent) panelComponentMap.get(panelId);
        HiLog.info(LABEL, "showPanel:  panel  ===> "+(panel==null));

        //---------
        for (IPanelComponent panelItem : panelComponentMap.values()) {
            HiLog.info(LABEL,"显示面板方法 "+(panelItem != panel) );
            if (panelItem instanceof Component) {
                HiLog.info(LABEL,"显示面板  =====》 "+(panelItem != panel));
                ((Component) panelItem).setVisibility(panelItem != panel ? Component.HIDE : Component.VISIBLE);
            }
        }

        layoutParams = (LayoutConfig) ((Component) panel).getLayoutConfig();
//        HiLog.info(LABEL,"showPanel height -->"+layoutParams.height);
//        HiLog.info(LABEL,"showPanel weight -->"+layoutParams.weight);
        Pair curSize = new Pair(layoutParams.width, layoutParams.height);
//        HiLog.info(LABEL,"------------------------------");
//        HiLog.info(LABEL,"showPanel height -->"+layoutParams.width);
//        HiLog.info(LABEL,"showPanel height -->"+size.f);
//        HiLog.info(LABEL,"------------------------------");
//        HiLog.info(LABEL,"------------------------------");
//        HiLog.info(LABEL,"showPanel height -->"+curSize.s);
//        HiLog.info(LABEL,"showPanel height -->"+size.s);
//        HiLog.info(LABEL,"------------------------------");
        if (curSize.f != size.f || curSize.s != size.s) {
            layoutParams.width = size.f;
            layoutParams.height = size.s;
            ((Component) panel).setLayoutConfig(layoutParams);
        }
        return curSize;
    }

    public void changeContainerHeight(int targetHeight) {
        if (layoutParams != null && layoutParams.height != targetHeight) {
            HiLog.info(LABEL, " layoutParams.height  " + layoutParams.height);
            HiLog.info(LABEL, " layoutParams  targetHeight  " + targetHeight);
            layoutParams.height = targetHeight;
        }
    }

    public void onFinishFlate() {
        try {
            assertComponent();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }
    public  void  setPanelLayoutId(List<Component> layoutComponent){
        this.paenllayoutComponent=layoutComponent;
        onFinishFlate();
    }

}