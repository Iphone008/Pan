package com.example.library.interfaces;

import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
import com.example.library.interfaces.IContentScroll.IGetScrollDistance;
import com.example.library.interfaces.IContentScroll.IGetScrollViewId;

public class ContentScrollMeasurerBuilder implements ContentScrollMeasurer {


    private IGetScrollDistance mIGetScrollDistance;
    private IGetScrollViewId mIGetScrollViewId;


    @Override
    public int getScrollDistance(int defaultDistance) {

        return mIGetScrollDistance != null ? mIGetScrollDistance.IgetScrollDistance(defaultDistance) : 0;
    }

    @Override
    public int getScrollViewId() {
        return mIGetScrollViewId == null ? mIGetScrollViewId.IgetScrollViewId() : -1;
    }

    public void getScrollDistance(IGetScrollDistance iGetScrollDistance) {
        this.mIGetScrollDistance = iGetScrollDistance;
    }

    public void getScrollViewId(IGetScrollViewId iGetScrollViewId) {
        this.mIGetScrollViewId = iGetScrollViewId;
    }


}
