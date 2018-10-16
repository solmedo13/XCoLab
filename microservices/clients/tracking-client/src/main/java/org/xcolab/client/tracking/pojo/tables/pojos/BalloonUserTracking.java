package org.xcolab.client.tracking.pojo.tables.pojos;

import org.xcolab.client.tracking.pojo.IBalloonUserTracking;

import java.sql.Timestamp;

public class BalloonUserTracking implements IBalloonUserTracking {

    private static final long serialVersionUID = 1869101588;

    private String uuid;
    private String email;
    private String parent;
    private String ip;
    private Timestamp createdAt;
    private Timestamp registrationDate;
    private Timestamp formFiledDate;
    private Long userId;
    private Long balloonTextId;
    private String referrer;
    private Double latitude;
    private Double longitude;
    private String city;
    private String country;
    private String extraData;
    private String balloonLinkUuid;
    private String balloonLinkContext;
    private String userAgent;

    public BalloonUserTracking() {}

    public BalloonUserTracking(BalloonUserTracking value) {
        this.uuid = value.uuid;
        this.email = value.email;
        this.parent = value.parent;
        this.ip = value.ip;
        this.createdAt = value.createdAt;
        this.registrationDate = value.registrationDate;
        this.formFiledDate = value.formFiledDate;
        this.userId = value.userId;
        this.balloonTextId = value.balloonTextId;
        this.referrer = value.referrer;
        this.latitude = value.latitude;
        this.longitude = value.longitude;
        this.city = value.city;
        this.country = value.country;
        this.extraData = value.extraData;
        this.balloonLinkUuid = value.balloonLinkUuid;
        this.balloonLinkContext = value.balloonLinkContext;
        this.userAgent = value.userAgent;
    }

