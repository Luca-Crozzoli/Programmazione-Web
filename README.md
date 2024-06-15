# Reverting to the previous version with icons and providing the readme file for download.

readme_content = """
# 📄 Project Requirements 2018

## 🌐 Web Programming 2017-2018
### 🎓 UniTs

## 📝 Project Overview

### 📜 Introduction
Develop a **web platform** to distribute documents to end clients, tracking their reading (corresponding to file download).

Documents can be uploaded via a **web interface** or through a **web service** (SOAP or REST, your choice). You must also create an essential client to demonstrate your web service.

> Elements marked with **"EXTRA:"** earn additional points (e.g., for honors) but are not strictly necessary.

### 📂 Entities
There are two types of entities: **Files** and **Actors**. Actors are divided into:

- **👨‍💼 Administrators**: Platform managers
- **📤 Uploaders**: System users who can upload and distribute files
- **👥 Consumers**: End users, recipients of the files. The same Consumer can be managed by multiple Uploaders (e.g., one Uploader is my insurance, another is my bank).

Authentication will occur with **username-password** (EXTRA: you can use Firebase if desired, but it's not strictly necessary).

#### 👨‍💼 Administrators
When the platform starts for the first time, there will be only one administrator user. Each Administrator can:
- Change their password
- **EXTRA:** Change their information (except username)
- **EXTRA:** Create/modify/delete other administrators (except username)
- Create/modify/delete Uploaders
- View a report listing:
  - The number of documents uploaded by each Uploader
  - The number of different consumers to whom these documents relate

  The report will default to show data from the previous month, but the analysis period can be modified using two date fields (from...to).

##### Required Fields
- **Username:** 📧 Email address
- **Name/Surname:** 📝 Single field
- **Email:** 📧

#### 📤 Uploaders
Each Uploader can:
- Change their password
- Change their information (except username)
- Create/modify/delete Consumers
- Upload/delete files for a Consumer

##### Required Fields
- **Username:** 🔤 Four alphanumeric characters
- **Name/Surname:** 📝 Single field
- **Email:** 📧
- **Logo Image:** 🖼️

#### 👥 Consumers
Each Consumer can:
- Download their files
- Change their information (except username)

##### Required Fields
- **Username:** 🔢 Corresponds to the tax code
- **Name/Surname:** 📝 Single field
- **Email:** 📧

#### 📄 Files
Each file is uploaded by an Uploader and directed to a Consumer.

##### Required Fields
- **File Name:** 📝
- **Upload Date/Time:** 📅🕒
- **View Date/Time:** 📅🕒
- **IP Address** used for viewing: 🌐
- A string containing a list of **hashtags** 🏷️ (not predefined)

### 🖥️ Platform Functionality

#### 🖥️ Main Screen
The main screen shows a **username/password authentication request** at the center or a link to **register** in the system (usable only by Consumers).

#### 🖥️ Main Screen - Consumers
If the Consumer has received documents from multiple Uploaders, it shows the list of Uploaders who sent documents (logo + description); clicking on one displays the list of documents uploaded by them.

If the Consumer has received documents from only one Uploader, it directly shows the list of documents uploaded by them (in short, the Uploader choice screen is not shown).

#### 🖥️ Document List - Consumers
At the top left is the **Uploader's logo**, as if the system belonged to them. Documents are shown in a table, ordered from most recent to least recent, with unread ones always at the top. The table must show the document name, upload date, and any reading date by the Consumer.

This mask also shows the list of **hashtags** 🏷️ linked to the present documents, and the user can filter them by selecting the corresponding hashtag.

#### 🖥️ Main Screen - Uploaders
The Uploader sees a list of Consumers, with the possibility to create a new one or delete an existing one.

Clicking on a Consumer shows the uploaded files, including tags, view dates, and IP addresses used for viewing.

The uploader must be able to upload a new file (specifying name and list of hashtags) or delete an existing one; every time a file is uploaded, the Consumer receives a Notification (see the dedicated paragraph).

#### 🖥️ Main Screen - Administrators
Free to design as you wish.

### ✉️ Notification
Whenever the system receives a new file, it will send a notification email to the Consumer containing:
- The name of the Uploader who sent it
- The file name
- A link to the system's homepage
- A link to download the file directly

> Note: The system must record the file download even when it occurs from the confirmation email.

### 🔧 Web Service
The system must expose a web service to send a file to a Consumer. The method should receive as input:
- **Consumer's Tax Code:** 🆔
- **Consumer's Email:** 📧
- **Consumer's Name/Surname:** 📝
- **File Name:** 📝
- **HashTag:** 🏷️
- The file to upload 📄

If the Consumer already exists, it will only upload the file and send them a Notification (see the dedicated paragraph); otherwise, it will first create a new Consumer.
"""

readme_file_path = "/mnt/data/README_Project_Requirements_2018.md"

with open(readme_file_path, "w") as file:
    file.write(readme_content)

readme_file_path
