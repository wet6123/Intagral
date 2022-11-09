package com.a304.intagral.config;

import com.a304.intagral.api.response.*;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.OAS_30)
                .additionalModels(
                        typeResolver.resolve(FollowHashtagPostRes.class),
                        typeResolver.resolve(FollowListGetRes.class),
                        typeResolver.resolve(FollowUserPostRes.class),
                        typeResolver.resolve(HashtagHotListRes.class),
                        typeResolver.resolve(HashtagProfileRes.class),
                        typeResolver.resolve(PostAddPostRes.class),
                        typeResolver.resolve(PostDetailRes.class),
                        typeResolver.resolve(PostLikePostRes.class),
                        typeResolver.resolve(PresetListRes.class),
                        typeResolver.resolve(SearchRes.class),
                        typeResolver.resolve(TokenRes.class),
                        typeResolver.resolve(UserLoginPostRes.class),
                        typeResolver.resolve(UserProfileRes.class))
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.a304.intagral.api"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securityContexts(newArrayList(securityContext()))
                .securitySchemes(newArrayList(apiKey()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Intagral Swagger")
                .description("Intagral 백엔드 API Docs")
                .version("1.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(SECURITY_SCHEMA_NAME, "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    public static final String SECURITY_SCHEMA_NAME = "Authorization";
    public static final String AUTHORIZATION_SCOPE_GLOBAL = "global";
    public static final String AUTHORIZATION_SCOPE_GLOBAL_DESC = "accessEverything";

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(AUTHORIZATION_SCOPE_GLOBAL, AUTHORIZATION_SCOPE_GLOBAL_DESC);
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(new SecurityReference(SECURITY_SCHEMA_NAME, authorizationScopes));
    }
}
