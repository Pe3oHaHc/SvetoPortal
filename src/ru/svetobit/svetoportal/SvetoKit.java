package ru.svetobit.svetoportal;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.svetobit.svetoportal.settings.Settings;

import java.util.ArrayList;

public class SvetoKit extends Application
{
    final public static String PROJECT_FOLDER = "project/testsite";
    final private static ArrayList<Project> mProjects = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Settings.loadSettings();

        mProjects.add(new Project());
    }

    public static void closeProject(Project project)
    {
        project.mProjectWindow.close();
        mProjects.remove(project);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
