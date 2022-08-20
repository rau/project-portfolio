import tweepy
from twilio.rest import Client
import time
import re


auth = tweepy.OAuthHandler()
auth.set_access_token()

twitterClient = tweepy.API(auth)

account_sid = ""
auth_token = ""

twilioClient = Client(account_sid, auth_token)

chipotleCode = ""
while True:
    tweets = twitterClient.user_timeline(id="", count=1)
    tweetText = tweets[0].text.lower()
    tweetText = re.sub(r"[^A-Za-z0-9 ]+", "", tweetText)
    allTweetWords = tweetText.split()
    if (
        "text" in allTweetWords
        and chipotleCode != allTweetWords[allTweetWords.index("text") + 1].upper()
    ):
        chipotleCode = allTweetWords[allTweetWords.index("text") + 1].upper()

    message = twilioClient.messages.create(to="", from_="", body=chipotleCode)

    time.sleep(10)
