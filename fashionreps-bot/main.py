import praw
import os
import json
from praw.models import Message

dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path)


def loginReddit():
    r = praw.Reddit(
        username="", password="", client_id="", client_secret="", user_agent="FREPS Bot"
    )
    return r


def addUsersToMailingList(redditClient):
    users = []
    messagesToBeMarkedRead = []
    for messageUserSent in redditClient.inbox.all():
        if isinstance(messageUserSent, Message):
            # if messageUserSent.subject == "Subscribe":
            messagesToBeMarkedRead.append(messageUserSent)
            try:
                users.append(messageUserSent.author.name)
            except Exception:
                pass

    redditClient.inbox.mark_read(messagesToBeMarkedRead)

    users = list(set(users))  # Remove duplicates
    with open("users.json", "a+") as file:
        usersTemp = json.load(file)
        print(type(usersTemp))
        users += usersTemp
        json.dump(users, file)


def writeToUsers(redditClient):
    with open("users.json", "r") as file:
        users = json.load(file)

    print(users)


def writeToFile(nameOfFile, submissionTitle, submissionLink):
    with open("textfiles/" + nameOfFile + ".txt", "a", encoding="utf-8") as file:
        file.write(submissionTitle + " " + submissionLink + "\n")


def processPosts(redditClient):
    open("file.txt", "w").close()  # Clears last weeks posts
    for submission in redditClient.subreddit("FashionReps").top(
        limit=50, time_filter="week"
    ):
        if int(submission.score) > 50:
            title = submission.title
            url = submission.url
            if submission.link_flair_text == "REVIEW":
                writeToFile("review", title, url)
            if submission.link_flair_text == "NEWS":
                writeToFile("news", title, url)
            if submission.link_flair_text == "WDYWT":
                writeToFile("wdywt", title, url)
            if submission.link_flair_text == "FIND":
                writeToFile("find", title, url)
            if submission.link_flair_text == "DISCUSSION":
                writeToFile("discussion", title, url)
            if submission.link_flair_text == "SHITPOST":
                writeToFile("shitpost", title, url)


redditClient = loginReddit()
addUsersToMailingList(redditClient)
writeToUsers(redditClient)
processPosts(redditClient)


"""
To-DO

Add unsubscribe option
Add option to subscribe to particular types of messages (Shitpost/DISCUSSION/Others)


"""
