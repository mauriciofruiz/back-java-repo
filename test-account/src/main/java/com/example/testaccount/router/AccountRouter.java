package com.example.testaccount.router;


import com.example.testaccount.constants.PathConstants;
import com.example.testaccount.handler.AccountHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AccountRouter {

    @Bean
    public RouterFunction<ServerResponse> accountRouterFunction(AccountHandler handler) {
        return RouterFunctions.route()
                .POST(PathConstants.ACCOUNT, handler::create)
                .PUT(PathConstants.ACCOUNT + PathConstants.ID_PARAM, handler::update)
                .GET(PathConstants.ACCOUNT + PathConstants.ID_PARAM, handler::getById)
                .GET(PathConstants.ACCOUNT, handler::getAll)
                .DELETE(PathConstants.ACCOUNT + PathConstants.ID_PARAM, handler::delete)
                .build();
    }
}
