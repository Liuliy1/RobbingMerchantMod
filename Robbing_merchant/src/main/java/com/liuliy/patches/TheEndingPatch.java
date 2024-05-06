package com.liuliy.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.liuliy.rooms.CustomShopRoom;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import java.util.ArrayList;
import java.util.Iterator;

public class TheEndingPatch{
    @SpirePatch(
            clz = TheEnding.class,
            method = "generateSpecialMap")
    public static class ChangeTheEndingShop {
        @SpirePostfixPatch
        public static void Postfix() {
                Iterator var0 = AbstractDungeon.map.iterator();
                while(var0.hasNext()) {
                    ArrayList<MapRoomNode> nodes = (ArrayList)var0.next();
                    Iterator var2 = nodes.iterator();

                    while(var2.hasNext()) {
                        MapRoomNode node = (MapRoomNode)var2.next();
                        if (node.getRoom() instanceof ShopRoom) {
                            node.setRoom(new CustomShopRoom());
                        }
                    }
                }


        }
    }
}
