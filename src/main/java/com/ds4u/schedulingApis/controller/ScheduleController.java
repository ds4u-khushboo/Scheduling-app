package com.ds4u.schedulingApis.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Schedule;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.ds4u.schedulingApis.configuration.ApiDevConfig;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ScheduleController {

    private ApiDevConfig apiDevConfig;

    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public Bundle getSchedule(String actor, String date, String type, String identifier, String actorLocation, String practiceCode) {

        if (actor == null || date == null || actorLocation == null) {
            throw new InvalidRequestException("Missing required parameters");
        }

        FhirContext ctx = FhirContext.forDstu2();
        IGenericClient genericClient = ctx.newRestfulGenericClient(apiDevConfig.getApiScheduleDevUrl());
        genericClient.search().forResource("Schedule").where(Schedule.ACTOR.hasId(actor));
        Bundle bundle = new Bundle();
        bundle.setId(new IdDt("7cec7ab4-c77e-4d54-a6eb-865562f07dc3"));
        bundle.setTotal(1);
        Bundle.Link link = bundle.addLink();
        link.setRelation("self");
        link.setUrl("https://azuhealow-preprod.healow.com/apps/api/v1/fhir/IFDECD/fhir/Schedule?actor=" + actor
                + "&actor.location=" + actorLocation + "&date=" + date + "&identifier=" + identifier
                + "&practice_code=" + practiceCode + "&type=" + type);
        Bundle.Entry entry = bundle.addEntry();
        Schedule schedule = new Schedule();
        schedule.setId("6956d137-4574-4d42-ab1a-5c109a1f9b93");
        schedule.setActor(new ResourceReferenceDt("2002001592"));
        schedule.setPlanningHorizon(new PeriodDt().setStart(new DateTimeDt("2024-06-10T00:00:00-04:00")));
        schedule.setPlanningHorizon(new PeriodDt().setEnd(new DateTimeDt("2024-06-10T00:00:00-04:00")));
        entry.setResource(schedule).setFullUrl("urn:uuid:" + schedule.getIdElement().getIdPart());
        bundle.addEntry(entry);
        String bundleJson = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
        System.out.println(bundleJson);
        return bundle;
    }
}
