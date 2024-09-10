# Producer-Consumer
This mini project is a producer-consumer model with a GUI, we choose a number of producers and consumers with static variable in the **Main.java** file and also the iteration number    
the GUI displays with text lines how the producer-consumer is working (which consumer takes which producer and some additional informations)

## Build and Execute instructions
This mini project is used with **Eclipse**, the Java version is **JavaSE-1.6**  
Once you got a project with this version you can now add the directories except **toolbox** (Faut que tu test ça avant)
You can then add the directory **toolbox** to the external jars (https://stackoverflow.com/questions/3280353/how-to-import-a-jar-in-eclipse)  
Once its done you can choose to change the default static value of **PROD_NB, CONS_NB, ITER_NB** in the Main.java file  
You can then execute the **Main.java** file.

## Screenshots
Once you execute the main file you should get this :  
![Capture d’écran du 2024-09-10 11-04-43](https://github.com/user-attachments/assets/203c92e7-4523-4c66-b9f8-b3154358cb37)  

When you start it with **"Démarrer"** there are some cases where there will be a **frozen detection**, it means that there is a deadlock situation between the producer and consumer, it shows this :  
![Capture d’écran du 2024-09-10 11-05-05](https://github.com/user-attachments/assets/2a8b7155-1d82-47ec-b8e3-33fb70b36f75)

You can force the program to end with the button **"Exterminer"**, it will kill all the producers and consumers that are left :  
![Capture d’écran du 2024-09-10 11-05-16](https://github.com/user-attachments/assets/8f3746f8-19d8-4b0c-aae9-51aae25b9776)
