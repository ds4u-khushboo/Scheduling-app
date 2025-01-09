package com.ds4u.schedulingApis.configuration;

import com.ds4u.schedulingApis.service.HealowDevService;
import com.ds4u.schedulingApis.service.HealowProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PrefetchConfiguration {

    private final HealowProdService slotService;

    @Autowired
    ApiLiveConfig apiLiveConfig;

    public PrefetchConfiguration(HealowProdService slotService) {
        this.slotService = slotService;
    }

    @Scheduled(fixedRate = 3600000) // Run every hour
    public void prefetchSlotData() throws IOException {
        String actor= apiLiveConfig.getActorLiveNpi();
        String identifier="none";
        String type="none";
        String date="";
        String location= apiLiveConfig.getActorLiveLocation();
        String start=date;
        String slotType="api1";
//        String star
        // Define scheduleId, slotType, start, and count parameters for prefetching
   //     String scheduleId = "exampleScheduleId"; // Replace with actual schedule ID
       // String slotType = apiLiveConfig.    // Replace with actual slot type
    //    String start = "exampleStart";           // Replace with actual start
        int count = 10;                          // Replace with actual count

        slotService.prefetchAndCacheSlots(actor, date,identifier, type, location, start, slotType, count);
    }
}
