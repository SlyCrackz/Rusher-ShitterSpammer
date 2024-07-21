package xyz.slycrack;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

public class ShitterSpammerPlugin extends Plugin {

    @Override
    public void onLoad() {
        this.getLogger().info("Loading ShitterSpammer Plugin");

        ShitterSpammerModule shitterSpammerModule = new ShitterSpammerModule();
        RusherHackAPI.getModuleManager().registerFeature(shitterSpammerModule);
    }

    @Override
    public void onUnload() {
        this.getLogger().info("Unloading ShitterSpammer Plugin");
    }
}