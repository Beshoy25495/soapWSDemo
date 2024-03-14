package com.bwagih.soapWSDemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.FaultMessageResolver;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.transform.TransformerException;
import java.io.IOException;

@Slf4j
public class CustomFaultMessageResolver implements FaultMessageResolver {
    @Override
    public void resolveFault(WebServiceMessage message) throws IOException {
        if (message instanceof SoapMessage) {
            SoapMessage soapMessage = (SoapMessage) message;
            SoapBody soapBody = soapMessage.getSoapBody();
            if (soapBody.hasFault()) {
                SoapFault soapFault = soapBody.getFault();

                String faultCode = soapFault.getFaultCode().getLocalPart();
                String faultString = soapFault.getFaultStringOrReason();

                log.error("SOAP Fault Code: " + faultCode);
                log.error("SOAP Fault String: " + faultString);
            }
        }
    }
}
