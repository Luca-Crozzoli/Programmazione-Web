
# 🌐 Project Web Programming 2020 🎓 UniTs

## Table of Contents
1. [🌐 Project requirements](#project-requirements)
2. [📚 Technologies Used](#technologies-used)
3. [⚙️ Installation](#installation)
4. [🚀 Usage](#usage)
5. [✨ Features](#features)
6. [📦 Dependencies](#dependencies)
7. [🛠️ Configuration](#configuration)
8. [📖 Documentation](#documentation)
9. [🔍 Examples](#examples)
10. [🐛 Troubleshooting](#troubleshooting)
11. [👥 Contributors](#contributors)
12. [📜 License](#license)


## 📝 Project Requirements

### 📜 Introduction
Develop a **web platform** to distribute documents to end clients, tracking their reading (corresponding to file download).

Documents can be uploaded via a **web interface** or through a **web service** (SOAP or REST, your choice). You must also create an essential client to demonstrate your web service.

> Elements marked with **"EXTRA:"** earn additional points (e.g., for honours) but are not strictly necessary.

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

## Technologies Used

### Back-End:
- **IntelliJ IDEA 2020**
- **Google Cloud SDK for Java 8**
- **Maven**
- **GAE Servlet Container**
- **RESTful Web Services** with HATEOAS paradigm using JAX-RS API with Jersey implementation
- **Swagger** for documentation compliant with OpenAPI
- **Google Mail API**
- **Auth0/java-jwt library** for programmatic security using JWT
- **GAE Datastore** NoSQL database with Objectify ORM
- File storage in DB using base64 encoding, files downloaded as octet-stream

### Front-End:
- **Visual Studio Code** (with Vetur extension)
- **VUE CLI**
- **BootstrapVue** dependency
- **Axios** HTTP client library for handling HTTP requests as promises

## Installation
To set up this project locally, follow these steps:
1. Clone the repository.
2. Install the required dependencies for both front-end and back-end.
3. Configure the necessary environment variables and API keys.
4. Deploy the application to Google App Engine if needed.

## Usage
### Essential REST Client:
- Client for logging in and uploading a document with a pre-registered uploader present in the GAE Datastore.
- **Username:** `up01`
- **Password:** `up01`
- For uploading a file for an already registered consumer, leave the name and email fields empty.

### Basic Admin User:
- **Username:** `admin@luca`
- **Password:** `Password123`
- A pre-registered uploader is available on the server to allow the execution of the REST Client.

### Live Version:
You can access the live version of the project [here](https://programmazioneweb2020-319008.oa.r.appspot.com/).

## Features
- Comprehensive RESTful API with HATEOAS paradigm
- Secure authentication using JWT
- NoSQL database integration with GAE Datastore
- Real-time file upload and download functionality
- Responsive front-end design using Vue.js and BootstrapVue

## Dependencies
- IntelliJ IDEA 2020
- Google Cloud SDK for Java 8
- Maven
- GAE Servlet Container
- JAX-RS API with Jersey
- Swagger
- Google Mail API
- Auth0/java-jwt
- GAE Datastore with Objectify ORM
- Visual Studio Code
- VUE CLI
- BootstrapVue
- Axios

## Configuration
Ensure to set up the following configurations:
- Google Cloud SDK credentials
- Database connection settings
- Mail API configuration
- JWT secret keys for authentication

## Documentation
The project uses Swagger for API documentation. Access the API documentation by navigating to the `/swagger-ui` endpoint on the live version of the application.

## Examples
Here are some example requests and responses for the REST API:

### Login Request:
```json
{
  "username": "up01",
  "password": "up01"
}
```




# Web Programming Project 2020

## Introduction
This project showcases a comprehensive web application leveraging various modern technologies for both back-end and front-end development. It highlights the implementation of RESTful web services, secure authentication, and a responsive front-end interface.



"""

