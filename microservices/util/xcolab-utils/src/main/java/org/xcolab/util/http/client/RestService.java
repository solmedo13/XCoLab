package org.xcolab.util.http.client;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import org.xcolab.util.http.UriProvider;
import org.xcolab.util.http.client.interfaces.HttpEndpoint;

import java.util.Objects;

public class RestService implements HttpEndpoint {

    private static final String SCHEMA = "http://";

    private final String serviceName;
    private final UriProvider uriProvider;
    private final String namespace;

    public RestService(CoLabService serviceName, String namespace) {
        this(serviceName.getServiceName(), namespace);
    }

    public RestService(String serviceName, String namespace) {
        Assert.notNull(serviceName, "Service name is required");
        Assert.notNull(serviceName, "Namespace is required");
        this.namespace = namespace;
        this.serviceName = serviceName;
        this.uriProvider = getBaseUrl(namespace);
    }

    public UriProvider getBaseUrl(String namespace) {
        return new UriProvider(SCHEMA + namespace + "-" + serviceName);
    }

    /**
     * Creates a new RestService instance that is identical to this one except for the new service
     * name.
     *
     * Has to be overridden in child classes to copy all other internal state, if present.
     *
     * @param serviceName The new service name
     * @return a copy of this instance with a different service name
     */
    public RestService withServiceName(String serviceName) {
        return new RestService(serviceName, namespace);
    }

    @Override
    public UriProvider getBaseUrl() {
        return uriProvider;
    }

    @Override
    public int hashCode() {
        return serviceName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RestService)) {
            return false;
        }
        final RestService other = (RestService) obj;
        return Objects.equals(serviceName, other.serviceName)
                && Objects.equals(namespace, other.namespace);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("baseUrl", getBaseUrl())
                .toString();
    }
}
