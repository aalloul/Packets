package com.example.aalloul.packets;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class will make it very easy to use gson.toJSON as it contains the types and all
 * for the moment I won't be using it as it requires quite some re-writing and re-factoring
 * Sore other day!
 */

class FragActions {

    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("number_packages")
    @Expose
    private Integer numberPackages;
    @SerializedName("drop_off_postalCode")
    @Expose
    private String dropOffPostalCode;
    @SerializedName("drop_off_country")
    @Expose
    private String dropOffCountry;
    @SerializedName("fragDuration")
    @Expose
    private Object fragDuration;
    @SerializedName("pickup_longitude")
    @Expose
    private Integer pickupLongitude;
    @SerializedName("pickup_postalCode")
    @Expose
    private String pickupPostalCode;
    @SerializedName("pickup_city")
    @Expose
    private String pickupCity;
    @SerializedName("drop_off_longitude")
    @Expose
    private Integer dropOffLongitude;
    @SerializedName("pickup_country")
    @Expose
    private String pickupCountry;
    @SerializedName("size_packages")
    @Expose
    private String sizePackages;
    @SerializedName("fragEnd")
    @Expose
    private Object fragEnd;
    @SerializedName("drop_off_address")
    @Expose
    private String dropOffAddress;
    @SerializedName("pickup_latitude")
    @Expose
    private Integer pickupLatitude;
    @SerializedName("pickup_state")
    @Expose
    private String pickupState;
    @SerializedName("fragStart")
    @Expose
    private Object fragStart;
    @SerializedName("pickup_date")
    @Expose
    private Object pickupDate;
    @SerializedName("drop_off_city")
    @Expose
    private String dropOffCity;
    @SerializedName("nextFrag")
    @Expose
    private String nextFrag;
    @SerializedName("fragActions")
    @Expose
    private String fragActions;
    @SerializedName("pickup_location_edited")
    @Expose
    private Boolean pickupLocationEdited;
    @SerializedName("drop_off_latitude")
    @Expose
    private Integer dropOffLatitude;

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public Integer getNumberPackages() {
        return numberPackages;
    }

    public void setNumberPackages(Integer numberPackages) {
        this.numberPackages = numberPackages;
    }

    public String getDropOffPostalCode() {
        return dropOffPostalCode;
    }

    public void setDropOffPostalCode(String dropOffPostalCode) {
        this.dropOffPostalCode = dropOffPostalCode;
    }

    public String getDropOffCountry() {
        return dropOffCountry;
    }

    public void setDropOffCountry(String dropOffCountry) {
        this.dropOffCountry = dropOffCountry;
    }

    public Object getFragDuration() {
        return fragDuration;
    }

    public void setFragDuration(Object fragDuration) {
        this.fragDuration = fragDuration;
    }

    public Integer getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(Integer pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getPickupPostalCode() {
        return pickupPostalCode;
    }

    public void setPickupPostalCode(String pickupPostalCode) {
        this.pickupPostalCode = pickupPostalCode;
    }

    public String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public Integer getDropOffLongitude() {
        return dropOffLongitude;
    }

    public void setDropOffLongitude(Integer dropOffLongitude) {
        this.dropOffLongitude = dropOffLongitude;
    }

    public String getPickupCountry() {
        return pickupCountry;
    }

    public void setPickupCountry(String pickupCountry) {
        this.pickupCountry = pickupCountry;
    }

    public String getSizePackages() {
        return sizePackages;
    }

    public void setSizePackages(String sizePackages) {
        this.sizePackages = sizePackages;
    }

    public Object getFragEnd() {
        return fragEnd;
    }

    public void setFragEnd(Object fragEnd) {
        this.fragEnd = fragEnd;
    }

    public String getDropOffAddress() {
        return dropOffAddress;
    }

    public void setDropOffAddress(String dropOffAddress) {
        this.dropOffAddress = dropOffAddress;
    }

    public Integer getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(Integer pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public String getPickupState() {
        return pickupState;
    }

    public void setPickupState(String pickupState) {
        this.pickupState = pickupState;
    }

    public Object getFragStart() {
        return fragStart;
    }

    public void setFragStart(Object fragStart) {
        this.fragStart = fragStart;
    }

    public Object getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Object pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getDropOffCity() {
        return dropOffCity;
    }

    public void setDropOffCity(String dropOffCity) {
        this.dropOffCity = dropOffCity;
    }

    public String getNextFrag() {
        return nextFrag;
    }

    public void setNextFrag(String nextFrag) {
        this.nextFrag = nextFrag;
    }

    public String getFragActions() {
        return fragActions;
    }

    public void setFragActions(String fragActions) {
        this.fragActions = fragActions;
    }

    public Boolean getPickupLocationEdited() {
        return pickupLocationEdited;
    }

    public void setPickupLocationEdited(Boolean pickupLocationEdited) {
        this.pickupLocationEdited = pickupLocationEdited;
    }

    public Integer getDropOffLatitude() {
        return dropOffLatitude;
    }

    public void setDropOffLatitude(Integer dropOffLatitude) {
        this.dropOffLatitude = dropOffLatitude;
    }

}