package br.edu.infnet.pagamento.domain.evento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Evento de domínio concreto: representa o fato "um pagamento foi aprovado".
 * Carrega apenas dados relevantes para quem for reagir a esse evento
 */
public class PagamentoAprovadoEvent extends DomainEvent {

    private final Long pagamentoId;
    private final Long pedidoId;
    private final Long usuarioId;
    private final BigDecimal valor;
    private final LocalDateTime processadoEm;

    public PagamentoAprovadoEvent(Long pagamentoId, Long pedidoId, Long usuarioId,
                                  BigDecimal valor, LocalDateTime processadoEm) {
        super();
        this.pagamentoId = pagamentoId;
        this.pedidoId = pedidoId;
        this.usuarioId = usuarioId;
        this.valor = valor;
        this.processadoEm = processadoEm;
    }

    public Long getPagamentoId() { return pagamentoId; }
    public Long getPedidoId() { return pedidoId; }
    public Long getUsuarioId() { return usuarioId; }
    public BigDecimal getValor() { return valor; }
    public LocalDateTime getProcessadoEm() { return processadoEm; }
}
