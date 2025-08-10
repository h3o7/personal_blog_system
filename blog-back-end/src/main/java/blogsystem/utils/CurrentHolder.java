package blogsystem.utils;

public class CurrentHolder {

    private static final ThreadLocal<String> CURRENT_LOCAL = new ThreadLocal<>();

    public static void setCurrentId(String employeeId) {
        CURRENT_LOCAL.set(employeeId);
    }

    public static String getCurrentId() {
        return CURRENT_LOCAL.get();
    }

    public static void remove() {
        CURRENT_LOCAL.remove();
    }
}
