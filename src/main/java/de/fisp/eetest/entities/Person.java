package de.fisp.eetest.entities;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Person implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id = null;
   @Version
   @Column(name = "version")
   private int version = 0;

   @Column
   private String vorname;

   @Column
   private String nachname;

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   @Override
   public boolean equals(Object that)
   {
      if (this == that)
      {
         return true;
      }
      if (that == null)
      {
         return false;
      }
      if (getClass() != that.getClass())
      {
         return false;
      }
      if (id != null)
      {
         return id.equals(((Person) that).id);
      }
      return super.equals(that);
   }

   @Override
   public int hashCode()
   {
      if (id != null)
      {
         return id.hashCode();
      }
      return super.hashCode();
   }

   public String getVorname()
   {
      return this.vorname;
   }

   public void setVorname(final String vorname)
   {
      this.vorname = vorname;
   }

   public String getNachname()
   {
      return this.nachname;
   }

   public void setNachname(final String nachname)
   {
      this.nachname = nachname;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (vorname != null && !vorname.trim().isEmpty())
         result += "vorname: " + vorname;
      if (nachname != null && !nachname.trim().isEmpty())
         result += ", nachname: " + nachname;
      return result;
   }
}