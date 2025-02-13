package com.example.testaccount.router;


import com.example.testaccount.constants.PathConstants;
import com.example.testaccount.handler.AccountHandler;
import com.example.testaccount.handler.MovementHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MovementRouter {

    @Bean
    public RouterFunction<ServerResponse> movementRouterFunction(MovementHandler handler) {
        return RouterFunctions.route()
                .POST(PathConstants.MOVEMENT, handler::create)
                .PUT(PathConstants.MOVEMENT + PathConstants.ID_PARAM, handler::update)
                .GET(PathConstants.MOVEMENT + PathConstants.ID_PARAM, handler::getById)
                .GET(PathConstants.MOVEMENT, handler::getAll)
                .DELETE(PathConstants.MOVEMENT + PathConstants.ID_PARAM, handler::delete)
                .GET(PathConstants.REPORTS, handler::getAccountStatus)
                .build();
    }
}
