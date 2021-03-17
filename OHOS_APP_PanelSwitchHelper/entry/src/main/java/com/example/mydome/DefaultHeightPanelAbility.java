package com.example.mydome;

import com.example.mydome.slice.DefaultPanelAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class DefaultHeightPanelAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DefaultPanelAbilitySlice.class.getName());
    }
}
