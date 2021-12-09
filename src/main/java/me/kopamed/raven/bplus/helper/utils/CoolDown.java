package me.kopamed.raven.bplus.helper.utils;

public class CoolDown {
    private long start;
    private long lasts;

    public CoolDown(long lasts){
        this.lasts = lasts;
    }

    public void start(){
        this.start = System.currentTimeMillis();
        //Utils.Player.sendMessageToSelf("Time started " + lasts/1000);
    }

    public boolean hasTimeElapsed(){
        //Utils.Player.sendMessageToSelf("Time finished");
        return System.currentTimeMillis() >= start + lasts;
    }

    public void setCooldown(long time){
        //Utils.Player.sendMessageToSelf("Set cooldown to " + time);
        this.lasts = time;
    }

    public long getElapsedTime(){
        return System.currentTimeMillis() - this.start;
    }

    public long getTimeLeft(){
        return lasts - (System.currentTimeMillis() - start);
    }
}
