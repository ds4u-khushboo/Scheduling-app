package com.ds4u.schedulingApis.response;

public class ResultResponse {
        private ScheduleResponse scheduleResponse;
        private SlotResponse slotResponse;

    public ScheduleResponse getScheduleResponse() {
        return scheduleResponse;
    }

    public void setScheduleResponse(ScheduleResponse scheduleResponse) {
        this.scheduleResponse = scheduleResponse;
    }

    public SlotResponse getSlotResponse() {
        return slotResponse;
    }

    public void setSlotResponse(SlotResponse slotResponse) {
        this.slotResponse = slotResponse;
    }
}
