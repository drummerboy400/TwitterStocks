/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterstocks;

import java.util.HashMap;

/**
 *
 * @author Bradley Holloway
 */
public class PercentDriver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Database.load();
        int iterations = 1000;

        Indicator indicator = Database.indicators.get(0);

        //Example of How to lod in Data using the GSON loaders (REDO WHEN INDICATOR CHANGES)
        
        //HashMap<String, float[]> wordCountZVectors = Database.getGSONMap(indicator.getName());
        //float[] indicatorData = Database.getGSONIndicator(indicator.getName());
        
        //Example of How to load in the Percent GSON data
        
        HashMap<String, float[]> percentZVectors = Database.getGSONPerMap(indicator.getName());
        float[] indicatorPerData = Database.getGSONIndicatorPer(indicator.getName());
        float[] goal = copy(indicatorPerData);
        float[] finalGoal = copy(goal);
        float[] total = new float[goal.length];
        String bestWord = "";
        double tempDistance;
        
        for (int iteration = 0; iteration < iterations; iteration++) {
            double minimumDistance = Double.MAX_VALUE;
            //System.out.println("Searching for closest vector... Iteration: " + iteration);
            for (String word : Database.words) {
                if (percentZVectors.get(word) != null) {
                    tempDistance = Compare.distanceBetweenScaled(goal, percentZVectors.get(word));
                    if (tempDistance < minimumDistance) {
                        minimumDistance = tempDistance;
                        bestWord = word;
                        //System.out.println("New Best: " + bestWord);
                    }
                }
            } //System.out.println("Done.");
            //System.out.println(bestWord + ", Weighted at: " + Compare.correctScale(goal, wordVectors.get(bestWord)));
            total = Compare.add(total, Compare.multiply(percentZVectors.get(bestWord), Compare.correctScale(goal, percentZVectors.get(bestWord))));
            goal = Compare.getDifference(finalGoal, total);
            //Grapher.createGraph(total, finalGoal, "Vector" + bestWord + "CTG" + iteration);
        }
        Grapher.createGraph(total, finalGoal, "Vector" + indicator.getName() + "Approximation" + 10000);

        /*double[][] indicatorGraph = Database.getIndicatorGraph(Database.indicators.get(2));
         double[][] wordPercent = Database.getPercentOfWordGraph(" the ");
         System.out.println("Got Percents and Indicator.");
         double[][] result = Compare.allignWordPercent(indicatorGraph, wordPercent);
         double[][] iResult = Compare.allignIndicatorPercent(indicatorGraph, wordPercent);
         System.out.println("Done.");
         float[] vectorW = Compare.convertToVectorZ(result);
         float[] vectorI = Compare.convertToVectorZ(iResult);
         System.out.println("Done.");
         Grapher.createGraph(vectorW,vectorI, "Test Percents");
         Grapher.createGraph(vectorW, "Test PercentsW");
         Grapher.createGraph(vectorI, "Test PercentsI");
         System.out.println("Done.");*/
    }
    public static float[] copy(float[] data)
    {
        float[] returns = new float[data.length];
        System.arraycopy(data, 0, returns, 0, data.length);
        return returns;
    }
}