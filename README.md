# vCard_pl
This program runs server which is searching for workers of Technological University of Łódź and then it is generating vcard to the one which you choose.

GET | /search/{user} | User is a string that matches workers family name or/and given name

GET | /download/{id} | After searching for workers you should have theirs id which matches id parameter
