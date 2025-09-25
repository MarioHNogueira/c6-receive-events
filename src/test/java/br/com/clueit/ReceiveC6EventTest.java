package br.com.clueit;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ReceiveC6EventTest {
    @Test
    void testReceiveBoletoEvent(){
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
        MatcherAssert.assertThat(response.getBody(), is("Recebido!"));
    }

}