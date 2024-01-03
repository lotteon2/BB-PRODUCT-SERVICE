package kr.bb.product.config.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MockingApi {
  public static void setUpProductDetailLikes(WireMockServer mockCacheApi) {
    mockCacheApi.stubFor(
        WireMock.get(WireMock.urlMatching("/client/likes/1/product/123"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBodyFile("wishlist/product-detail-likes.json")));
  }

  public static void setUpProductsLikes(WireMockServer mockCacheApi) {
    mockCacheApi.stubFor(
        WireMock.post(WireMock.urlMatching("/client/likes/1"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBodyFile("wishlist/products-likes.json")));
  }

  public static void setUpStorePolicy(WireMockServer mockServer) {
    mockServer.stubFor(
        WireMock.post(WireMock.urlMatching("/client/store/policy"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBodyFile("wishlist/store/store-policy.json")));
  }
}
