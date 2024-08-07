package com.fy.qa.ws.config;

import com.fy.qa.ws.imitate.impl.InventoryInquiryServiceImpl;
import com.fy.qa.ws.impl.ImitateWebServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class CXFConfiguration {

    @Bean
    public Endpoint endpoint(Bus bus) {
//        EndpointImpl endpoint = new EndpointImpl(bus, new ImitateWebServiceImpl());
//        endpoint.publish("/iscm-serving/NewEgyServerService");
        EndpointImpl endpoint = new EndpointImpl(bus, new InventoryInquiryServiceImpl());
        endpoint.publish("/iscm-serving/InventoryInquiryServiceImpl");
        return endpoint;
    }
}
