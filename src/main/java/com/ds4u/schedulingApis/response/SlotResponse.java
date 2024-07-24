package com.ds4u.schedulingApis.response;

import java.util.List;

public class SlotResponse {
    private String resourceType;
    private String id;
    private Meta meta;
    private List<Link> link;
    private List<Entry> entry;

    // Getters and setters

    public static class Meta {
        private String lastUpdated;

        // Getters and setters
    }

    public static class Link {
        private String relation;
        private String url;

        // Getters and setters
    }

    public static class Entry {
        private String fullUrl;
        private Resource resource;

        // Getters and setters

        public static class Resource {
            private String resourceType;
            private String id;
            private Type type;
            private Schedule schedule;
            private String freeBusyType;
            private String start;
            private String end;

            // Getters and setters

            public static class Type {
                private List<Coding> coding;

                // Getters and setters

                public static class Coding {
                    private String system;

                    // Getters and setters
                }
            }

            public static class Schedule {
                private String reference;
            }
        }
    }

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

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Link> getLink() {
        return link;
    }

    public void setLink(List<Link> link) {
        this.link = link;
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }
}
