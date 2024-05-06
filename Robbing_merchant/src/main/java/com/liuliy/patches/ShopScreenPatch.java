package com.liuliy.patches;

import basemod.devcommands.potions.Potions;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.liuliy.monsters.MerchantInRob;
import com.liuliy.rooms.CustomShopRoom;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.util.ArrayList;


public class ShopScreenPatch {
    public static final boolean REPLACEMENT_RUG_IMAGE = true;

    @SpirePatch(clz = ShopScreen.class,method = "initCards")
    public static class changeCardPrice
    {
        //将所有卡牌价格设置为0
        @SpireInsertPatch(rlocs = {12,32}, localvars = {"c"})
        public static void Insert(AbstractCard c)
        {
            if (MerchantInRob.DEAD)
                c.price = 0;
        }
    }

    @SpirePatch(clz = ShopScreen.class,method = "purchaseCard")
    public static class hasPurchaseCard
    {
        //购买卡牌后从保存的列表中删除卡牌
        @SpireInsertPatch(rlocs = {2}, localvars = {"hoveredCard"})
        public static void Insert(AbstractCard hoveredCard)
        {
           if (CustomShopRoom.lastCards1.contains(hoveredCard))
           {
               CustomShopRoom.lastCards1.remove(hoveredCard);
           }
           else if(CustomShopRoom.lastCards2.contains(hoveredCard))
           {
               CustomShopRoom.lastCards2.remove(hoveredCard);
           }
        }
    }
    @SpirePatch(clz = ShopScreen.class,method = "initRelics")
    public static class saveRelics
    {

        //保存抢劫前的遗物,并将价格设置为0
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __instance,@ByRef ArrayList<StoreRelic>[] ___relics)
        {
            if (!MerchantInRob.DEAD){
                CustomShopRoom.lastRelics = new ArrayList();
                for (StoreRelic r: ___relics[0])
                {
                    CustomShopRoom.lastRelics.add(r);
                }

            }
            else {
                ___relics[0].clear();
                ___relics[0] = new ArrayList();
                for (StoreRelic r: CustomShopRoom.lastRelics)
                {
                    r.price=0;
                    r.isPurchased=false;
                    ___relics[0].add(r);
                }
            }
        }

    }

    @SpirePatch(clz = ShopScreen.class,method = "initPotions")
    public static class savePotions
    {
        //保存抢劫前的药水,并将价格设置为0
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __instance,@ByRef ArrayList<StorePotion>[] ___potions)
        {
            if (!MerchantInRob.DEAD){
                CustomShopRoom.lastPotions = new ArrayList();
                for (StorePotion p: ___potions[0])
                {
                    StorePotion tempP = p;
                    CustomShopRoom.lastPotions.add(tempP);
                }
                if (!CustomShopRoom.hasPurchasePotions.isEmpty())
                {
                    CustomShopRoom.hasPurchasePotions.clear();
                }
            }
            else {
                ___potions[0].clear();
                ___potions[0] = new ArrayList();

                for (StorePotion p: CustomShopRoom.lastPotions)
                {
                    p.price=0;
                    p.isPurchased=false;
                    ___potions[0].add(p);
                }
            }
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "updateHand")
    public static class deleteHandImage {

        @SpirePrefixPatch
        public static void Prefix(ShopScreen __instance,@ByRef float[] ___handX) {
            if (MerchantInRob.DEAD) {
                ___handX[0] = 9999;
            }
        }

    }
    @SpirePatch(clz = ShopScreen.class, method = "createSpeech")
    public static class shutUp {

        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(ShopScreen __instance) {
            if (MerchantInRob.DEAD) {
               return SpireReturn.Return();
            }
            else {
                return SpireReturn.Continue();
            }
        }

    }

    @SpirePatch(clz = ShopScreen.class, method = "welcomeSfx")
    public static class shutUp2 {

        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(ShopScreen __instance) {
            if (MerchantInRob.DEAD) {
                return SpireReturn.Return();
            }
            else {
                return SpireReturn.Continue();
            }
        }

    }
    @SpirePatch(clz = ShopScreen.class, method = "playMiscSfx")
    public static class shutUp3 {

        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(ShopScreen __instance) {
            if (MerchantInRob.DEAD) {
                return SpireReturn.Return();
            }
            else {
                return SpireReturn.Continue();
            }
        }

    }

    @SpirePatch(clz = ShopScreen.class, method = "playBuySfx")
    public static class shutUp4 {

        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(ShopScreen __instance) {
            if (MerchantInRob.DEAD) {
                return SpireReturn.Return();
            }
            else {
                return SpireReturn.Continue();
            }
        }

    }
}
