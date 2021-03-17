package com.example.mydome;

import com.example.mydome.slice.ChatCusContentScrollAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ChatCusContentScrollAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ChatCusContentScrollAbilitySlice.class.getName());
    }
}
