package com.uber.driver.onboarding.core.repository.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "driver_info")
public class DriverInfo {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(updatable = false, length = 36)
    private String id;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "ready_to_ride", columnDefinition = "BIT")
    private boolean readyToRide;

    @Column(name = "received_lc", columnDefinition = "BIT")
    private boolean receivedLC;

    @Column(name = "received_rc", columnDefinition = "BIT")
    private boolean receivedRC;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public boolean isReadyToRide() {
        return readyToRide;
    }

    public boolean isReceivedLC() {
        return receivedLC;
    }

    public void setReceivedLC(boolean receivedLC) {
        this.receivedLC = receivedLC;
    }

    public boolean isReceivedRC() {
        return receivedRC;
    }

    public void setReceivedRC(boolean receivedRC) {
        this.receivedRC = receivedRC;
    }

    public void setReadyToRide(boolean readyToRide) {
        this.readyToRide = readyToRide;
    }

    @Override
    public String toString() {
        return "DriverInfo{" +
                "id='" + id + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", readyToRide=" + readyToRide +
                ", receivedLC=" + receivedLC +
                ", receivedRC=" + receivedRC +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DriverInfo that = (DriverInfo) o;
        return readyToRide == that.readyToRide &&
                receivedLC == that.receivedLC &&
                receivedRC == that.receivedRC &&
                Objects.equals(id, that.id) &&
                Objects.equals(licenseNumber, that.licenseNumber) &&
                Objects.equals(vehicleNumber, that.vehicleNumber) &&
                Objects.equals(registrationNumber, that.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, licenseNumber, vehicleNumber, registrationNumber, readyToRide, receivedLC, receivedRC);
    }
}
