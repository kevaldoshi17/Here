
import requests
import json
import http.client, urllib.parse
import time
import os, time

url = 'http://d63fe6c43c57.ngrok.io' # Address of the OctoPrint Server
header = {'Content-Type': 'application/json'} #Basic request's header

subscriptionKey = '391f2303ab9f400ab6c16da6c954b439'

host = 'api.cognitive.microsoft.com'
path = '/bing/v7.0/localbusinesses/search'




def get_local():
    headers = {'Ocp-Apim-Subscription-Key': subscriptionKey}
    conn = http.client.HTTPSConnection (host)
    conn.request ("GET", path + params, None, headers)
    response = conn.getresponse ()
    return response.read ()

try:

    moddate1 = os.stat("sentences.json")[8]
    while True:
    ##########
        # Values to modify.

        # YOUR-APP-ID: The App ID GUID found on the www.luis.ai Application Settings page.
        appId = 'd593e6ae-4aa9-495a-81f6-84cbef15367e'

        # YOUR-PREDICTION-KEY: Your LUIS authoring key, 32 character value.
        prediction_key = '117303a031e64383901e363ae0b73e25'

        # YOUR-PREDICTION-ENDPOINT: Replace with your authoring key endpoint.
        # For example, "https://westus.api.cognitive.microsoft.com/"
        prediction_endpoint = 'https://shellhacks-luis.cognitiveservices.azure.com/'

        
        moddate2 = os.stat("sentences.json")[8]

        if moddate1 != moddate2:
            utterance = json.loads(requests.get(url + '/user_sentences', headers=header).text)
            requests.delete(url + '/response/1', headers=header)
            requests.delete(url + '/user_sentences/1', headers=header)
            

            final = (utterance[-1])['body']
            latitude = (utterance[-1])['latitude']
            longitude = (utterance[-1])['longitude']

            # The utterance you want to use.
            #utterance = 'I want two large pepperoni pizzas on thin crust please'
            ##########

            # The headers to use in this REST call.
            headers = {
            }

            # The URL parameters to use in this REST call.
            params ={
                'query': final,
                'timezoneOffset': '0',
                'verbose': 'true',
                'show-all-intents': 'true',
                'spellCheck': 'false',
                'staging': 'false',
                'subscription-key': prediction_key
            }


            # Make the REST call.
            response = requests.get(f'{prediction_endpoint}luis/prediction/v3.0/apps/{appId}/slots/production/predict', headers=headers, params=params)

            # Display the results on the console.
            if 'Places.Product' in (json.loads(response.text)['prediction']['entities']):
                entity = (json.loads(response.text)['prediction']['entities']['Places.Product'])[0]
            elif 'Places.Cuisine' in (json.loads(response.text)['prediction']['entities']):
                entity = (json.loads(response.text)['prediction']['entities']['Places.Cuisine'])[0]
            elif 'keyPhrase' in (json.loads(response.text)['prediction']['entities']):
                entity = (json.loads(response.text)['prediction']['entities']['keyPhrase'])[0]
            print(response.text)
        
            params = '?q=' + urllib.parse.quote (entity) + '&mkt=en-us' + '&localCircularView=' + str(latitude) + ',' + str(longitude) + ',5000'
            result = get_local()
            requests.post(url + '/response', headers=header,data=result)
            print(json.dumps(json.loads(result), indent=4))

            moddate1 = os.stat("sentences.json")[8]

        time.sleep(0.5)



except Exception as e:
    # Display the error string.
    print(f'{e}')
