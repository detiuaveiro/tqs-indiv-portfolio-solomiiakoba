package com.tqs.zeromonos.model;

import lombok.Getter;
import java.util.EnumSet;
import java.util.Set;

@Getter
public enum Status {
    RECEIVED("Pedido recebido"),
    ASSIGNED("Pedido atribuído"),
    IN_PROGRESS("Em progresso"),
    COMPLETED("Concluído"),
    CANCELED("Cancelado");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public Set<Status> getAllowedTransitions() {
        return switch (this) {
            case RECEIVED -> EnumSet.of(ASSIGNED, CANCELED);
            case ASSIGNED -> EnumSet.of(IN_PROGRESS, CANCELED);
            case IN_PROGRESS -> EnumSet.of(COMPLETED, CANCELED);
            case COMPLETED, CANCELED -> EnumSet.noneOf(Status.class); // estados finais
        };
    }

    public boolean canTransitionTo(Status next) {
        return getAllowedTransitions().contains(next);
    }
}
