import os
import schedule
import time
from dotenv import load_dotenv
from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

load_dotenv()
chrome_options = Options()
chrome_options.add_argument("--use-fake-ui-for-media-stream")

dir_path = os.path.dirname(os.path.realpath(__file__))

def quit_program():
    exit()

def join_class():
    link = RS2_LINK
    driver = webdriver.Chrome(ChromeDriverManager().install(), options = chrome_options)
    driver.get(link)

    try:
        element = WebDriverWait(driver, 5).until(
            EC.presence_of_element_located((By.CLASS_NAME, "confirm"))
        )
    except:
        pass

    audio = driver.find_element_by_class_name('confirm')
    audio.click()

    print('confirmed')

    try:
        element = WebDriverWait(driver, 5).until(
            EC.presence_of_element_located((By.CLASS_NAME, "confirm"))
        )
    except:
        pass

    video = driver.find_element_by_class_name('confirm')
    video.click()

    print('confirmed again')

    time.sleep(5100)
    driver.find_element_by_tag_name('body').send_keys(Keys.CONTROL + 'w')

    quit()

join_class()
