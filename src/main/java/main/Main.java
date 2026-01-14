package main;

import commands.Home;
import commands.SetHome;
import crud.DataBase;
import utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {
    private DataBase db;

    public void onEnable() {
        saveDefaultConfig();
        //start db
        this.db = new DataBase(this);
        try {
            db.getConnection();
        } catch (SQLException e) {throw new RuntimeException(e);}
        db.initialize();

        registerCommands();
        Bukkit.getConsoleSender().sendMessage(Utils.colorMessage("&2Plugin Teleport has been enabled!"));
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Utils.colorMessage("&2Plugin Teleport has been disabled!"));
    }
    //register commands
    public void registerCommands() {
        this.getCommand("setHome").setExecutor(new SetHome(this, this));
        this.getCommand("home").setExecutor(new Home(this, this));
    }
    //injection
    public DataBase getDb() {
        return db;
    }
}
