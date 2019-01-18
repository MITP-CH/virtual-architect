/*
     Copyright 2018 MITC or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package virtualarchitect;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;

import virtualarchitect.handlers.CancelandStopIntentHandler;
import virtualarchitect.handlers.FallbackIntentHandler;
import virtualarchitect.handlers.HelloWorldIntentHandler;
import virtualarchitect.handlers.HelpIntentHandler;
import virtualarchitect.handlers.LaunchRequestHandler;
import virtualarchitect.handlers.SessionEndedRequestHandler;
import virtualarchitect.handlers.IntroIntentHandler;

import com.amazon.ask.SkillStreamHandler;

public class VirtualArchitectStreamHandler extends SkillStreamHandler {

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

    public VirtualArchitectStreamHandler() {
        super(getSkill());
    }

}
