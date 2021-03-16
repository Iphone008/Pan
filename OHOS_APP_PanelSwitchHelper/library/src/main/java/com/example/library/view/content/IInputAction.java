package com.example.library.view.content;

import com.example.library.interfaces.ContentScrollMeasurerBuilder;
import ohos.aafwk.ability.OnClickListener;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;

import java.util.List;

public interface IInputAction {
    void addSecondaryInputView(TextField TexField);

    void removeSecondaryInputView(TextField textField);

    void setEditTextClickListener(Component.ClickedListener l);

    void setEditTextFocusChangeListener(Component.FocusChangedListener l);

    void requestKeyboard();

    void hideKeyboard(boolean clearFocus);

    boolean showKeyboard();

    TextField getFullScreenPixelInputView();

    void updateFullScreenParams(boolean isFullScreen, int panelId, int panelHeight);

    void recycler();

    void  initIInputAction();

    void assertView() throws Exception;

    Component findTriggerView(int id);

}
