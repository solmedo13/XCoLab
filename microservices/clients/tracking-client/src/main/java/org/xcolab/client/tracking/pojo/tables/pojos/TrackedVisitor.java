package org.xcolab.client.tracking.pojo.tables.pojos;

import org.xcolab.client.tracking.pojo.ITrackedVisitor;

import java.sql.Timestamp;

public class TrackedVisitor implements ITrackedVisitor {

    private static final long serialVersionUID = -1101057440;

    private String uuid;
    private Long userId;
    private Timestamp createdAt;

    public TrackedVisitor() {}

    public TrackedVisitor(TrackedVisitor value) {
        this.uuid = value.uuid;
        this.userId = value.userId;
        this.createdAt = value.createdAt;
    }

    public TrackedVisitor(String uuid, Long userId, Timestamp createdAt) {
        this.uuid = uuid;
        this.userId = userId;
        this.createdAt = createdAt;
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
    public Long getUserId() {
        return this.userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
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
        final TrackedVisitor other = (TrackedVisitor) obj;
        if (uuid == null) {
            if (other.uuid != null) { return false; }
        } else if (!uuid.equals(other.uuid)) { return false; }
        if (userId == null) {
            if (other.userId != null) { return false; }
        } else if (!userId.equals(other.userId)) { return false; }
        if (createdAt == null) {
            if (other.createdAt != null) { return false; }
        } else if (!createdAt.equals(other.createdAt)) { return false; }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TrackedVisitor (");

        sb.append(uuid);
        sb.append(", ").append(userId);
        sb.append(", ").append(createdAt);

        sb.append(")");
        return sb.toString();
    }
}
