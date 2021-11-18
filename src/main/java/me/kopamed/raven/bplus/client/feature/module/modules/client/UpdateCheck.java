package me.kopamed.raven.bplus.client.feature.module.modules.client;

import me.kopamed.raven.bplus.client.Raven;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.helper.manager.VersionManager;
import me.kopamed.raven.bplus.helper.utils.Utils;
import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.setting.settings.Description;
import me.kopamed.raven.bplus.client.feature.setting.settings.Tick;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.net.MalformedURLException;
import java.net.URL;

public class UpdateCheck extends Module {
    public static Description howToUse;
    public static Tick copyToClipboard;
    public static Tick openLink;
    public UpdateCheck() {
        super("Update", ModuleCategory.client, 0);
        this.registerSetting(howToUse = new Description(Utils.Java.uf("command") + ": update"));
        this.registerSetting(copyToClipboard = new Tick("Copy to clipboard", true));
        this.registerSetting(openLink = new Tick("Open dl in browser", true));
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        VersionManager versionManager = Raven.client.getVersionManager();
        if (versionManager.isOutdated()) {
            Utils.Player.sendMessageToSelf("The current version or Raven B+ is outdated. Visit https://github.com/Kopamed/Raven-bPLUS to download the latest version.");
            Utils.Player.sendMessageToSelf("https://github.com/Kopamed/Raven-bPLUS");
        }
        if (versionManager.isBeta()) {
            Utils.Player.sendMessageToSelf("Man is on beta and asking for stable. You mad bruv?"); // todo change this weird gay shit
            Utils.Player.sendMessageToSelf("https://github.com/Kopamed/Raven-bPLUS");
        }
        else {
            Utils.Player.sendMessageToSelf("You are on the latest public version!");
        }
        if (copyToClipboard.isToggled()) {
            if (Utils.Client.copyToClipboard(Raven.client.getVersionManager().getSourceURL()))
                Utils.Player.sendMessageToSelf("Successfully copied download link to clipboard!");
        }
        if(openLink.isToggled()) {
            URL url = null;
            try {
                url = new URL(Raven.client.getVersionManager().getSourceURL());
                Utils.Client.openWebpage(url);
            } catch (MalformedURLException bruh) {
                bruh.printStackTrace();
                Utils.Player.sendMessageToSelf("&cFailed to open page! Please report this bug in Raven b+'s discord");
            }
        }
        this.disable();
    }
}