*****************************************************
	       Warped Framework 2D 
*****************************************************

Welcome to the ReadMe file for Warped Framework 2D.
This library is intended for developers using the Eclipse IDE.
It can be used as a starting point for JVM desktop applications.

	  ----=== Requirements ===----
   - Java JDK 21 (probably compatible with java 8.0 but untested)
	https://www.oracle.com/au/java/technologies/downloads/

   - Eclipse IDE
	https://eclipseide.org/

      
	 ----=== Getting Started ===----      
Note : at this point you should already have installed the necessary requirments and now have eclipse IDE open.

If you want to make an app straight away you should follow the steps under Adding WF2D.
If you want to make changes to the framework you will need build the framework in eclipse follow the steps under Building WF2D.


	 ----=== Building WF2D ===----      	
   1. Click File -> Open Projects from File System.
   2. In the window that opens click the button Directory.
   3. Navigate to the directory containing this file.
   4. Finish
   5. The project should now be runable / editable.	


	 ----=== Adding WF2D ===----      	
Note : at this point you should have already created a new project and located the WF2D.jar file that you will be adding. (one should already be in the same directory as this readme go to lib/jar)
   1. Right click on your project and select Properties.
   2. In the window that opens click Java Build Path from the list on the left.
   3. On the Build Path Page select the Libraries tab at the top.
   4. Click on Modulepath
   5. Click on add JARs...
   6. Naviagte to 'THISDIRECTORY/lib/jar/JAR' and select ok.
	replace THISDIRECTORY with the directory that this readme is contained in .
	replace JAR with the jar version of the framework that you want to use
	replace JAR with each of the required librarys (WF2Dversion ,  javafx.base , javafx.controls , javafx.fxml , javafx.graphics , javafx.media , javafx.swing , javafx.web , javafx-swt).
   7. Under Modulepath select JRE System Library and expand the options.
   8. Select Native library loctaion and press edit.
   9. Naviagte to 'THISDIRECTORY/lib/native' and select ok.
	replace THISDIRECTORY with the directory that this readme is contained in .
   10. Press Apply and Close.
   11. In your project open the directory src and then open the text file module-info.
   12. This text file should contain the following text replacing YOURPROJECTNAME with the actual name of your project

	module YOURPROJECTNAME{
		requires WarpedFramework2D;
		requires javafx.base;
		requires javafx.controls;
		requires javafx.fxml;
		requires javafx.graphics;	
		requires javafx.swing;
		requires javafx.media;
	}


   
   13. The project should now be able to use the methods and functions from the framework. 
   14. To learn how to build an application with the fuctions in WarpedFramework go to https://www.youtube.com/@sumgamelab


   

