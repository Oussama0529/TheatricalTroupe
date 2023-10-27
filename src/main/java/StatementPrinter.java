import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {
  public static final String TRAGEDY = "tragedy";
  public static final String COMEDY = "comedy";

  public String print(Invoice invoice, HashMap<String, Play> plays) {
    StringBuilder result = new StringBuilder();
    int totalAmount = 0;
    int volumeCredits = 0;
    String customer = invoice.customer;
    result.append(String.format("Statement for %s\n", invoice.customer));
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      int thisAmount = 0;

      switch (play.type) {
        case TRAGEDY:
          thisAmount = 40000;
          if (perf.audience > 30) {
            thisAmount += 1000 * (perf.audience - 30);
          }
          break;
        case COMEDY:
          thisAmount = 30000;
          if (perf.audience > 20) {
            thisAmount += 10000 + 500 * (perf.audience - 20);
          }
          thisAmount += 300 * perf.audience;
          break;
        default:
          throw new Error("Unknown type: " + play.type);
      }

      // Add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // Add extra credit for every ten comedy attendees
      if (COMEDY.equals(play.type)) {
        volumeCredits += Math.floor(perf.audience / 5);
      }

      // Print line for this order
      result.append(String.format("  %s: %s (%d seats)\n", play.name, currencyFormatter.format(thisAmount / 100.0), perf.audience));
      totalAmount += thisAmount;
    }
    result.append(String.format("Amount owed is %s\n", currencyFormatter.format(totalAmount / 100.0)));
    result.append(String.format("You earned %d credits\n", volumeCredits));
    return result.toString();
  }
}
