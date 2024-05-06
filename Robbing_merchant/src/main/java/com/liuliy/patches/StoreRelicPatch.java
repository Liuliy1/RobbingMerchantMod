package com.liuliy.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.liuliy.monsters.MerchantInRob;
import com.liuliy.rooms.CustomShopRoom;
import com.megacrit.cardcrawl.shop.StoreRelic;

import java.util.ArrayList;

public class StoreRelicPatch {
    @SpirePatch(clz = StoreRelic.class,method = "purchaseRelic")
    public static class hasPurchaseRelic
    {
        //购买遗物后从保存的列表中删除遗物
        @SpireInsertPatch(rlocs = 2)
        public static void Insert(StoreRelic _instance)
        {
            if (CustomShopRoom.lastRelics.contains(_instance) && !MerchantInRob.DEAD)
            {
                CustomShopRoom.lastRelics.remove(_instance);
            }
            CustomShopRoom.hasPurchaseRelics.add(_instance);

        }
    }
}
