# ![Hourglass](./media/logo-wide-588x256.png)

Hourglass is a Minecraft Forge mod that gives you control over the passage of time. It allows you to
customize the length of the day-night cycle and alters the Minecraft sleep mechanic by accelerating
the speed of time.

This mod (optionally) replaces the vanilla sleep functionality with a smooth and natural transition
to morning by accelerating the passage of time while you're in bed. In multiplayer, time will pass
faster depending on the percentage of players who are currently sleeping. This removes the need for
any sleep voting system or player threshold, as any number of players can have an impact on the
duration of the night.

Hourglass allows for customization of the day-night cycle duration, and can control day and night
speed independently. Rather than the vanilla duration of 20 minutes, you can slow down time to make
a day in Minecraft last as long as a day in real life, or speed up the passage of nights for a more
forgiving experience.

Download it on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/hourglass).

[![](http://cf.way2muchnoise.eu/title/hourglass.svg)](https://www.curseforge.com/minecraft/mc-mods/hourglass)

## Commands

#### `/hourglass config <config-key> [<value>]`

Many of the config options can be changed in-game via this command.

If the `<value>` argument is omitted, displays the config option's current value.

## Support

The Overworld is currently the only dimension supported. However, most dimensions in Minecraft
(including custom dimensions) derive their time information from the Overworld and will therefore
elapse time at the same rate.

This mod does not save data to world files and can safely be safely removed from a Minecraft
installation.

## Configuration

When customizing Hourglass, the **speed of time** is controlled using a multiplier. A value of 1 is equivalent
to vanilla speed (20 minutes for a full day-night cycle). Setting daySpeed and nightSpeed to 0.5
will cut the speed of time in half, doubling the duration of a full day to 40 minutes. Likewise,
doubling the configured speed to 2.0 will result in a shortened day lasting only 10 minutes from
one morning to the next.

### Default Server Config

```toml
[general]
	#The speed at which time passes during the day.
	#Day is defined as any time between 23500 (middle of dawn) and 12500 (middle of dusk) the next day.
	#Vanilla speed: 1.0
	#Range: 0.0 ~ 24000.0
	daySpeed = 1.0

	#The speed at which time passes during the night.
	#Night is defined as any time between 12500 (middle of dusk) and 23500 (middle of dawn).
	#Vanilla speed: 1.0
	#Range: 0.0 ~ 24000.0
	nightSpeed = 1.0

	#When true, displays a clock in the sleep interface.
	displayBedClock = true

	#Accelerate the passage of weather at the same rate as the passage of time, making weather events
	#elapse faster while the passage of time is accelerated. Clear weather is not accelerated.
	#Note: This setting is not applicable if game rule doWeatherCycle is false.
	accelerateWeather = true

[sleep]
	#Enables or disables the sleep feature of this mod. Enabling this setting will modify the vanilla
	#sleep functionality and may conflict with other sleep mods. If disabled, the remaining settings
	#in this section will not apply.
	enableSleepFeature = true

	#The minimum speed at which time passes when only 1 player is sleeping in a full server.
	#Range: 0.0 ~ 24000.0
	sleepSpeedMin = 2.0

	#The maximum speed at which time passes when all players are sleeping. A value of 120
	#is approximately equal to the time it takes to sleep in vanilla.
	#Range: 0.0 ~ 24000.0
	sleepSpeedMax = 120.0

	#The speed at which time passes when all players are sleeping.
	#Set to -1 to disable this feature (sleepSpeedMax will be used when all players are sleeping).
	#Range: -1.0 ~ 24000.0
	sleepSpeedAll = -1.0

	#Set to 'true' for the weather to clear when players wake up in the morning as it does in vanilla.
    #Set to 'false' to allow weather to pass realistically overnight if accelerateWeather is enabled.
	#Note: This setting is ignored if game rule doWeatherCycle is false.
	clearWeatherOnWake = true

#This section defines settings for notification messages.
#All messages in this section support Minecraft formatting codes (https://minecraft.fandom.com/wiki/Formatting_codes).
#All messages in this section support variable substitution in the following format: ${variableName}
#Supported variables differ per message.
[messages]
	#This message is sent to morningMessageTarget after a sleep cycle has completed in it.
	#Available variables:
	#sleepingPlayers -> the number of players in the current dimension who were sleeping.
	#totalPlayers -> the number of players in the current dimension (spectators are not counted).
	#sleepingPercentage -> the percentage of players in the current dimension who were sleeping (does not include % symbol).
	morningMessage = "§e§oTempus fugit!"

	#Sets the message type for the morning message.
	#SYSTEM: Appears as a message in the chat. (e.g., "Respawn point set")
	#GAME_INFO: Game information that appears above the hotbar (e.g., "You may not rest now, the bed is too far away").
	#Allowed Values: SYSTEM, GAME_INFO
	morningMessageType = "GAME_INFO"

	#Sets the target for the morning message.
	#ALL: Send the message to all players on the server.
	#DIMENSION: Send the message to all players in the current dimension.
	#SLEEPING: Only send the message to those who just woke up.
	#Allowed Values: ALL, DIMENSION, SLEEPING
	morningMessageTarget = "DIMENSION"

	#This message is sent to bedMessageTarget when a player starts sleeping.
	#Available variables:
	#player -> the player who started sleeping.
	#sleepingPlayers -> the number of players in the current dimension who are sleeping.
	#totalPlayers -> the number of players in the current dimension (spectators are not counted).
	#sleepingPercentage -> the percentage of players in the current dimension who are sleeping (does not include % symbol).
	inBedMessage = "${player} is now sleeping. [${sleepingPlayers}/${totalPlayers}]"

	#This message is sent to bedMessageTarget when a player gets out of bed (without being woken up naturally at morning).
	#Available variables:
	#player -> the player who left their bed.
	#sleepingPlayers -> the number of players in the current dimension who are sleeping.
	#totalPlayers -> the number of players in the current dimension (spectators are not counted).
	#sleepingPercentage -> the percentage of players in the current dimension who are sleeping (does not include % symbol).
	outOfBedMessage = "${player} has left their bed. [${sleepingPlayers}/${totalPlayers}]"

	#Sets the message type for inBedMessage and outOfBedMessage.
	#SYSTEM: Appears as a message in the chat (e.g., "Respawn point set").
	#GAME_INFO: Game information that appears above the hotbar (e.g., "You may not rest now, the bed is too far away").
	#Allowed Values: SYSTEM, GAME_INFO
	bedMessageType = "GAME_INFO"

	#Sets the target for inBedMessage and outOfBedMessage.
	#ALL: Send the message to all players on the server.
	#DIMENSION: Send the message to all players in the current dimension.
	#SLEEPING: Only send the message to players who are currently sleeping.
	#Allowed Values: ALL, DIMENSION, SLEEPING
	bedMessageTarget = "DIMENSION"
```

### Default Client Config

```toml
[gui]
	#Sets the screen alignment of the bed clock.
	#Allowed Values: TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_LEFT, CENTER_CENTER, CENTER_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
	clockAlignment = "TOP_RIGHT"

	#This setting prevents clock wobble when getting in bed by updating the clock's position every tick.
	#As a side-effect, the clock won't wobble when first viewed as it does in vanilla. This setting is
	#unused if displayBedClock is false.
	preventClockWobble = true

	#Sets the distance between the clock and the edge of the screen.
	#Unused if clockAlignment is CENTER_CENTER.
	#Range: > 0
	clockMargin = 16

	#Sets the scale of the bed clock.
	#Range: > 1
	clockScale = 64
```
