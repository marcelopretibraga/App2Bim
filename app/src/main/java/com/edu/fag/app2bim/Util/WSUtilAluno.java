package com.edu.fag.app2bim.Util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

public class WSUtilAluno {

    private final static String NAMESPACE = "http://ws.webpncd.fasul.com/";
    private final static String URL = "http://192.168.0.108:8080/requestws/requestws";
    private final static String SOAP_ACTION = "http://ws.webpncd.fasul.com/";

    public static String consomeWS(String metodo, List<PropertyInfo> parametros){
        String resultado = "";
        try  {
            SoapObject request = new SoapObject(NAMESPACE, metodo);//montando o objeto de requisição
            //Recebo uma lista de Parametros e caso preenchida vou adicionar na Requisicao
            if (parametros != null)
                for(PropertyInfo prop : parametros)
                    request.addProperty(prop);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);//Envelope da requisição
            envelope.setOutputSoapObject(request);//Setando no caso o método no envelope de requisição
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);//HTTPTransport como HttpURLConnection do java nativo
            try {
                androidHttpTransport.call(SOAP_ACTION+metodo, envelope);//call = chamada da requisição
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();//Resposta
                resultado = resultsRequestSOAP.toString();
                System.out.println("Resultado............"+resultado);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

}

