package com.example.testaccount.router;


import com.example.testaccount.constants.PathConstants;
import com.example.testaccount.handler.AccountTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AccountTypeRouter {

    @Bean
    public RouterFunction<ServerResponse> accountTypeRouterFunction(AccountTypeHandler handler) {
        return RouterFunctions.route()
                .POST(PathConstants.ACCOUNT_TYPE, handler::create)
                .GET(PathConstants.ACCOUNT_TYPE + PathConstants.ID_PARAM, handler::getById)
                .GET(PathConstants.ACCOUNT_TYPE, handler::getAll)
                .build();
    }
}
