package com.example.mydome;

import com.example.mydome.slice.ContentAbilitySlice;
import com.example.mydome.slice.ResetAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ResetAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ResetAbilitySlice.class.getName());
    }
}
