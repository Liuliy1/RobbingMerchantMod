package com.liuliy.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.liuliy.monsters.MerchantInRob;
import com.liuliy.rooms.CustomShopRoom;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.RenderScene;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import java.util.ArrayList;
import javassist.CtBehavior;

@SpirePatch(
        clz = ProceedButton.class,
        method = "update"
)
public class ProceedButtonPatch {
    public ProceedButtonPatch() {
    }

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn Insert(ProceedButton __instance) {
        AbstractRoom r = AbstractDungeon.getCurrRoom();

        if (!(r instanceof CustomShopRoom) || (!((CustomShopRoom)r).startedCombat || !MerchantInRob.DEAD) || !CustomShopRoom.toRob) {
            return SpireReturn.Continue();
        } else {
            AbstractRoom tRoom = new CustomShopRoom();
            tRoom.rewards.clear();
            AbstractDungeon.combatRewardScreen.clear();
            AbstractDungeon.currMapNode.setRoom(tRoom);
            AbstractDungeon.getCurrRoom().rewards.clear();
            AbstractDungeon.scene.nextRoom(tRoom);
            CardCrawlGame.fadeIn(1.5F);
            AbstractDungeon.rs = RenderScene.NORMAL;
            tRoom.onPlayerEntry();
            AbstractDungeon.closeCurrentScreen();
            return SpireReturn.Return((Object)null);
        }

    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "closeCurrentScreen");
            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList(), finalMatcher)[2]};
        }
    }
}
