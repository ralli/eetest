package de.fisp.eetest.dto.person;

import de.fisp.eetest.validations.NotBlank;

import javax.validation.constraints.Size;

public class CreatePersonRequest {
  @Size(max=30, message = "Der Vorname darf maximal 30 Zeichen lang sein.")
  @NotBlank(message = "Geben Sie bitte einen Vornamen ein")
  private String vorname;
  @Size(max=30, message = "Der Nachname darf maximal 30 Zeichen lang sein.")
  @NotBlank(message = "Geben Sie bitte einen Nachnamen ein")
  private String nachname;

  public String getNachname() {
    return nachname;
  }

  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  public String getVorname() {

    return vorname;
  }

  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  @Override
  public String toString() {
    return "CreatePersonRequest{" +
      "vorname='" + vorname + '\'' +
      ", nachname='" + nachname + '\'' +
      '}';
  }
}
