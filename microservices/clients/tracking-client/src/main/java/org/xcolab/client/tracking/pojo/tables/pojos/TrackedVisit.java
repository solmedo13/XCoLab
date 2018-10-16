package org.xcolab.client.tracking.pojo.tables.pojos;

import org.xcolab.client.tracking.pojo.ITrackedVisit;

import java.sql.Timestamp;

public class TrackedVisit implements ITrackedVisit {

    private static final long serialVersionUID = -984486387;

    private Long id;
    private String visitorUuid;
    private String ip;
    private String city;
    private String country;
    private String url;
    private String browser;
    private String headers;
    private String referer;
    private Timestamp createdAt;

    public TrackedVisit() {}

    public TrackedVisit(TrackedVisit value) {
        this.id = value.id;
        this.visitorUuid = value.visitorUuid;
        this.ip = value.ip;
        this.city = value.city;
        this.country = value.country;
        this.url = value.url;
        this.browser = value.browser;
        this.headers = value.headers;
        this.referer = value.referer;
        this.createdAt = value.createdAt;
    }

    public TrackedVisit(Long id, String visitorUuid, String ip, String city, String country,
            String url, String browser, String headers, String referer, Timestamp createdAt) {
        this.id = id;
        this.visitorUuid = visitorUuid;
        this.ip = ip;
        this.city = city;
        this.country = country;
        this.url = url;
        this.browser = browser;
        this.headers = headers;
        this.referer = referer;
        this.createdAt = createdAt;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getVisitorUuid() {
        return this.visitorUuid;
    }

    @Override
    public void setVisitorUuid(String visitorUuid) {
        this.visitorUuid = visitorUuid;
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
    public String getUrl() {
        return this.url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getBrowser() {
        return this.browser;
    }

    @Override
    public void setBrowser(String browser) {
        this.browser = browser;
    }

    @Override
    public String getHeaders() {
        return this.headers;
    }

    @Override
    public void setHeaders(String headers) {
        this.headers = headers;
    }

    @Override
    public String getReferer() {
        return this.referer;
    }

    @Override
    public void setReferer(String referer) {
        this.referer = referer;
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
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        final TrackedVisit other = (TrackedVisit) obj;
        if (id == null) {
            if (other.id != null) { return false; }
        } else if (!id.equals(other.id)) { return false; }
        if (visitorUuid == null) {
            if (other.visitorUuid != null) { return false; }
        } else if (!visitorUuid.equals(other.visitorUuid)) { return false; }
        if (ip == null) {
            if (other.ip != null) { return false; }
        } else if (!ip.equals(other.ip)) { return false; }
        if (city == null) {
            if (other.city != null) { return false; }
        } else if (!city.equals(other.city)) { return false; }
        if (country == null) {
            if (other.country != null) { return false; }
        } else if (!country.equals(other.country)) { return false; }
        if (url == null) {
            if (other.url != null) { return false; }
        } else if (!url.equals(other.url)) { return false; }
        if (browser == null) {
            if (other.browser != null) { return false; }
        } else if (!browser.equals(other.browser)) { return false; }
        if (headers == null) {
            if (other.headers != null) { return false; }
        } else if (!headers.equals(other.headers)) { return false; }
        if (referer == null) {
            if (other.referer != null) { return false; }
        } else if (!referer.equals(other.referer)) { return false; }
        if (createdAt == null) {
            if (other.createdAt != null) { return false; }
        } else if (!createdAt.equals(other.createdAt)) { return false; }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((visitorUuid == null) ? 0 : visitorUuid.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((browser == null) ? 0 : browser.hashCode());
        result = prime * result + ((headers == null) ? 0 : headers.hashCode());
        result = prime * result + ((referer == null) ? 0 : referer.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TrackedVisit (");

        sb.append(id);
        sb.append(", ").append(visitorUuid);
        sb.append(", ").append(ip);
        sb.append(", ").append(city);
        sb.append(", ").append(country);
        sb.append(", ").append(url);
        sb.append(", ").append(browser);
        sb.append(", ").append(headers);
        sb.append(", ").append(referer);
        sb.append(", ").append(createdAt);

        sb.append(")");
        return sb.toString();
    }
}
