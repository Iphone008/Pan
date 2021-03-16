package com.example.mydome.provider;


import com.example.mydome.ResourceTable;
import com.example.mydome.bean.ChatInfo;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.util.List;

public class ChatProvider extends BaseItemProvider {

    private List<ChatInfo> mList;
    private AbilitySlice mSlice;
    private int TYPE = 0;

    public ChatProvider(List<ChatInfo> list, AbilitySlice slice) {
        this.mList = list;
        this.mSlice = slice;
    }

    @Override
    public int getItemComponentType(int position) {
        if (position % 2 == 0) {
            TYPE = 1;
            return TYPE;
        } else {
            TYPE = 0;
            return TYPE;
        }
    }

    @Override
    public int getComponentTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        ViewHolder viewHolder = null;
        if (component == null) {
            if (TYPE == 0) {
                component = LayoutScatter.getInstance(mSlice).parse(ResourceTable.Layout_item_chat, null, false);
            } else {
                component = LayoutScatter.getInstance(mSlice).parse(ResourceTable.Layout_item_chat2, null, false);
            }
            viewHolder = new ViewHolder();
            viewHolder.text = (Text) component.findComponentById(ResourceTable.Id_tv);
            component.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) component.getTag();
        }
        ChatInfo item = mList.get(i);
        viewHolder.text.setText(item.getMessage());
        /*if (i % 2 == 0) {
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setRgbColor(new RgbColor(139, 139, 122));
            viewHolder.text.setBackground(shapeElement);
            viewHolder.text.setTextAlignment(TextAlignment.RIGHT);
        } else {
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setRgbColor(new RgbColor(211, 211, 211));
            viewHolder.text.setBackground(shapeElement);
            viewHolder.text.setTextAlignment(TextAlignment.LEFT);
        }*/

        return component;
    }

    //为了优化LisContainer不可避免的使用了ViewHolder来复用
    private class ViewHolder {
        DirectionalLayout headC;
        Text text;
    }
}
