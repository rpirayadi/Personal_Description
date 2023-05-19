# Personal Description
Personal Description is a one page mobile app to introduce yourself.

## Show toast for 10 seconds
In the onCreate method the showToastManyTimes is called to show the welcome message five times in a row. There is a boolean defined so the toast is only shown when the user enters the app not by configuration change.

![alt text](https://github.com/rpirayadi/Personal_Description/blob/master/readme%20images/toast.PNG)

In the showToastManyTimes method the toast is shown in a thread. Note that we can't use the UI thread and must start a new thread.


![alt text](https://github.com/rpirayadi/Personal_Description/blob/master/readme%20images/email.PNG)


## Send email
In order to send email, method sendEmail is implemented. It creates an intent named emailIntent and then the activity is started. Email Address is passed as an Extra. Note that the email address is defiened as a string resource.

![alt text](https://github.com/rpirayadi/Personal_Description/blob/master/readme%20images/email.PNG)

## Make phone call
It is also desired to type in the phone Number. This is done like sending the email.

![alt text](https://github.com/rpirayadi/Personal_Description/blob/master/readme%20images/phone.PNG)

## Reading paragraphs from json file

We need two functions to read the description paragraphs from json file. The first function is readJsonFile which reads the file and outputs a string. The second one is parseJsonStringToJSONArray which turns this string to Json array.

![alt text](https://github.com/rpirayadi/Personal_Description/blob/master/readme%20images/read.PNG)


## Add paragraphs to layout

Now we need to create textViews from the json array read in the previous section. The function addParagraphsToLayout iterates over different json objects in the json array and adds the views. In order to create the views it uses the createParagraphView method. createParagraphView creates a new view using Inflator and sets the texts defined by the json object.

![alt text](https://github.com/rpirayadi/Personal_Description/blob/master/readme%20images/paragraph.PNG)


## Dark mode/ Light mode

In the end dark mode is added to the application using a switch. Note that secondary colors are set in the themes files. Recreate method is also overwritten to remove the eye flicker.

![alt text](https://github.com/rpirayadi/Personal_Description/blob/master/readme%20images/day_night.PNG)

