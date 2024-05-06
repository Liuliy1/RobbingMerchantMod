package com.liuliy.npcs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.liuliy.monsters.MerchantInRob;
import com.liuliy.rooms.CustomShopRoom;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MerchantAfterRob  implements Disposable {
    private static final CharacterStrings characterStrings;
    public static final String[] NAMES;
    public static final String[] TEXT;
    public static final String[] ENDING_TEXT;
    public AnimatedNpc anim;
    public static final float DRAW_X;
    public static final float DRAW_Y;
    public Hitbox hb;
    private  ArrayList<AbstractCard> cards1;
    private  ArrayList<AbstractCard> cards2;
    private ArrayList<String> idleMessages;
    private float speechTimer;
    private boolean saidWelcome;
    private static final float MIN_IDLE_MSG_TIME = 40.0F;
    private static final float MAX_IDLE_MSG_TIME = 60.0F;
    private static final float SPEECH_DURATION = 3.0F;
    private int shopScreen;
    protected float modX;
    protected float modY;

    public MerchantAfterRob() {
        this(0.0F, 0.0F, 1);
    }

    public MerchantAfterRob(float x, float y, int newShopScreen) {
        this.hb = new Hitbox(360.0F * Settings.scale, 300.0F * Settings.scale);
        this.idleMessages = new ArrayList();
        this.speechTimer = 1.5F;
        this.saidWelcome = true;
        this.shopScreen = 1;
        //商人是否死亡
        if (!MerchantInRob.DEAD) {
            this.anim = new AnimatedNpc(DRAW_X + 256.0F * Settings.scale, AbstractDungeon.floorY + 30.0F * Settings.scale, "images/npcs/merchant/skeleton.atlas", "images/npcs/merchant/skeleton.json", "idle");
        }
        this.cards1 = new ArrayList();
        this.cards2 = new ArrayList();


        AbstractCard c;
        for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy();
            c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy()) {
        }

        this.cards1.add(c);

        for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy(); Objects.equals(c.cardID, ((AbstractCard)this.cards1.get(this.cards1.size() - 1)).cardID) || c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy()) {
        }

        this.cards1.add(c);

        for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy(); c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy()) {
        }

        this.cards1.add(c);

        for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy(); Objects.equals(c.cardID, ((AbstractCard)this.cards1.get(this.cards1.size() - 1)).cardID) || c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy()) {
        }

        this.cards1.add(c);

        for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.POWER, true).makeCopy(); c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.POWER, true).makeCopy()) {
        }

        this.cards1.add(c);
        this.cards2.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.UNCOMMON).makeCopy());
        this.cards2.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE).makeCopy());
        if (AbstractDungeon.id.equals("TheEnding")) {
            Collections.addAll(this.idleMessages, ENDING_TEXT);
        } else {
            Collections.addAll(this.idleMessages, TEXT);
        }

        this.speechTimer = 1.5F;
        this.modX = x;
        this.modY = y;
        this.hb.move(DRAW_X + (250.0F + x) * Settings.scale, DRAW_Y + (130.0F + y) * Settings.scale);
        this.shopScreen = newShopScreen;
        if (!MerchantInRob.DEAD){

            CustomShopRoom.lastCards1= new ArrayList();
            CustomShopRoom.lastCards2= new ArrayList();
            AbstractDungeon.shopScreen.init(this.cards1, this.cards2);
            for (AbstractCard c1 : this.cards1)
            {

                CustomShopRoom.lastCards1.add(c1);
            }
            for (AbstractCard c2 : this.cards2)
            {

                CustomShopRoom.lastCards2.add(c2);
            }
        }
        else{
            ArrayList<AbstractCard> tempcards1 = new ArrayList();
            ArrayList<AbstractCard> tempcards2 = new ArrayList();
            for (AbstractCard card:CustomShopRoom.lastCards1)
            {
                tempcards1.add(card.makeCopy());
            }
            for (AbstractCard card:CustomShopRoom.lastCards2)
            {
                tempcards2.add(card.makeCopy());
            }
            //显示之前的卡
            AbstractDungeon.shopScreen.init(tempcards1, tempcards2);
            System.out.println("lastPotions: "+CustomShopRoom.lastPotions.size());
            System.out.println("lastRelics: "+CustomShopRoom.lastRelics.size());
            //将删卡费用降为0
            ShopScreen.purgeCost = 0;
            ShopScreen.actualPurgeCost = 0;
        }


    }

    public void update() {
        this.hb.update();
        if ((this.hb.hovered && InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed()) && !AbstractDungeon.isScreenUp && !AbstractDungeon.isFadingOut && !AbstractDungeon.player.viewingRelics) {
            AbstractDungeon.overlayMenu.proceedButton.setLabel(NAMES[0]);
            this.saidWelcome = true;
            AbstractDungeon.shopScreen.open();
            this.hb.hovered = false;
        }



    }


    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.MERCHANT_RUG_IMG, DRAW_X + this.modX, DRAW_Y + this.modY, 512.0F * Settings.scale, 512.0F * Settings.scale);
        if (this.hb.hovered) {
            sb.setBlendFunction(770, 1);
            sb.setColor(Settings.HALF_TRANSPARENT_WHITE_COLOR);
            sb.draw(ImageMaster.MERCHANT_RUG_IMG, DRAW_X + this.modX, DRAW_Y + this.modY, 512.0F * Settings.scale, 512.0F * Settings.scale);
            sb.setBlendFunction(770, 771);
        }

        if (this.anim != null) {
            this.anim.render(sb);
        }

        if (Settings.isControllerMode) {
            sb.setColor(Color.WHITE);
            sb.draw(CInputActionSet.select.getKeyImg(), DRAW_X - 32.0F + 150.0F * Settings.scale, DRAW_Y - 32.0F + 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

        this.hb.render(sb);
    }

    public void dispose() {
        if (this.anim != null) {
            this.anim.dispose();
        }
    }

     public void spawnHitbox() {
        this.hb = new Hitbox(500.0F * Settings.scale, 700.0F * Settings.scale);
        this.hb.move(DRAW_X, DRAW_Y);
       }

    static {
        characterStrings = CardCrawlGame.languagePack.getCharacterString("Merchant");
        NAMES = characterStrings.NAMES;
        TEXT = characterStrings.TEXT;
        ENDING_TEXT = characterStrings.OPTIONS;
        DRAW_X = (float)Settings.WIDTH * 0.5F + 34.0F * Settings.xScale;
        DRAW_Y = AbstractDungeon.floorY - 109.0F * Settings.scale;
    }
}
