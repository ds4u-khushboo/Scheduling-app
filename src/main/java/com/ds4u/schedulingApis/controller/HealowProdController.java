package com.ds4u.schedulingApis.controller;

import com.ds4u.schedulingApis.response.ResultResponse;
import com.ds4u.schedulingApis.service.ECWProdService;
import com.ds4u.schedulingApis.service.HealowProdService;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class HealowProdController {
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Autowired
    private ECWProdService ecwProdService;

    @Autowired
    private HealowProdService healowProdService;

    @RequestMapping(value = "/getScheduleConnect", method = RequestMethod.GET)
    public ResultResponse getCombinedSlots(
            @RequestParam String actor,
            @RequestParam String date,
            @RequestParam String location,
            @RequestParam String slotType,
            @RequestParam String start,
            @RequestParam int count) {
        return ecwProdService.getCombinedResponse(actor, date, location, slotType, start, count);
    }

    @GetMapping("/liveSchedule")
    public String getSchedule(@RequestParam String actor, @RequestParam String date, @RequestParam String type, @RequestParam String identifier, @RequestParam Integer actorLocation) throws IOException {
        return healowProdService.getSchedule(actor, date, type, identifier, actorLocation);
    }

    @GetMapping("/liveSlot")
    public String getSlots(@RequestParam String schedule, String slotType, String start, Integer count) throws IOException {
        return healowProdService.getSlots(schedule, slotType, start, count);
    }

    @GetMapping("/liveCombineResponse")
    public JsonNode getSchedule(@RequestParam String actor, String date, String identifier, String type, String location, String slotType, String start, int count) throws IOException {

        return healowProdService.getSchedules(actor, date, identifier, type, location, slotType, start, count);
    }
}




