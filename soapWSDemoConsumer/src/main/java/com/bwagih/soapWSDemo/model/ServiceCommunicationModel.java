package com.bwagih.soapWSDemo.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.xml.bind.Marshaller;
import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ServiceCommunicationModel<REQ> {
    private REQ request;
    @Nullable
    private String uri;
    private String soapAction;
    private boolean supportJAXBElementClass = false;
    @Nullable
    private String packagesToScan;

    private Integer connectionTimeout;
    private Integer readTimeout;

    private Map<String, Object> jaxbContextProperties = new HashMap<>();
    private Map<String, Object> marshallerProperties = new HashMap<>();

    private Map<String, Object> unmarshallerProperties = new HashMap<>();


    public ServiceCommunicationModel(@Nullable String uri, @Nullable String packagesToScan) {
        this.uri = uri;
        this.packagesToScan = packagesToScan;
        this.marshallerProperties.putAll(getDefaultMarshallerProperties());
        this.connectionTimeout = 300;
        this.readTimeout = 100;
    }

    public ServiceCommunicationModel(REQ request, @Nullable String uri, String soapAction, boolean supportJAXBElementClass, @Nullable String packagesToScan) {
        this(uri, packagesToScan);
        this.request = request;
        this.soapAction = soapAction;
        this.supportJAXBElementClass = supportJAXBElementClass;
    }

    private Map<String, ?> getDefaultMarshallerProperties() {
        Map<String, Object> marshallerProperties = new HashMap<>();
        marshallerProperties.putIfAbsent(Marshaller.JAXB_FRAGMENT, true);
        marshallerProperties.putIfAbsent(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshallerProperties.putIfAbsent(Marshaller.JAXB_ENCODING, "UTF-8");
        return marshallerProperties;
    }


}
