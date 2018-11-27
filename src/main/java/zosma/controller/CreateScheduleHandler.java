package zosma.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class CreateScheduleHandler implements RequestStreamHandler  {

	public LambdaLogger logger = null;

	// handle to our s3 storage
	private AmazonS3 s3 = AmazonS3ClientBuilder.standard()
			.withRegion("us-east-2").build();

	boolean useRDS = true;

	/*// Load from RDS, if it exists
 
	//@throws Exception 
	boolean createConstant(String name, double value) throws Exception {
		if (logger != null) { logger.log("in createConstant"); }
		ConstantsDAO dao = new ConstantsDAO();
		
		// check if present
		Constant exist = dao.getConstant(name);
		Constant constant = new Constant (name, value);
		if (exist == null) {
			return dao.addConstant(constant);
		} else {
			return dao.updateConstant(constant);
		}
	}*/

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		// TODO Auto-generated method stub

	}
}
