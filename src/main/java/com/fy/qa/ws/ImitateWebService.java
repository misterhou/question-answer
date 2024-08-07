package com.fy.qa.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(serviceName = "INewEgyServerServiceService", targetNamespace = "http://webService.com/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ImitateWebService {

    @WebMethod
    @WebResult String smartApiMethod(@WebParam(name = "code") String code, @WebParam(name = "infoString") String infoString);
}
