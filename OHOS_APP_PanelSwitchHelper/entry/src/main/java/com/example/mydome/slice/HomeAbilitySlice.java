package com.example.mydome.slice;

import com.example.mydome.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeAbilitySlice extends AbilitySlice {
    final String activity_title = "聊天场景 Activity实现";
    final String activity_1 = "activity 无标题栏";
    final String activity_2 = "activity 有标题栏";
    final String activity_3 = "activity 自定义标题栏";
    final String activity_4 = "activity 有标题栏，状态栏着色";
    final String activity_5 = "activity 无标题栏，状态栏透明，不绘制到状态栏";
    final String activity_6 = "activity 无标题栏，状态栏透明，绘制到状态栏";

    final String fragment_title = "聊天场景 Fragment实现";
    final String fragment_1 = "fragment 无标题栏";
    final String fragment_2 = "fragment 自定义标题栏";
    final String fragment_3 = "fragment 自定义标题栏，状态栏着色";
    final String fragment_4 = "fragment 自定义标题栏，状态栏透明";

    final String window_title = "聊天场景 other window 实现";
    final String window_1 = "DialogFragment";
    final String window_2 = "PopupWindow";
    final String window_3 = "Dialog";

    final String scene_title = "扩展场景";
    final String scene_1 = "视频播放(优于b站)";
    final String scene_2 = "信息流评论(同微信朋友圈效果)";
    //    final String scene_2_2 = "信息流评论(同微信朋友圈效果-非dialog)";
    final String scene_3 = "PC直播(优于虎牙效果)";
    final String scene_4 = "手机直播(优于抖音效果)";
    final String scene_5 = "复杂聊天场景";

    final String api_content_container_title = "api 内容容器及扩展";
    final String api_content_container_1 = "基于 LinearLayout 实现";
    final String api_content_container_2 = "基于 RelativeLayout 实现";
    final String api_content_container_3 = "基于 FrameLayout 实现";
    final String api_content_container_4 = "自定义布局实现";

    final String api_define_content_container_scroll_title = "api 内容容器内部布局干预滑动";
    final String api_define_content_container_scroll = "内容区域干预子View滑动行为";

    final String api_cus_panel_title = "api 面板扩展及默认高度设置";
    final String api_cus_panel = "自定义PanelView";
    final String api_cus_panel_height = "未获取输入法高度前高度兼容";

    final String api_reset_title = "api 自动隐藏软键盘/面板";
    final String api_reset_1 = "点击内容容器收起面板(默认处理)";
    final String api_reset_2 = "点击空白 View 收起面板";
    final String api_reset_3 = "点击原生 RecyclerView 收起面板";
    final String api_reset_4 = "点击自定义 RecyclerView 收起面板";
    final String api_reset_5 = "关闭点击内容容器收起面板";
    final String[] groupStrings = {
            activity_title,
            fragment_title,
            window_title,
            scene_title,
            api_content_container_title,
            api_define_content_container_scroll_title,
            api_cus_panel_title,
            api_reset_title};
    ListContainer listContainer;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_home);
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_list);
        findComponentById(ResourceTable.Id_tip_tv);
        listContainer.setItemProvider(new BaseItemProvider() {
            @Override
            public int getCount() {
                return groupStrings.length;
            }

            @Override
            public Object getItem(int i) {
                return groupStrings[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
                component = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_item_home, null, false);
                Text itemTv = (Text) component.findComponentById(ResourceTable.Id_home_text);
                itemTv.setText(groupStrings[i]);
                return component;
            }
        });
        listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {

                Intent secondIntent = new Intent();
                // 指定待启动FA的bundleName和abilityName
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName("com.example.mydome")
                        .withAbilityName("com.example.mydome.TransitAbility")
                        .build();
                ArrayList<String> titles=new ArrayList<>();
                switch (groupStrings[i]) {
                    case activity_title:
                        titles.addAll(Arrays.asList(new String[]{activity_1,activity_2,activity_3,activity_4,activity_5,activity_6}));
                        break;
                    case fragment_title:
                        titles.addAll(Arrays.asList(new String[]{fragment_1,fragment_2,fragment_3,fragment_4}));
                        break;
                    case window_title:
                        titles.addAll(Arrays.asList(new String[] {window_1,window_2,window_3}));
                        break;
                    case scene_title:
                        titles.addAll(Arrays.asList(new String[]{scene_1,scene_2,scene_3,scene_4,scene_5}));
                        break;
                    case api_content_container_title:
                        titles.addAll(Arrays.asList(new String[] {api_content_container_1,api_content_container_2,api_content_container_3,api_content_container_4}));
                        break;
                    case api_define_content_container_scroll_title:
                        titles.addAll(Arrays.asList(new String[]{api_define_content_container_scroll}));
                        break;
                    case api_cus_panel_title:
                        titles.addAll(Arrays.asList(new String[] {api_cus_panel,api_cus_panel_height}));
                        break;
                    case api_reset_title:
                        titles.addAll(Arrays.asList(new String[] {api_reset_1,api_reset_2,api_reset_3,api_reset_4,api_reset_5}));
                        break;
                }
                secondIntent.setStringArrayListParam("titles", titles);
                secondIntent.setOperation(operation);
                // 通过AbilitySlice的startAbility接口实现启动另一个页面
                startAbility(secondIntent);
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
