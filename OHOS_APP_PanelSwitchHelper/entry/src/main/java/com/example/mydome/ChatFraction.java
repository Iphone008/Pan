package com.example.mydome;

import com.example.library.PanelSwitchHelper;
import com.example.library.view.PanelSwitchLayout;
import com.example.mydome.anno.ChatPageType;
import com.example.mydome.bean.ChatInfo;
import com.example.mydome.scene.adapter.ChatProvider;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.service.WindowManager;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatFraction extends Fraction {
    ListContainer ChatList;
    int type = 0;
    Text titleBar;
    Component component;
    private Image emotion_btn, addbt;
    private PanelSwitchHelper mHelper;
    private ChatProvider mChatProvider;
    private PanelSwitchLayout mPanelSwitchLayout;
    public ChatFraction(int type) {
        this.type = type;
    }
    List<Component> PanelLayoutId = new ArrayList<>();
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_chat, null, false);
        return component;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        getFractionAbility().getWindow().setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_PAN);
        //构建Panel的LayoutComponent源
        PanelLayoutId.add(LayoutScatter.getInstance(getFractionAbility()).parse(ResourceTable.Layout_panel_add_layout, null, false));
        PanelLayoutId.add(LayoutScatter.getInstance(getFractionAbility()).parse(ResourceTable.Layout_panel_add_layout, null, false));
        PanelLayoutId.add(LayoutScatter.getInstance(getFractionAbility()).parse(ResourceTable.Layout_panel_add_layout, null, false));
        ChatList = (ListContainer) component.findComponentById(ResourceTable.Id_chat_list);
        titleBar=(Text) component.findComponentById(ResourceTable.Id_title_bar);
        mPanelSwitchLayout = (PanelSwitchLayout) component.findComponentById(ResourceTable.Id_panel_switch_layout);
        List<ChatInfo> list = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            list.add(new ChatInfo("模拟数据第" + i + "条"));
        }
        mChatProvider = new ChatProvider(getFractionAbility(),30);
        ChatList.setItemProvider(mChatProvider);
        addbt = (Image) component.findComponentById(ResourceTable.Id_add_btn);
        emotion_btn = (Image) component.findComponentById(ResourceTable.Id_emotion_btn);

        mPanelSwitchLayout.setPanelLayoutId(PanelLayoutId);
        mPanelSwitchLayout.onFinishInflate();
        switch (type) {
            case ChatPageType.DEFAULT:
                titleBar.setVisibility(Component.HIDE);
                break;
            case ChatPageType.TITLE_BAR:
                titleBar.setVisibility(Component.VISIBLE);
                titleBar.setText("Fragment-自定义标题栏");
                break;
            case ChatPageType.COLOR_STATUS_BAR:
                getFractionAbility().getWindow().addFlags(WindowManager.LayoutConfig.MARK_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                titleBar.setVisibility(Component.VISIBLE);
                titleBar.setText("Fragment-自定义标题栏,状态栏着色");
                try {
                    getFractionAbility().getWindow().setStatusBarColor(getFractionAbility().getResourceManager().getElement(ResourceTable.Color_colorPrimary).getColor());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NotExistException e) {
                    e.printStackTrace();
                } catch (WrongTypeException e) {
                    e.printStackTrace();
                }
                break;
            case ChatPageType.TRANSPARENT_STATUS_BAR:
                getFractionAbility().getWindow().addFlags(WindowManager.LayoutConfig.MARK_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                titleBar.setVisibility(Component.VISIBLE);
                titleBar.setText("Fragment-自定义标题栏，状态栏透明");
                try {
                    getFractionAbility().getWindow().setStatusBarColor(getFractionAbility().getResourceManager().getElement(ResourceTable.Color_colorPrimary).getColor());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NotExistException e) {
                    e.printStackTrace();
                } catch (WrongTypeException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }

    @Override
    protected void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    protected void onBackground() {
        super.onBackground();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onComponentDetach() {
        super.onComponentDetach();
    }
}
