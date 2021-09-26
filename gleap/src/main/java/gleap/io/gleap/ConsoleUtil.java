package gleap.io.gleap;

class ConsoleUtil {

    public static void clearConsole() {
        try {
            Runtime.getRuntime().exec("logcat - c");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
