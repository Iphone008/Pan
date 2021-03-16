package com.example.library.interfaces;

import com.example.library.interfaces.IContentScroll.IGetPanelId;
import com.example.library.interfaces.IContentScroll.IGetTargetPanelDefaultHeight;
import com.example.library.interfaces.IContentScroll.ISynchronizeKeyboardHeight;
import com.example.library.interfaces.IContentScroll.PanelHeightMeasurer;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class PanelHeightMeasurerBuilder implements PanelHeightMeasurer {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001400, "MY_TAG_ContentContainerImpl");

    private IGetTargetPanelDefaultHeight mIGetTargetPanelDefaultHeight = null;
    private IGetPanelId mIGetPanelId = null;
    private ISynchronizeKeyboardHeight mISynchronizeKeyboardHeight = null;
    @Override
    public boolean synchronizeKeyboardHeight() {
        return mISynchronizeKeyboardHeight == null ? mISynchronizeKeyboardHeight.IsynchronizeKeyboardHeight() : true;
    }

    @Override
    public int getTargetPanelDefaultHeight() {
        return mIGetTargetPanelDefaultHeight == null ? mIGetTargetPanelDefaultHeight.IgetTargetPanelDefaultHeight() : 0;
    }

    @Override
    public int getPanelTriggerId() {
        return mIGetPanelId == null ? mIGetPanelId.IgetPanelId() : -1;
    }

    public void getTargetPanelDefaultHeight(IGetTargetPanelDefaultHeight iGetTargetPanelDefaultHeight) {
        this.mIGetTargetPanelDefaultHeight = iGetTargetPanelDefaultHeight;
    }

    public void getPanelTriggerId(IGetPanelId iGetPanelId) {
        this.mIGetPanelId = iGetPanelId;
    }

    public void synchronizeKeyboardHeight(ISynchronizeKeyboardHeight iSynchronizeKeyboardHeight) {
        this.mISynchronizeKeyboardHeight = iSynchronizeKeyboardHeight;
    }


}
