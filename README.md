# LachShield Plugin

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20.2-brightgreen.svg)
![Plugin Version](https://img.shields.io/badge/Plugin%20Version-1.2.1-blue.svg)

LachShield was a Minecraft Paper plugin designed to manage player connections based on their IP addresses, now with new features and improvements.

## Features

- **IP Limit Control**: Limit the number of player accounts that can join from the same IP address. Set the IP limit using the `/lachshield iplimit <number>` command.
- **Custom Kick Message**: Customize the message that players receive when they exceed the IP limit by editing the `config.yml` file.
- **Admin Privileges**: Players with the `lachshield.admin` permission can bypass the IP limit, ensuring server administrators have control.

## Installation

1. Download the latest release of the plugin from the [Releases](https://github.com/LachCrafter/LachShield/releases) section.
2. Place the downloaded JAR file into the `plugins` folder of your Minecraft server.
3. Restart or reload your server to enable the plugin.

## Usage

### Setting the IP Limit

Use the following command to set the IP limit:

/lachshield iplimit <number>

Replace `<number>` with the desired IP limit. Players who exceed this limit will be kicked from the server with a customizable message.

### Custom Kick Message

You can customize the kick message by editing the `config.yml` file. Look for the `kickMessage` option and set your desired message there.

## Permissions

- `lachshield.admin`: Allows users to bypass and edit the IP limit and access admin privileges.

## Contributing

Contributions are welcome! If you find a bug or want to suggest an enhancement, please create an issue in the [Issue Tracker](https://github.com/LachCrafter/LachShield/issues). If you'd like to contribute code, fork the repository, create a new branch, make your changes, and then create a pull request.

## License

This project is licensed under the [MIT License](https://github.com/LachCrafter/LachShield/blob/master/LICENSE).

## Contact

For questions or inquiries, you can reach out to me on Discord: lachcrafter.
