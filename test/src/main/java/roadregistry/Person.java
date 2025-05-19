package roadregistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Person {
  private String personID;
  private String firstName;
  private String lastName;
  private String address;
  private String birthdate;
  private List<DemeritRecord> demeritHistory = new ArrayList<>();
  private boolean isSuspended;
  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  static Path peopleFilePath = Path.of("data/people.txt");
  private static Path demeritsFilePath = Path.of("data/demerits.txt");
  public static void setPeopleFilePath(Path p) {
    peopleFilePath = p;
  }
  public static void setDemeritFilePath(Path p) {
    demeritsFilePath = p;
  }

  private int calculateAge(String birthdateStr) {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate birth = LocalDate.parse(birthdateStr, fmt);
    return Period.between(birth, LocalDate.now()).getYears();
  }

  public String getPersonID() { return personID; }

  public Person(String personID, String firstName, String lastName,
                String address, String birthdate) {
    this.personID  = personID;
    this.firstName = firstName;
    this.lastName  = lastName;
    this.address   = address;
    this.birthdate = birthdate;
  }

  public boolean addPerson() {
    if (!AddPersonValidator.validatePersonID(personID))   return false;
    if (!AddPersonValidator.validateAddress(address))     return false;
    if (!AddPersonValidator.validateBirthdate(birthdate)) return false;

    // 2) steps to add the person to csv by creating the line
    String line = String.join(",",
        personID, firstName, lastName, address, birthdate
    ) + System.lineSeparator();

    // 3) Append to file
    try {

      Files.writeString(
          peopleFilePath,
          line,
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND
      );
      return true;
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  public boolean updatePersonalDetails(String newID, String newFirst, String newLast, String newAddr, String newBirth) {
    // condition 1 check if age is less than 18 years old
    int age = calculateAge(this.birthdate);
    boolean addressChanged = !newAddr.equals(this.address);
    if (age < 18 && addressChanged) {
      return false;
    }

    // condition 2 check if birthday is passed as params, other field has to remain the same
    boolean birthChanged   = !newBirth.equals(this.birthdate);
    boolean otherFieldChanged =
      !newID   .equals(this.personID)
      || !newFirst.equals(this.firstName)
      || !newLast .equals(this.lastName)
      || addressChanged;
    if (birthChanged && otherFieldChanged) {
      return false;
    }

    // condition 3 check if first character is a number, and it is an even number, the id can not be changed.
    char first = this.personID.charAt(0);
    if (Character.isDigit(first) && ((first - '0') % 2 == 0)
        && !newID.equals(this.personID))
    {
      return false;
    }

    // if all condition checks succeed, proceed to update the person into the txt
    try {
      List<String> all = Files.readAllLines(peopleFilePath);
      String updatedLine = String.join(",",
          newID, newFirst, newLast, newAddr, newBirth);
      List<String> replaced = all.stream()
          .map(line -> line.startsWith(this.personID + ",")
              ? updatedLine
              : line)
          .collect(Collectors.toList());
      Files.write(peopleFilePath, replaced);
    } catch (IOException e) {
      return false;
    }

    // proceed to update the personal details
    this.personID  = newID;
    this.firstName = newFirst;
    this.lastName  = newLast;
    this.address   = newAddr;
    this.birthdate = newBirth;

    return true;
  }

  public boolean addDemeritPoints(String offenseDate, int points) {
    /* condition 1, validate the offense date format is dd-mm-yyyy, reusing the same function with addPerson */
    if (!AddPersonValidator.validateBirthdate(offenseDate)) return false;

    /* condition 2 points must be between 1 to 6 */
    if (points < 1 || points > 6) return false;

    /*
    // condition 3 ( if needed )
    demeritHistory.add(new DemeritRecord(offenseDate, points));

    LocalDate cutoff = LocalDate.now().minusYears(2);
    int total = demeritHistory.stream()
        .filter(r -> r.getOffenseDate().isAfter(cutoff))
        .mapToInt(DemeritRecord::getPoints)
        .sum();

    // under/over 21 threshold
    int age = Period.between(
        LocalDate.parse(this.birthdate, DTF),
        LocalDate.now()
    ).getYears();
    int threshold = (age < 21) ? 6 : 12;
    if (total > threshold) {
      isSuspended = true;
    }

    // write to demerit file.txt
    String line = String.join(",",
        this.personID,
        offenseDate,
        String.valueOf(points)
    ) + System.lineSeparator();
    try {
      Files.writeString(
          demeritsFilePath,
          line,
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND
      );
    } catch (IOException e) {
      return false;
    }
    */

    return true;
  }

  /* getter` */
  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getAddress() {
    return address;
  }

  public String getBirthdate() {
    return birthdate;
  }

  /*
  public boolean isSuspended() {
    return isSuspended;
  }

   */
}
