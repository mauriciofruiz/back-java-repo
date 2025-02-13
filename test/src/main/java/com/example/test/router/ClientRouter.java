package com.example.test.router;

import com.example.test.constants.PathConstants;
import com.example.test.handler.ClientHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ClientRouter {

    @Bean
    public RouterFunction<ServerResponse> clientRouterFunction(ClientHandler handler) {
        return RouterFunctions.route()
                .POST(PathConstants.CLIENT_PATH, handler::create)
                .PUT(PathConstants.CLIENT_PATH + PathConstants.ID_PARAM, handler::update)
                .GET(PathConstants.CLIENT_PATH + PathConstants.ID_PARAM, handler::getById)
                .GET(PathConstants.CLIENT_PATH, handler::getAll)
                .DELETE(PathConstants.CLIENT_PATH + PathConstants.ID_PARAM, handler::delete)
                .build();
    }
}
