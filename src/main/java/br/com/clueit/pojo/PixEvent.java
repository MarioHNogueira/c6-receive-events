package br.com.clueit.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.util.Date;
import java.util.List;

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PixEvent {
    @JsonProperty("endToEndId")
    String endToEndId;
    @JsonProperty("txid")
    String txId;
    @JsonProperty("valor")
    double valor;
    @JsonProperty("componentesValor")
    List<ValorOriginal> componentsValor;
    @JsonProperty("chave")
    String chave;
    @JsonProperty("horario")
    Date horario;
    @JsonProperty("infoPagador")
    String infoPagador;
    @JsonProperty("devolucoes")
    List<Devolucao> devolucoes;

    @Data
    public static class ValorOriginal{
        double original;
    }

    @Data
    public static class Devolucao{
        int id;
        String rtrId;
        double valor;
        String natureza;
        String descricao;
        Horario horario;
        String status;
        String motivo;
    }

    @Data
    public static class Horario{
        Date solicitacao;
        Date liquidacao;
    }
}


