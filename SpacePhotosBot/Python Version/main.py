import praw
import config
import tweepy
import urllib
import time

# Log In Reddit


def loginReddit():
    r = praw.Reddit(username=config.username, password=config.password,
                    client_id=config.client_id, client_secret=config.client_secret, user_agent="Twitter Bot")
    return r

# Log in to twitter


def loginTwitter():
    auth = tweepy.OAuthHandler(config.consumer_key, config.consumer_secret)
    auth.set_access_token(config.access_token, config.access_token_secret)
    api = tweepy.API(auth)
    return api


comments = []


def getPhotoReddit(r):
    for submission in r.subreddit('SpacePorn').hot(limit=10):
        if 'i.redd.it' in submission.url:
            urllib.request.urlretrieve(submission.url, submission.id + ".jpg")
            return submission.id + ".jpg"


def uploadPhotoTwitter(api, filename):
    api.update_with_media(filename)


def main():
    r = loginReddit()
    img = getPhotoReddit(r)
    uploadPhotoTwitter(loginTwitter(), img)


main()
