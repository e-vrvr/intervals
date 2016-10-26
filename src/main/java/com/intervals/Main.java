package com.intervals;

import com.intervals.service.MainProcessor;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Main {

//        |_ _| \| |_   _| __| _ \ \ / /_\ | |  / __|
//         | || .` | | | | _||   /\ V / _ \| |__\__ \
//        |___|_|\_| |_| |___|_|_\ \_/_/ \_\____|___/
    public static void main(String[] args) throws IOException, JAXBException {
        printLogo();
        MainProcessor main = new MainProcessor();
        main.process(args);
    }

    private static void printLogo() {
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("  |_ _| \\| |_   _| __| _ \\ \\ / /_\\ | |  / __|");
        System.out.println("   | || .` | | | | _||   /\\ V / _ \\| |__\\__ \\");
        System.out.println("  |___|_|\\_| |_| |___|_|_\\ \\_/_/ \\_\\____|___/");
        System.out.println("-----------------------------------------------");
    }
}
