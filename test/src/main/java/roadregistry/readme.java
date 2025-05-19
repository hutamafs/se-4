/*
* User stories
*
* 1. As a RoadRegistry admin, I want to add a new person record so that their basic details are stored in the RoadRegistry.
  Acceptance Criteria:

* With valid inputs (10-char ID meeting format rules, “|”-delimited address, DD-MM-YYYY birthdate), if I call addPerson(), the method returns true.
* If one of the inputs is invalid, such as birthdate format or missing delimited address, if I call addPerson, the method returns false, and the method will not write to the file.
* If all the validation at addPerson succeeds, a new file that has five fields (personId, first name, last name, address and birthdate) will be written to the people.txt.
* If addPerson() fails, then people.txt remains unchanged (or is not created).

* 2. As a RoadRegistry Admin, I want to prevent adding duplicate person records so that the registry remains clean.
  Acceptance Criteria:

* Given a personID already in people.txt, when I call addPerson() with that same ID, then the method returns false, and the person.txt remain unchanged.
* Given a new, unique personID, addPerson() returns true.
* Duplicate detection ignores case only on the ID (IDs are case‐sensitive).

* 3. As a RoadRegistry admin, I want to update a person’s details so that corrections or changes can be persisted under business rules.
  Acceptance Criteria:

* When the user is under 18 years old, and if I change the address and call updatePersonalDetails(...), it will return false.
* When I change the person's birthday, and try to change other fields while calling updatePersonalDetails(...), it will return false.
* If I try to change person ID at updatePersonalDetails(...) when the person ID first character is a number and even, it will return false.

* 4. As a RoadRegistry admin, I want to record a demerit point offense so that the person’s driving record is up-to-date.
  Acceptance Criteria:

* When I try to add a demerit point offense date and input it with other format than 'dd-mm-yyyy' through calling addDemeritPoints(), it will return false.
* When I try to add demerit points with points less than 1 or more than 6 points, it will return false.
* Given a valid date and points for a person under 21 whose cumulative points in the last two years exceed 6, addDemeritPoints(...) returns “Success” and isSuspended==true.
* Given a valid date and points for a person over 21 whose cumulative points in the last two years exceed 12, addDemeritPoints(...) returns “Success” and isSuspended==true.

* */