package com.example.mydome.slice;

import com.example.mydome.*;
import com.example.mydome.anno.ApiContentType;
import com.example.mydome.anno.ApiResetType;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;

import java.util.ArrayList;
import java.util.List;

public class TransitAbilitySlice extends AbilitySlice {

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
    ListContainer list;
    List<String> childStrings = new ArrayList<>();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_transit);
        childStrings.addAll(intent.getStringArrayListParam("titles"));
        list = (ListContainer) findComponentById(ResourceTable.Id_transit_list);
        list.setItemProvider(new BaseItemProvider() {
            @Override
            public int getCount() {
                return childStrings.size();
            }

            @Override
            public Object getItem(int i) {
                return childStrings.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
                component = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_item_home, null, false);
                Text childitem = (Text) component.findComponentById(ResourceTable.Id_home_text);
                childitem.setText(childStrings.get(i));
                return component;
            }
        });
        list.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                switch (childStrings.get(i)) {
                    case activity_1: {
//                        ChatActivity.start(MainActivity.this, ChatPageType.DEFAULT);
                        break;
                    }
                    case activity_2: {
//                        ChatActivity.start(MainActivity.this, ChatPageType.TITLE_BAR);
                        break;
                    }
                    case activity_3: {
//                        ChatActivity.start(MainActivity.this, ChatPageType.CUS_TITLE_BAR);
                        break;
                    }
                    case activity_4: {
//                        ChatActivity.start(MainActivity.this, ChatPageType.COLOR_STATUS_BAR);
                        break;
                    }
                    case activity_5: {
//                        ChatActivity.start(MainActivity.this, ChatPageType.TRANSPARENT_STATUS_BAR);
                        break;
                    }
                    case activity_6: {
//                        ChatActivity.start(MainActivity.this, ChatPageType.TRANSPARENT_STATUS_BAR_DRAW_UNDER);
                        break;
                    }

                    case fragment_1: {
//                        ChatFragmentActivity.startFragment(MainActivity.this, ChatPageType.DEFAULT);
                        break;
                    }
                    case fragment_2: {
//                        ChatFragmentActivity.startFragment(MainActivity.this, ChatPageType.TITLE_BAR);
                        break;
                    }
                    case fragment_3: {
//                        ChatFragmentActivity.startFragment(MainActivity.this, ChatPageType.COLOR_STATUS_BAR);
                        break;
                    }
                    case fragment_4: {
//                        ChatFragmentActivity.startFragment(MainActivity.this, ChatPageType.TRANSPARENT_STATUS_BAR);
                        break;
                    }

                    case window_1: {
//                        DialogFragment dialogFragment = new ChatDialogFragment();
//                        dialogFragment.showNow(getSupportFragmentManager(), "dialogFragment");
                        break;
                    }
                    case window_2: {
//                        PopupWindow popupWindow = new ChatPopupWindow(MainActivity.this);
//                        popupWindow.showAtLocation(mBinding.getRoot(), Gravity.NO_GRAVITY, 0, 0);
                        break;
                    }
                    case window_3: {
//                        Dialog dialog = new ChatDialog(MainActivity.this);
//                        dialog.show();
                        break;
                    }

                    case scene_1: {
//                        startActivity(new Intent(MainActivity.this, BiliBiliSampleActivity.class));
                        break;
                    }
                    case scene_2: {
//                        startActivity(new Intent(MainActivity.this, FeedDialogActivity.class));
                        break;
                    }
//                    case scene_2_2:{
//                        startActivity(new Intent(MainActivity.this, FeedActivity.class));
//                        break;
//                    }
                    case scene_3: {
//                        startActivity(new Intent(MainActivity.this, PcHuyaLiveActivity.class));
                        break;
                    }
                    case scene_4: {
//                        startActivity(new Intent(MainActivity.this, PhoneDouyinLiveActivity.class));
                        break;
                    }
                    case scene_5: {
//                        startActivity(new Intent(MainActivity.this, ChatSuperActivity.class));
                        break;
                    }

                    case api_cus_panel: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiContentType.Linear);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(CusPanelAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }

                    case api_cus_panel_height: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiContentType.Linear);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(DefaultHeightPanelAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }

                    case api_define_content_container_scroll: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiContentType.Linear);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(ChatCusContentScrollAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }
                    case api_content_container_1: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiContentType.Linear);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(ContentAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }
                    case api_content_container_2: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiContentType.Relative);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(ContentAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }
                    case api_content_container_3: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiContentType.Frame);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(ContentAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }
                    case api_content_container_4: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiContentType.CUS);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(ContentAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }

                    case api_reset_1: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiResetType.ENABLE);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(ResetAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }
                    case api_reset_2: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiResetType.ENABLE_EmptyView);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(ResetAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }
                    case api_reset_3: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiResetType.ENABLE_RecyclerView);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(ResetAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }
                    case api_reset_4: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiResetType.ENABLE_HookActionUpRecyclerview);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(ResetAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }
                    case api_reset_5: {
                        Intent intent = new Intent();
                        intent.setParam(Constants.KEY_CONTENT_TYPE, ApiResetType.DISABLE);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName(getBundleName())
                                .withAbilityName(ResetAbility.class.getName())
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                        break;
                    }
                }
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
