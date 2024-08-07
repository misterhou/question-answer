package com.fy.qa.ws.imitate;


import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(serviceName = "InventoryInquiryServiceImpl", targetNamespace = "http://service.inventoryinquiry.api.serving.iscm.ls.com/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface InventoryInquiryService {

    @WebMethod
    @WebResult String ZMMWTK01_SSKC_001(String orgName);
}
