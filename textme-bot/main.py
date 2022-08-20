import yagmail
import praw
import keyring
import urllib.request
import os
import pyowm
import time
from selenium import webdriver
from selenium.webdriver.chrome.options import Options

dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path)


class TextBot:
    def __init__(self):
        self.redditClient = praw.Reddit(
            username="",
            password=keyring.get_password("reddit", "password"),
            client_id=keyring.get_password("reddit", "client_id"),
            client_secret=keyring.get_password("reddit", "client_secret"),
            user_agent="FREPS Bot",
        )
        self.imagePhoneNum = ""
        self.gmailClient = yagmail.SMTP("")
        self.weatherClient = pyowm.OWM(keyring.get_password("pyowm", "api"))

    def getEarthPorn(self):
        for submission in self.redditClient.subreddit("EarthPorn").top(
            limit=1, time_filter="day"
        ):
            imgUrl = submission.url
            imageName = imgUrl.split("/")[-1]
            urllib.request.urlretrieve(imgUrl, "img/" + imageName)
            imageName = "img/" + imageName

        return imageName

    def getTopNews(self):
        links = []
        for submission in self.redditClient.subreddit("Foodforthought").top(
            limit=1, time_filter="day"
        ):
            links.append(submission.url)
        for submission in self.redditClient.subreddit("TrueReddit").top(
            limit=1, time_filter="day"
        ):
            links.append(submission.url)
        for submission in self.redditClient.subreddit("worldnews").top(
            limit=1, time_filter="day"
        ):
            links.append(submission.url)
        links = self.outlineize(links)
        return links

    def outlineize(self, urls):
        options = Options()
        options.headless = True
        driver = webdriver.Chrome(
            dir_path + r"\chromedriver.exe"
        )  # chrome_options=options
        driver.get("https://outline.com/")

        compiledLinks = []

        for url in urls:
            inputBox = driver.find_element_by_class_name("source-input")
            inputBox.send_keys(url)
            submit = driver.find_element_by_class_name("clean")
            submit.click()
            time.sleep(10)
            if driver.current_url != "https://outline.com/":
                compiledLinks.append(driver.current_url)
            print(driver.current_url + " Current Url")
            driver.get("https://outline.com/")

        return compiledLinks

    def getShowerThought(self):
        for submission in self.redditClient.subreddit("ShowerThoughts").top(
            limit=1, time_filter="day"
        ):
            return submission.title

    def getWeather(self):
        observation = self.weatherClient.weather_at_id(4763793)  # Herndon
        w = observation.get_weather()
        avgTemp = w.get_temperature("fahrenheit").get("temp")
        status = w.get_status().lower()
        if "rain" in status:
            status = "rainy"
        finalStr = (
            "Today, it will be "
            + status
            + ", with an average temperature of "
            + str(avgTemp)
            + "."
        )
        return finalStr

    def compileContent(self):
        self.earthPornImage = self.getEarthPorn()
        self.showerThought = self.getShowerThought().strip()
        self.topNews = self.getTopNews()
        self.weather = self.getWeather()
        contents = "Shower Thought:" + "\n" + self.showerThought + "\n\n"
        contents = contents + "News:\n"
        for url in self.topNews:
            contents = contents + url + "\n\n"
        contents = contents + "Weather\n" + self.weather
        self.gmailClient.send(
            to=self.imagePhoneNum, subject="", contents=contents, newline_to_break=False
        )
        self.gmailClient.send(
            to=self.imagePhoneNum,
            subject="",
            attachments=self.earthPornImage,
            newline_to_break=False,
        )


client = TextBot()
client.compileContent()
