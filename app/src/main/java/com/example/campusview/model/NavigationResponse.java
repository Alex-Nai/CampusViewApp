package com.example.campusview.model;

import java.util.List;

public class NavigationResponse {
    private boolean success;
    private String message;
    private List<NavigationStep> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NavigationStep> getData() {
        return data;
    }

    public void setData(List<NavigationStep> data) {
        this.data = data;
    }

    public static class NavigationStep {
        private String instruction;
        private double distance;
        private double duration;
        private List<LatLng> path;

        public String getInstruction() {
            return instruction;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public double getDuration() {
            return duration;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }

        public List<LatLng> getPath() {
            return path;
        }

        public void setPath(List<LatLng> path) {
            this.path = path;
        }

        public static class LatLng {
            private double latitude;
            private double longitude;

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }
        }
    }
} 