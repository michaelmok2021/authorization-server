package auth.backend.gateway_client.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteDefinitionRepository {


    private Map<String,RouteDefinitionPersist> routeMap;

    public RouteDefinitionRepository() {
        routeMap = new HashMap<>();
    }

    //TODO: Save and load to file
    public void savePersist(RouteDefinitionPersist po) {
        routeMap.put(po.getRouteId(),po);
    }

    public List<RouteDefinitionPersist> findAll() {
        return (List<RouteDefinitionPersist>) routeMap.values();
    }

    public void deleteByRouteId(String s) {
        routeMap.remove(s);
    }
}
