package com.example.mydome;

import com.example.mydome.slice.TransitAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class TransitAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(TransitAbilitySlice.class.getName());
    }
}
