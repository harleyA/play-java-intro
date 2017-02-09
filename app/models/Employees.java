package models;

import scala.collection.generic.BitOperations;

import javax.persistence.*;

@Entity
public class Employees
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column (name = "employeeId")
    public Long employeeId;

    @Column(name = "firstName")
    public String firstName;

    @Column(name= "lastName")
    public String lastName;

    @Column(name= "photo")
    public byte[] photo;
}
