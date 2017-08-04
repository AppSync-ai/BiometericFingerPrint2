package com.biometrics.rssolution.soapreq;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.util.Log;

public class EnrollStudentTask extends
		AbstractProgressableAsyncTask<SoapObject, String> {

	private static final String TAG = "UpdateStudentDetails";
	// private static final String WSDL_URL =
	// "http://www.sdi.gov.in/sdipaservice/dgetservice.asmx?WSDL";
	// private static final String WS_NAMESPACE = "http://tempuri.org/";
	// private static final String WS_METHOD_NAME = "GetStudentDetails";
	//http://110.172.171.190/DGETSERVICE/dgetservice.asmx?WSDL
	private static final String WSDL_URL = "http://www.sdi.gov.in/sdipaservice/dgetservice.asmx?WSDL";
	private static final String WS_NAMESPACE = "http://tempuri.org/";
	private static final String WS_METHOD_NAME = "UpdateStudentDetails";
	private static final Context Context = null;
	private String StudentFingerSampleOne;
	private String StudentId;
	private String VTPRegNo;
	private String StudentFingerSampleSecond;
	private String StudentPhoto;
	private String BatchId;

	public EnrollStudentTask(String StudentId, String VTPRegNo, String BatchId,
			String StudentPhoto, String StudentFingerSampleOne,
			String StudentFingerSampleSecond) {

		this.StudentId = StudentId;
		this.VTPRegNo = VTPRegNo;
		this.BatchId = BatchId;
		this.StudentFingerSampleOne = StudentFingerSampleOne;
		this.StudentFingerSampleSecond = StudentFingerSampleSecond;
		//this.StudentPhoto = StudentPhoto;
		this.StudentPhoto = "dssf";

	}

	public SoapObject createRequest() {
		SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

		PropertyInfo property = new PropertyInfo();
		property.setNamespace(WS_NAMESPACE); // to ensure that the element-name
												// is prefixed with the
//		Log.i("print", "fp1" + StudentFingerSampleOne);
//		Log.i("print", "fp2" + StudentFingerSampleSecond);// namespace
		property.setName("StudentID");
		property.setValue(StudentId);
		request.addProperty(property);
		
		
		PropertyInfo property1 = new PropertyInfo();
		property1.setNamespace(WS_NAMESPACE); // to ensure that the element-name
												// is prefixed with the
		property1.setName("VTPRegNo");
		property1.setValue(VTPRegNo);
		request.addProperty(property1);
		
		PropertyInfo property2 = new PropertyInfo();
		property2.setNamespace(WS_NAMESPACE); // to ensure that the element-name
												// is prefixed with the
		property2.setName("BatchID");
		property2.setValue(BatchId);
		request.addProperty(property2);
		
		
		PropertyInfo property3 = new PropertyInfo();
		property3.setNamespace(WS_NAMESPACE); // to ensure that the element-name
												// is prefixed with the
		property3.setName("StudentPhoto");
		property3.setValue(StudentPhoto);
		request.addProperty(property3);
		
		PropertyInfo property4 = new PropertyInfo();
		property4.setNamespace(WS_NAMESPACE); // to ensure that the element-name
												// is prefixed with the
		property4.setName("StudentFingerSampleOne");
		property4.setValue(StudentFingerSampleOne);
		request.addProperty(property4);
		
		PropertyInfo property5 = new PropertyInfo();
		property5.setNamespace(WS_NAMESPACE); // to ensure that the element-name
												// is prefixed with the
		property5.setName("StudentFingerSampleSecond");
		property5.setValue(StudentFingerSampleSecond);
		request.addProperty(property5);

		return request;
	}

	@Override
	protected String performTaskInBackground(SoapObject parameter)
			throws Exception {
		// 1. Create SOAP Envelope using the request
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(parameter);
		Log.i("nik", "param:" + parameter);

		// 2. Create a HTTP Transport object to send the web service request
		HttpTransportSE httpTransport = new HttpTransportSE(WSDL_URL, 12000);
		httpTransport.debug = true; // allows capture of raw request/respose in
									// Logcat

		// 3. Make the web service invocation
		httpTransport.call(WS_NAMESPACE + WS_METHOD_NAME, envelope);

		Log.d(TAG, "HTTP REQUEST:\n" + httpTransport.requestDump);
		Log.d(TAG, "HTTP RESPONSE:\n" + httpTransport.responseDump);
		//
		String   result2="-50" ;
		if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			
			Log.i("Result........................", soapObject.toString());
			result2 = parseSOAPResponse(soapObject);
		} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
															// FAILURE
			SoapFault soapFault = (SoapFault) envelope.bodyIn;
			throw new Exception(soapFault.getMessage());
		}
		
		try
		{
         com.mantra.mfs100.Constants.EnrollStatus=result2;
		}
		catch (Exception sf)
		{}
		return result2 ;
		
		
		
		
		
	}

	private String   parseSOAPResponse(SoapObject response) {
	//int isSuccess = Integer
			//.parseInt(response
				//	.getPrimitivePropertySafelyAsString("UpdateStundentDetailsResult"));

		String isSuccess =response.getPrimitivePropertySafelyAsString("UpdateStundentDetailsResult");
		
		return isSuccess;
	}
}
