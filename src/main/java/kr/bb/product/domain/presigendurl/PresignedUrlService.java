package kr.bb.product.domain.presigendurl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PresignedUrlService {
  public static PresignedUrlData getPresignedUrl(
      String prefix, String fileName, AmazonS3 amazonS3, String bucket) {
    String onlyOneFileName = onlyOneFileName(fileName);

    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        getGeneratePreSignedUrlRequest(bucket, prefix + "/" + onlyOneFileName);
    return PresignedUrlData.builder()
        .presignedUrl(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString())
        .build();
  }

  private static GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(
      String bucket, String fileName) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(getPreSignedUrlExpiration());
    generatePresignedUrlRequest.addRequestParameter(
        Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());
    return generatePresignedUrlRequest;
  }

  private static Date getPreSignedUrlExpiration() {
    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += 1000 * 60 * 5;
    expiration.setTime(expTimeMillis);
    return expiration;
  }

  private static String onlyOneFileName(String fileName) {
    return UUID.randomUUID().toString() + fileName;
  }
}
