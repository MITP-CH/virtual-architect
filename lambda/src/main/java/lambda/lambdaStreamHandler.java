/*
     Copyright 2018 MITC or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package main.java.lambda;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.SkillStreamHandler;
import main.java.lambda.handlers.CancelandStopIntentHandler;
import main.java.lambda.handlers.FallbackIntentHandler;
import main.java.lambda.handlers.HelloWorldIntentHandler;
import main.java.lambda.handlers.HelpIntentHandler;
import main.java.lambda.handlers.LaunchRequestHandler;
import main.java.lambda.handlers.SessionEndedRequestHandler;
import main.java.lambda.handlers.IntroIntentHandler;

public class lambdaStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new HelloWorldIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler(),
                        new IntroIntentHandler())
                // Add your skill id below
                .withSkillId("amzn1.ask.skill.e84a2290-db26-45df-88bb-8fc7683dbdd4")
                .build();
    }

    public lambdaStreamHandler() {
        super(getSkill());
    }

}
