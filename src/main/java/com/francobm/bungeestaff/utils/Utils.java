package com.francobm.bungeestaff.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class Utils {

    public static String ChatColor(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static BaseComponent[] Text(String string){
        return TextComponent.fromLegacyText(ChatColor(string));
    }
}
