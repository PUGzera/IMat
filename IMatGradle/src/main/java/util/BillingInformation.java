package util;

import static validators.BillingInformationFormValidator.*;

public class BillingInformation {
    private String email;
    private String phone;
    private String ssn;
    private String zip;
    private String firstName;
    private String lastName;
    private String address;

    public BillingInformation(String email, String phone, String ssn, String zip, String firstName, String lastName, String address) {
        setEmail(email);
        setPhone(phone);
        setSsn(ssn);
        setZip(zip);
        setFirstName(firstName);
        setAddress(lastName);
        setAddress(address);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(validEmail(email))this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if(validatePhoneNumber(phone))this.phone = phone;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        if(validSSN(ssn))this.ssn = ssn;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        if(validZipCode(zip))this.zip = zip;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(validateNames(firstName))this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if(validateNames(lastName))this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if(validateNames(address))this.address = address;
    }

    public boolean isValid(){
        return !(email.isEmpty() && ssn.isEmpty() && zip.isEmpty() && address.isEmpty() && firstName.isEmpty() && lastName.isEmpty());
    }
}
