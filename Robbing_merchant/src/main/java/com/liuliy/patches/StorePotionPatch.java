package com.liuliy.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.liuliy.monsters.MerchantInRob;
import com.liuliy.rooms.CustomShopRoom;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;

import java.util.ArrayList;

public class StorePotionPatch {

    @SpirePatch(clz = StorePotion.class,method = "purchasePotion")
    public static class hasPurchaseRelic
    {
        //购买遗物后从保存的列表中删除遗物
        @SpireInsertPatch(rlocs = 7)
        public static void Insert(StorePotion _instance)
        {
            if (CustomShopRoom.lastPotions.contains(_instance) && !MerchantInRob.DEAD)
            {
                CustomShopRoom.lastPotions.remove(_instance);
            }
            CustomShopRoom.hasPurchasePotions.add(_instance);

        }
    }
}
