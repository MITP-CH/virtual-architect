/*
     Copyright 2018 MITC or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package main.java.lambda.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.alexa.presentation.apl.RenderDocumentDirective;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelloWorldIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("HelloWorldIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Hello Virtual Architect";
       
        ObjectMapper mapper = new ObjectMapper();
        try {
	        TypeReference<HashMap<String, Object>> documentMapType = new TypeReference<HashMap<String, Object>>() {};
	        Map<String, Object> document = mapper.readValue(new File("main.json"), documentMapType);
	        TypeReference<HashMap<String, Object>> dataSourceMapType = new TypeReference<HashMap<String, Object>>() {};
	        Map<String, Object> dataSource = mapper.readValue(new File("datasources.json"), dataSourceMapType);
	        RenderDocumentDirective documentDirective = RenderDocumentDirective.builder()
	                .withToken("virtualarchitect")
	                .withDocument(document)
	                .withDatasources(dataSource)
	                .build();
	
	        return input.getResponseBuilder() 
	                .withSpeech(speechText)
	                // .withSimpleCard("MITC Virtual Architect", speechText)
	                .addDirective(documentDirective)
	                .build();
        }
        catch(IOException e) {

            throw new AskSdkException("Unable to read or deserialize apl data", e);
        }
    }

}
