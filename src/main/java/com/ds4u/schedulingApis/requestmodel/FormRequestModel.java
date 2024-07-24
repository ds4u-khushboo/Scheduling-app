package com.ds4u.schedulingApis.requestmodel;

public class FormRequestModel {
     private String bookingFor;
        private String firstName;
        private String lastName;
        private String birthDate;
        private String sex;
        private String phoneNumber;
        private String email;
        private String infoForProvider;
        private String day;
        private String time;

        public String getBookingFor() {
            return bookingFor;
        }

        public void setBookingFor(String bookingFor) {
            this.bookingFor = bookingFor;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(String birthDate) {
            this.birthDate = birthDate;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getInfoForProvider() {
            return infoForProvider;
        }

        public void setInfoForProvider(String infoForProvider) {
            this.infoForProvider = infoForProvider;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

}
