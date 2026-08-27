package br.edu.infnet.pagamento.domain;

import br.edu.infnet.pagamento.domain.vo.CartaoDeCredito;
import br.edu.infnet.pagamento.domain.vo.Dinheiro;
import br.edu.infnet.pagamento.exception.PagamentoRecusadoException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pagamento {

    private final Long id;

    private final Long pedidoId;
    private final Long usuarioId;

    private final Dinheiro valor;
    private final CartaoDeCredito cartao;

    private StatusPagamento status;
    private String motivoRecusa;
    private String codigoAutorizacao;
    private LocalDateTime processadoEm;

    //Lista de eventos ainda não publicados
    private final List<Object> eventos =  new ArrayList<>();

    private Pagamento(Long id, Long pedidoId, Long usuarioId,
                      Dinheiro valor, CartaoDeCredito cartao) {
        this.id = id;
        this.pedidoId = Objects.requireNonNull(pedidoId, "pedidoId é obrigatório");
        this.usuarioId = Objects.requireNonNull(usuarioId, "usuarioId é obrigatório");
        this.valor = Objects.requireNonNull(valor, "valor é obrigatório");
        this.cartao = cartao;
        this.status = StatusPagamento.PENDENTE;
    }

    public static Pagamento solicitar(Long pedidoId, Long usuarioId,
                                      Dinheiro valor, CartaoDeCredito cartao) {

        if (valor.getValor().compareTo(new java.math.BigDecimal("10000.00")) > 0) {
            throw new PagamentoRecusadoException("LIMITE_EXCEDIDO");
        }

        if (cartao != null && cartao.ultimosQuatroDigitos().equals("0000")) {
            throw new PagamentoRecusadoException("CARTAO_BLOQUEADO");
        }

        return new Pagamento(null, pedidoId, usuarioId, valor, cartao);
    }

    // Só o próprio agregado muda seu status — nunca um setStatus() de fora.
    public void aprovar(String codigoAutorizacao) {
        if (status != StatusPagamento.PENDENTE) {
            throw new IllegalStateException("Pagamento já foi processado");
        }
        this.status = StatusPagamento.APROVADO;
        this.codigoAutorizacao = codigoAutorizacao;
        this.processadoEm = LocalDateTime.now();

        this.eventos.add(new PagamentoAprovadoEvent(
                this.id, this.pedidoId, this.usuarioId, this.valor.getValor(), this.processadoEm
        ));
    }
    
    // A camada de aplicação chama isto após salvar o agregado,
    // para pegar os eventos pendentes e publicá-los.
    public List<Object> getEventosPendentes() {
        return List.copyOf(eventos);
    }

    public void limparEventos() {
        eventos.clear();
    }

    public void recusar(String motivo) {
        if (status != StatusPagamento.PENDENTE) {
            throw new IllegalStateException("Pagamento já foi processado");
        }
        this.status = StatusPagamento.RECUSADO;
        this.motivoRecusa = motivo;
        this.processadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getPedidoId() { return pedidoId; }
    public Long getUsuarioId() { return usuarioId; }
    public Dinheiro getValor() { return valor; }
    public CartaoDeCredito getCartao() { return cartao; }
    public StatusPagamento getStatus() { return status; }
    public String getMotivoRecusa() { return motivoRecusa; }
    public String getCodigoAutorizacao() { return codigoAutorizacao; }
    public LocalDateTime getProcessadoEm() { return processadoEm; }
}
