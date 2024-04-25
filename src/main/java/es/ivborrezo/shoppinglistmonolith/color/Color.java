package es.ivborrezo.shoppinglistmonolith.color;

public enum Color {
	RED("Red"),
    GREEN("Green"),
    BLUE("Blue"),
    YELLOW("Yellow"),
    TEAL("Teal"),
    ORANGE("Orange"),
    BROWN("Brown"),
    PURPLE("Purple"),
    PINK("Pink"),
    GRAY("Gray"),
    LIGHT_BLUE("Light Blue"),
    MAGENTA("Magenta"),
    LIME("Lime"),
    CYAN("Cyan"),
    OLIVE("Olive"),
    TAN("Tan"),
    SKY_BLUE("Sky Blue"),
    LAVENDER("Lavender"),
    BABY_BLUE("Baby Blue"),
    DARK_GREEN("Dark Green");

    private final String value;

    Color(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Color getColor(String text) {
        for (Color color : Color.values()) {
            if (color.value.equalsIgnoreCase(text)) {
                return color;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
