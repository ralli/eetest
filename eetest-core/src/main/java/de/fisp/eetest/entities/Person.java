package de.fisp.eetest.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Person implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private long id;

  @Column
  private String vorname;

  @Column
  private String nachname;

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null) {
      return false;
    }
    if (getClass() != that.getClass()) {
      return false;
    }

    return id == ((Person) that).id;
  }

  @Override
  public int hashCode() {
    return (int) id;
  }

  public String getVorname() {
    return this.vorname;
  }

  public void setVorname(final String vorname) {
    this.vorname = vorname;
  }

  public String getNachname() {
    return this.nachname;
  }

  public void setNachname(final String nachname) {
    this.nachname = nachname;
  }

  @Override
  public String toString() {
    return "Person{" +
            "id=" + id +
            ", vorname='" + vorname + '\'' +
            ", nachname='" + nachname + '\'' +
            '}';
  }
}