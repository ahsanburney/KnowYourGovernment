package com.example.kulsum.knowyourgovernment;


import java.io.Serializable;

public class Official implements Serializable {
    private static final String TAG = "official";
    private String topLocation;
    private String officialDetails;
    private String officialName;
    private String line1;
    private String line2;
    private String line3;
    private String city;
    private String state;
    private String zip;
    private String side;
    private String phone;
    private String url;
    private String email;
    private String imageUrl;
    private String facebook;
    private String twitter;
    private String youtube;
    private String googleplus;


    public Official(String officialDetails, String officialName, String line1, String line2, String line3, String city, String state,
                    String zip, String side, String phone, String email, String url, String imageUrl, String topLocation,
                    String googleplus, String facebook, String twitter, String youtube) {
        this.officialDetails = officialDetails;
        this.officialName = officialName;
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.side = side;
        this.phone = phone;
        this.url = url;
        this.email =email;
        this.imageUrl = imageUrl;
        this.googleplus = googleplus;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
        this.topLocation = topLocation;
    }

    public String getOfficialDetails() {
        return officialDetails;
    }
    public String getOfficialName() {
        return officialName;
    }
    public String getLine1() {
        return line1;
    }
    public String getLine2() {
        return line2;
    }
    public String getLine3() {
        return line3;
    }
    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }
    public String getZip() {
        return zip;
    }
    public String getPhone() {
        return phone;
    }
    public String getSide() {
        return side;
    }
    public String getUrl() {
        return url;
    }
    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getTopLocation() {
        return topLocation;
    }
    public String getGooglePlus() {
        return googleplus;
    }
    public String getFacebook() {
        return facebook;
    }
    public String getTwitter() {
        return twitter;
    }
    public String getYouTube() {
        return youtube;
    }

    public void setTopLocation(String topLocation) {
        this.topLocation = topLocation;
    }

    public void setOfficialDetails(String officialDetails) {
        this.officialDetails = officialDetails;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public void setGoogleplus(String googleplus) {
        this.googleplus = googleplus;
    }
}
