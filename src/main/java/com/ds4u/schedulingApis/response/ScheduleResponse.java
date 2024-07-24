package com.ds4u.schedulingApis.response;

import java.util.List;

public class ScheduleResponse {
    private String resourceType;
    private String id;
    private Meta meta;
    private List<Link> link;
    private List<Entry> entry;

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
// Getters and setters

    public static class Meta {
        private String lastUpdated;

        public String getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
// Getters and setters
    }

    public static class Link {
        private String relation;
        private String url;

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
// Getters and setters
    }

    public static class Entry {
        private String fullUrl;
        private Resource resource;

        public String getFullUrl() {
            return fullUrl;
        }

        public void setFullUrl(String fullUrl) {
            this.fullUrl = fullUrl;
        }

        public Resource getResource() {
            return resource;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }
// Getters and setters

        public static class Resource {
            private String resourceType;
            private String id;
            private Actor actor;
            private PlanningHorizon planningHorizon;

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

            public Actor getActor() {
                return actor;
            }

            public void setActor(Actor actor) {
                this.actor = actor;
            }

            public PlanningHorizon getPlanningHorizon() {
                return planningHorizon;
            }

            public void setPlanningHorizon(PlanningHorizon planningHorizon) {
                this.planningHorizon = planningHorizon;
            }
// Getters and setters

            public static class Actor {
                private String reference;

                // Getters and setters
            }

            public static class PlanningHorizon {
                private String start;
                private String end;

                public String getStart() {
                    return start;
                }

                public void setStart(String start) {
                    this.start = start;
                }

                public String getEnd() {
                    return end;
                }

                public void setEnd(String end) {
                    this.end = end;
                }
// Getters and setters
            }
        }
    }
}
