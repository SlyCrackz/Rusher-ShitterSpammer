// File: xyz/slycrack/ShitterSpammerModule.java
package xyz.slycrack;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.rusherhack.client.api.events.player.EventInteract;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.utils.ChatUtils;
import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.BooleanSetting;
import org.rusherhack.core.setting.NumberSetting;
import org.rusherhack.core.setting.StringSetting;

public class ShitterSpammerModule extends ToggleableModule {

    private final StringSetting message = new StringSetting("Message", "The message to send when hitting a player", "You just got spammed!");
    private final StringSetting whisperCommand = new StringSetting("Whisper Command", "The command used for whispering", "/w");
    private final BooleanSetting limitByHits = new BooleanSetting("Limit By Hits", false);
    private final NumberSetting<Integer> hitLimit = new NumberSetting<>("Hit Limit", 1, 1, 10);

    private int hitCounter = 0;

    public ShitterSpammerModule() {
        super("ShitterSpammer", "Sends a preset whisper message to a player when you hit them", ModuleCategory.MISC);
        this.registerSettings(this.message, this.whisperCommand, this.limitByHits, this.hitLimit);
    }

    @Subscribe
    private void onPlayerInteract(EventInteract event) {
        if (!this.isToggled()) {
            return;
        }

        if (event.getTargetEntity() instanceof Player) {
            Player target = (Player) event.getTargetEntity();
            if (shouldSendMessage()) {
                sendWhisperMessage(target, this.message.getValue());
                hitCounter = 0; // Reset hit counter after sending the message
            } else {
                hitCounter++;
            }
        }
    }

    private boolean shouldSendMessage() {
        if (limitByHits.getValue() && hitCounter < hitLimit.getValue() - 1) {
            return false;
        }
        return true;
    }

    private void sendWhisperMessage(Player target, String message) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null && target != null) {
            String whisperCmd = whisperCommand.getValue() + " " + target.getName().getString() + " " + message;
            minecraft.player.connection.sendCommand(whisperCmd);
            //ChatUtils.print("Whispered to " + target.getName().getString() + ": " + message);
        }
    }

    @Override
    public void onEnable() {
        ChatUtils.print("ShitterSpammer enabled");
    }

    @Override
    public void onDisable() {
        ChatUtils.print("ShitterSpammer disabled");
        hitCounter = 0;
    }
}
