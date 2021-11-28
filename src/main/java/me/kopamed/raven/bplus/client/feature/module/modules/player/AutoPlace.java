package me.kopamed.raven.bplus.client.feature.module.modules.player;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.helper.manager.ModuleManager;
import me.kopamed.raven.bplus.client.feature.setting.settings.DescriptionSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.BooleanSetting;
import me.kopamed.raven.bplus.helper.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class AutoPlace extends Module {
   public static DescriptionSetting ds;
   public static BooleanSetting a;
   public static BooleanSetting b;
   public static NumberSetting c;
   private double lfd = 0.0D;
   private final int d = 25;
   private long l = 0L;
   private int f = 0;
   private MovingObjectPosition lm = null;
   private BlockPos lp = null;

   public AutoPlace() {
      super("AutoPlace", ModuleCategory.Player, 0);
      this.registerSetting(ds = new DescriptionSetting("FD: FPS/80"));
      this.registerSetting(c = new NumberSetting("Frame delay", 8.0D, 0.0D, 30.0D, 1.0D));
      this.registerSetting(a = new BooleanSetting("Hold right", true));
   }

   public void guiUpdate() {
      if (this.lfd != c.getInput()) {
         this.rv();
      }

      this.lfd = c.getInput();
   }

   public void onDisable() {
      if (a.isToggled()) {
         this.rd(4);
      }

      this.rv();
   }

   public void update() {
      if (a.isToggled() && Mouse.isButtonDown(1) && !mc.thePlayer.capabilities.isFlying && !ModuleManager.fastPlace.isToggled()) {
         ItemStack i = mc.thePlayer.getHeldItem();
         if (i == null || !(i.getItem() instanceof ItemBlock)) {
            return;
         }

         this.rd(mc.thePlayer.motionY > 0.0D ? 1 : 1000);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void bh(DrawBlockHighlightEvent ev) {
      if (Utils.Player.isPlayerInGame()) {
         if (mc.currentScreen == null && !mc.thePlayer.capabilities.isFlying) {
            ItemStack i = mc.thePlayer.getHeldItem();
            if (i != null && i.getItem() instanceof ItemBlock) {
               MovingObjectPosition m = mc.objectMouseOver;
               if (m != null && m.typeOfHit == MovingObjectType.BLOCK && m.sideHit != EnumFacing.UP && m.sideHit != EnumFacing.DOWN) {
                  if (this.lm != null && (double)this.f < c.getInput()) {
                     ++this.f;
                  } else {
                     this.lm = m;
                     BlockPos pos = m.getBlockPos();
                     if (this.lp == null || pos.getX() != this.lp.getX() || pos.getY() != this.lp.getY() || pos.getZ() != this.lp.getZ()) {
                        Block b = mc.theWorld.getBlockState(pos).getBlock();
                        if (b != null && b != Blocks.air && !(b instanceof BlockLiquid)) {
                           if (!a.isToggled() || Mouse.isButtonDown(1)) {
                              long n = System.currentTimeMillis();
                              if (n - this.l >= 25L) {
                                 this.l = n;
                                 if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, i, pos, m.sideHit, m.hitVec)) {
                                    Utils.Client.setMouseButtonState(1, true);
                                    mc.thePlayer.swingItem();
                                    mc.getItemRenderer().resetEquippedProgress();
                                    Utils.Client.setMouseButtonState(1, false);
                                    this.lp = pos;
                                    this.f = 0;
                                 }

                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void rd(int i) {
      try {
         if (FastPlace.r != null) {
            FastPlace.r.set(mc, i);
         }
      } catch (IllegalAccessException | IndexOutOfBoundsException var3) {
      }
   }

   private void rv() {
      this.lp = null;
      this.lm = null;
      this.f = 0;
   }
}
