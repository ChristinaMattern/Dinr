package com.example.dinr;
/*@author Nola Smtih
@date 4/25/2019 */
public class User {

    public String fName;
    public String major;
    public String location;
    public String year;
    public String userId;
    public String lName;

    public User(){

    }

    public String getfName() {

        return fName;
    }

    public void setfName(String fName) {

        this.fName = fName;
    }
    public String getlName() {

        return lName;
    }

    public void setlName(String lName) {

        this.lName = lName;
    }
    public String getMajor() {

        return major;
    }

    public void setMajor(String major) {

        this.major = major;
    }
    public String getLocation() {

        return location;
    }

    public void setLocation(String location) {

        this.location = location;
    }
    public String getYear() {

        return year;
    }

    public void setYear(String year) {

        this.year = year;
    }

    public String getUserId() {

        return userId;
    }

    public void setOtherID(String userId) {

        this.userId= userId;
    }

    public User(String fName,String major, String location, String year, String userId,String lName) {
        this.fName = fName;
        this.year=year;
        this.location=location;
        this.major=major;
        this.userId=userId;
        this.lName=lName;

    }
}