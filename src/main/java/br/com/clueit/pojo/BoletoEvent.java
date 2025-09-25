package br.com.clueit.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoletoEvent {
    @JsonProperty("external_id")
    String externalId;
    @JsonProperty("date_time")
    String dateTime;
    @JsonProperty("client_id")
    String clientId;
    @JsonProperty("partner_id")
    String partnerId;
    @JsonProperty("status")
    String status;
    @JsonProperty("service")
    String service;
    @JsonProperty("information")
    String information;
}
