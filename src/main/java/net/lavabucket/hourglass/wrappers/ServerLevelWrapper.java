/*
 * Copyright (C) 2021 Nick Iacullo
 *
 * This file is part of Hourglass.
 *
 * Hourglass is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hourglass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Hourglass.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.lavabucket.hourglass.wrappers;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.lavabucket.hourglass.HourglassMod;
import net.lavabucket.hourglass.time.SleepStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper.UnableToAccessFieldException;

/**
 * This class acts as a wrapper for {@link ServerLevel} to increase consistency between Minecraft
 * versions.
 *
 * Since the {@link ServerLevel} class changes its name and package between different versions of
 * Minecraft, supporting different Minecraft versions would require modifications to any class that
 * imports or references {@link ServerLevel}. This class consolidates these variations into itself,
 * allowing other classes to depend on it instead.
 */
public class ServerLevelWrapper extends Wrapper<ServerLevel> {

    private static final Logger LOGGER = LogManager.getLogger();

    // Store classes at the top to minimize file changes between Minecraft versions.
    private static final Class<ServerLevel> levelClass = ServerLevel.class;
    private static final Class<ServerLevelData> levelDataClass = ServerLevelData.class;
    private static final Class<DerivedLevelData> derivedLevelDataClass = DerivedLevelData.class;

    /** The {@link ServerLevelData} of the wrapped level. */
    public final ServerLevelData levelData;

    /**
     * Instantiates a new object.
     * @param level  the server level to wrap
     */
    public ServerLevelWrapper(LevelAccessor level) {
        super(levelClass.cast(level));
        this.levelData = levelDataClass.cast(this.get().getLevelData());
    }

    /** {@return true if the 'daylight cycle' game rule is enabled in {@link #wrapped}} */
    public boolean daylightRuleEnabled() {
        return wrapped.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
    }

    /** {@return true if the 'weather cycle' game rule is enabled in {@link #wrapped}} */
    public boolean weatherRuleEnabled() {
        return wrapped.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE);
    }

    /**
     * Sets the 'random tick speed' game rule for {@link #wrapped}.
     * @param speed  the new random tick speed
     */
    public void setRandomTickSpeed(int speed) {
        wrapped.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).set(speed, wrapped.getServer());
    }

    /**
     * Convenience method that returns true if the weather cycle is progressing in {@link #wrapped}.
     * @return true if the weather cycle is progressing in {@link #wrapped}
     */
    public boolean weatherCycleEnabled() {
        return weatherRuleEnabled() && wrapped.dimensionType().hasSkyLight();
    }

    /**
     * Emulates the vanilla functionality for stopping weather, as access modifiers prevent calls
     * to the methods that do this in vanilla.
     */
    public void stopWeather() {
        levelData.setRainTime(0);
        levelData.setRaining(false);
        levelData.setThunderTime(0);
        levelData.setThundering(false);
    }

    /**
     * Sets the {@link ServerLevel} sleep status using reflection, as access modifiers prevent this.
     * In Minecraft versions lower than 1.17 this method should do nothing.
     *
     * @param newStatus  the new sleep status
     */
    public void setSleepStatus(SleepStatus newStatus) {
        try {
            Field sleepStatus = ObfuscationReflectionHelper.findField(levelClass, "f_143245_");
            sleepStatus.setAccessible(true);
            sleepStatus.set(wrapped, newStatus);
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException | UnableToAccessFieldException e) {
            LOGGER.warn(HourglassMod.MARKER, "Error setting sleep status.", e);
            return;
        }
    }

    /**
     * Performs vanilla morning wakeup functionality to wake up all sleeping players.
     */
    public void wakeUpAllPlayers() {
        wrapped.players().stream()
                .map(player -> new ServerPlayerWrapper(player))
                .filter(ServerPlayerWrapper::isSleeping)
                .forEach(player -> player.get().stopSleepInBed(false, false));
    }

    /**
     * {@return true if {@code level} is a derived level}
     * @param level  the level to check
     */
    public static boolean isDerived(LevelAccessor level) {
        return level != null && level.getLevelData().getClass() == derivedLevelDataClass;
    }

    /** {@return true if {@code level} is an instance of a server level} */
    public static boolean isServerLevel(LevelAccessor level) {
        return level != null && level.getClass() == levelClass;
    }

}
