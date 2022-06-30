3.0.4 [2018-07-18]

### Improvements
* Switched from Evernote Job Manager to Google Work Manager for improved support of background services
* Change build.gradle to have a more clear representation of library versions


### Fixes
* Displayed time of report is now adjusted for TimeZone/Location of device
* Retrieve Cognito ID at app start to prevent failures while retrieving Cognito ID during request
* Sharing a Point of Interest now shares Deep Link with address
* Update deep link URL to use gershad.com instead of m.gershad.com

3.0.5 [2018-08-02]

### Fixes
* Update Congito Configiuration with longer time out to prevent issues with less stable connections
* Convert report times to Asia/Tehran