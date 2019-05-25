package com.beliefrevision;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {
    private List<String> cnfSentences = new ArrayList<>();
    private int counter = 0;

    Util() {

        try (
                BufferedReader br = new BufferedReader(new FileReader("tests/CNF1"))) {
            for (String line; (line = br.readLine()) != null; ) {
                cnfSentences.add(line);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public String getRandomLine(){
        Random rand = new Random();

        return cnfSentences.get(rand.nextInt(cnfSentences.size()));
    }

    public String getNextLine() {
        counter++;
        String tmp = cnfSentences.get(counter);

        if(counter == cnfSentences.size()){
            counter = 0;
        }
        return tmp;
    }
}
