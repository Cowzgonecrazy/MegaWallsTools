package cowzgonecrazy.megawallstools.Modules;

public class C {

    public static final String COLOR_CODE_SYMBOL = "\u00A7";
    public static final String BLACK = COLOR_CODE_SYMBOL + "0";
    public static final String DARK_BLUE = COLOR_CODE_SYMBOL + "1";
    public static final String DARK_GREEN = COLOR_CODE_SYMBOL + "2";
    public static final String DARK_AQUA = COLOR_CODE_SYMBOL + "3";
    public static final String DARK_RED = COLOR_CODE_SYMBOL + "4";
    public static final String DARK_PURPLE = COLOR_CODE_SYMBOL + "5";
    public static final String GOLD = COLOR_CODE_SYMBOL + "6";
    public static final String GRAY = COLOR_CODE_SYMBOL + "7";
    public static final String DARK_GRAY = COLOR_CODE_SYMBOL + "8";
    public static final String BLUE = COLOR_CODE_SYMBOL + "9";
    public static final String GREEN = COLOR_CODE_SYMBOL + "a";
    public static final String AQUA = COLOR_CODE_SYMBOL + "b";
    public static final String RED = COLOR_CODE_SYMBOL + "c";
    public static final String LIGHT_PURPLE = COLOR_CODE_SYMBOL + "d";
    public static final String YELLOW = COLOR_CODE_SYMBOL + "e";
    public static final String WHITE = COLOR_CODE_SYMBOL + "f";
    public static final String OBFUSCATED = COLOR_CODE_SYMBOL + "k";
    public static final String BOLD = COLOR_CODE_SYMBOL + "l";
    public static final String STRIKETHROUGH = COLOR_CODE_SYMBOL + "m";
    public static final String UNDERLINE = COLOR_CODE_SYMBOL + "n";
    public static final String ITALIC = COLOR_CODE_SYMBOL + "o";
    public static final String RESET = COLOR_CODE_SYMBOL + "r";

    public static String compileColor(String color) {
        String compiledColor = "";
        if (color.equalsIgnoreCase("black")) {
            compiledColor = BLACK;
        } else if (color.equalsIgnoreCase("dark_blue") || color.equalsIgnoreCase("darkblue") || color.equalsIgnoreCase("dark blue")) {
            compiledColor = DARK_BLUE;
        } else if (color.equalsIgnoreCase("dark_green") || color.equalsIgnoreCase("darkgreen") || color.equalsIgnoreCase("dark green")) {
            compiledColor = DARK_GREEN;
        } else if (color.equalsIgnoreCase("dark_aqua") || color.equalsIgnoreCase("darkaqua") || color.equalsIgnoreCase("dark aqua")) {
            compiledColor = DARK_AQUA;
        } else if (color.equalsIgnoreCase("dark_red") || color.equalsIgnoreCase("darkred") || color.equalsIgnoreCase("dark red")) {
            compiledColor = DARK_RED;
        } else if (color.equalsIgnoreCase("dark_purple") || color.equalsIgnoreCase("darkpurple") || color.equalsIgnoreCase("dark purple")) {
            compiledColor = DARK_PURPLE;
        } else if (color.equalsIgnoreCase("dark_gray") || color.equalsIgnoreCase("darkgray") || color.equalsIgnoreCase("dark gray")) {
            compiledColor = DARK_GRAY;
        } else if (color.equalsIgnoreCase("gold") || color.equalsIgnoreCase("dark_yellow") || color.equalsIgnoreCase("dark yellow")) {
            compiledColor = GOLD;
        } else if (color.equalsIgnoreCase("gray")) {
            compiledColor = GRAY;
        } else if (color.equalsIgnoreCase("blue")) {
            compiledColor = BLUE;
        } else if (color.equalsIgnoreCase("green")) {
            compiledColor = GREEN;
        } else if (color.equalsIgnoreCase("aqua")) {
            compiledColor = AQUA;
        } else if (color.equalsIgnoreCase("red")) {
            compiledColor = RED;
        } else if (color.equalsIgnoreCase("light_purple") || color.equalsIgnoreCase("lightpurple") || color.equalsIgnoreCase("light purple")) {
            compiledColor = LIGHT_PURPLE;
        } else if (color.equalsIgnoreCase("yellow")) {
            compiledColor = YELLOW;
        } else if (color.equalsIgnoreCase("white")) {
            compiledColor = WHITE;
        } else {
            compiledColor = C.GREEN;
            Utils.sendMessage("Setting color failed!");
        }
        return compiledColor;
    }

    public static Integer compileHexColor(String color) {
        String hexColor;
        Integer compiledHexColor;

        if (color.equalsIgnoreCase("black")) {
            hexColor = "000000";
        } else if (color.equalsIgnoreCase("dark_blue") || color.equalsIgnoreCase("darkblue") || color.equalsIgnoreCase("dark blue")) {
            hexColor = "0000AA";
        } else if (color.equalsIgnoreCase("dark_green") || color.equalsIgnoreCase("darkgreen") || color.equalsIgnoreCase("dark green")) {
            hexColor = "00AA00";
        } else if (color.equalsIgnoreCase("dark_aqua") || color.equalsIgnoreCase("darkaqua") || color.equalsIgnoreCase("dark aqua")) {
            hexColor = "00AAAA";
        } else if (color.equalsIgnoreCase("dark_red") || color.equalsIgnoreCase("darkred") || color.equalsIgnoreCase("dark red")) {
            hexColor = "AA0000";
        } else if (color.equalsIgnoreCase("dark_purple") || color.equalsIgnoreCase("darkpurple") || color.equalsIgnoreCase("dark purple")) {
            hexColor = "AA00AA";
        } else if (color.equalsIgnoreCase("dark_gray") || color.equalsIgnoreCase("darkgray") || color.equalsIgnoreCase("dark gray")) {
            hexColor = "555555";
        } else if (color.equalsIgnoreCase("gold") || color.equalsIgnoreCase("dark_yellow") || color.equalsIgnoreCase("dark yellow")) {
            hexColor = "FFAA00";
        } else if (color.equalsIgnoreCase("gray")) {
            hexColor = "AAAAAA";
        } else if (color.equalsIgnoreCase("blue")) {
            hexColor = "5555FF";
        } else if (color.equalsIgnoreCase("green")) {
            hexColor = "55FF55";
        } else if (color.equalsIgnoreCase("aqua")) {
            hexColor = "55FFFF";
        } else if (color.equalsIgnoreCase("red")) {
            hexColor = "FF5555";
        } else if (color.equalsIgnoreCase("light_purple") || color.equalsIgnoreCase("lightpurple") || color.equalsIgnoreCase("light purple")) {
            hexColor = "FF55FF";
        } else if (color.equalsIgnoreCase("yellow")) {
            hexColor = "FFFF55";
        } else if (color.equalsIgnoreCase("white")) {
            hexColor = "FFFFFF";
        } else {
            hexColor = "55FF55";
            Utils.sendMessage("Setting color failed!");
        }
        compiledHexColor = Integer.parseInt(hexColor, 16);
        return compiledHexColor;
    }
}
