# LachShield
A customizable plugin that enhances the server security and features.

### Features
- **IP Limit Control**: Limit the number of player accounts that can join from the same IP address.
- **Kick AFK players**: Kick players that are AFK in a specified time.
- **Prevent Nether-Roof**: Prevent players from accessing the Nether Roof.
- **Player Information Obfuscation**: Prevents players from viewing information from other players through hacks (e.g. health, armor durability).
- **Anti Pearl Phase**: Prevent players from phasing through blocks using ender pearls.

### Installation
1. Download the latest release of the plugin from the [Releases](https://github.com/LachCrafter/LachShield/releases) section.
2. *(Optional)* For the `PlayerObfuscator` feature, download the `PacketEvents` dependency [here](https://modrinth.com/plugin/packetevents/versions).
3. Place the downloaded JAR file into the `plugins` folder of your Minecraft server.
4. Restart your server to load and enable the plugin.

### Configuration
You can configure the plugin in the `config.yml` file.
> Full documentation can be found on the [wiki](https://github.com/LachCrafter/LachShield/wiki/Configuration).

### Commands
- `/lachshield`: Displays information about the plugin and available commands.
- `/lachshield reload <config|all|feature>`: Reloads specific features, the configuration or the whole plugin. 
- `/lachshield enable <feature>`: Enables a specific feature.
- `/lachshield disable <feature>`: Disables a specific feature.
- `/lachshield iplimit <number>`: Sets the IP limit.

### Permissions
- `lachshield.admin`: Bypasses all restrictions and grants access to all commands.
- `lachshield.commands`: Allows access to LachShield commands.
- `lachshield.broadcast`: Grants access to the broadcast command.
- `lachshield.antiAfk`: Exempts the player from AFK checks in the `AntiAFK` feature.
- `lachshield.preventRoof`: Allows access to the Nether Roof.

### Contributing
Contributions are welcome! If you find a bug or want to suggest an enhancement, please create an issue in the [Issue Tracker](https://github.com/LachCrafter/LachShield/issues).
If you'd like to contribute code, fork the repository, create a new branch, make your changes, and then create a pull request.

### License
This project is licensed under the [MIT License](https://github.com/LachCrafter/LachShield/blob/master/LICENSE).

### Contact
For questions or inquiries, you can reach out to me via email: [contact@lachcrafter.de](mailto:contact@lachcrafter.de) or by creating an issue.