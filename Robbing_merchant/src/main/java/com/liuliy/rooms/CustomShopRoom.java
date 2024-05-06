package com.liuliy.rooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.liuliy.UI.RobButton;
import com.liuliy.monsters.MerchantInRob;
import com.liuliy.npcs.MerchantAfterRob;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;

import java.util.ArrayList;

public class CustomShopRoom extends ShopRoom {
    private MerchantAfterRob MerchantAfterRob;
    private boolean MerchantAfterRobShown;
    public boolean startedCombat = false;
    public static boolean toRob=false;
    public static boolean hasRobbed = false;
    public static ArrayList<StoreRelic> hasPurchaseRelics;
    public static ArrayList<StorePotion> hasPurchasePotions;
    public  static ArrayList<AbstractCard> lastCards1;
    public  static ArrayList<AbstractCard> lastCards2;
    public  static ArrayList<StoreRelic> lastRelics;
    public  static ArrayList<StorePotion> lastPotions;
    public RobButton robButton;


    public CustomShopRoom() {
        this.merchant = null;
        this.robButton = new RobButton();

        CustomShopRoom.hasPurchasePotions = new ArrayList();
        CustomShopRoom.hasPurchaseRelics = new ArrayList();
    }

    @Override
    public void setMerchant(Merchant merc) {

        super.setMerchant(merc);

    }

    @Override
    public void onPlayerEntry() {
        if (!CustomShopRoom.hasPurchasePotions.isEmpty())
        {
            for (StorePotion potion:CustomShopRoom.hasPurchasePotions){
                CustomShopRoom.lastPotions.remove(potion);
                System.out.println("lastPotions.remove");
            }
        }
        if (!AbstractDungeon.id.equals("TheEnding")) {
            playBGM("SHOP");
        }

    //选择抢劫时跳转
        if (!MerchantInRob.DEAD && toRob)
        {
            CardCrawlGame.music.fadeOutTempBGM();
            startCombat();
            this.startedCombat = true;



        }
        else if(MerchantInRob.DEAD && toRob && !hasRobbed){
            showMerchant();
            System.out.println("hasrobbed");
            hasRobbed=true;
        }
        else if(!MerchantInRob.DEAD && !toRob && !hasRobbed ){
            showMerchant();

        }
        else if (MerchantInRob.DEAD)
        {
            System.out.println("robNothing");
            ShopScreen.purgeCost = 9999;
            ShopScreen.actualPurgeCost = 9999;
        }
    }

    @Override
    public AbstractCard.CardRarity getCardRarity(int roll) {
        return super.getCardRarity(roll);
    }


@Override
public void update() {
    super.update();
    if (this.MerchantAfterRob != null && this.MerchantAfterRobShown) {
        this.MerchantAfterRob.update();
    }

    this.robButton.update();
}

    @Override
    public void render(SpriteBatch sb) {
        if (this.MerchantAfterRob != null && this.MerchantAfterRobShown) {
            this.MerchantAfterRob.render(sb);
        }

        this.robButton.render(sb);
        super.render(sb);
        this.renderTips(sb);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.MerchantAfterRob != null)
        {
            this.MerchantAfterRob.dispose();
            this.MerchantAfterRob = null;
        }
    }

    public static void startCombat()
    {

        AbstractDungeon.closeCurrentScreen();
        (AbstractDungeon.getCurrRoom()).phase = RoomPhase.COMBAT;
        AbstractDungeon.lastCombatMetricKey = MerchantInRob.ID;
        (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup((AbstractMonster)new MerchantInRob());
        (AbstractDungeon.getCurrRoom()).monsters.init();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
        m.usePreBattleAction();
        m.useUniversalPreBattleAction();
       }
        AbstractRoom.waitTimer = 0.1F;
         AbstractDungeon.player.preBattlePrep();
    }

    public void showMerchant() {
        if (this.MerchantAfterRob == null) {
            this.MerchantAfterRob = new MerchantAfterRob();

        }

        this.MerchantAfterRobShown = true;

    }
}
