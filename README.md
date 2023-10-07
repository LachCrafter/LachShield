# LachShield Plugin

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20.2-brightgreen.svg)
![Plugin Version](https://img.shields.io/badge/Plugin%20Version-1.2-blue.svg)

LachShield is a Minecraft Paper plugin designed to limit the number of player accounts that can join the server from the same IP address.

## Features

- Limits the number of player accounts per IP address to three.
- Players attempting to join with a fourth account from the same IP address will be kicked with a message.

## Installation

1. Download the latest release of the plugin from the [Releases](https://github.com/LachCrafter/LachShield/releases) section.
2. Place the downloaded JAR file into the `plugins` folder of your Minecraft server.
3. Restart or reload your server to enable the plugin.

## Commands

Commands to manage IP restrictions:

- `/lachshield iplimit <number>`: Set the IP limit to restrict the number of players who can join from the same IP address.

## Usage

Once the plugin is installed, you can use the new `/lachshield iplimit` command to set the IP limit. Players attempting to join with a fourth account from the same IP address will be kicked from the server with a message indicating the account limit has been exceeded.

## Contributing

Contributions are welcome! If you find a bug or want to suggest an enhancement, please create an issue in the [Issue Tracker](https://github.com/LachCrafter/LachShield/issues). If you'd like to contribute code, fork the repository, create a new branch, make your changes, and then create a pull request.

## License

This project is licensed under the [MIT License](LICENSE).

## Contact

For questions or inquiries, you can reach out to me at Discord: [lachcrafter].
