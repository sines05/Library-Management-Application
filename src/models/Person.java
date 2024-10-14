package models;
import java.io.File; // Photo
import java.sql.Blob; // Co the de luu tru hinh anh trong db

public abstract class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String mobileNo;
    private String address;
    private String mail;
    private String gender;
    private File photo;
    private String dateOfBirth;

    public Person() {

    }

    public Person(int id, String firstName, String middleName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public Person(String firstName, String middleName, String lastName, String mobileNo, String address, String mail, String gender, File photo, String dateOfBirth) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobileNo = mobileNo;
        this.address = address;
        this.mail = mail;
        this.gender = gender;
        this.photo = photo;
        this.dateOfBirth = dateOfBirth;
    }

    public Person(int id, String firstName, String middleName, String lastName, String mobileNo, String address, String mail, String gender, File photo, String dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobileNo = mobileNo;
        this.address = address;
        this.mail = mail;
        this.gender = gender;
        this.photo = photo;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }
}
