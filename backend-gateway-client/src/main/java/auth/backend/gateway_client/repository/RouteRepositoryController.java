package auth.backend.gateway_client.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Slf4j
/**
 * https://github.com/ctlove0523/spring-samples/tree/master/spring-cloud-gateway
 */
public class RouteRepositoryController implements org.springframework.cloud.gateway.route.RouteDefinitionRepository, ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;
    @Autowired
    private RouteDefinitionRepository repository;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinitionPersist> routeDefinitionPersists = repository.findAll();
        if (routeDefinitionPersists.isEmpty()) {
            return Flux.empty();
        }

        return Flux.fromIterable(routeDefinitionPersists)
                .map(routeDefinitionPersist -> {
                    RouteDefinition definition = new RouteDefinition();
                    definition.setId(routeDefinitionPersist.getRouteId());
                    definition.setFilters(routeDefinitionPersist.getFilters());
                    definition.setMetadata(routeDefinitionPersist.getMetadata());
                    definition.setOrder(routeDefinitionPersist.getRouteOrder());
                    definition.setPredicates(routeDefinitionPersist.getPredicates());
                    definition.setUri(UriComponentsBuilder.fromUriString(routeDefinitionPersist.getUri()).build().toUri());
                    return definition;
                }).onErrorStop();
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap((Function<RouteDefinition, Mono<? extends Void>>) routeDefinition -> {
            RouteDefinitionPersist po = new RouteDefinitionPersist();
            po.setRouteId(routeDefinition.getId());
            po.setFilters(routeDefinition.getFilters());
            po.setMetadata(routeDefinition.getMetadata());
            po.setPredicates(routeDefinition.getPredicates());
            po.setRouteOrder(routeDefinition.getOrder());
            po.setUri(routeDefinition.getUri().toString());
            repository.savePersist(po);

            publisher.publishEvent(new RefreshRoutesEvent(this));
            return Mono.empty();
        }).doOnError(throwable -> log.error("exception ", throwable));
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(s -> {
            repository.deleteByRouteId(s);
            publisher.publishEvent(new RefreshRoutesEvent(this));
            return Mono.empty();
        });
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }


}
