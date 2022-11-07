package com.fabricio.campsite.config;

import com.fabricio.campsite.exceptions.ConfigurationException;
import com.fabricio.campsite.exceptions.IssueEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@Slf4j
public class SpringDocRedirectConfiguration extends SwaggerIndexPageTransformer {

    private static final String OPEN_API_URL_PLACE_HOLDER =
            "https://petstore.swagger.io/v2/swagger.json";
    private static final String INTERNAL_REDIRECT_URL = "/v3/api-docs";
    private static final String CUSTOMER_FACING_HOST_NAME_PATTERN = "campsite";
    private static final String CUSTOMER_FACING_REDIRECT_URL =
            "/pws/uc/stored-contract" + INTERNAL_REDIRECT_URL;

    public SpringDocRedirectConfiguration(
            SwaggerUiConfigProperties swaggerUiConfig,
            SwaggerUiOAuthProperties swaggerUiOAuthProperties,
            SwaggerUiConfigParameters swaggerUiConfigParameters,
            ObjectMapper objectMapper) {
        super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerUiConfigParameters, objectMapper);
    }

    @Override
    public Resource transform(
            HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain)
            throws IOException {
        if (resource.getURL().toString().endsWith("/index.html")) {
            String html = getHtmlContent(resource);
            html =
                    overwritePetStore(
                            html,
                            request.getContextPath(),
                            request.getServerName().endsWith(CUSTOMER_FACING_HOST_NAME_PATTERN));
            return new TransformedResource(resource, html.getBytes());
        } else {
            return resource;
        }
    }

    private String overwritePetStore(String html, String contextPath, boolean isExternalFacingUrl) {
        String internalRedirectUrl =
                StringUtils.isNotEmpty(contextPath)
                        ? contextPath + INTERNAL_REDIRECT_URL
                        : INTERNAL_REDIRECT_URL;
        String redirectUrl = isExternalFacingUrl ? CUSTOMER_FACING_REDIRECT_URL : internalRedirectUrl;
        return html.replace(OPEN_API_URL_PLACE_HOLDER, redirectUrl);
    }

    private String getHtmlContent(Resource resource) {
        try {
            var inputStream = resource.getInputStream();
            java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
            String content = s.next();
            inputStream.close();
            return content;
        } catch (IOException e) {
            log.error("Error getHtmlContent - {}", e);
            throw ConfigurationException.error(IssueEnum.INTERNAL_SERVER_ERROR);
        }
    }
}