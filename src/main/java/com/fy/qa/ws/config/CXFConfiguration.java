package com.fy.qa.ws.config;

import com.fy.qa.ws.imitate.impl.BillSignServiceImpl;
import com.fy.qa.ws.imitate.impl.INewEgyServerServiceImpl;
import com.fy.qa.ws.imitate.impl.InventoryInquiryServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class CXFConfiguration {

    @Bean
    public Endpoint endpointNewEgyServerService(Bus bus) {
        EndpointImpl endpoint = new EndpointImpl(bus, new INewEgyServerServiceImpl());
        endpoint.publish("/iscm-serving/NewEgyServerService");
        return endpoint;
    }
    @Bean
    public Endpoint endpointInventoryInquiryService(Bus bus) {
        EndpointImpl endpoint = new EndpointImpl(bus, new InventoryInquiryServiceImpl());
        endpoint.publish("/iscm-serving/InventoryInquiryServiceImpl");
        return endpoint;
    }

    @Bean
    public Endpoint endpointBillSignService(Bus bus) {
        EndpointImpl endpoint = new EndpointImpl(bus, new BillSignServiceImpl());
        endpoint.publish("/iscm-serving/BillSignServiceImpl");
        return endpoint;
    }
}
