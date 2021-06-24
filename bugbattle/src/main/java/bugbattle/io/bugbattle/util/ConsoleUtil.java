package bugbattle.io.bugbattle.util;

public class ConsoleUtil {

    public static void clearConsole() {
        try {
            Runtime.getRuntime().exec("logcat - c");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
