**Danish**: 01/10-2011 : Downloaderen virker nu igen, men sikkert kortvarigt hvis tv2 får færten ;-) TV2 havde ødelagt vores system lidt med deres PgP key billede, og diverse andre forstyrrende elementer. Men det skulle være fixet, således at vi fjerner alt som ikke er relevant for vores gøremål. God fornøjelse!


---


**Danish**: 30/12-2010 : Downloaderen virker igen (men sikkert kortvarigt), men det virker som om tv2 har indført et "blacklist" system, der gør at vi kun kan hente ca. 50 plader per uge. På nuværende tidspunkt ved vi ikke hvordan dette blacklist system virker.


---


## Introduction ##
The danish television company TV2 hosts a bingo banko game every saturday, where players can download any number of game "boards", and play while watching the tv.

The usual way of playing is to print a few sheets of paper, each having 3 boards on it, and use your pencil to mark the numbers as they pop up on the screen during the show.

If you win, you call the studio and hope your lucky...

This application changes this way of playing somewhat.

Instead of printing, you start the "downloader" usually some hours before the show starts. When the show begins, you stop the downloader and start the "Game Client". The game client will then load all the boards that was downloaded (up to 1050 boards), and ask you to key in the numbers as they appear up on the screen during the show.

The game client will show you which boards that are close to winning, and show you the control codes that you need to supply when you call.

This way, you can be pretty sure that you WILL win all 3 games (1 row, 2 rows and full board), but as you'll soon learn, it's hard to get through to the studio.

## Purpose ##
The main purpose of this project is to force TV2 to change the game mechanics, to make it fair for all players, also players that don't know how to code utilities like this one.

## Howto ##
This project has 2 "applications" that can be used to help you play the bingobanko game.

BoardDownloader (downloader.bat) : Start this to automatically download the game "boards" from the TV2 site, places them in a data directory. The application does OCR recognition on the images, and creates 2 data files with the numbers. One is a textual file, for testing purposes, that is human readable, and a data file that is binary which will be loaded by the GameApp.

GameApp (gameclient.bat) : Start this when you are ready to play the game (saturday evening). Reads all the downloaded boards, and asks you to input the "numbers" from the television show. It shows you which numbers you need in order to get bingobanko on 1, 2 and full board.

The boarddownloader should be started some time before the game commences, in order to download as many boards as possible. If you do, it usually means that you will in all 3 games.

So, with this program your job is to be fast on the phone.. to get through to the studio and collect your prices!

Good luck!

--michael.toft@gmail.com