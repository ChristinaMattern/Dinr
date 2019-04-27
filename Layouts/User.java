package com.example.dinr;
/*@author Nola Smtih
@date 4/25/2019 */
public class User {

    public String name;
    public String major;
    public String location;
    public String year;
    public String otherID;

    public User(){

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }
    public String getMajor() {

        return major;
    }

    public void setMajor(String Major) {

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

    public String getOtherID() {

        return otherID;
    }

    public void setOtherID(String otherID) {

        this.otherID= otherID;
    }

    public User(String name,String major, String location, String year, String otherID) {
        this.name = name;
        this.year=year;
        this.location=location;
        this.major=major;
        this.otherID=otherID;

    }
}