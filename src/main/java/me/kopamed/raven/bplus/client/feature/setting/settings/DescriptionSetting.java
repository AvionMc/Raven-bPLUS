package me.kopamed.raven.bplus.client.feature.setting.settings;

import me.kopamed.raven.bplus.client.feature.setting.Setting;
import me.kopamed.raven.bplus.client.feature.setting.SettingType;
import me.kopamed.raven.bplus.client.visual.clickgui.plus.component.Component;
import me.kopamed.raven.bplus.client.visual.clickgui.plus.component.components.ModuleComponent;
import me.kopamed.raven.bplus.client.visual.clickgui.plus.component.components.settings.DescriptionComponent;
import me.kopamed.raven.bplus.client.visual.clickgui.plus.component.components.settings.RangeSliderComponent;

public class DescriptionSetting extends Setting {
   private String desc;

   public DescriptionSetting(String t) {
      super(t, SettingType.DESCRIPTION);
      this.desc = t;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String t) {
      this.desc = t;
   }

   @Override
   public Component createComponent(ModuleComponent moduleComponent) {
      return new DescriptionComponent(this, moduleComponent);
   }
}