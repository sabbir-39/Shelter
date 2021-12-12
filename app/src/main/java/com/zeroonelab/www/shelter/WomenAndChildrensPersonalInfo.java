package com.zeroonelab.www.shelter;

/**
 * Created by HP on 5/4/2018.
 */

public class WomenAndChildrensPersonalInfo {

    public String name ;
    public String mobileNumber ;
    public String email ;
    public String password ;
    public String userID ;
    public ContactNumber contactNumber  ;

    public WomenAndChildrensPersonalInfo() {
    }

    public WomenAndChildrensPersonalInfo(String name, String mobileNumber, String email, String password, String userID, ContactNumber contactNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
        this.userID = userID;
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ContactNumber getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(ContactNumber contactNumber) {
        this.contactNumber = contactNumber;
    }

    public static class ContactNumber
    {
        public String contactNumber1;
        public String contactNumber2;
        public String contactNumber3;
        public String contactNumber4;
        public String contactNumber5;

        public ContactNumber() {
        }

        public ContactNumber(String contactNumber1, String contactNumber2, String contactNumber3, String contactNumber4, String contactNumber5) {
            this.contactNumber1 = contactNumber1;
            this.contactNumber2 = contactNumber2;
            this.contactNumber3 = contactNumber3;
            this.contactNumber4 = contactNumber4;
            this.contactNumber5 = contactNumber5;
        }
    }

}
