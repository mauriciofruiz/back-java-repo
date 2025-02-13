package com.example.test.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAttributes extends DefaultErrorAttributes {

    @Override
/*
 * Sobrescribe el método `getErrorAttributes` para personalizar los atributos de error.
 *
 * @param request El objeto `ServerRequest` que contiene la solicitud del servidor.
 * @param options Las opciones de atributos de error `ErrorAttributeOptions`.
 * @return Un mapa de atributos de error personalizados.
 */
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        // Crear un nuevo mapa para los atributos de error
        Map<String, Object> errorAttributes = new HashMap<>();

        // Obtener el error asociado con la solicitud
        Throwable throwable = super.getError(request);

        // Verificar si el error es una instancia de CustomException
        if (throwable instanceof CustomException customException) {
            // Agregar el estado y el mensaje del CustomException al mapa de atributos de error
            errorAttributes.put("status", customException.getStatus());
            errorAttributes.put("message", customException.getMessage());
        }

        // Devolver el mapa de atributos de error personalizados
        return errorAttributes;
    }
}
