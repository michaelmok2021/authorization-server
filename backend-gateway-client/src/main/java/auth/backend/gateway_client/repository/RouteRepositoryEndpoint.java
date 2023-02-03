package auth.backend.gateway_client.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.cloud.gateway.actuate.GatewayControllerEndpoint;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RestControllerEndpoint(id = "persist")
public class RouteRepositoryEndpoint extends GatewayControllerEndpoint {

    @Autowired
    public RouteRepositoryEndpoint(List<GlobalFilter> globalFilters, List<GatewayFilterFactory> gatewayFilters, List<RoutePredicateFactory> routePredicates, RouteDefinitionWriter routeDefinitionWriter, RouteLocator routeLocator, RouteDefinitionLocator routeDefinitionLocator) {
        super(globalFilters, gatewayFilters, routePredicates, routeDefinitionWriter, routeLocator, routeDefinitionLocator);
        log.info("Created RouteControllerEndpoint.");
    }

    @Override
    @PostMapping("/refresh")
    public Mono<Void> refresh() {
        log.debug("RouteControllerEndpoint refresh.");

        return super.refresh();
    }

    @PostMapping("/save")
    public String save(@RequestBody String filename) {
        String returnStr = "";
        log.debug("RouteControllerEndpoint save.\nSaving routes...");

        try (FileWriter file = new FileWriter(filename)) {
            log.debug(String.format("Writing definitions to %s", filename));
            Flux<Object> routeDefinition = super.routesdef().collectList().flatMapMany(Flux::just);;

            log.info(routeDefinition.toString());
            file.write(routeDefinition.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return returnStr;
    }

    @PostMapping("/load")
    public String load(@RequestBody String filename) {
        String returnStr = null;
        String jsonStringArray = null;
        List<RouteDefinition> loadedRoutes;
        List<String> loadedIds = new LinkedList<>();
        ObjectMapper mapper = new ObjectMapper();

        log.debug("RouteControllerEndpoint load.\nLoading routes...");

        try {
            log.debug(String.format("Filename/Body: %s", filename));
            log.debug("Reading file as string...");
            jsonStringArray = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            returnStr = String.format("Failed to load from file: %s<br>Exception: %s", filename, e.getMessage());
        }

        try {
            log.debug("Reading file as list.");
            loadedRoutes = Arrays.asList(mapper.readValue(jsonStringArray, RouteDefinition[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.debug("Iterating through loadedRoutes");
        for (RouteDefinition route : loadedRoutes) {

            String id = route.getId();

            log.debug(String.format("Loading ID: %s", id));

            try {
                Mono.just(route).doOnNext(this::validateRouteDefinition)
                        .flatMap(routeDefinition -> super.routeDefinitionWriter.save(Mono.just(routeDefinition).map(r -> {
                            r.setId(id);
                            log.debug("Saving route: " + route);
                            return r;
                        })).then(Mono.defer(() -> Mono.just(ResponseEntity.created(URI.create("/routes/" + id)).build()))))
                        .switchIfEmpty(Mono.defer(() -> Mono.just(ResponseEntity.badRequest().build())));
                loadedIds.add(id);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        super.refresh();

        if (returnStr == null) {
            returnStr = "Successfully loaded routes: " + loadedIds + super.routesdef();
        }
        return returnStr;
    }

//    Private methods from AbstractGatewayControllerEndpoint
    private void validateRouteDefinition(RouteDefinition routeDefinition) {
        Set<String> unavailableFilterDefinitions = routeDefinition.getFilters().stream().filter(rd -> !isAvailable(rd))
                .map(FilterDefinition::getName).collect(Collectors.toSet());

        Set<String> unavailablePredicatesDefinitions = routeDefinition.getPredicates().stream()
                .filter(rd -> !isAvailable(rd)).map(PredicateDefinition::getName).collect(Collectors.toSet());
        if (!unavailableFilterDefinitions.isEmpty()) {
            handleUnavailableDefinition(FilterDefinition.class.getSimpleName(), unavailableFilterDefinitions);
        }
        else if (!unavailablePredicatesDefinitions.isEmpty()) {
            handleUnavailableDefinition(PredicateDefinition.class.getSimpleName(), unavailablePredicatesDefinitions);
        }
    }

    private void handleUnavailableDefinition(String simpleName, Set<String> unavailableDefinitions) {
        final String errorMessage = String.format("Invalid %s: %s", simpleName, unavailableDefinitions);
        log.warn(errorMessage);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    private boolean isAvailable(FilterDefinition filterDefinition) {
        return GatewayFilters.stream()
                .anyMatch(gatewayFilterFactory -> filterDefinition.getName().equals(gatewayFilterFactory.name()));
    }

    private boolean isAvailable(PredicateDefinition predicateDefinition) {
        return routePredicates.stream()
                .anyMatch(routePredicate -> predicateDefinition.getName().equals(routePredicate.name()));
    }
}
