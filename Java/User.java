package com.example.dinr;

    public class User {
        private String id;
        private String fName;
        private String lName ;
        private String email;
        private String password;

        public User() {

        }

        public User(String id,String fName, String lName, String email, String password) {
            this.fName=fName;
            this.lName=lName;
            this.email=email;
            this.password=password;
            this.id = id;
        }




    }
