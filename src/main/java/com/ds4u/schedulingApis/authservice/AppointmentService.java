package com.ds4u.schedulingApis.authservice;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.ds4u.schedulingApis.dataModels.*;
import com.ds4u.schedulingApis.entity.PatientInfo;
import com.ds4u.schedulingApis.entity.Provider;
import com.ds4u.schedulingApis.requestmodel.Appointment;
import com.ds4u.schedulingApis.requestmodel.FormRequestModel;
import com.ds4u.schedulingApis.requestmodel.Patient;
import com.ds4u.schedulingApis.respository.BookingInfoRespository;
import com.ds4u.schedulingApis.respository.InsuranceRepository;
import com.ds4u.schedulingApis.respository.PatientInfoRepository;
import com.ds4u.schedulingApis.respository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private PatientInfoRepository patientInfoRepository;

    public Map<String, List<String>> getAvailableSlots(@RequestParam String provider, @RequestParam String date) {

        String fileName = "src/main/resources/slot.txt";
        File file = new File(fileName);
        Map<String, List<String>> availableSlots = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String slotDate = parts[0].trim();
                    String slotProvider = parts[1].trim();
                    if (slotDate.equals(date) && slotProvider.equals(provider)) {
                        List<String> slots = new ArrayList<>();

                        for (int i = 2; i < parts.length; i++) {
                            slots.add(parts[i].trim());
                        }
                        availableSlots.put(slotDate, slots);

                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return availableSlots;
    }

    public Map<String, List<Slot>> findSlotsByProviderAndDates(String providerId, List<String> dates) throws IOException {
//        List<Slot> slots = new ArrayList<>();
//        List<LocalDate> dates = dateStrings.stream()
//                .map(LocalDate::parse)
//                .collect(Collectors.toList());
//
//        try (Stream<String> lines = Files.lines(Paths.get("src/main/resources/slot.txt"))) {
//            slots = lines.skip(1) // Skip header line if present
//                    .map(line -> {
//                        String[] parts = line.split(",");
//                        String fileProviderId = parts[1].trim(); // Assuming provider name is at index 1
//                        LocalDate slotDate = LocalDate.parse(parts[0].trim()); // Parse slot date directly as LocalDate
//                        if (providerId.equals(fileProviderId) && dates.contains(slotDate)) {
//                            List<LocalTime> times = new ArrayList<>();
//                            for (int i = 2; i < parts.length; i++) {
//                                times.add(LocalTime.parse(parts[i].trim(), DateTimeFormatter.ofPattern("hh:mm:ss")));
//                            }
//                            return new Slot(providerId, slotDate, times);
//                        } else {
//                            return null;
//                        }
//                    })
//                    .filter(slot -> slot != null)
//                    .collect(Collectors.toList());
//        } catch (IOException e) {
//            e.printStackTrace(); // Handle file reading error
//        }
//        return slots;
//    }
        List<Slot> slots = new ArrayList<>();
        Map<String, List<Slot>> slotsMap = new HashMap<>();
//        LocalDate start = LocalDate.parse(startDate, DATE_FORMATTER);
//        LocalDate end = start.plusDays(6); // Calculate end date as start date + 6 days

        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/slot.txt"));

        for (String line : lines) {
            String[] parts = line.split(",");
            String date = parts[0];
            String storedProvider = parts[1];
            List<String> timeslots = List.of(parts).subList(2, parts.length);

            if (providerId.equals(storedProvider) && dates.contains(date)) {
                Slot slot = new Slot();
                slot.setStartDate(date);
                slot.setProviderId(providerId);
                slot.setTimes(timeslots);

                if (!slotsMap.containsKey(date)) {
                    slotsMap.put(date, new ArrayList<>());
                }
                slotsMap.get(date).add(slot);
            }
        }

        return slotsMap;
    }

    public ProviderSlot getSlotsForWeek(String startDate) throws IOException {
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate start = LocalDate.parse(startDate, DATE_FORMATTER);
        LocalDate end = start.plusDays(6); // Calculate end date as start date + 6 days

        List<String> dates = new ArrayList<>();
        LocalDate currentDate = start;
        while (!currentDate.isAfter(end)) {
            dates.add(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            currentDate = currentDate.plusDays(1);
        }
        List<Slot> apptSlots = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/slot.txt"));
        for (String line : lines) {
            String[] parts = line.split(",");
            String date = parts[0];
            String storedProvider = parts[1];
            List<String> timeslots = List.of(parts).subList(2, parts.length);

            apptSlots = dates.stream().map(day -> {
                Slot appointmentSlot = new Slot();
                appointmentSlot.setStartDate(date);
                appointmentSlot.setTimes(timeslots);


                List<Slot> slots = lines.stream()
                        .filter(linee -> line.startsWith(startDate))
                        .map(linee -> {
                            String[] part = line.split(",");
                            Slot slot = new Slot();
                            // slot.setStartDate(part[2]);
                            //slot.setStartTime(LocalDateTime.parse(parts[2])); // Assuming the first slot time
                            return slot;
                        })
                        .collect(Collectors.toList());

                appointmentSlot.setAppt_slots(slots);
                appointmentSlot.setMore(false);
                return appointmentSlot;
            }).collect(Collectors.toList());
        }
        ProviderSlot providerSlots = new ProviderSlot();
        providerSlots.setStatus("success");
        providerSlots.setToday_date(LocalDate.now().format(DATE_FORMATTER));
        providerSlots.setStart_date(start.format(DATE_FORMATTER));
        providerSlots.setEnd_date(end.format(DATE_FORMATTER));
        providerSlots.setDays(dates.size());
        providerSlots.setEnd_time("23:59:00");
        providerSlots.setNext_avil_day("");
        providerSlots.setPrevious(1);
        providerSlots.setDate_list(dates);
        providerSlots.setAppt_slots(apptSlots);

        return providerSlots;
    }

    public String bookAppointment(Patient patient) {

        try {
            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setMobilePhone(patient.getMobilePhone());
            patientInfo.setLastName(patient.getLastName());
            patientInfo.setDateOfBirth(patient.getDateOfBirth());
            patientInfo.setEmail(patient.getEmail());
            patientInfo.setGender(patient.getGender());
            patientInfo.setId(patient.getId());
            patientInfo.setAddressLine1(patient.getAddressLine1());
            patientInfo.setCityName(patient.getCityName());
            patientInfo.setAddressLine2(patient.getAddressLine2());
            patientInfo.setStateId(patient.getStateId());

            PatientInfo savedPatient = patientInfoRepository.save(patientInfo);
            if (!savedPatient.equals(patient)) {
                return "Appointment booked successfully!";
            } else {
                return "patient is booked";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Set patientInfo in bookingInfo
//        bookingInfo.setPatientInfo(savedPatient);
//
//        // Save booking information
//        bookingInfoRespository.save(bookingInfo);

        return "Appointment booked successfully!";
    }

    public HashMap<String, List<String>> getProvidersFromFile(String speciality) {
        List<String> providers = new ArrayList<>();


        HashMap<String, List<String>> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/providers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                System.out.println("parts" + parts);
                if (parts.length == 3 && parts[2].trim().equalsIgnoreCase(speciality.trim())) {
                    providers.add(parts[1].trim());
                    System.out.println("parts" + parts[1]);
                    System.out.println("providers:::" + providers);
                }
            }
            map.put("providers", providers);
            System.out.println("map" + map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;

    }

    public List<Provider> getProvidersList(String speciality, String providerName) {
        List<Provider> providers = new ArrayList<>();
        if (speciality != null && providerName != null) {
            providers = providerRepository.findBySpecialtyAndName(speciality, providerName);
        } else {
            providers = providerRepository.findBySpecialty(speciality);
        }

        return providers;
    }

    public List<PatientInfo> getPatientList(Long id) {
        List<String> providers = new ArrayList<>();

        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + id));

        System.out.println("provider" + provider.getName());
        String name = provider.getName();
        List<PatientInfo> patientInfo = patientInfoRepository.findByProvider(name);

//            if(name==patientInfo.)
//            System.out.println("patientInfo"+patientInfo);
//        }
        return patientInfo;
    }


    public Appointment bookAppointment(FormRequestModel request) {
        Appointment fhirAppointment = new Appointment();
        fhirAppointment.setStatus("proposed");
        fhirAppointment.setReason(new Appointment.Reason(request.getInfoForProvider()));
        fhirAppointment.setDescription("The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list.");
        fhirAppointment.setComment("Additional comments about the appointment.");

        Appointment.Contained patient = new Appointment.Contained();
        patient.setResourceType("Patient");
        patient.setId("patA");

        Appointment.Name name = new Appointment.Name();
        name.setUse("usual");
        name.setFamily(Collections.singletonList(request.getLastName()));
        name.setGiven(Collections.singletonList(request.getFirstName()));
        patient.setName(Collections.singletonList(name));

        Appointment.Telecom phone = new Appointment.Telecom();
        phone.setSystem("phone");
        phone.setValue(request.getPhoneNumber());
        phone.setUse("mobile");

        Appointment.Telecom email = new Appointment.Telecom();
        email.setSystem("email");
        email.setValue(request.getEmail());
        email.setUse("home");

        patient.setTelecom(Arrays.asList(phone, email));
        patient.setGender(request.getSex());
        patient.setBirthDate(request.getBirthDate());

        Appointment.Address address = new Appointment.Address();
        address.setUse("home");
        address.setType("both");
        address.setText("132, My Street, Kingston, NY, 12401, US");
        address.setLine(Arrays.asList("132", "My Street"));
        address.setCity("Kingston");
        address.setState("NY");
        address.setPostalCode("12401");
        address.setCountry("US");

        patient.setAddress(Collections.singletonList(address));

        Appointment.Contained coverage = new Appointment.Contained();
        coverage.setResourceType("Coverage");
        Appointment.Coding coding = new Appointment.Coding();
        coding.setSystem("eCW_insurance_coding_system(Cash/Insurance/Not-Applicable)");
        coding.setCode("insurance_code");
        coding.setDisplay("insurance name");
        Appointment.Type type = new Appointment.Type();
        type.setCoding(Collections.singletonList(coding));
        type.setText("insurance name");
        coverage.setType(type);

        fhirAppointment.setContained(Arrays.asList(patient, coverage));

        Appointment.Participant practitionerParticipant = new Appointment.Participant();
        Appointment.Actor practitionerActor = new Appointment.Actor("#patA");
        practitionerActor.setReference("Practitioner/1234564789");
        practitionerParticipant.setActor(practitionerActor);
        practitionerParticipant.setRequired("required");

        Appointment.Participant patientParticipant = new Appointment.Participant();
        Appointment.Actor patientActor = new Appointment.Actor("#patA");
        patientActor.setReference("#patA");
        patientParticipant.setActor(patientActor);
        patientParticipant.setRequired("required");

        fhirAppointment.setParticipant(Arrays.asList(practitionerParticipant, patientParticipant));

        return fhirAppointment;
    }
}

