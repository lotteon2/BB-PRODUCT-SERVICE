package kr.bb.product.config;

import static org.assertj.core.api.BDDAssertions.then;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DynamoDBConfigurationTest {
  private AmazonDynamoDB amazonDynamoDb;
  private Map<String, AttributeValue> item;

  @BeforeEach
  void setup() {
    AWSCredentials awsCredentials = new BasicAWSCredentials("key1", "key2");
    AWSCredentialsProvider awsCredentialsProvider =
        new AWSStaticCredentialsProvider(awsCredentials);
    EndpointConfiguration endpointConfiguration =
        new EndpointConfiguration("http://localhost:9000", "ap-northeast-2");

    amazonDynamoDb =
        AmazonDynamoDBClientBuilder.standard()
            .withCredentials(awsCredentialsProvider)
            .withEndpointConfiguration(endpointConfiguration)
            .build();

    item = new HashMap<>();
    item.put("id", (new AttributeValue()).withS("uuid"));
    item.put("orders", (new AttributeValue()).withN("1"));
    item.put("content", (new AttributeValue()).withS("comment content"));
    item.put("deleted", (new AttributeValue()).withBOOL(false));
    item.put("createdAt", (new AttributeValue()).withS("to be changed"));
    item.put("deletedAt", (new AttributeValue()).withS("to be changed"));
  }

  @Test
  @Disabled
  void createTable() {
    CreateTableRequest createTableRequest =
        (new CreateTableRequest())
            .withAttributeDefinitions(
                new AttributeDefinition("id", ScalarAttributeType.S),
                new AttributeDefinition("orders", ScalarAttributeType.N),
                new AttributeDefinition("createdAt", ScalarAttributeType.S))
            .withTableName("flower")
            .withKeySchema(new KeySchemaElement("id", KeyType.HASH))
            .withGlobalSecondaryIndexes(
                (new GlobalSecondaryIndex())
                    .withIndexName("byFlower")
                    .withKeySchema(
                        new KeySchemaElement("orders", KeyType.HASH),
                        new KeySchemaElement("createdAt", KeyType.RANGE))
                    .withProjection((new Projection()).withProjectionType(ProjectionType.ALL))
                    .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L)))
            .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

    boolean hasTableBeenCreated =
        TableUtils.createTableIfNotExists(amazonDynamoDb, createTableRequest);
    then(hasTableBeenCreated).isFalse();
  }

  @Test
  @Disabled
  void getItem_ShouldBeCalledAfterPuttingItem_FoundItem() {
    PutItemRequest putItemRequest = (new PutItemRequest()).withTableName("flower").withItem(item);
    amazonDynamoDb.putItem(putItemRequest);

    Map<String, AttributeValue> key = new HashMap<>();
    key.put("id", (new AttributeValue()).withS("uuid"));

    GetItemRequest getItemRequest = (new GetItemRequest()).withTableName("flower").withKey(key);

    GetItemResult getItemResult = amazonDynamoDb.getItem(getItemRequest);
    System.out.println(getItemResult);

    then(getItemResult.getItem()).containsAllEntriesOf(item);
  }
}
