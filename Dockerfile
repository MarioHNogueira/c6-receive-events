FROM  public.ecr.aws/lambda/provided

ADD target/c6-event-receiver-1.0.0-SNAPSHOT-runner /var/runtime/bootstrap
RUN chmod ugo+x /var/runtime/bootstrap

CMD ["io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest"]