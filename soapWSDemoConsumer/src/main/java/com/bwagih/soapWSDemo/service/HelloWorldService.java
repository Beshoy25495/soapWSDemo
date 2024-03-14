package com.bwagih.soapWSDemo.service;

import com.bwagih.soapWSDemo.model.ServiceCommunicationModel;
import com.bwagih.soapWSDemo.model.generation.greeting.GetAgeResponse;
import com.bwagih.soapWSDemo.model.generation.greeting.GetBirthdayRequest;
import com.bwagih.soapWSDemo.model.generation.greeting.ObjectFactory;
import com.bwagih.soapWSDemo.utils.ServiceCommunicationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class HelloWorldService {


    public Integer getAge() {

        GetBirthdayRequest request = getGetBirthdayRequest();

        ServiceCommunicationModel<GetBirthdayRequest> communicationModel = getGetBirthdayRequestServiceCommunicationModel(request);

        GetAgeResponse response = ServiceCommunicationUtil.externalCommunicationApi(communicationModel);

        return response.getAge();
    }


    private static ServiceCommunicationModel<GetBirthdayRequest> getGetBirthdayRequestServiceCommunicationModel(GetBirthdayRequest request) {
        return ServiceCommunicationModel.<GetBirthdayRequest>builder()
                .uri("http://localhost:9696/ws/defaultWsdl11Definition")
                .soapAction("http://www.webservicesoap.org/birthday/getBirthdayRequest")
                .packagesToScan("com.bwagih.soapWSDemo.model.generation.greeting")
                .supportJAXBElementClass(true)
                .request(request)
                .build();

    }

    private static GetBirthdayRequest getGetBirthdayRequest() {
        ObjectFactory factory = new ObjectFactory();
        GetBirthdayRequest request = factory.createGetBirthdayRequest();
        request.setDay(25);
        request.setMonth(4);
        request.setYear(1995);
        return request;
    }

}