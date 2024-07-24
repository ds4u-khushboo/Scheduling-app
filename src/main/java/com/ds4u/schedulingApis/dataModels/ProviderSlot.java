package com.ds4u.schedulingApis.dataModels;

import java.util.List;

public class ProviderSlot {
        private String status;
        private String today_date;
        private String start_date;
        private String end_date;
        private int days;
        private String end_time;
        private String next_avil_day;
        private int previous;
        private List<String> date_list;
        private List<Slot> appt_slots;

        // Getters and setters
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getToday_date() {
            return today_date;
        }

        public void setToday_date(String today_date) {
            this.today_date = today_date;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getNext_avil_day() {
            return next_avil_day;
        }

        public void setNext_avil_day(String next_avil_day) {
            this.next_avil_day = next_avil_day;
        }

        public int getPrevious() {
            return previous;
        }

        public void setPrevious(int previous) {
            this.previous = previous;
        }

        public List<String> getDate_list() {
            return date_list;
        }

        public void setDate_list(List<String> date_list) {
            this.date_list = date_list;
        }

        public List<Slot> getAppt_slots() {
            return appt_slots;
        }

        public void setAppt_slots(List<Slot> appt_slots) {
            this.appt_slots = appt_slots;
        }
    }
