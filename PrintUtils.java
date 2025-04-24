import java.util.Scanner;

public class PrintUtils {

    public static String waitForNextInput() {
        System.out.println();
        System.out.println("Press ENTER to continue.");

        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    public static void printError(String message) {
        System.out.print("ERROR: ");
        System.out.println(message);
    }
}
