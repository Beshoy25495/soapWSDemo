package com.bwagih.soapWSDemo.utils;

import com.bwagih.soapWSDemo.model.ServiceCommunicationModel;
import lombok.experimental.UtilityClass;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.net.ssl.SSLContext;
import javax.xml.bind.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;


@UtilityClass
public class ServiceCommunicationUtil {

    public static <REQ, RES> RES externalCommunicationApi(ServiceCommunicationModel<REQ> communicationModel) {

        WebServiceTemplate serviceTemplate = webServiceTemplate(communicationModel);

        RES returnObject = (RES) serviceTemplate.marshalSendAndReceive(
                communicationModel.getUri(),
                communicationModel.getRequest(),
                new SoapActionCallback(communicationModel.getSoapAction()));

        return returnObject;
    }


    public static <REQ> WebServiceTemplate webServiceTemplate(ServiceCommunicationModel<REQ> communicationModel) {
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

        webServiceTemplate.setMarshaller(jaxb2Marshaller(communicationModel));
        webServiceTemplate.setUnmarshaller(jaxb2Marshaller(communicationModel));

        webServiceTemplate.setFaultMessageResolver(new CustomFaultMessageResolver());
        webServiceTemplate.setInterceptors(new ClientInterceptor[]{new CustomClientInterceptor()});
//        webServiceTemplate.setMessageSender(configureWebServiceTemplate("test", "test"));


        return webServiceTemplate;
    }

    public static <REQ> Jaxb2Marshaller jaxb2Marshaller(ServiceCommunicationModel<REQ> communicationModel) {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();

        jaxb2Marshaller.setPackagesToScan(communicationModel.getPackagesToScan());
        jaxb2Marshaller.setMarshallerProperties(communicationModel.getMarshallerProperties());
        jaxb2Marshaller.setJaxbContextProperties(communicationModel.getJaxbContextProperties());
        jaxb2Marshaller.setUnmarshallerProperties(communicationModel.getUnmarshallerProperties());
        jaxb2Marshaller.setSupportJaxbElementClass(communicationModel.isSupportJAXBElementClass());

        return jaxb2Marshaller;
    }


    public static <RES> RES convertFromXmlToPojoClass(String data, Class<RES> instance) throws JAXBException {
        Unmarshaller unmarshaller = JAXBContext.newInstance(instance).createUnmarshaller();
        JAXBElement<RES> element = (JAXBElement<RES>) unmarshaller.unmarshal(new StringReader(data));
        return element.getValue();
    }

    public static <REQ> String convertFromPojoClassToXml(REQ data, Class<REQ> instance) throws JAXBException {
        Marshaller marshaller = JAXBContext.newInstance(instance).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8);
        StringWriter writer = new StringWriter();
        marshaller.marshal(data, writer);
        return writer.toString();
    }


    public HttpComponentsMessageSender configureWebServiceTemplate(String user, String pwd) {

        RequestConfig requestConfig = RequestConfig.custom().setStaleConnectionCheckEnabled(true).build();
        SSLContext sslContext = SSLContexts.createDefault();
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                NoopHostnameVerifier.INSTANCE // Disables hostname verification
        );
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pwd));

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setSSLSocketFactory(sslSocketFactory)
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();


        return new HttpComponentsMessageSender(httpClient);

    }


}
