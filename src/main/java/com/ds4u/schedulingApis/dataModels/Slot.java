package com.ds4u.schedulingApis.dataModels;

import java.time.LocalDateTime;
import java.util.List;

public class Slot {
    private List<String> times;
    private String slotDate;
    private String providerId;
    private LocalDateTime startTime;


    private List<Slot> slots;

    private String endTime;

    public Slot()
    {}
    public Slot(String providerId, LocalDateTime startTime, String endTime) {
        this.providerId = providerId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public List<String> getTimes() {
        return times;
    }

    public String getSlotDate() {
        return slotDate;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public void setStartDate(String slotDate) {
        this.slotDate = slotDate;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setAppt_slots(List<Slot> slots) {
        this.slots = slots;

    }

    public void setMore(boolean b) {
    }
}
