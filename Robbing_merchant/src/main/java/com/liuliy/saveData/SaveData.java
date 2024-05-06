//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.liuliy.saveData;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liuliy.monsters.MerchantInRob;
import com.liuliy.rooms.CustomShopRoom;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import java.util.ArrayList;
import java.util.HashMap;

import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SaveData {
    private static Logger saveLogger = LogManager.getLogger("robbingMerchantSaveData");

    private static boolean merchantDead;
    private static boolean merchantToRob;
    private static boolean merchantHasRob;

    public SaveData() {
    }


    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "loadSave"
    )
    public static class loadSave {
        public loadSave() {
        }

        @SpirePostfixPatch
        public static void loadSave(AbstractDungeon __instance, SaveFile file) {
            System.out.println("merchantDead"+SaveData.merchantDead);
            MerchantInRob.DEAD = SaveData.merchantDead;
            CustomShopRoom.toRob = SaveData.merchantToRob;
            CustomShopRoom.hasRobbed = SaveData.merchantHasRob;

        }
    }

    @SpirePatch(
            clz = SaveAndContinue.class,
            method = "loadSaveFile",
            paramtypez = {String.class}
    )
    public static class LoadDataFromFile {
        public LoadDataFromFile() {
        }

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"gson", "savestr"}
        )
        public static void loadCustomSaveData(String path, Gson gson, String savestr) {
            try {
                robbingMerchantSaveData data = gson.fromJson(savestr, robbingMerchantSaveData.class);

                SaveData.merchantDead = data.MERCHANT_DEAD;
                SaveData.merchantToRob = data.MERCHANT_TOROB;
                SaveData.merchantHasRob = data.MERCHANT_HASROB;
                SaveData.saveLogger.info("Loaded robbingMerchant save data successfully.");
            } catch (Exception var4) {
                SaveData.saveLogger.error("Failed to load robbingMerchant save data.");
                var4.printStackTrace();
            }

        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Gson.class, "fromJson");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = SaveAndContinue.class,
            method = "save",
            paramtypez = {SaveFile.class}
    )
    public static class SaveDataToFile {
        public SaveDataToFile() {
        }

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"params"}
        )
        public static void addCustomSaveData(SaveFile save, HashMap<Object, Object> params) {

            params.put("MERCHANT_DEAD", SaveData.merchantDead);
            params.put("MERCHANT_TOROB", SaveData.merchantToRob);
            params.put("MERCHANT_HASROB", SaveData.merchantHasRob);

        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GsonBuilder.class, "create");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = SaveFile.class,
            method = "<ctor>",
            paramtypez = {SaveFile.SaveType.class}
    )
    public static class SaveTheSaveData {
        public SaveTheSaveData() {
        }

        @SpirePostfixPatch
        public static void saveAllTheSaveData(SaveFile __instance, SaveFile.SaveType type) {

            SaveData.merchantDead = MerchantInRob.DEAD;
            SaveData.merchantToRob = CustomShopRoom.toRob;
            SaveData.merchantHasRob = CustomShopRoom.hasRobbed;
        }
    }
}