    public BalloonUserTracking(String uuid, String email, String parent, String ip,
            Timestamp createdAt, Timestamp registrationDate, Timestamp formFiledDate, Long userId,
            Long balloonTextId, String referrer, Double latitude, Double longitude, String city,
            String country, String extraData, String balloonLinkUuid, String balloonLinkContext,
            String userAgent) {
        this.uuid = uuid;
        this.email = email;
        this.parent = parent;
        this.ip = ip;
        this.createdAt = createdAt;
        this.registrationDate = registrationDate;
        this.formFiledDate = formFiledDate;
        this.userId = userId;
        this.balloonTextId = balloonTextId;
        this.referrer = referrer;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
        this.extraData = extraData;
        this.balloonLinkUuid = balloonLinkUuid;
        this.balloonLinkContext = balloonLinkContext;
        this.userAgent = userAgent;
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getParent() {
        return this.parent;
    }

    @Override
    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String getIp() {
        return this.ip;
    }

    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Timestamp getRegistrationDate() {
        return this.registrationDate;
    }

    @Override
    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public Timestamp getFormFiledDate() {
        return this.formFiledDate;
    }

    @Override
    public void setFormFiledDate(Timestamp formFiledDate) {
        this.formFiledDate = formFiledDate;
    }

    @Override
    public Long getUserId() {
        return this.userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public Long getBalloonTextId() {
        return this.balloonTextId;
    }

    @Override
    public void setBalloonTextId(Long balloonTextId) {
        this.balloonTextId = balloonTextId;
    }

    @Override
    public String getReferrer() {
        return this.referrer;
    }

    @Override
    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    @Override
    public Double getLatitude() {
        return this.latitude;
    }

    @Override
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public Double getLongitude() {
        return this.longitude;
    }

    @Override
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getCountry() {
        return this.country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String getExtraData() {
        return this.extraData;
    }

    @Override
    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    @Override
    public String getBalloonLinkUuid() {
        return this.balloonLinkUuid;
    }

    @Override
    public void setBalloonLinkUuid(String balloonLinkUuid) {
        this.balloonLinkUuid = balloonLinkUuid;
    }

    @Override
    public String getBalloonLinkContext() {
        return this.balloonLinkContext;
    }

    @Override
    public void setBalloonLinkContext(String balloonLinkContext) {
        this.balloonLinkContext = balloonLinkContext;
    }

    @Override
    public String getUserAgent() {
        return this.userAgent;
    }

    @Override
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        final BalloonUserTracking other = (BalloonUserTracking) obj;
        if (uuid == null) {
            if (other.uuid != null) { return false; }
        } else if (!uuid.equals(other.uuid)) { return false; }
        if (email == null) {
            if (other.email != null) { return false; }
        } else if (!email.equals(other.email)) { return false; }
        if (parent == null) {
            if (other.parent != null) { return false; }
        } else if (!parent.equals(other.parent)) { return false; }
        if (ip == null) {
            if (other.ip != null) { return false; }
        } else if (!ip.equals(other.ip)) { return false; }
        if (createdAt == null) {
            if (other.createdAt != null) { return false; }
        } else if (!createdAt.equals(other.createdAt)) { return false; }
        if (registrationDate == null) {
            if (other.registrationDate != null) { return false; }
        } else if (!registrationDate.equals(other.registrationDate)) { return false; }
        if (formFiledDate == null) {
            if (other.formFiledDate != null) { return false; }
        } else if (!formFiledDate.equals(other.formFiledDate)) { return false; }
        if (userId == null) {
            if (other.userId != null) { return false; }
        } else if (!userId.equals(other.userId)) { return false; }
        if (balloonTextId == null) {
            if (other.balloonTextId != null) { return false; }
        } else if (!balloonTextId.equals(other.balloonTextId)) { return false; }
        if (referrer == null) {
            if (other.referrer != null) { return false; }
        } else if (!referrer.equals(other.referrer)) { return false; }
        if (latitude == null) {
            if (other.latitude != null) { return false; }
        } else if (!latitude.equals(other.latitude)) { return false; }
        if (longitude == null) {
            if (other.longitude != null) { return false; }
        } else if (!longitude.equals(other.longitude)) { return false; }
        if (city == null) {
            if (other.city != null) { return false; }
        } else if (!city.equals(other.city)) { return false; }
        if (country == null) {
            if (other.country != null) { return false; }
        } else if (!country.equals(other.country)) { return false; }
        if (extraData == null) {
            if (other.extraData != null) { return false; }
        } else if (!extraData.equals(other.extraData)) { return false; }
        if (balloonLinkUuid == null) {
            if (other.balloonLinkUuid != null) { return false; }
        } else if (!balloonLinkUuid.equals(other.balloonLinkUuid)) { return false; }
        if (balloonLinkContext == null) {
            if (other.balloonLinkContext != null) { return false; }
        } else if (!balloonLinkContext.equals(other.balloonLinkContext)) { return false; }
        if (userAgent == null) {
            if (other.userAgent != null) { return false; }
        } else if (!userAgent.equals(other.userAgent)) { return false; }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((registrationDate == null) ? 0 : registrationDate.hashCode());
        result = prime * result + ((formFiledDate == null) ? 0 : formFiledDate.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((balloonTextId == null) ? 0 : balloonTextId.hashCode());
        result = prime * result + ((referrer == null) ? 0 : referrer.hashCode());
        result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
        result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((extraData == null) ? 0 : extraData.hashCode());
        result = prime * result + ((balloonLinkUuid == null) ? 0 : balloonLinkUuid.hashCode());
        result =
                prime * result + ((balloonLinkContext == null) ? 0 : balloonLinkContext.hashCode());
        result = prime * result + ((userAgent == null) ? 0 : userAgent.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BalloonUserTracking (");

        sb.append(uuid);
        sb.append(", ").append(email);
        sb.append(", ").append(parent);
        sb.append(", ").append(ip);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(registrationDate);
        sb.append(", ").append(formFiledDate);
        sb.append(", ").append(userId);
        sb.append(", ").append(balloonTextId);
        sb.append(", ").append(referrer);
        sb.append(", ").append(latitude);
        sb.append(", ").append(longitude);
        sb.append(", ").append(city);
        sb.append(", ").append(country);
        sb.append(", ").append(extraData);
        sb.append(", ").append(balloonLinkUuid);
        sb.append(", ").append(balloonLinkContext);
        sb.append(", ").append(userAgent);

        sb.append(")");
        return sb.toString();
    }
}
