package br.edu.infnet.pagamento.domain.evento;

import java.time.LocalDateTime;

/**
 * Abstração de um Evento de Domínio: representa "algo que aconteceu"
 * dentro do domínio, sempre com o instante em que ocorreu.
 */
public abstract class DomainEvent {

    private final LocalDateTime ocorridoEm;

    protected DomainEvent() {
        this.ocorridoEm = LocalDateTime.now();
    }

    public LocalDateTime getOcorridoEm() {
        return ocorridoEm;
    }
}
