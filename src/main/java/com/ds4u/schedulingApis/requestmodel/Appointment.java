package com.ds4u.schedulingApis.requestmodel;

import java.util.List;

public class Appointment {
    private String resourceType;
    private List<Contained> contained;
    private String status;
    private Reason reason;
    private String description;
    private List<Slot> slot;
    private String comment;
    private List<Participant> participant;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public List<Contained> getContained() {
        return contained;
    }

    public void setContained(List<Contained> contained) {
        this.contained = contained;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Slot> getSlot() {
        return slot;
    }

    public void setSlot(List<Slot> slot) {
        this.slot = slot;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Participant> getParticipant() {
        return participant;
    }

    public void setParticipant(List<Participant> participant) {
        this.participant = participant;
    }

    public static class Contained {
        private String resourceType;
        private String id;
        private List<Name> name;
        private List<Telecom> telecom;
        private String gender;
        private String birthDate;
        private List<Address> address;
        private Type type;

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Name> getName() {
            return name;
        }

        public void setName(List<Name> name) {
            this.name = name;
        }

        public List<Telecom> getTelecom() {
            return telecom;
        }

        public void setTelecom(List<Telecom> telecom) {
            this.telecom = telecom;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(String birthDate) {
            this.birthDate = birthDate;
        }

        public List<Address> getAddress() {
            return address;
        }

        public void setAddress(List<Address> address) {
            this.address = address;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }
// Getters and setters omitted for brevity
    }

    public static class Name {
        private String use;
        private List<String> family;
        private List<String> given;

        public Name(String use, List<String> family, List<String> given) {
            this.use = use;
            this.family = family;
            this.given = given;
        }

        public Name() {

        }

        public String getUse() {
            return use;
        }

        public void setUse(String use) {
            this.use = use;
        }

        public List<String> getFamily() {
            return family;
        }

        public void setFamily(List<String> family) {
            this.family = family;
        }

        public List<String> getGiven() {
            return given;
        }

        public void setGiven(List<String> given) {
            this.given = given;
        }
// Getters and setters omitted for brevity
    }

    public static class Telecom {
        private String system;
        private String value;
        private String use;

        public Telecom(String system, String value, String use) {
            this.system = system;
            this.value = value;
            this.use = use;
        }

        public Telecom() {

        }

        public String getSystem() {
            return system;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUse() {
            return use;
        }

        public void setUse(String use) {
            this.use = use;
        }
// Getters and setters omitted for brevity
    }

    public static class Address {
        private String use;
        private String type;
        private String text;
        private List<String> line;
        private String city;
        private String state;
        private String postalCode;
        private String country;

        public String getUse() {
            return use;
        }

        public void setUse(String use) {
            this.use = use;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<String> getLine() {
            return line;
        }

        public void setLine(List<String> line) {
            this.line = line;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
// Getters and setters omitted for brevity
    }

    public static class Type {
        private List<Coding> coding;
        private String text;

        public Type(List<Coding> coding, String text) {
            this.coding = coding;
            this.text = text;
        }

        public Type() {

        }

        public List<Coding> getCoding() {
            return coding;
        }

        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
// Getters and setters omitted for brevity
    }

    public static class Coding {
        private String system;
        private String code;
        private String display;

        public Coding(String system, String code, String display) {
            this.system = system;
            this.code = code;
            this.display = display;
        }

        public Coding() {

        }

        public String getSystem() {
            return system;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
// Getters and setters omitted for brevity
    }

    public static class Reason {
        private String text;

        public Reason(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
// Getters and setters omitted for brevity
    }

    public static class Slot {
        private String reference;

        public Slot(String reference) {
            this.reference = reference;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }
// Getters and setters omitted for brevity
    }

    public static class Participant {
        private Actor actor;
        private String required;

        public Actor getActor() {
            return actor;
        }

        public void setActor(Actor actor) {
            this.actor = actor;
        }

        public String getRequired() {
            return required;
        }

        public void setRequired(String required) {
            this.required = required;
        }
// Getters and setters omitted for brevity
    }

    public static class Actor {
        private String reference;

        public Actor(String hashtag) {
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }
// Getters and setters omitted for brevity
    }
}
