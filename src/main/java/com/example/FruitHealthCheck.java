package com.example;

import io.smallrye.health.HealthStatus;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class FruitHealthCheck {

    @Produces
    @ApplicationScoped
    @Liveness
    HealthCheck liveCheck() {
        return HealthStatus.up("successful-live");
    }

    @Produces
    @ApplicationScoped
    @Readiness
    HealthCheck readyCheck() {
        return HealthStatus.state("successful-read", this::isReady);
    }

    private boolean isReady() {
        return true;
    }
}