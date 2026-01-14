package crud;

import model.CoordinatesDB;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.*;

//Crud DataBase
public class DataBase {
    private Connection connection;
    private final File storageFolder;

    public DataBase(Plugin plugin) {
        this.storageFolder = new File(plugin.getDataFolder(), "coordinatesDB");
    }

    public Connection getConnection() throws SQLException {
        if(connection != null && !connection.isClosed()) {
            return connection;
        }

        //create folder if doesn't  exist
        if(!storageFolder.exists()) {
            storageFolder.mkdirs();
        }
        //create the drive of sql and connect
        File databaseFile = new File(storageFolder, "coordinates.db");

        String url = "jdbc:sqlite:" + databaseFile.getAbsolutePath();
        connection = DriverManager.getConnection(url);
        return connection;
    }

    //create Table if doesn't exist
    public void initialize() {
        try(Statement statement = getConnection().createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS coordinatesDB(" +
                            "uuid VARCHAR(36) PRIMARY KEY, " +
                            "world TEXT NOT NULL," +
                            "x DOUBLE NOT NULL," +
                            "y DOUBLE NOT NULL," +
                            "z DOUBLE NOT NULL," +
                            "yaw FLOAT NOT NULL," +
                            "pitch FLOAT NOT NULL)"
            );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //crud
    //getByUUID
    public CoordinatesDB getByUUID(String uuid) {
        String sql = "SELECT * FROM coordinatesDB WHERE uuid = ?";
        //connect
        try(PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            //send info
            if(resultSet.next()) {
                return new CoordinatesDB(
                        uuid,
                        resultSet.getString("world"),
                        resultSet.getDouble("x"),
                        resultSet.getDouble("y"),
                        resultSet.getDouble("z"),
                        resultSet.getFloat("yaw"),
                        resultSet.getFloat("pitch")
                );
            }
        }catch (SQLException e){e.printStackTrace();}
        //if fail, return null
        return null;
    }

    //create
    public void createCoordinates(CoordinatesDB coordinatesDB) {
        String sql = "INSERT OR IGNORE INTO coordinatesDB(uuid, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = getConnection().prepareStatement(sql)){
            statement.setString(1, coordinatesDB.getUuid());
            statement.setString(2, coordinatesDB.getWorld());
            statement.setDouble(3, coordinatesDB.getX());
            statement.setDouble(4, coordinatesDB.getY());
            statement.setDouble(5, coordinatesDB.getZ());
            statement.setFloat(6, coordinatesDB.getYaw());
            statement.setFloat(7, coordinatesDB.getPitch());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update
    public void updateCoordinates(CoordinatesDB coordinatesDB) {
        String sql = "UPDATE coordinatesDB SET world=?, x=?, y=?, z=?, yaw=?, pitch=? WHERE uuid = ? ";
        try(PreparedStatement statement = getConnection().prepareStatement(sql)){
            statement.setString(1, coordinatesDB.getWorld());
            statement.setDouble(2, coordinatesDB.getX());
            statement.setDouble(3, coordinatesDB.getY());
            statement.setDouble(4, coordinatesDB.getZ());
            statement.setFloat(5, coordinatesDB.getYaw());
            statement.setFloat(6, coordinatesDB.getPitch());
            statement.setString(7, coordinatesDB.getUuid());
            statement.executeUpdate();
        }catch (SQLException e){e.printStackTrace();}
    }
}
