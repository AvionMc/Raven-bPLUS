package me.kopamed.raven.bplus.client.feature.setting.settings;

import me.kopamed.raven.bplus.client.feature.setting.Setting;
import me.kopamed.raven.bplus.client.feature.setting.SettingType;
import me.kopamed.raven.bplus.client.visual.clickgui.plus.component.Component;
import me.kopamed.raven.bplus.client.visual.clickgui.plus.component.components.ModuleComponent;
import me.kopamed.raven.bplus.client.visual.clickgui.plus.component.components.settings.ComboComponent;
import me.kopamed.raven.bplus.client.visual.clickgui.plus.component.components.settings.RangeSliderComponent;

import java.util.Arrays;
import java.util.List;

public class ComboSetting extends Setting {
    private final List<String> options;
    private int currentPos =0;
    private String currentMode;

    public ComboSetting(String settingName, String[] modes, String defaultPos){
        super(settingName, SettingType.COMBO);

        this.options = Arrays.asList(modes);
        this.currentPos = this.options.indexOf(defaultPos);

        this.currentMode = this.options.get(currentPos);
    }


    public ComboSetting(String settingName, String[] modes, int defaultPos){
        super(settingName, SettingType.COMBO);

        this.options = Arrays.asList(modes);
        this.currentPos = defaultPos;

        this.currentMode = this.options.get(currentPos);
    }

    public String getMode(){
        return this.currentMode;
    }

    public void setMode(String val){
        this.currentMode = val;
        this.currentPos = this.options.indexOf(val);
    }

    public void nextMode(){
        if(currentPos < this.options.size() - 1){
            this.currentPos++;
            this.currentMode = this.options.get(currentPos);
        }else {
            this.currentPos = 0;
            this.currentMode = this.options.get(currentPos);
        }
    }

    @Override
    public Component createComponent(ModuleComponent moduleComponent) {
        return new ComboComponent(this, moduleComponent);
    }
}
