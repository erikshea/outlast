Steps to run non-deployed application:

*Download JavaFX 15 (https://gluonhq.com/products/javafx/), unpack

In Eclipse:
*Window->Preferences->Java->Build Path->User Libraries->New,
	then add the external jars in javafx's "lib" folder.
*Right-click on project->Properties->Java Build Path->Classpath->Add Library->User Library,
	then select your javafx library
*Right-click on project->Run As->Run Configurations, make "application.ImitamagochiMain" main class,
	then click on Arguments->VM arguments,
	then enter the following line, substituting "F:\dev\javafx-sdk-15\lib" with your own javafx lib dir:
--module-path "F:\dev\javafx-sdk-15\lib" --add-modules javafx.controls,javafx.fxml ${build_files}