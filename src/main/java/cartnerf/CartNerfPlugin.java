package cartnerf;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class CartNerfPlugin extends JavaPlugin implements CommandExecutor, Listener {

    private double reductionPercent;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reductionPercent = clamp(getConfig().getDouble("reduction-percent", 0.0));
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("cartnerf").setExecutor(this);
        getLogger().info("CartNerf enabled. TNT minecart damage reduced by " + format(reductionPercent) + "%.");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCartDamage(EntityDamageByEntityEvent event) {
        if (reductionPercent <= 0.0) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof ExplosiveMinecart)) return;

        // setDamage scales the armor/resistance modifiers along with the base damage.
        event.setDamage(event.getDamage() * (1.0 - reductionPercent / 100.0));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("cartnerf.admin")) {
            sender.sendMessage("You don't have permission to do that.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("TNT minecart damage is reduced by " + format(reductionPercent) + "%.");
            return true;
        }

        double value;
        try {
            value = Double.parseDouble(args[0].replace("%", "").trim());
        } catch (NumberFormatException e) {
            sender.sendMessage("Usage: /cartnerf <0-100>%");
            return true;
        }
        if (value < 0.0 || value > 100.0) {
            sender.sendMessage("Percentage must be between 0 and 100.");
            return true;
        }

        reductionPercent = value;
        getConfig().set("reduction-percent", value);
        saveConfig();
        sender.sendMessage("TNT minecart damage now reduced by " + format(value) + "%.");
        return true;
    }

    private static double clamp(double value) {
        return Math.max(0.0, Math.min(100.0, value));
    }

    private static String format(double value) {
        return value == Math.rint(value)
                ? String.valueOf((long) value)
                : String.format(Locale.ROOT, "%.1f", value);
    }
}
