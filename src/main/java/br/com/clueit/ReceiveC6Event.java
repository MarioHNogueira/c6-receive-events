package br.com.clueit;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.utils.StringUtils;

@Slf4j
@Named("eventHandler")
public class ReceiveC6Event implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @ConfigProperty(name="boleto_topic_arn")
    String boletoTopicArn;
    @ConfigProperty(name="pix_topic_arn")
    String pixTopicArn;
    @ConfigProperty(name = "aws_user_key")
    String awsUserkey;
    @ConfigProperty(name = "aws_user_secret")
    String awsUserSecret;
    @ConfigProperty(name="aws_region")
    String awsRegion;
    @ConfigProperty(name="log_auditoria")
    String logAuditoria;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            if ("ligado".equalsIgnoreCase(logAuditoria)) {
                Log.info("Enconded: "+ input.getIsBase64Encoded());
                Log.info(new ObjectMapper().writeValueAsString(input));
            }
            if (StringUtils.isEmpty(input.getBody())) {
                throw new IllegalArgumentException("Body is empty");
            }
            String mensagem = input.getBody();
            if (input.getIsBase64Encoded()){
                mensagem = new String(java.util.Base64.getDecoder().decode(mensagem));
                if ("ligado".equalsIgnoreCase(logAuditoria)) {
                    Log.info("Body Base64: " + mensagem);
                }
            }
            if (!mensagem.contains("BANK_SLIP") &&
                !mensagem.contains("endToEndId")){
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(500)
                        .withBody("Formato de evento inválido!!");
            }
            if (input.getBody().contains("BANK_SLIP")) {
                publish(mensagem, boletoTopicArn);
            }
            if (input.getBody().contains("endToEndId")){
                publish(mensagem, pixTopicArn);
            }
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody("Recebido!");
        }catch (Exception e){
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody(e.getMessage());
        }
    }
    private void publish(String message, String topicArn){
        PublishRequest request = PublishRequest.builder().message(message)
                .topicArn(topicArn)
                .build();
        try (SnsClient snsClient = SnsClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(awsUserkey, awsUserSecret)))
                .build()) {
            snsClient.publish(request);
        }
    }
}
