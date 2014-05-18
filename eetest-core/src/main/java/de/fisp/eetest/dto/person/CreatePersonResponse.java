package de.fisp.eetest.dto.person;

public class CreatePersonResponse {
  private long id;

  public CreatePersonResponse(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
