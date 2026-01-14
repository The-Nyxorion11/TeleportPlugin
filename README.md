# Teleportation plugin

This plugin allows players to use the /sethome command to  set a teleportation point and then use the /home command to teleport to it. 

# What technologies are used?

SQLite: For data persistence.

#-How does it work?

First of all, the plugin is divided into two commands:

# /sethome

When the player activates this command, it activates a new thread to avoid lag. In this thread, it checks if the player already had information in the database. If so, it will only update the existing information. 

After doing so, it will send a message to the player's chat saying that the creation of the teleportation point was successful.

# /home

This command teleports the player to the previously set point. To activate this command, a point must have been set previously.

It activates a thread again in which it searches for the coordinates in the database using the player's UUID and teleports them. 

#SQLite

This is what stores data such as coordinates with a mini crud that consists of obtaining, creating, and updating.

It looks something like this:

# Configuration
When the plugin starts, it will create a folder with the same name, which contains two things: a config.yml file and a folder.

# Config:
Here you can change the messages that will be sent to the player according to their actions.

# Folder
This is where the database file is stored.

Translated with DeepL.com (free version)
