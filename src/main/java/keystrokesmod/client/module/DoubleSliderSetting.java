package keystrokesmod.client.module;

import keystrokesmod.client.main.Raven;
import org.lwjgl.Sys;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleSliderSetting extends Setting {
    private final String name;
    public static final String settingType = "doubleslider";
    private double valMax, valMin;
    private final double max;
    private final double min;
    private final double interval;

    private final double defaultValMin, defaultValMax;

    public DoubleSliderSetting(String settingName, double defaultValueMin, double defaultValueMax, double min, double max, double intervals) {
        super(settingName, settingType);
        this.name = settingName;
        this.valMin = defaultValueMin;
        this.valMax = defaultValueMax;
        this.min = min;
        this.max = max;
        this.interval = intervals;
        this.defaultValMin = valMin;
        this.defaultValMax = valMax;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void resetToDefaults() {
        this.setValueMin(defaultValMin);
        this.setValueMax(defaultValMax);
    }

    public double getInputMin() {
        return round(this.valMin, 2);
    }
    public double getInputMax() {
        return round(this.valMax, 2);
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public void setValueMin(double n) {
        System.out.println(this.name + " " + n);
        n = correct(n, this.min, this.valMax);
        System.out.println(n);
        n = (double)Math.round(n * (1.0D / this.interval)) / (1.0D / this.interval);
        System.out.println(n);
        this.valMin = n;
        System.out.println(this.valMin + " " + this.getInputMin() + " " + n);
    }

    public void setValueMax(double n) {
        n = correct(n, this.valMin, this.max);
        n = (double)Math.round(n * (1.0D / this.interval)) / (1.0D / this.interval);
        this.valMax = n;
    }

    public static double correct(double val, double min, double max) {
        val = Math.max(min, val);
        val = Math.min(max, val);
        return val;
    }

    public static double round(double val, int p) {
        if (p < 0) {
            return 0.0D;
        } else {
            BigDecimal bd = new BigDecimal(val);
            bd = bd.setScale(p, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }
}