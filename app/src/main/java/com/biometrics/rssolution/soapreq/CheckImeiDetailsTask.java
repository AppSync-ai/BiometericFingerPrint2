package com.biometrics.rssolution.soapreq;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class CheckImeiDetailsTask extends
		AbstractProgressableAsyncTask<SoapObject, Integer> {

	public  String Imei,Vtp,Descr;
	
	private static final String TAG = "VTPIMEICheck";
	// private static final String WSDL_URL =
	// "http://www.sdi.gov.in/sdipaservice/dgetservice.asmx?WSDL";
	// private static final String WS_NAMESPACE = "http://tempuri.org/";
	// private static final String WS_METHOD_NAME = "GetStudentDetails";
	//http://110.172.171.190/DGETSERVICE/dgetservice.asmx?WSDL
	
//	private static final String WSDL_URL = "http://www.sdi.gov.in/sdipaservice/dgetservice.asmx?WSDL";
//	private static final String WS_NAMESPACE = "http://tempuri.org/";
//	private static final String WS_METHOD_NAME = "InsertIMEIDetails";
	

	private static final String WSDL_URL = "http://onlinerealsoft.com/vtp.asmx?WSDL";
	private static final String WS_NAMESPACE = "http://tempuri.org/";
	private static final String WS_METHOD_NAME = "VTPIMEICheck";

	
	
	
	public CheckImeiDetailsTask(String imeino,String vtpno,String Desc) {
		
		this.Imei=imeino;
		this.Vtp=vtpno;
		this.Descr=Desc;

	}

	public  SoapObject createRequest()  {
		SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

		PropertyInfo property = new PropertyInfo();
		property.setNamespace(WS_NAMESPACE); // to ensure that the element-name
												// is prefixed with the
												// namespace
		property.setName("IMEINo");
		property.setValue(Imei);
		// PropertyInfo property1 = new PropertyInfo();
		// property.setNamespace(WS_NAMESPACE); // to ensure that the
		// element-name is prefixed with the namespace
		// property.setName("VtpRegNo");
		// property.setValue("422100284");
		// PropertyInfo property2 = new PropertyInfo();
		// property.setNamespace(WS_NAMESPACE); // to ensure that the
		// element-name is prefixed with the namespace
		// property.setName("BatchId");
		// property.setValue("22100284ICT70515006");
		request.addProperty(property);
		// request.addProperty("ImeiNo", "352496067116");

//		request.addProperty("VTPRegNo",Vtp);
//		request.addProperty("Description", Descr);
		return request;
	}

	@Override
	protected Integer performTaskInBackground(SoapObject parameter)
			throws Exception {
		// 1. Create SOAP Envelope using the request
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(parameter);
		Log.i("nik", "param:" + parameter);

		// 2. Create a HTTP Transport object to send the web service request
		HttpTransportSE httpTransport = new HttpTransportSE(WSDL_URL, 120000);
		httpTransport.debug = true; // allows capture of raw request/respose in
									// Logcat

		// 3. Make the web service invocation
		httpTransport.call(WS_NAMESPACE + WS_METHOD_NAME, envelope);

		Log.d(TAG, "HTTP REQUEST:\n" + httpTransport.requestDump);
		Log.d(TAG, "HTTP RESPONSE:\n" + httpTransport.responseDump);
		//
		Integer result = null;
		if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			Log.i("Result........................", soapObject.toString());
			result = parseSOAPResponse(soapObject);
		} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
															// FAILURE
			SoapFault soapFault = (SoapFault) envelope.bodyIn;
			throw new Exception(soapFault.getMessage());
		}

		// SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
		// SoapEnvelope.VER11);
		// envelope.dotNet=true;
		// envelope.setOutputSoapObject(request); .
		// HttpTransportSE httpTransport = new HttpTransportSE(URL);
		// httpTransport.debug = true;
		// try {
		// httpTransport.call(SOAP_ACTION, envelope);
		// SoapObject result = (SoapObject) envelope.bodyIn;
		// Log.i("Result........................", result.toString());

		// SoapSerializationEnvelope envp = new
		// SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envp.dotNet = true;
		// envp.setOutputSoapObject(parameter);
		// HttpTransportSE androidHttpTransport = new HttpTransportSE(WSDL_URL);
		// try {
		// androidHttpTransport.call(WS_NAMESPACE + WS_METHOD_NAME, envp);
		// if(((SoapPrimitive)envp.getResponse())!=null){
		// Log.i("WS success","yess");
		// // result=((SoapPrimitive)envp.getResponse());
		// }
		// } catch (Exception e) {
		// Log.i("WS Error->",e.toString());
		// }

		return result;
	}

	private Integer parseSOAPResponse(SoapObject response) {
//		CityForecastBO cityForecastResult = null;
//		SoapObject cityForecastNode = (SoapObject) response
//				.getProperty("InsertIMEIDetailsResponse");
		// see the wsdl for the definition of "ForecastReturn" (which can be
		// null/empty)
		// i.e. <s:element name="GetCityForecastByZIPResponse"> element
//		if (cityForecastNode != null) {
			// see <s:complexType name="ForecastReturn"> element for definition
			// of "ForecastReturn"

			// "Success" is a mandatory node, so no need to do any null checks.
			int isSuccess = Integer.parseInt(response
					.getPrimitivePropertySafelyAsString("VTPIMEICheckResult"));

//			 String responseText =
//			 cityForecastNode.getPrimitivePropertySafelyAsString("ResponseText");
//			 String state =
//			 cityForecastNode.getPrimitivePropertySafelyAsString("State");
//			 String city =
//			 cityForecastNode.getPrimitivePropertySafelyAsString("City");
//			 String weatherStationCity =
//			 cityForecastNode.getPrimitivePropertySafelyAsString("WeatherStationCity");
//			 cityForecastResult = new CityForecastBO(isSuccess, state, city,
//			 weatherStationCity, responseText);
//
//			Log.i(TAG,
//					String.format(
//							" --> isSuccess: %b, responseText: %s, state: %s, city: %s, weatherStationCity: %s.",
//							isSuccess));
//			 "ForecastResult" (an array) can be empty according to the wsdl
//			 definition. Therefore, we do a null check before processing
//			 any data.
//			 SoapObject forecastResultsNode = (SoapObject)
//			 cityForecastNode.getPropertySafely("StudentsList");
//			 if (forecastResultsNode != null) {
//			 // Definition for Forecast node can be found at <s:complexType
////			 name="Forecast"> element in wsdl
//			 for (int i = 0; i < forecastResultsNode.getPropertyCount(); i++)
//			 {
//			 SoapObject forecastNode = (SoapObject)
//			 forecastResultsNode.getProperty(i);
//			
//			 String date =
//			 forecastNode.getPrimitivePropertySafelyAsString("StudentName");
//			 String weatherId =forecastNode.getPrimitivePropertySafelyAsString("PermanentProfileNo");
//			 cityForecastResult=new CityForecastBO(date, weatherId);
////			 Short.parseShort(forecastNode.getPrimitivePropertySafelyAsString("PermanentProfileNo"));
//			 // description can be null
//			 // <s:element minOccurs="0" maxOccurs="1" name="Desciption"
////			 type="s:string"/>
////			 String description =
////			 forecastNode.getPrimitivePropertySafelyAsString("Desciption");
////			 ForecastInfo forecast = cityForecastResult.addForecast(date,
////			 weatherId, description);
//			
//			 // "Temperatures" and "ProbabilityOfPrecipiation" nodes are
////			 mandatory within a "Forecast" node
//			 // <s:element minOccurs="1" maxOccurs="1" name="Temperatures"
////			 type="tns:temp"/>
//			 // <s:element minOccurs="1" maxOccurs="1"
////			 name="ProbabilityOfPrecipiation" type="tns:POP"/>
//			 
//			 
////			 SoapObject temperatureNode =
////			 (SoapObject)forecastNode.getProperty("Temperatures");
////			 String moringLowTemp =
////			 temperatureNode.getPrimitivePropertySafelyAsString("MorningLow");
////			 String daytimeHighTemp =
////			 temperatureNode.getPrimitivePropertySafelyAsString("DaytimeHigh");
////			 forecast.setTemperature(moringLowTemp, daytimeHighTemp);
//			
////			 SoapObject precipicationProbabilityNode =
////			 (SoapObject)forecastNode.getProperty("ProbabilityOfPrecipiation");
////			 String nightTimePercentage =
////			 precipicationProbabilityNode.getPrimitivePropertySafelyAsString("Nighttime");
////			 String dayTimePercentage =
////			 precipicationProbabilityNode.getPrimitivePropertySafelyAsString("Daytime");
////			 forecast.setPrecipiationProbability(nightTimePercentage,
////			 dayTimePercentage);
			
//			 Log.i("nik","result code: :"+isSuccess);
//			 Log.i("nik","id :"+weatherId);
//			 }
//			 }
//		}

		return isSuccess;
	}
}
