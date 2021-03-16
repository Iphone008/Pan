package com.example.mydome;

import com.example.mydome.slice.ChatAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ChatAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ChatAbilitySlice.class.getName());
    }
}
