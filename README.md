JTrainer is an exam simulator I wrote couple of years ago in Java. See also [the project website](http://jtrainer.stankowic-development.net) for more details and screenshots.

Current version: **1.6** from 31th october 2012



#Changelog
- It is possible now to define less than 5 answers (*not needed answers are defined as __XXX_NOANS__ - see __dataset format__*)
- Only wrong answers are highlighted
- When opening a catalog the last used folder is pre-selected
- Radio button selection bug fixed - previous selections are deleted for new questions

#Execution
If Java and your desktop environment is configured properly is is sufficient to double click the JAR archive.
If this doesn't work for your environment it is also possible to execute JTrainer using the command line:
```
java -jar jtrainer.jar
```

#Catalog and dataset format
Catalog files need to follow a specified catalog and dataset format. Refer to the following sections and the example:

##Catalog format
```
Number of questions
Catalog name
Catalog description
Author <email>
Date
Time limit (if you don't want a limit to be set simply enter 0 here)
(Datasets...)
```

##Dataset format
```
Question
Type
Answer(s)
1 answer for type 0
5 answers for types 1 and 2 (not needed answers are defined as XXX_NOANS)
Number of correct answer (counting starts at 0!)
Hint (if you don't want a hint to be displays enter NONE here)
Explanation (if you don't want an explantion enter NONE here)
```

**Note:** You have to define one or five answers (*not needed answers can be marked as not-needed by suppliying __XXX_NOANS__*)

###Question types
There are three types of questions:
- **0**: Only one answer - has to be typed in
- **1**: Multiple answers, one is correct
- **2**: Multiple answers, multiple are correct

##Example catalog

```
4
Test catalog
Catalog for testing purposes
Christian Stankowic <info@stankowic-development.net>
31.10.2012
1
Please enter letter after A
0
B
NONE
NONE
Please select answer B and C
2
A
B
C
D
XXX_NOANS
1,2
Hint for question 2
Explanation for question 2
Please select answers D and E
2
A
B
C
D
E
3,4
Hint for question 3
Explanation for question 3
What's the result of 12 multiplied with 12?
1
153
144
192
120
112
1
The number is smaller than 180
Every kid knows that it's 144!
```

#Language file
It is possible to translate JTrainer into other languages.

##Usage
The english language file is included in the program. A different language file is loaded if it is named as "locale.txt" and exists in the same directory JTrainer is located. You can find additional langauge files on the project website or the source code (*__lang_file__ directory*).
It is not possible to use old language files for newer JTrainer releases - you'll always have to use the latest version of your language file.

##Format
If you want to create your own language file have a look at the ``localizationFile`` class or copy an existing language file. The file line defines the JTrainer version the language file was built for.

```
1.6
Author <email>
Translations
...
```

#Known bugs
- none so far :-)

#Notes
This software is published under the terms of GNU Public License 3.0 - please read the attached file ``LICENSE``.
This product uses symbols of the free symbol set "Famfamfam Silk" - see the following page for more information: http://www.famfamfam.com/lab/icons/silk

#Contact
Don't hesitate to contact me if you got any questions or problems. Feel free to use this software without any charges. I would be happy to get feedback and maybe bug reports by you.
Use the following contact data to get in contact with me:
Christian Stankowic <info@stankowic-development.net>
[http://jtrainer.stankowic-development.net](http://jtrainer.stankowic-development.net)
Follow [@stankowic_devel](https://www.twitter.com/stankowic_devel) on Twitter!
