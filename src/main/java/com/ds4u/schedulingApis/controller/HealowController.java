package com.ds4u.schedulingApis.controller;

import ca.uhn.fhir.model.dstu2.resource.Schedule;
import com.ds4u.schedulingApis.authservice.FhirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RestControllerAdvice
public class HealowController {

    @Value("fhir.server.url")
    public String fhirUrl;

    @Autowired
    private FhirService fhirService;

    @GetMapping(value = "/search")
    public String searchSChedules(@RequestParam String actor, @RequestParam String type, @RequestParam String date, @RequestParam String identifier, @RequestParam String actorLocation) {
        return fhirService.searchSchedulesByActor(actor, type, date, identifier, actorLocation);
    }

    @GetMapping("/searchSlots")
    public String searchSlots(@RequestParam String schedule, @RequestParam String slotType, @RequestParam String start, @RequestParam Integer count) {

        return fhirService.searchSlotsBySchedule(schedule, slotType, start, count);
    }

    @PostMapping(value = "/schedules", consumes = {"application/json+fhir"}, produces = {"application/json+fhir"})
    public String createSchedules(@RequestBody Schedule schedule, @RequestParam String PracticeCode) {
        try {
            return fhirService.bookAppointment(schedule, PracticeCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/fetch")
    public List<String> fetchtime() {
        try {
            return fhirService.fetchStartTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
