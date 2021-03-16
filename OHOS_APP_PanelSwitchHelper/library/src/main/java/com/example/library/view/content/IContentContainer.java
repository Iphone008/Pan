package com.example.library.view.content;

import com.example.library.interfaces.ContentScrollMeasurerBuilder;
import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
import ohos.agp.components.Component;

import java.util.List;

public interface IContentContainer {
    //容器行为
    Component findTriggerView(int id);

    void layoutContainer(int l, int t, int r, int b,
                         List<ContentScrollMeasurer> contentScrollMeasurer, int defaultScrollHeight, boolean canScrollOutsize,
                         boolean reset);

    void changeContainerHeight(int targetHeight);

    //输入相关
    IInputAction getInputActionImpl();

    //隐藏相关
    IResetAction getResetActionImpl();



}

