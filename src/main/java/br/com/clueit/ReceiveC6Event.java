package br.com.clueit;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Named("boletoEventHandler")
public class ReceiveC6Event implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @ConfigProperty(name = "topic.arn")
    String topicArn;
    @ConfigProperty(name = "aws.user-key")
    String awsUserkey;
    @ConfigProperty(name = "aws.user-secret")
    String awsUserSecret;
    @ConfigProperty(name="aws.region")
    String awsRegion;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            PublishRequest request = PublishRequest.builder()
                    .message(input.getBody())
                    .topicArn(topicArn)
                    .build();
            try (SnsClient snsClient = SnsClient.builder()
                    .region(Region.of(awsRegion))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                            .create(awsUserkey, awsUserSecret)))
                    .build()) {
                snsClient.publish(request);
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(200)
                        .withBody("Recebido!");
            }
        }catch (Exception e){
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody(e.getMessage());
        }
    }
}
