package validators;

public class BillingInformationFormValidator {

    public static boolean validatePhoneNumber(String phone){
        return phone.matches("07+\\d{8}") || phone.equals("");
    }

    public static boolean validEmail(String email) {
        return email.matches("[A-z\\d\\.-]{1,}@[A-z\\d\\.-]{1,}\\.[A-z]{2,}");
    }

    public static boolean validSSN(String ssn) {
        return ssn.matches("(19|20)\\d\\d(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])\\d{4}");
    }

    public static boolean validateNames(String name) {
        return name.length() > 0;
    }

    public static boolean validZipCode(String zipcode) {
        return zipcode.matches("[\\d]{5}");
    }


}
