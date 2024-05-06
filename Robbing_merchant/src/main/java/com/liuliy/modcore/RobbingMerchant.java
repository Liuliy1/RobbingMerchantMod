package com.liuliy.modcore;
import basemod.BaseMod;
import basemod.interfaces.*;

import basemod.patches.com.megacrit.cardcrawl.screens.DeathScreen.PostDeathHook;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import com.liuliy.monsters.MerchantInRob;
import com.liuliy.rooms.CustomShopRoom;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.UIStrings;

@SpireInitializer
public class RobbingMerchant implements EditStringsSubscriber, PostDeathSubscriber {

    public static String MOD_ID = "RobbingMerchant";
    public static String assetPath(String path) {
        return MOD_ID + "/" + path;
    }
    public RobbingMerchant() {

        BaseMod.subscribe(this);

   }

    // 注解需要调用的方法，必须写
    public static void initialize() {
        new RobbingMerchant();
    }


    @Override
    public void receiveEditStrings() {
        String lang;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        }

        String filePath = "RobbingMerchant/localization/" + lang + "/RobbingMerchant-MonsterString.json";
        String filePath2 = "RobbingMerchant/localization/" + lang + "/RobbingMerchant-UIString.json";
        // 尝试加载文件
        BaseMod.loadCustomStringsFile(MonsterStrings.class, filePath);
        BaseMod.loadCustomStringsFile(UIStrings.class, filePath2);
    }

    @Override
    public void receivePostDeath() {
        CustomShopRoom.hasRobbed=false;
        CustomShopRoom.toRob=false;
        MerchantInRob.DEAD=false;
    }
}