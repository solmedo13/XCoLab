package org.xcolab.util;

import net.spy.memcached.MemcachedClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.xcolab.util.exceptions.EntityNotFoundException;
import org.xcolab.util.exceptions.ServiceNotFoundException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public final class RequestUtils {

    private static final int MEMCACHED_TIMEOUT = 3;

    private static final RestTemplate restTemplate = new RestTemplate();

    private static MemcachedClient memcached;

    static {
        try {
            memcached = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        } catch (IOException ignored) {
            memcached = null;
        }
    }

    private RequestUtils() {
    }

    public static <T> T getFirstFromList(UriComponentsBuilder uriBuilder,
            ParameterizedTypeReference<List<T>> typeReference) throws EntityNotFoundException {
        return getFirstFromList(uriBuilder, typeReference, null);
    }

    public static <T> T getFirstFromList(UriComponentsBuilder uriBuilder,
            ParameterizedTypeReference<List<T>> typeReference, String cacheQueryIdentifier)
            throws EntityNotFoundException {
        uriBuilder.queryParam("startRecord", 0);
        uriBuilder.queryParam("limitRecord", 1);

        final boolean cacheActive = memcached != null && cacheQueryIdentifier != null;
        final String cachePrefix = "_" + typeReference.getType() + "_listFirst_";
        T ret;
        if (cacheActive) {
            //noinspection unchecked
            ret = (T) memcached.get(cachePrefix + cacheQueryIdentifier);
            if (ret != null) {
                return ret;
            }
        }

        final List<T> list = getList(uriBuilder, typeReference);
        if (list.isEmpty()) {
            throw new EntityNotFoundException();
        }
        ret = list.get(0);

        if (cacheActive) {
            memcached.add(cachePrefix + cacheQueryIdentifier, MEMCACHED_TIMEOUT, ret);
        }

        return ret;
    }

    public static <T> List<T> getList(UriComponentsBuilder uriBuilder,
            ParameterizedTypeReference<List<T>> typeReference) {
        return getList(uriBuilder, typeReference, null);
    }

    public static <T> List<T> getList(UriComponentsBuilder uriBuilder,
            ParameterizedTypeReference<List<T>> typeReference,
            String cacheQueryIdentifier) {
        List<T> ret;
        final boolean cacheActive = memcached != null && cacheQueryIdentifier != null;
        final String cachePrefix = "_" + typeReference.getType() + "_list_";
        if (cacheActive) {
            //noinspection unchecked
            ret = (List<T>) memcached.get(cachePrefix + cacheQueryIdentifier);
            if (ret != null) {
                return ret;
            }
        }
        ResponseEntity<List<T>> response = restTemplate.exchange(uriBuilder.build().toString(),
                HttpMethod.GET, null, typeReference);
        ret = response.getBody();

        if (cacheActive) {
            memcached.add(cachePrefix + cacheQueryIdentifier, MEMCACHED_TIMEOUT, ret);
        }
        return ret;
    }

    public static <T> T get(UriComponentsBuilder uriBuilder, Class<T> entityType)
            throws EntityNotFoundException {
        return get(uriBuilder, entityType, null);
    }

    public static <T> T get(UriComponentsBuilder uriBuilder, Class<T> entityType,
            String cacheQueryIdentifier)
            throws EntityNotFoundException {
        try {
            return getUnchecked(uriBuilder, entityType, cacheQueryIdentifier);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                if (isNotFoundException(e)) {
                    throw new EntityNotFoundException();
                }
                throw new ServiceNotFoundException(uriBuilder.build().toString());
            }
            throw e;
        }
    }

    public static <T> T getUnchecked(UriComponentsBuilder uriBuilder, Class<T> entityType) {
        return getUnchecked(uriBuilder, entityType, null);
    }

    public static <T> T getUnchecked(UriComponentsBuilder uriBuilder, Class<T> entityType,
            String cacheQueryIdentifier) {
        T ret;
        final boolean cacheActive = memcached != null && cacheQueryIdentifier != null;
        final String cachePrefix = "_" + entityType.getSimpleName() + "_";
        if (cacheActive) {
            //noinspection unchecked
            ret = (T) memcached.get(cachePrefix + cacheQueryIdentifier);
            if (ret != null) {
                return ret;
            }
        }
        ret = restTemplate.getForObject(uriBuilder.build().toString(), entityType);
        if (cacheActive) {
            memcached.add(cachePrefix + cacheQueryIdentifier, MEMCACHED_TIMEOUT, ret);
        }
        return ret;
    }

    public static int getCount(UriComponentsBuilder uriBuilder) {
        try {
            final HttpHeaders httpHeaders = restTemplate
                    .headForHeaders(uriBuilder.build().toString());
            final List<String> countHeaders = httpHeaders.get("X-Total-Count");
            if (countHeaders.isEmpty()) {
                return 0;
            }
            return Integer.valueOf(countHeaders.get(0));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ServiceNotFoundException(uriBuilder.build().toString());
            }
            throw e;
        }
    }

    private static boolean isNotFoundException(HttpClientErrorException e) {
        return e.getResponseBodyAsString().contains("NotFoundException");
    }

    public static <T> boolean put(UriComponentsBuilder uriBuilder, T entity) {
        HttpEntity<T> httpEntity = new HttpEntity<>(entity);

        return restTemplate
                .exchange(uriBuilder.build().toString(), HttpMethod.PUT, httpEntity, Boolean.class)
                .getBody();
    }

    public static <T> T post(UriComponentsBuilder uriBuilder, Object entity, Class<T> returnType) {
        return restTemplate.postForObject(uriBuilder.build().toString(), entity, returnType);
    }
}