package auth.backend.gateway_client.repository;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteDefinitionPersist {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public List<PredicateDefinition> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<PredicateDefinition> predicates) {
        this.predicates = predicates;
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public int getRouteOrder() {
        return routeOrder;
    }

    public void setRouteOrder(int routeOrder) {
        this.routeOrder = routeOrder;
    }

    public RouteDefinitionPersist(String id, String routeId, List<PredicateDefinition> predicates, List<FilterDefinition> filters, String uri, Map<String, Object> metadata, int routeOrder) {
        this.id = id;
        this.routeId = routeId;
        this.predicates = predicates;
        this.filters = filters;
        this.uri = uri;
        this.metadata = metadata;
        this.routeOrder = routeOrder;
    }

    private String id;
    private String routeId;

    private List<PredicateDefinition> predicates;

    private List<FilterDefinition> filters = new ArrayList<>();

    private String uri;

    private Map<String,Object> metadata;

    private int routeOrder;

    public RouteDefinitionPersist() {
    }
}