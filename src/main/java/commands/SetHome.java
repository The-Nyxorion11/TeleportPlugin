package commands;

import crud.DataBase;
import main.Main;
import model.CoordinatesDB;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import utils.Utils;

import java.util.UUID;

public class SetHome implements CommandExecutor {
    private Plugin plugin;
    private final Main main;

    public SetHome(Plugin plugin, Main main) {
        this.plugin = plugin;
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.colorMessage("&4Only one player can perform this action!"));
            return true;
        }
        Location location = ((Player) sender).getLocation();

        UUID uuid = ((Player) sender).getUniqueId();
        String uuidString = uuid.toString();

       Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
           DataBase db = main.getDb();

           if (db.getByUUID(uuidString) == null) {
               //create if doesn't exist info
               db.createCoordinates(setInfoCoordinates(location, uuidString));
           }else {
               db.updateCoordinates(setInfoCoordinates(location, uuidString));
           }
           Bukkit.getScheduler().runTask(plugin, () -> {
               sender.sendMessage(Utils.colorMessage("&aSet Home!"));
           });
       });
        return false;
    }

    //set info for create and update
    public CoordinatesDB setInfoCoordinates(Location location, String uuidString){
        //get world

        //get coordinates
        String nameWorld = location.getWorld().getName();
        Double x = location.getX();
        Double y = location.getY();
        Double z = location.getZ();
        Float yaw = location.getYaw();
        Float pitch = location.getPitch();

        return new CoordinatesDB(uuidString,nameWorld,x,y,z,yaw,pitch);
    }
}
