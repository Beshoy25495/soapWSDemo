package com.bwagih.soapWSDemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

@Slf4j
public class CustomClientInterceptor implements ClientInterceptor {
    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        // - Add custom HTTP headers to the request
        // - Log information about the request
        // - Modify the request payload
        log.info("CustomClientInterceptor: Handling request...");
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        // - Extract data from the response
        // - Perform validation on the response
        // - Log information about the response
        log.info("CustomClientInterceptor: Handling response...");
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        // - Extract information from the fault message
        // - Retry the request if certain conditions are met
        // - Log information about the fault
        log.error("CustomClientInterceptor: Handling fault...");
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
        // - Release resources used during the request
        // - Log information about the completion
        log.info("CustomClientInterceptor: After completion...");
    }
}
