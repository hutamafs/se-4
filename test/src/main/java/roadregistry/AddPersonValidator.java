package roadregistry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddPersonValidator {
  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  // validate id when person is added ( condition 1 )
  public static boolean validatePersonID(String id) {
    if (id == null || id.length() != 10) return false;
    // first two chars 2–9
    if (!id.substring(0,2).matches("[2-9]{2}")) return false;
    // last two uppercase letters
    if (!id.substring(8).matches("[A-Z]{2}")) return false;
    // check if the ID have at least 2 special characters between position 3 to 8
    String middle = id.substring(2,8);
    long specialCount = middle.chars()
        .filter(c -> !Character.isLetterOrDigit(c))
        .count();
    return specialCount >= 2;
  }

  // validate address for condition 2
  public static boolean validateAddress(String addr) {
    if (addr == null) return false;
    String[] parts = addr.split("\\|");
    // check if there are 5 for the address parts when string is being split
    if (parts.length != 5) return false;
    // check if it is victoria for the state
    return "Victoria".equals(parts[3]);
  }

  // check birthdate has been formatted correctly dd-mm-yyyy
  public static boolean validateBirthdate(String date) {
    if (date == null) return false;
    try {
      LocalDate.parse(date, DTF);
      return true;
    } catch (DateTimeParseException ex) {
      return false;
    }
  }


}
