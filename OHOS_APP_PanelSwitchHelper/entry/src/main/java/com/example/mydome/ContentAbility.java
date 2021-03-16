package com.example.mydome;

import com.example.mydome.slice.ContentAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ContentAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ContentAbilitySlice.class.getName());
    }
}
