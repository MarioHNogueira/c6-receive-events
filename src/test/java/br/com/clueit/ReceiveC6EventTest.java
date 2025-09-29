package br.com.clueit;

import br.com.clueit.pojo.BoletoEvent;
import br.com.clueit.pojo.PixEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ReceiveC6EventTest {
    @Test
    void testReceiveBoletoEvent() throws JsonProcessingException {
        APIGatewayV2HTTPEvent request = APIGatewayV2HTTPEvent.builder().build();
        BoletoEvent be = new BoletoEvent();
        be.setClientId("12379787");
        be.setService("BANK_SLIP");
        be.setExternalId("123456789");
        be.setDateTime("2021-01-01T00:00:00");
        be.setStatus("RECEIVED");
        be.setInformation("Information");
        be.setPartnerId("123456789");
        request.setBody(new ObjectMapper().writeValueAsString(be));
        final  APIGatewayProxyResponseEvent response = given()
            .contentType("application/json")
            .body(request)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .as(APIGatewayProxyResponseEvent.class);
        MatcherAssert.assertThat(response.getBody(), is("Recebido!"));
    }
    @Test
    void testReceivePixEvent() throws JsonProcessingException {
        APIGatewayV2HTTPEvent request = APIGatewayV2HTTPEvent.builder().build();
        PixEvent pe = new PixEvent();
        pe.setEndToEndId("123456789");
        pe.setChave("snds;odfbsdiufgsdf");
        pe.setValor(100.0);
        PixEvent.Horario ho = new PixEvent.Horario();
        ho.setLiquidacao(new Date());
        ho.setSolicitacao(new Date());
        pe.setHorario(new Date());
        pe.setInfoPagador("Info Pagador");
        pe.setTxId("098098989");
        pe.setDevolucoes(null);
        request.setBody(new ObjectMapper().writeValueAsString(pe));
        final  APIGatewayProxyResponseEvent response = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .as(APIGatewayProxyResponseEvent.class);
        MatcherAssert.assertThat(response.getBody(), is("Recebido!"));
    }

    @Test
    void testWrongFormat(){
        APIGatewayV2HTTPEvent request = APIGatewayV2HTTPEvent.builder().build();
        request.setBody("Teste");
        final  APIGatewayProxyResponseEvent response = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .as(APIGatewayProxyResponseEvent.class);
        MatcherAssert.assertThat(response.getBody(), is("Formato de evento inválido!!"));
    }

}