package roadregistry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class DemeritRecord {
  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  private final LocalDate offenseDate;
  private final int points;

  public DemeritRecord(String dateStr, int points) {
    try {
      this.offenseDate = LocalDate.parse(dateStr, DTF);
    } catch (DateTimeParseException ex) {
      throw new IllegalArgumentException("Invalid date format, expected DD-MM-YYYY", ex);
    }
    if (points < 1 || points > 6) {
      throw new IllegalArgumentException("Points must be between 1 and 6");
    }
    this.points = points;
  }

  public LocalDate getOffenseDate() {
    return offenseDate;
  }

  public int getPoints() {
    return points;
  }

  @Override
  public String toString() {
    return DTF.format(offenseDate) + ":" + points;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DemeritRecord that)) return false;
    return points == that.points &&
        offenseDate.equals(that.offenseDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(offenseDate, points);
  }
}
