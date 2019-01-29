from __future__ import print_function
import urllib
import json 
import requests 
import base64
import boto3

# --- LeanIX API call to fetch the application description from the DB ---

def leanix_query_desc(appname):

    api_token = 'zaU8S2nVrg5NfMrazAU7S8DGNJr6qEzKfSgAsUOW'
    auth_url = 'https://app.leanix.net/services/mtm/v1/oauth2/token'
    request_url = 'https://app.leanix.net/services/pathfinder/v1/factSheets/graphql' 
    x = 'apitoken:'+api_token
    xb64 = base64.b64encode(x.encode())
    basic = 'Basic '+xb64.decode()
    data={'Content-Type':'application/x-www-form-urlencoded','Authorization':basic}

    # Get the bearer token - see https://dev.leanix.net/v4.0/docs/authentication
    response = requests.post(url=auth_url, data="grant_type=client_credentials", headers=data)
    response.raise_for_status() 

    access_token = response.json()['access_token']
    auth_header = 'Bearer ' + access_token
    data2 = {'Content-Type': 'application/json', 'Authorization': auth_header}

    # General function to call LeanIX API given a query

    response = requests.get(url=request_url, headers=data2)
    response.raise_for_status()
    return response.json()

# app=call()['data']
# print(app['name'] + " " + app['description'])

# --- DynamoDB API call to fetch the term description from the DB ---

def dynamodb_query_desc(term):
    return ""

# --- define LeanIX API query to pick up description for the factsheet name
def get_leanix_desc(intent):
    session_attributes = {}
   
    card_title = intent['slots']['factsheet']['value']
    print(card_title)
    speech_output = "Description from LeanIX API"
    reprompt_text = "Reprompt"
    should_end_session = False
    return build_response(session_attributes, build_speechlet_response(
        card_title, speech_output, reprompt_text, should_end_session))
     
#--- get term description from dynamoDB ---
def get_dynamodb_desc (intent):
    session_attributes = {}
    card_title = intent['slots']['term']['value']
    print(card_title)

    dynamodb = boto3.resource(
        'dynamodb', region_name='us-west-2', endpoint_url="http://localhost:8000")

    table = dynamodb.Table('Movies')
    response = table.query(
    KeyConditionExpression=Key('year').eq(1985)
)
    speech_output = "Description from DynamoDB"
    reprompt_text = "Reprompt"
    should_end_session = False
    return build_response(session_attributes, build_speechlet_response(
        card_title, speech_output, reprompt_text, should_end_session))

# --------------- Helpers that build all of the responses ----------------------
 
def build_speechlet_response(title, output, reprompt_text, should_end_session):
    return {
        'outputSpeech': {
            'type': 'PlainText',
            'text': output
        },
        'card': {
            'type': 'Simple',
            'title': "SessionSpeechlet - " + title,
            'content': "SessionSpeechlet - " + output
        },
        'reprompt': {
            'outputSpeech': {
                'type': 'PlainText',
                'text': reprompt_text
            }
        },
        'shouldEndSession': should_end_session
    }
 
def build_response(session_attributes, speechlet_response):
    return {
        'version': '1.0',
        'sessionAttributes': session_attributes,
        'response': speechlet_response
    }
 
# --------------- Functions that control the skill's behavior ------------------
 
def get_welcome_response():
    """ If we wanted to initialize the session to have some attributes we could
    add those here
    """
 
    session_attributes = {}
    card_title = "MIT Virtual Architect"
    speech_output = "Welcome to Virtual Enterprise Architect " \
                    "Which term you would like to get described by me? " 
    # If the user either does not reply to the welcome message or says something
    # that is not understood, they will be prompted again with this text.
    reprompt_text = "Name a term that you'd like to get more information on."
    should_end_session = False
    return build_response(session_attributes, build_speechlet_response(
        card_title, speech_output, reprompt_text, should_end_session))
 
def handle_session_end_request():
    card_title = "Session Ended"
    speech_output = "OK, connection will be terminated. "
    # Setting this to true ends the session and exits the skill.
    should_end_session = True
    return build_response({}, build_speechlet_response(
        card_title, speech_output, None, should_end_session))

#--------------- Events ------------------
 
def on_session_started(session_started_request, session):
    """ Called when the session starts """
 
    print("on_session_started requestId=" + session_started_request['requestId']
          + ", sessionId=" + session['sessionId'])
 
def on_launch(launch_request, session):
    """ Called when the user launches the skill without specifying what they
    want
    """
    print("on_launch requestId=" + launch_request['requestId'] +
          ", sessionId=" + session['sessionId'])
    # Dispatch to your skill's launch
    return get_welcome_response()

def on_intent(intent_request, session):
    """ Called when the user specifies an intent for this skill """
 
    print("on_intent requestId=" + intent_request['requestId'] +
          ", sessionId=" + session['sessionId'])
 
    intent = intent_request['intent']
    intent_name = intent_request['intent']['name']
 
    # Dispatch to your skill's intent handlers        
    if intent_name == "GlossaryIntent":
        return get_dynamodb_desc(intent)
    elif intent_name == "LeanIXIntent":
        return get_leanix_desc(intent)
    elif intent_name == "AMAZON.HelpIntent":
        return get_welcome_response()
    elif intent_name == "AMAZON.CancelIntent" or intent_name == "AMAZON.StopIntent":
        return handle_session_end_request()
    else:
        raise ValueError("Invalid intent")

def on_session_ended(session_ended_request, session):
    """ Called when the user ends the session.
 
    Is not called when the skill returns should_end_session=true
    """
    print("on_session_ended requestId=" + session_ended_request['requestId'] +
          ", sessionId=" + session['sessionId'])
    # add cleanup logic here
 
# --------------- Main handler ------------------
 
def lambda_handler(event, context):
    """ Route the incoming request based on type (LaunchRequest, IntentRequest,
    etc.) The JSON body of the request is provided in the event parameter.
    """
    print("event.session.application.applicationId=" +
          event['session']['application']['applicationId'])
 
    """
    Uncomment this if statement and populate with your skill's application ID to
    prevent someone else from configuring a skill that sends requests to this
    function.
    """

    if (event['session']['application']['applicationId'] !=
        "amzn1.ask.skill.e84a2290-db26-45df-88bb-8fc7683dbdd4"):
         raise ValueError("Invalid Application ID")
 
    if event['session']['new']:
        on_session_started({'requestId': event['request']['requestId']},
                           event['session'])
 
    if event['request']['type'] == "LaunchRequest":
        return on_launch(event['request'], event['session'])
    elif event['request']['type'] == "IntentRequest":
        return on_intent(event['request'], event['session'])
    elif event['request']['type'] == "SessionEndedRequest":
        return on_session_ended(event['request'], event['session'])
