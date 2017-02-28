package com.hanbit.sp.aop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbit.sp.aop.ExceptionAspect;
import com.hanbit.sp.exception.SooException;

@Aspect
@Component
@Order(1)
public class ExceptionAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAspect.class);
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
		Object retVal = null;
		
		try {
			retVal = joinPoint.proceed();
		}
		catch (Throwable t) {
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
			ResponseBody responseBody = methodSignature.getMethod().getAnnotation(ResponseBody.class);
			
			if (responseBody == null) {
				throw t;
			}
			else {
				ServletRequestAttributes requestAttributes =
						(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				HttpServletResponse response = requestAttributes.getResponse();
				
				LOGGER.error(t.getMessage(), t);
				
				Map result = new HashMap();
				result.put("errorMsg", t.getMessage());
				
				//컨트롤러의 throw가 RuntimeException 대신 SooException일 때 //
				if (t instanceof SooException) {
					result.put("errorCode", ((SooException) t).getErrorCode());
				}
				else {
					result.put("errorCode", "UNKNOWN");
				}
				
				String json = JSON_MAPPER.writeValueAsString(result);
				byte[] content = json.getBytes();
				
				response.setStatus(1500);
				response.setContentType("application/json;charset=utf-8");
				response.setContentLength(content.length);
				response.getOutputStream().write(content);
				
				return null;
			}
		}
		
		return retVal;
	}
}
