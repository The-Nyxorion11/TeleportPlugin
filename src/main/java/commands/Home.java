package commands;

import crud.DataBase;
import main.Main;
import model.CoordinatesDB;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import utils.Utils;

import java.util.UUID;

public class Home implements CommandExecutor {

    private Plugin plugin;
    private final Main main;

    public Home(Plugin plugin, Main main) {
        this.plugin = plugin;
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Utils.colorMessage("&4Only one player can perform this action!"));
            return true;
        }

        UUID uuid = ((Player) sender).getUniqueId();
        String uuidString = uuid.toString();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            DataBase db = main.getDb();
            CoordinatesDB getCoordinates = db.getByUUID(uuidString);

            if ( getCoordinates == null) {
                //create if doesn't exist info
                sender.sendMessage(Utils.colorMessage(plugin.getConfig().getString("messages.no-room")));

            }else {
                sender.sendMessage(Utils.colorMessage(plugin.getConfig().getString("messages.teleporting")));
                Bukkit.getScheduler().runTask(plugin, () -> {
                    World world = Bukkit.getWorld(getCoordinates.getWorld());
                    Location location = new Location(world, getCoordinates.getX(), getCoordinates.getY(), getCoordinates.getZ(), getCoordinates.getYaw(), getCoordinates.getPitch());

                    ((Player) sender).teleport(location);
                });
            }
        });

        return false;
    }
}
