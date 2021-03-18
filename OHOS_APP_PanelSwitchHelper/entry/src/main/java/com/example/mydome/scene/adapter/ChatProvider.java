package com.example.mydome.scene.adapter;

import com.example.mydome.ResourceTable;
import ohos.aafwk.ability.Ability;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

import java.util.ArrayList;
import java.util.List;
//BaseItemProvider
public class ChatProvider extends BaseItemProvider {
    private Ability mAbility;
    private int msgCount;
    List<ChatInfo> mData;
    public ChatProvider(Ability ability,int msgNum) {
        mAbility=ability;
        mData = new ArrayList<>();
        for (int i = 0; i < msgNum; i++) {
            mData.add(new ChatInfo("模拟数据第" + (i + 1) + "条",i%2==0));
        }
        msgCount=msgNum;
    }
    public void insertInfo(ChatInfo chatInfo) {
        if (chatInfo != null) {
            mData.add(chatInfo);
            notifyDataSetItemInserted(mData.size() - 1);
        }
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        if(mData.get(i).owner){
            component= LayoutScatter.getInstance(mAbility).parse(ResourceTable.Layout_vh_chat_right_layout, null, false);
        }else{
            component= LayoutScatter.getInstance(mAbility).parse(ResourceTable.Layout_vh_chat_lift_layout, null, false);
        }
        Image avatar=(Image) component.findComponentById(ResourceTable.Id_avatar);
        Text msg=(Text) component.findComponentById(ResourceTable.Id_text);
        msg.setText(mData.get(i).message);
        avatar.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                new ToastDialog(mAbility).setContentText("点击了头像");
            }
        });
        msg.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                new ToastDialog(mAbility).setContentText("点击了消息");
            }
        });
        avatar.setLongClickedListener(new Component.LongClickedListener() {
            @Override
            public void onLongClicked(Component component) {
                new ToastDialog(mAbility).setContentText("长按了头像");
            }
        });
        msg.setLongClickedListener(new Component.LongClickedListener() {
            @Override
            public void onLongClicked(Component component) {
                new ToastDialog(mAbility).setContentText("长按了消息");
            }
        });
        return component;
    }
}
