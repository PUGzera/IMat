package imat.user;


import imat.util.BillingInformation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class User {

    public static PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    @Pattern(regexp = "[A-z\\d\\.-]{1,}@[A-z\\d\\.-]{1,}\\.[A-z]{2,}")
    private String email;
    @Size(min = 8)
    private String password;
    @Pattern(regexp = "(19|20)\\d\\d(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])\\d{4}")
    private String ssn;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Pattern(regexp = "07+\\d{8}")
    private String mobilePhoneNumber;
    @NotNull
    private String address;
    @Pattern(regexp = "[\\d]{5}")
    private String postCode;
    @NotNull
    private String postAddress;
    @NotNull
    private String role;

    protected User() {}

    public User(String email, String password, String ssn, String firstName, String lastName, String mobilePhoneNumber, String address, String postCode, String postAddress) {
        this.email = email;
        setPassword(password);
        this.ssn = ssn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.address = address;
        this.postCode = postCode;
        this.postAddress = postAddress;
        role = "Role_Customer";
    }

    public BillingInformation billingInformation() {
        return new BillingInformation(email, mobilePhoneNumber, ssn, postCode, firstName, lastName, address);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public String getSsn() {
        return ssn;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getPostAddress() {
        return postAddress;
    }

    public String getRole() {
        return role;
    }
}
