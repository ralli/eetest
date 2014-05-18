package de.fisp.eetest.dto.person;

import de.fisp.eetest.entities.Person;

import java.util.List;

public class FindPersonsResponse {
  private List<Person> persons;

  public FindPersonsResponse(List<Person> persons) {
    this.persons = persons;
  }

  public List<Person> getPersons() {
    return persons;
  }
}
