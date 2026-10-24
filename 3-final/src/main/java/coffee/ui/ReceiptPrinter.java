package coffee.ui;

/** The shop's little receipt printer. */
public class ReceiptPrinter {
    /** Global state: the last problem, or null if the last print worked. */
    public static String lastError = null;

    private static int paperLeft = 1;   // receipts left on the roll

    public static void print(String receipt) {
        if (paperLeft == 0) {
            lastError = "out of paper";
            return;
        }
        paperLeft--;
        lastError = null;
        System.out.println(receipt);
    }
}
