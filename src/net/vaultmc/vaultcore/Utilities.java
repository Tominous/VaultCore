package net.vaultmc.vaultcore;

import net.vaultmc.vaultcore.settings.PlayerCustomColours;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public final class Utilities {

    public static Location deserializeLocation(String s) {
        if (s == null) return null;
        String[] parts = s.split(VaultCore.SEPARATOR);
        return new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + VaultCore.SEPARATOR + location.getX() + VaultCore.SEPARATOR + location.getY() + VaultCore.SEPARATOR +
                location.getZ();
    }

    /**
     * @param message      Message to format.
     * @param replacements The variables you wish to insert to the message.
     * @param sender       The player receiving the message, needed for custom colours.
     * @return Compiled message
     * @author Aberdeener
     */
    public static String formatMessage(String message, VLCommandSender sender, Object... replacements) {
        HashMap<String, String> colours = PlayerCustomColours.defaultColours;
        if (sender instanceof VLPlayer) {
            if (((VLPlayer) sender).isOnline())
                colours = PlayerCustomColours.getColours((VLPlayer) sender);
            else colours = PlayerCustomColours.getColoursFromFile((VLPlayer) sender);
        }
        int num = 0;
        StringBuilder sb = new StringBuilder();
        try {
            for (String s : message.split(" ")) {
                if (s.matches(".*?\\{.*?}.*")) {
                    String before = StringUtils.substringBefore(s, "{");
                    if (before.matches(".*?<.*?>.*")) {
                        String context = before.substring(0, before.length() - 1);
                        before = "&" + colours.get(context);
                    }
                    String after = s.substring(s.lastIndexOf("}") + 1);
                    if (after.matches(".*?<.*?>.*")) {
                        String context = after.substring(0, after.length() - 1);
                        after = "&" + colours.get(context);
                    }
                    s = replacements[num].toString();
                    sb.append(before).append(s).append(after).append(" ");
                    num++;
                } else {
                    sb.append(s).append(" ");
                }
            }
            return sb.toString().trim();
        } catch (NullPointerException e) {
            return ChatColor.DARK_RED + "There was an error formatting a message. Usually this means invalid message path, or a variable is null. Please report this.";
        } catch (ArrayIndexOutOfBoundsException e) {
            return ChatColor.DARK_RED + "There was an error formatting a message. This usually there are too many variables in the message string. Please report this.";
        }
    }

    /**
     * @param message Message to edit
     * @return Edited message
     * @author Aberdeener
     */
    static List<String> apostrophe = Arrays.asList("hes", "shes", "dont", "wont", "cant", "wouldnt", "shouldnt", "its", "hows", "isnt", "im", "thats");
    static List<String> punctuation = Arrays.asList(".", "!", "?");

    public static String grammarly(String message) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String word : message.split(" ")) {
            // Capitalize "i"
            if (word.equals("i")) word = word.toUpperCase();
            // Add apostrophe
            if (apostrophe.contains(word)) {
                word = word.substring(0, word.length() - 1) + "'" + word.substring(word.length() - 1);
            }
            // Uppercase if first word
            if (first)
                // Check if first char is a custom key. Not a fantastic solution, but sorta works.
                if (!Character.isLetterOrDigit(word.charAt(0)) && String.valueOf(word.charAt(0)).equals(word)) {
                    sb.append(word).append(" ");
                    first = false;
                    continue;
                } else if (!Character.isLetterOrDigit(word.charAt(0)))
                    word = word.charAt(0) + capitalizeMessage(word.substring(1));
                else word = capitalizeMessage(word);
            first = false;
            sb.append(word).append(" ");
        }
        message = sb.toString().trim();
        // Punctuation for last word
        if (!punctuation.contains(Character.toString(message.charAt(message.length() - 1)))) {
            message = message + ".";
        }
        return message;
    }

    /**
     * @param message Message to capitalize.
     * @return Capitalized message.
     * @author Aberdeener
     */
    public static String capitalizeMessage(String message) {
        return message.substring(0, 1).toUpperCase() + message.substring(1);
    }

    /**
     * @param millis Time in milliseconds you wish to turn into a duration.
     * @return Duration from milliseconds
     * @author Aberdeener
     */
    private static final Map<String, Long> timeparts = new LinkedHashMap<>();

    public static String millisToTime(long millis, boolean newline, boolean period) {

        long millisInSecond = 1000L;
        long millisInMinute = 60000L;
        long millisInHour = 3600000L;
        long millisInDay = 86400000;
        long millisInMonth = 2592000000L;
        long millisInYear = 31536000000L;

        long years = (long) Math.floor(millis / millisInYear);
        long monthMillis = millis % millisInYear;
        long months = (long) Math.floor(monthMillis / millisInMonth);
        long dayMillis = millis % millisInMonth;
        long days = (long) Math.floor(dayMillis / millisInDay);
        long hourMillis = millis % millisInDay;
        long hours = (long) Math.floor(hourMillis / millisInHour);
        long minuteMillis = millis % millisInHour;
        long minutes = (long) Math.floor(minuteMillis / millisInMinute);
        long secondMillis = millis % millisInMinute;
        long seconds = (long) Math.floor(secondMillis / millisInSecond);

        timeparts.put("year", years);
        timeparts.put("month", months);
        timeparts.put("day", days);
        timeparts.put("hour", hours);
        timeparts.put("minute", minutes);
        timeparts.put("second", seconds);

        StringBuilder sb = new StringBuilder();

        for (String section : timeparts.keySet()) {
            long duration = timeparts.get(section);

            if (duration > 0) {
                int position = new ArrayList<>(timeparts.keySet()).indexOf(section);
                String ending = (duration == 1 ? ", " : "s, ");
                // If the second last entry is == 1, we can assume we dont need a break.
                if (position == 4 && newline) {
                    ending = (duration == 1 ? " and " : "s \nand ");
                } else if (position == 4 && !newline) {
                    ending = (duration == 1 ? " and " : "s and ");
                }
                // If it is the last entry then add a period.
                else if (position == 5) {
                    if (period) {
                        ending = (duration == 1 ? ". " : "s.");
                    } else {
                        ending = (duration == 1 ? " " : "s");
                    }
                }
                sb.append(ChatColor.DARK_GREEN).append(duration).append(ChatColor.YELLOW).append(" ").append(section).append(ending);
            }
        }
        return sb.toString();
    }

    /**
     * @param millis Time in milliseconds to turn into a date.
     * @return Date from milliseconds
     * @author Aberdeener
     */
    public static String millisToDate(long millis) {
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date epoch = new Date(millis);
        return format.format(epoch);
    }

    /**
     * @param list       String List to turn into comma seperated String.
     * @param chatColour Whether to use ChatColors on commas.
     * @return Comma separated List<String>
     * @author Aberdeener
     */
    public static String listToString(Collection<String> list, boolean chatColour) {

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String word : list) {
            if (first) {
                sb.append(word);
                first = false;
                continue;
            }
            if (chatColour) sb.append(ChatColor.YELLOW).append(", ").append(ChatColor.RESET).append(word);
            else sb.append(", ").append(word);
        }
        return sb.toString();
    }

    /**
     * @param bytes Storage in bytes to convert to human readable kb/mb/gb etc
     * @return Human readable storage
     */
    public static String bytesToReadable(long bytes) {
        long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        return b < 1024L ? bytes + " b"
                : b <= 0xfffccccccccccccL >> 40 ? String.format("%.1f Kb", bytes / 0x1p10)
                : b <= 0xfffccccccccccccL >> 30 ? String.format("%.1f Mb", bytes / 0x1p20)
                : b <= 0xfffccccccccccccL >> 20 ? String.format("%.1f Gb", bytes / 0x1p30)
                : b <= 0xfffccccccccccccL >> 10 ? String.format("%.1f Tb", bytes / 0x1p40)
                : b <= 0xfffccccccccccccL ? String.format("%.1f Pb", (bytes >> 10) / 0x1p40)
                : String.format("%.1f Eb", (bytes >> 20) / 0x1p40);
    }
}
