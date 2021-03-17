package com.example.demo.scene.api;

import com.example.library.view.panel.IPanelComponent;
import ohos.agp.components.*;
import ohos.app.Context;

public class CusPanelView extends ComponentContainer implements IPanelComponent {

    private int triggerViewId = 0;
    private boolean isToggle = true;
    private boolean isShowState = false;
    private Component LayoutComponent = null;

    public CusPanelView(Context context) {
        super(context);
    }

    public CusPanelView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initView(context, attrSet);
    }

    public CusPanelView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initView(context, attrSet);
    }

    private void initView(Context context, AttrSet attrSet) {
        triggerViewId = attrSet.getAttr("cus_panel_trigger").map(Attr::getIntegerValue).orElse(-1);
        isToggle = attrSet.getAttr("cus_panel_toggle").map(Attr::getBoolValue).orElse(false);

        Text text = new Text(context);
        text.setText("自定义面板");
        text.setTextSize(18, Text.TextSizeType.FP);
        addComponent(text);
    }

    @Override
    public int getBindingTriggerViewId() {
        return 0;
    }

    @Override
    public boolean isTriggerViewCanToggle() {
        return isToggle;
    }

    @Override
    public boolean isShowing() {
        if (isShowState) {
            isShowState = false;
        } else {
            isShowState = true;
        }
        return isShowState;
    }

    @Override
    public void onFinishFlate() {
        try {
            assertComponent();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public void setPanelLayoutId(Component panelLayoutComponent) {
        this.LayoutComponent = panelLayoutComponent;
    }

    @Override
    public void assertComponent() throws Exception {
        if (triggerViewId == -1) {
            throw new RuntimeException("PanelView -- you must set 'panel_layout' and panel_trigger by Integer id");
        }
        if (this instanceof Component) {
        } else {
            throw new RuntimeException("PanelView -- should be a view!");
        }
        addComponent(LayoutComponent);
    }
}
