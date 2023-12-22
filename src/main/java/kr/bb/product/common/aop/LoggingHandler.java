package kr.bb.product.common.aop;

import autovalue.shaded.com.google.common.base.Joiner;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoggingHandler {
  private static final Logger logger = LoggerFactory.getLogger(LoggingHandler.class);

  @Value("${spring.config.active.on-profile}")
  private String activeProfile;

  @Around("within(kr.bb.product.domain..*)")
  public Object logging(ProceedingJoinPoint pjp) throws Throwable {

    String params = getRequestParams();

    long startAt = System.currentTimeMillis();

    try {
      logger.info(
          "[REQUEST] : {}({}) = {}",
          pjp.getSignature().getDeclaringTypeName(),
          pjp.getSignature().getName(),
          params);

      Object result = pjp.proceed();

      long endAt = System.currentTimeMillis();

      logger.info(
          "[RESPONSE] : {}({}) = {} ({}ms)",
          pjp.getSignature().getDeclaringTypeName(),
          pjp.getSignature().getName(),
          result,
          endAt - startAt);

      return result;
    } catch (Exception e) {
      // Log error at ERROR level
      logger.error(
          "[ERROR] : {}({}) = {}",
          pjp.getSignature().getDeclaringTypeName(),
          pjp.getSignature().getName(),
          e.getMessage());

      // Rethrow the exception
      throw e;
    }
  }

  // get requset value
  private String getRequestParams() {

    String params = "";

    RequestAttributes requestAttribute = RequestContextHolder.getRequestAttributes();

    if (requestAttribute != null) {
      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

      Map<String, String[]> paramMap = request.getParameterMap();

      if (!paramMap.isEmpty()) {
        params = " [" + paramMapToString(paramMap) + "]";
      }
    }
    return params;
  }

  private String paramMapToString(Map<String, String[]> paramMap) {
    return paramMap.entrySet().stream()
        .map(
            entry ->
                String.format("%s -> (%s)", entry.getKey(), Joiner.on(",").join(entry.getValue())))
        .collect(Collectors.joining(", "));
  }
}
