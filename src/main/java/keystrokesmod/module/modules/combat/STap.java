package keystrokesmod.module.modules.combat;

import keystrokesmod.ay;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleSettingSlider;
import keystrokesmod.module.ModuleSettingTick;
import keystrokesmod.module.modules.world.AntiBot;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.concurrent.ThreadLocalRandom;

public class STap extends Module {
    public static ModuleSettingSlider range;
    public static ModuleSettingTick onlyPlayers;
    public static ModuleSettingSlider minActionTicks, maxActionTicks, minOnceEvery, maxOnceEvery;
    public static double comboLasts;
    public static boolean comboing, hitCoolDown, alreadyHit;
    public static int hitTimeout, hitsWaited;

    public STap(){
        super("STap", category.combat, 0);
        this.registerSetting(onlyPlayers = new ModuleSettingTick("Only combo players", true));
        this.registerSetting(minActionTicks = new ModuleSettingSlider("Min ms: ", 75, 1, 500, 5));
        this.registerSetting(maxActionTicks = new ModuleSettingSlider("Man ms: ", 120, 1, 500, 5));
        this.registerSetting(minOnceEvery = new ModuleSettingSlider("Once every min hits: ", 1, 1, 10, 1));
        this.registerSetting(maxOnceEvery = new ModuleSettingSlider("Once every max hits: ", 1, 1, 10, 1));
        this.registerSetting(range = new ModuleSettingSlider("Range: ", 2.85, 1, 6, 0.05));
    }

    public void guiUpdate(){
        ay.correctSliders(minActionTicks, maxActionTicks);
        ay.correctSliders(minOnceEvery, maxOnceEvery);
    }


    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        if(!ay.isPlayerInGame())
            return;

        if(comboing) {
            if(System.currentTimeMillis() >= comboLasts){
                comboing = false;
                finishCombo();
                return;
            }else {
                return;
            }
        }



        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof Entity && Mouse.isButtonDown(0)) {
            Entity target = mc.objectMouseOver.entityHit;
            //////System.out.println(target.hurtResistantTime);
            if(target.isDead) {
                return;
            }

            if (mc.thePlayer.getDistanceToEntity(target) <= range.getInput()) {
                if (target.hurtResistantTime <= 10) {

                    if (onlyPlayers.isToggled()){
                        if (!(target instanceof EntityPlayer)){
                            return;
                        }
                    }

                    if(AntiBot.bot(target)){
                        return;
                    }


                    if (hitCoolDown && !alreadyHit) {
                        //////System.out.println("coolDownCheck");
                        hitsWaited++;
                        if(hitsWaited >= hitTimeout){
                            //////System.out.println("hiit cool down reached");
                            hitCoolDown = false;
                            hitsWaited = 0;
                        } else {
                            //////System.out.println("still waiting for cooldown");
                            alreadyHit = true;
                            return;
                        }
                    }

                    //////System.out.println("Continued");

                    if(!alreadyHit){
                        //////System.out.println("Startring combo code");
                        guiUpdate();
                        if(minOnceEvery.getInput() == maxOnceEvery.getInput()) {
                            hitTimeout =  (int)minOnceEvery.getInput();
                        } else {

                            hitTimeout = ThreadLocalRandom.current().nextInt((int)minOnceEvery.getInput(), (int)maxOnceEvery.getInput());
                        }
                        hitCoolDown = true;
                        hitsWaited = 0;

                        comboLasts = ThreadLocalRandom.current().nextDouble(minActionTicks.getInput(),  maxActionTicks.getInput()+0.01) + System.currentTimeMillis();
                        comboing = true;
                        startCombo();
                        //////System.out.println("Combo started");
                        alreadyHit = true;
                    }
                } else {
                    if(alreadyHit){
                        //////System.out.println("UnHit");
                    }
                    alreadyHit = false;
                    //////System.out.println("REEEEEEE");
                }
            }
        }
    }

    private static void finishCombo() {
        if(!Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())){
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        }
    }

    private static void startCombo() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
        KeyBinding.onTick(mc.gameSettings.keyBindBack.getKeyCode());
    }
}