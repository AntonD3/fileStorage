## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Usage](#usage)

## General info
Application that allows store files information in the cloud, categorize them with tags and search through them.
## Technologies
Project created with:
* Java version: 11
* SpringBoot version: 2.4.1
* Maven
* Elasticsearch
	
## Usage
### How to build
Standalone jar
`./mvnw.cmd clean install`

Ready jar file you can find in 
`\target\`

### Start
`./mvnw.cmd spring-boot:run`

Also, you can run jar file after building.

You can edit application.properties files:
```
server.port
elasticsearch.host
elasticsearch.port
```

### Endpoints
The program designed as a rest api.
#### Upload
`POST /file`
```json
{
   "name": "file_name.ext",
   "size" : 121231                           # size in bytes
}
```
as a result of success you will receive status 200 and body:
```json
{
   "ID": "unique file ID"
}
```
#### Delete file
`DELETE  /file/{ID}`

If success it returns status 200 and body:
```json
{"success": true}
```
#### Assign tags to file
Also, tou can add tags to file:
`POST /file/{ID}/tags`
```json
["tag1", "tag2", "tag3"]
```
#### Remove tags from file
`DELETE /file/{ID}/tags`
```json
["tag1", "tag3"]
```
#### List files with pagination optionally filtered by tags and filename
`GET /file?tags=tag1,tag2,tag3&page=2&size=3`
Here:
* tags - [optional] list of tags to filter by. Only files containing ALL of supplied tags should return. If tags parameter is omitted - don't apply tags filtering i.e. return all files.
* page - [optional] the 0-based parameter for paging. If not provided use 0 (the first page)
* size - [optional] the page size parameter. If not passed use default value 10
* q - [optional] the filename substring parameter

Example of result:
```json
{
   "total": 25,
   "page": [
       {
          "id": "ID1",
          "name": "presentation.pdf",
          "size": 123123,
          "tags": ["work"]
       },
       {
          "id": "ID2",
          "name": "file.mp3",
          "size": 123123,
          "tags": ["audio", "jazz"]
       },
       {
          "id": "ID3",
          "name": "film.mp4",
          "size": 123123,
          "tags": ["video"]
       }
   ]
}

```