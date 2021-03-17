package com.example.mydome;

import com.example.mydome.slice.CusPanelAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class CusPanelAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(CusPanelAbilitySlice.class.getName());
    }
}
