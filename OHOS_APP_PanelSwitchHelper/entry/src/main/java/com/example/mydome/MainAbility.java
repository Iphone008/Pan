package com.example.mydome;

import com.example.mydome.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilityForm;
import ohos.aafwk.content.Intent;
import ohos.agp.components.ComponentContainer;
import ohos.agp.window.service.WindowManager;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        getWindow().setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_PAN);
        super.setMainRoute(MainAbilitySlice.class.getName());
    }

    @Override
    protected AbilityForm onCreateForm() {
        return super.onCreateForm();
    }

    @Override
    public void setUIContent(ComponentContainer componentContainer) {
        super.setUIContent(componentContainer);
    }
}
