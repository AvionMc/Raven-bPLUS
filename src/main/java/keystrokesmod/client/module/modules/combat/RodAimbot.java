package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.SliderSetting;
import keystrokesmod.client.module.TickSetting;
import keystrokesmod.client.module.modules.world.AntiBot;
import keystrokesmod.client.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraftforge.client.event.MouseEvent;
import keystrokesmod.client.lib.fr.jmraich.rax.event.FMLEvent;

import java.util.Iterator;

public class RodAimbot extends Module {
   public static SliderSetting a;
   public static SliderSetting b;
   public static TickSetting c;

   public RodAimbot() {
      super("RodAimbot", Module.category.combat, 0);
      this.registerSetting(a = new SliderSetting("FOV", 90.0D, 15.0D, 360.0D, 1.0D));
      this.registerSetting(b = new SliderSetting("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
      this.registerSetting(c = new TickSetting("Aim invis", false));
   }

   @FMLEvent
   public void x(MouseEvent ev) {
      if (ev.button == 1 && ev.buttonstate && Utils.Player.isPlayerInGame() && mc.currentScreen == null) {
         if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFishingRod && mc.thePlayer.fishEntity == null) {
            Entity en = this.gE();
            if (en != null) {
               ev.setCanceled(true);
               Utils.Player.aim(en, -7.0F, true);
               mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
            }
         }

      }
   }

   public Entity gE() {
      int f = (int)a.getInput();
      Iterator var2 = mc.theWorld.playerEntities.iterator();

      EntityPlayer en;
      do {
         do {
            do {
               do {
                  if (!var2.hasNext()) {
                     return null;
                  }

                  en = (EntityPlayer)var2.next();
               } while(en == mc.thePlayer);
            } while(en.deathTime != 0);
         } while(!c.isToggled() && en.isInvisible());
      } while((double)mc.thePlayer.getDistanceToEntity(en) > b.getInput() || AntiBot.bot(en) || !Utils.Player.fov(en, (float)f));

      return en;
   }
}