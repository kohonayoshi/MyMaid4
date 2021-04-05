/*
 * jaoLicense
 *
 * Copyright (c) 2021 jao Minecraft Server
 *
 * The following license applies to this project: jaoLicense
 *
 * Japanese: https://github.com/jaoafa/jao-Minecraft-Server/blob/master/jaoLICENSE.md
 * English: https://github.com/jaoafa/jao-Minecraft-Server/blob/master/jaoLICENSE-en.md
 */

package com.jaoafa.mymaid4.lib;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.List;

public class SKKColorManager {
    static List<NamedTextColor> TextColors = Arrays.asList(
        NamedTextColor.GRAY,
        NamedTextColor.WHITE,
        NamedTextColor.DARK_BLUE,
        NamedTextColor.BLUE,
        NamedTextColor.AQUA,
        NamedTextColor.DARK_AQUA,
        NamedTextColor.DARK_GREEN,
        NamedTextColor.GREEN,
        NamedTextColor.YELLOW,
        NamedTextColor.GOLD,
        NamedTextColor.RED,
        NamedTextColor.DARK_RED,
        NamedTextColor.DARK_PURPLE,
        NamedTextColor.LIGHT_PURPLE);

    static List<ChatColor> ChatColors = Arrays.asList(
        ChatColor.GRAY,
        ChatColor.WHITE,
        ChatColor.DARK_BLUE,
        ChatColor.BLUE,
        ChatColor.AQUA,
        ChatColor.DARK_AQUA,
        ChatColor.DARK_GREEN,
        ChatColor.GREEN,
        ChatColor.YELLOW,
        ChatColor.GOLD,
        ChatColor.RED,
        ChatColor.DARK_RED,
        ChatColor.DARK_PURPLE,
        ChatColor.LIGHT_PURPLE);

    static int getVoteCount(Player player) {
        PlayerVoteDataMono pvd = new PlayerVoteDataMono(player);

        return pvd.getVoteCount();
    }

    static NamedTextColor getCustomColor(Player player) {
        PlayerVoteDataMono pvd = new PlayerVoteDataMono(player);
        String color = pvd.getCustomColor();
        if (color == null) return null;

        return MyMaidLibrary.getNamedTextColor(color);
    }

    static ChatColor getCustomChatColor(Player player) {
        PlayerVoteDataMono pvd = new PlayerVoteDataMono(player);
        String color = pvd.getCustomColor();
        if (color == null) return null;
        try {
            return ChatColor.valueOf(color);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * プレイヤーの四角色を取得する
     *
     * @param player プレイヤー
     *
     * @return 四角色
     */
    public static NamedTextColor getPlayerColor(Player player) {
        int count = getVoteCount(player);
        NamedTextColor color = TextColors.get(calculateRank(count));
        NamedTextColor customColor = getCustomColor(player);
        if (customColor != null) color = customColor;
        return color;
    }

    /**
     * プレイヤーの四角色を取得する
     *
     * @param player プレイヤー
     *
     * @return 四角色
     */
    public static ChatColor getPlayerChatColor(Player player) {
        int count = getVoteCount(player);
        ChatColor color = ChatColors.get(calculateRank(count));
        ChatColor customColor = getCustomChatColor(player);
        if (customColor != null) color = customColor;
        return color;
    }

    /**
     * ランク数値を取得する
     *
     * @param vote_count 投票数
     *
     * @return ランク数値 (0 <= n <= 13)
     */
    static int calculateRank(int vote_count) {
        if (vote_count == 0)
            return 0;
        if (vote_count <= 5)
            return 1;
        if (vote_count >= 160)
            return 13;
        return (vote_count - 5) / 14 + 1;
    }

    public static Component getPlayerSKKTabListComponent(Player player) {
        Team team = Bukkit.getServer().getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName());

        boolean threwIllegalStateException = false;
        try {
            if (team != null) team.color();
        } catch (IllegalStateException e) {
            threwIllegalStateException = true;
        }

        return team == null || threwIllegalStateException ?
            Component.text().append(
                Component.text("■").color(getPlayerColor(player)),
                Component.text(player.getName())
            ).build() :
            Component.text().append(
                Component.text("■").color(getPlayerColor(player)),
                Component.text(player.getName(), team.color())
            ).build();
    }

    public static void setPlayerSKKTabList(Player player) {
        player.playerListName(getPlayerSKKTabListComponent(player));
    }
}
