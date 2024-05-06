package com.liuliy.monsters;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.liuliy.npcs.MerchantAfterRob;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.purple.Devotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.DevotionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.DevotionEffect;
import jdk.nashorn.internal.runtime.Debug;

import java.util.Iterator;
import java.util.Random;

public class MerchantInRob extends AbstractMonster {
    public static final String ID = "RobbingMerchant:MerchantInRob";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static boolean DEAD = false;
    public static final String NAME = monsterStrings.NAME;

    public static final String ENCOUNTER_NAME = "RobbingMerchant:MerchantInRob";
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean firstTurn = true;
    private MerchantAfterRob MerchantAfterRob;

    public static void initialize() {
        new MerchantInRob();
    }

    private static final int STR_AMOUNT = 2;
    private static final int A_2_MAGIC_DMG = 9;
    private static final int A_2_STR_AMOUNT = 3;
    private int strikeDmg = 5;
    private int healAmt = 50;

    private int invincibleAmt = 50;
    private static int maxHealth = 100 ;
    private static final byte ATTACK = 1;
    private static final byte HEAL = 2;
    private static final byte BUFF = 3;


    private boolean hasHeal = false;
private boolean hasPower = false;

    public MerchantInRob() {

        this(0f, 0f);


    }

    public MerchantInRob(float x, float y) {

        super(NAME, ID, maxHealth, 0.0F, 0.0F, 170.0F, 275.0F, null, x, y);
//        this.type = EnemyType.BOSS;
        maxHealth = 100 + (AbstractDungeon.actNum-1)*50;
        setHp(maxHealth);
        this.loadAnimation("RobbingMerchant/img/merchant/skeleton.atlas", "RobbingMerchant/img/merchant/skeleton.json", 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        this.damage.add(new DamageInfo((AbstractCreature) this, this.strikeDmg));
        this.damage.add(new DamageInfo((AbstractCreature) this, this.strikeDmg*2));
        DEAD = false;
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(1.2F);

    }

    @Override
    public void usePreBattleAction() {

        AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new ShoutAction((AbstractCreature) this, DIALOG[0]));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            //attack_normal
            case 1:
                playSfx();
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new AnimateSlowAttackAction((AbstractCreature) this));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new DamageAction((AbstractCreature) AbstractDungeon.player,
                        this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

                break;
            //heal
            case 2:
                playSfx();
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new WaitAction(0.25F));
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                    if (!m.isDying && !m.isEscaping) {
                        AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new HealAction((AbstractCreature) m,
                                (AbstractCreature) this, this.healAmt *AbstractDungeon.actNum));
                    }
                }
                break;
            //start buff
            case 3:
//
                playSfx();
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, AbstractDungeon.actNum*10));
                switch (AbstractDungeon.actNum){
                    case 4:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, invincibleAmt), invincibleAmt));
                    case 3:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CuriosityPower(this,2)));
                    case 2:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this,10)));
                    case 1:
                        AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new ApplyPowerAction((AbstractCreature) this,
                                (AbstractCreature) this, (AbstractPower) new DemonFormPower((AbstractCreature) this, 2), 2));
                        AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new ApplyPowerAction((AbstractCreature) this,
                                (AbstractCreature) this, (AbstractPower) new MetallicizePower((AbstractCreature) this, 4), 4));
                }
                break;
            //run away
            case 4:
                CardCrawlGame.sound.play("VO_MERCHANT_2B");
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new EscapeAction(this));
                break;
            //poison
            case 5:
                playSfx();
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new WaitAction(0.25F));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new ApplyPowerAction((AbstractCreature) AbstractDungeon.player,
                        (AbstractCreature) this, (AbstractPower) new FrailPower(
                                (AbstractCreature) AbstractDungeon.player, 2, true), 3));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new ApplyPowerAction((AbstractCreature) AbstractDungeon.player,
                        (AbstractCreature) this, (AbstractPower) new WeakPower((AbstractCreature) AbstractDungeon.player, 3, true), 3));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new ApplyPowerAction((AbstractCreature) AbstractDungeon.player,
                        (AbstractCreature) this, (AbstractPower) new VulnerablePower((AbstractCreature) AbstractDungeon.player, 4, true), 4));
                break;
            //attack plus
            case 6:
                playSfx();
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new AnimateSlowAttackAction((AbstractCreature) this));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new DamageAction((AbstractCreature) AbstractDungeon.player,
                        this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

                break;

            case 7:
                CardCrawlGame.sound.play("VO_MERCHANT_2B");
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, AbstractDungeon.actNum*5));
                break;

        }
        if (hasPower){
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new ApplyPowerAction((AbstractCreature) this,
                    (AbstractCreature) this, (AbstractPower) new StrengthPower((AbstractCreature) this, 2), 2));
        }
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new RollMoveAction(this));

    }

    @Override
    protected void getMove(int num) {
        int curHeal = 0;
        int maxHeal = 0;
        int move = (int) (Math.random() * 3 + 1);

        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (!m.isDying && !m.isEscaping) {
                curHeal = m.currentHealth;
                maxHeal = m.maxHealth;

            }
        }
        //开局激活能力
        if (curHeal == maxHeal && !hasPower ) {
            setMove(MOVES[0],(byte) 3, Intent.DEFEND_BUFF);
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new ShoutAction((AbstractCreature) this, DIALOG[0]));
            hasPower=true;
            return;
        }
        //回血
       else if (curHeal < maxHeal / 2 && !hasHeal) {
            setMove(MOVES[2],(byte) 2, AbstractMonster.Intent.BUFF);
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new ShoutAction((AbstractCreature) this, DIALOG[1]));
            hasHeal=true;
            return;
        }
        //扔药
       else if (move==1 && !lastMove((byte) 5)) {
            setMove(MOVES[1],(byte) 5, AbstractMonster.Intent.DEBUFF);
            return;
        }
       else if (move==2 && !lastMove((byte) 6)) {
            setMove((byte) 6, Intent.ATTACK, ((DamageInfo) this.damage.get(0)).base);
            return;
       }
       else if (move==3 && !lastMove((byte) 7)) {
           setMove((byte)7, Intent.DEFEND);
       } else
        setMove((byte) 1, AbstractMonster.Intent.ATTACK, ((DamageInfo) this.damage.get(0)).base);


    }

    @Override
    public void die() {
        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;

            for (AbstractPower p : this.powers) {
                p.onDeath();
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMonsterDeath(this);
            }
            addToTop((AbstractGameAction) new ClearCardQueueAction());


            setMove((byte) 4, AbstractMonster.Intent.UNKNOWN);
            createIntent();
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new ShoutAction((AbstractCreature) this, DIALOG[2]));

            applyPowers();
            DEAD=true;
            this.firstTurn = false;
        }

    }

    private void playSfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_3A");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_3B");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_3C");
        }
    }
    public void showMerchant()
    {
        System.out.println("showMerchant");

        (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;

        this.MerchantAfterRob.spawnHitbox();
        AbstractRoom.waitTimer = 0.1F;

    }

}