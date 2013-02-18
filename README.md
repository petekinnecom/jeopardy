Jeopardy
========

I've always loved having trivia questions ready for long car rides.  For long car rides where I'm by myself, I fire up this no-frills app to download and read me Jeopardy questions outloud.

Check out the [J!-Archive](http://j-archive.com/) to find an almost endless supply of trivia questions (ahem, answers.  Sorry Alex.)  The J-archive has no public API, so this app does some web-scraping and a touch of regexing to pull out the questions.  It then both presents them in an activity view and passes them to Android's text-to-speech processor to be read outloud.  

This app was written in a hustle before a long, lonely car ride.  Because of potential copyright issues, it's not going to the Play Store.  To install it, clone the repo, open the Eclipse project and push it your phone / tablet.  Because it's not meant for release, the UI is quite ugly, but it gets the job done!

Development notes:
-  The [webscraper](https://github.com/petekinnecom/jeopardy/blob/master/src/org/petekinnecom/jeopardy/MainActivity.java#L104) code is very fragile.  Ideally, the html should be processed by an XML parser.

