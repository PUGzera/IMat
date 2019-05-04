package util;

import java.util.Date;

import static validators.BillingInformationFormValidator.validateNames;

public class CreditCard implements PaymentMethod {

    private CardType cardType;
    private String holdersName;
    private int validMonth;
    private int validYear;
    private String cardNumber;
    private int verificationCode;

    public CreditCard(CardType cardType, String holdersName, int validMonth, int validYear, String cardNumber, int verificationCode) {
        setCardType(cardType);
        setHoldersName(holdersName);
        setValidMonth(validMonth);
        setValidYear(validYear);
        setCardNumber(cardNumber);
        setVerificationCode(verificationCode);
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        if(cardNumber.matches("\\d{16}")) this.cardNumber = cardNumber;
    }

    public CardType getCardType() {
        return this.cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getHoldersName() {
        return this.holdersName;
    }

    public void setHoldersName(String holdersName) {
        if(validateNames(holdersName))this.holdersName = holdersName;
    }

    public int getValidMonth() {
        return this.validMonth;
    }

    public void setValidMonth(int validMonth) {
        if(validMonth > 0 && validMonth < 13) this.validMonth = validMonth;
    }

    public int getValidYear() {
        return this.validYear;
    }

    public void setValidYear(int validYear) {
        if(validYear >= new Date().getYear()) this.validYear = validYear;
    }

    public int getVerificationCode() {
        return this.verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        if(verificationCode >= 100 && verificationCode <= 999) this.verificationCode = verificationCode;
    }

    @Override
    public boolean isValid() {
        return cardType != null && !cardNumber.isEmpty() && validMonth != 0 && validYear != 0 && verificationCode != 0 && !holdersName.isEmpty();
    }

    public enum CardType {
        MASTER_CARD, VISA
    }
}

