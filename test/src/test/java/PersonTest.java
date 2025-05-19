import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import roadregistry.AddPersonValidator;
import roadregistry.Person;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;

class PersonTest {

  @TempDir
  Path tmpDir;

  Path peopleFile;

  @BeforeEach
  void setup() {
    // setup to the people.txt file to write all the valid Person class
    peopleFile = tmpDir.resolve("people.txt");
    Person.setPeopleFilePath(peopleFile);
  }

  // reusable function to do the actual test of addPerson
  private void runAddPersonTest(Person p, boolean expectWrite) throws Exception {
    boolean result = p.addPerson();
    assertEquals(expectWrite, result, "Return value");
    if (expectWrite) {
      assertTrue(Files.exists(peopleFile), "File should exist");
      String content = Files.readString(peopleFile);
      assertTrue(content.contains(p.getPersonID()), "File should contain ID");
    } else {
      assertFalse(Files.exists(peopleFile), "File should not exist");
    }
  }

  @Test
  void addPerson_validInput_returnsTrue() throws Exception {
    Person p = new Person("56s_d%&fAB", "Hutama", "Saputra",
        "32|Highland St|Melbourne|Victoria|Australia",
        "15-11-1990");
    runAddPersonTest(p, true);
  }

  // test invalid id
  @Test
  void addPerson_invalidId_returnsFalse() throws Exception {
    Person p = new Person("BAD_ID_123", "Hutama", "Saputra",
        "32|Highland St|Melbourne|Victoria|Australia",
        "15-11-1990");
    runAddPersonTest(p, false);
  }

  // test address if state is not victoria
  @Test
  void addPerson_invalidState_returnsFalse() throws Exception {
    Person p = new Person("56s_d%&fAB", "Hutama", "Saputra",
        "32|Highland St|Sydney|NSW|Australia",
        "15-11-1990");
    runAddPersonTest(p, false);
  }

  private void runUpdateDetailTest(Person p,
                                   String newID, String newFirst, String newLast,
                                   String newAddr, String newBirth,
                                   boolean expectSuccess,
                                   java.util.function.Supplier<String> fieldGetter,
                                   String expectedValue) {
    boolean ok = p.updatePersonalDetails(newID, newFirst, newLast, newAddr, newBirth);
    assertEquals(expectSuccess, ok, "Return value");
    if (expectSuccess) {
      assertEquals(expectedValue, fieldGetter.get(), "successfully updated");
    } else {
      assertNotEquals(expectedValue, fieldGetter.get(), "update failed");
    }
  }

    @Test
    void cannotChangeAddressIfUnder18() {
      Person p = new Person("56s_d%&fAB", "Hutama", "Saputra",
          "32|Highland St|Sydney|NSW|Australia",
          "15-11-2010");
      p.addPerson();

      runUpdateDetailTest(
          p,
          p.getPersonID(), p.getFirstName(), p.getLastName(),
          "3|rose|mary|Melbourne|Victoria|Australia",
          p.getBirthdate(),
          false,
          p::getAddress,
          "3|rose|mary|Melbourne|Victoria|Australia"
      );
    };

  @Test
  void cannotChangeOtherDetailsIfBirthdayChange() {
    Person p = new Person("56s_d%&fAB", "Hutama", "Saputra",
        "32|Highland St|Melbourne|Victoria|Australia",
        "24-11-2010");
    p.addPerson();


    runUpdateDetailTest(
        p,
        p.getPersonID(), p.getFirstName(), p.getLastName(),
        p.getAddress(), "14-11-1990",               // changing birthdate
        true,
        p::getLastName,
        "Saputra"
    );
  };

  @Test
  void cannotChangeIdIfFirstDigitEven() {
    Person p = new Person("88s_d%&fAB", "Hutama", "Saputra",
        "32|Highland St|Melbourne|Victoria|Australia",
        "24-11-2010");
    p.addPerson();

    runUpdateDetailTest(
        p,
        "78s_d%&fAB", // new ID
        p.getFirstName(), p.getLastName(),
        p.getAddress(), p.getBirthdate(),
        false,
        p::getPersonID,
        "78s_d%&fAB"
    );
  }

  @Test
  void addDemeritPointsWithInvalidDate() {
    Person p = new Person("88s_d%&fAB", "Hutama", "Saputra",
        "32|Highland St|Melbourne|Victoria|Australia",
        "24-11-2010");
    p.addPerson();

    boolean status = p.addDemeritPoints("11-24-2025", 5);
    assertFalse(status);
  }

  @Test
  void addDemeritPointsWithValidPoint() {
    Person p = new Person("88s_d%&fAB", "Hutama", "Saputra",
        "32|Highland St|Melbourne|Victoria|Australia",
        "24-11-2010");
    p.addPerson();

    boolean status = p.addDemeritPoints("24-11-2025", 5);
    assertTrue(status);

  }
}
