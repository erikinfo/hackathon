package io.swagger.ApplicationStart;

import io.swagger.CdsServiceTemplateApplication;
import io.swagger.api.CDSHooks;

public class ApplicationRunner {

    public static void main(String[] args) {
        // Run CdsServiceTemplateApplication
        Thread cdsThread = new Thread(() -> {
            try {
                CdsServiceTemplateApplication.main(args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        cdsThread.start();
        try {
            cdsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<< CdsServiceTemplateApplication DONE PROCESSING >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("\n");

        // Wait for 1 second
        sleep(1000);

        // Run CDSHooks
        Thread cdshooksThread = new Thread(() -> {
            CDSHooks.main(args);
        });
        cdshooksThread.start();
        try {
            cdshooksThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<< CDSHooks DONE PROCESSING >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("\n");

        // Wait for 1 second
        sleep(1000);


        // Change working directory to "ui"
        String uiDirectory = "path/to/ui";
        System.setProperty("user.dir", uiDirectory);
        System.out.println("\n");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<< DIRECTORY CHANGED >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("\n");

        // Run "npm start" command
        /*
        ProcessBuilder npmProcess = new ProcessBuilder("npm", "start");

        npmProcess.inheritIO();
        try {
            Process npm = npmProcess.start();
            npm.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

