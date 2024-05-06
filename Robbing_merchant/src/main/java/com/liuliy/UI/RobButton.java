package com.liuliy.UI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.liuliy.monsters.MerchantInRob;
import com.liuliy.rooms.CustomShopRoom;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.core.jackson.ContextDataAsEntryListSerializer;


// 自定义按钮类
public  class RobButton {
    private static  float HITBOX_W = 512;
    private static  float HITBOX_H = 512;
    private Texture robButtonImg;
    private Texture robButtonShadow;
    private String label;
    private float current_x;
    private float current_y;
    private float target_x;
    private float wavyTimer;
    protected Hitbox hb;
    protected Color activeColor;
    protected Color inactiveColor;
    public boolean pressed;
    private BitmapFont font;
    public int height;
    public int width;
    private boolean isHidden;
    public static final String ID = "RobbingMerchant:RobButton";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public RobButton() {
        this.robButtonImg = ImageMaster.loadImage("RobbingMerchant/img/shop/robButton.png");
        this.robButtonShadow=ImageMaster.loadImage("RobbingMerchant/img/shop/robButtonShadow.png");
        this.current_x = 1670.0F * Settings.xScale;;
        this.current_y = 520.0F * Settings.scale;
        String[] TEXT = uiStrings.TEXT;
        this.label = TEXT[0];

        HITBOX_W = 280.0F * Settings.scale;
        HITBOX_H = 156.0F * Settings.scale;
        this.font = FontHelper.buttonLabelFont;
        this.hb = new Hitbox(99999, 99999, HITBOX_W, HITBOX_H);
        this.activeColor = Color.WHITE;
        this.inactiveColor = new Color(0.6F, 0.6F, 0.6F, 1.0F);

    }
    public void update() {
        this.hb.update();
        if (this.hb.hovered && InputHelper.justClickedLeft) {
            this.pressed = true;
            System.out.println("click");
            CustomShopRoom.toRob = true;
            AbstractRoom tRoom = new CustomShopRoom();
            tRoom.rewards.clear();
            AbstractDungeon.combatRewardScreen.clear();
            AbstractDungeon.currMapNode.setRoom(tRoom);
            AbstractDungeon.getCurrRoom().rewards.clear();
            AbstractDungeon.scene.nextRoom(tRoom);
            CardCrawlGame.fadeIn(1.5F);
            AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
            tRoom.onPlayerEntry();
            AbstractDungeon.closeCurrentScreen();
            InputHelper.justClickedLeft = false;

        }

    }

    public void render(SpriteBatch sb) {
        if (this.hb.hovered) {
            sb.setColor(this.activeColor);
        } else {
            sb.setColor(this.inactiveColor);
        }
        //如果商人没死，没开始抢
         if (!MerchantInRob.DEAD && !CustomShopRoom.toRob) {
             //渲染抢劫按钮
             renderButton(sb);
//             renderShadow(sb);
             //渲染按钮碰撞箱
             this.hb.render(sb);
             this.hb.move(this.current_x, this.current_y);
             //渲染“抢劫”字体
             if (this.hb.hovered && !this.hb.clickStarted) {
                 FontHelper.renderFontCentered(sb, this.font, this.label, this.current_x, this.current_y, Settings.CREAM_COLOR);
             } else if (this.hb.clickStarted) {
                 FontHelper.renderFontCentered(sb, this.font, this.label, this.current_x, this.current_y, Color.LIGHT_GRAY);
             } else {
                 FontHelper.renderFontCentered(sb, this.font, this.label, this.current_x, this.current_y, Settings.LIGHT_YELLOW_COLOR);
             }
         }

    }



    private void renderButton(SpriteBatch sb) {
        sb.draw(robButtonImg, this.current_x - 256, this.current_y - 256, 256.0F, 256.0F, 512.0F, 512.0F,
                Settings.scale * 1.1F + MathUtils.cos(this.wavyTimer) / 50.0F,
                Settings.scale * 1.1F + MathUtils.cos(this.wavyTimer) / 50.0F,
                0.0F, 0, 0, 512, 512, false, false);

    }
}
