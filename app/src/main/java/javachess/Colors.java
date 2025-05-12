package javachess;

/**
 * Class that contains ANSI escape codes for coloring text in the console.
 */
public class Colors {
    public static final String RESET = "\u001B[0m";

    public static final String BLACK    = "\033[38;2;0;0;0m";
    public static final String WHITE    = "\033[38;2;255;255;255m";
    public static final String RED      = "\033[38;2;255;0;0m";
    public static final String GREEN    = "\033[38;2;0;255;0m";
    public static final String YELLOW   = "\033[38;2;255;255;0m";
    public static final String BLUE     = "\033[38;2;0;0;255m";
    public static final String PURPLE   = "\033[38;2;255;0;255m";
    public static final String CYAN     = "\033[38;2;0;255;255m";

    public static final String BLACK_BACKGROUND     = "\033[40m";
    public static final String LIGHT_GRAY_BACKGROUND = "\033[48;2;180;180;180m";
    public static final String DARK_GRAY_BACKGROUND = "\033[48;2;120;120;120m";
    public static final String RED_BACKGROUND       = "\033[41m";
    public static final String GREEN_BACKGROUND     = "\033[42m";
    public static final String YELLOW_BACKGROUND    = "\033[43m";
    public static final String BLUE_BACKGROUND      = "\033[44m";
    public static final String PURPLE_BACKGROUND    = "\033[45m";
    public static final String CYAN_BACKGROUND      = "\033[46m";
    public static final String WHITE_BACKGROUND     = "\033[47m";

    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";
    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m";
    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";
    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";
}