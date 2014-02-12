/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterstocks;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Bradley Holloway
 */
public class VectorPairing {

    public static void dotProductWeighting(Indicator indicator, int iterations, boolean percentBased, boolean iterationGraphing) {
        System.out.println("\nBeginning Vector Analysis on " + indicator.getName() + ".");
        WordWeightTable wt = new WordWeightTable();
        //Example of How to lod in Data using the GSON loaders (REDO WHEN INDICATOR CHANGES)
        HashMap<String, float[]> ZVectors = (percentBased) ? Database.getGSONPerMap(indicator.getName()) : Database.getGSONMap(indicator.getName());
        float[] indicatorPerData = Database.getGSONIndicator(indicator.getName());
        //double[][] indicatorData = Database.getIndicatorGraph(indicator);
        //float[] indicatorPerData = Database.getIndicatorVector(indicatorData);

        //Example of How to load in the Percent GSON data

        //HashMap<String, float[]> percentZVectors = Database.getGSONPerMap(indicator.getName());
        //float[] indicatorPerData = Database.getGSONIndicatorPer(indicator.getName());
        float[] tempData;
        float[] goal = copy(indicatorPerData);
        final float[] finalGoal = copy(goal);
        float[] total = new float[goal.length];
        String bestWord = "";
        double tempDistance;

        for (int iteration = 0; iteration < iterations; iteration++) {
            double minimumDistance = Double.MAX_VALUE;
            //System.out.println("Searching for closest vector... Iteration: " + iteration);
            for (String word : Database.RevWords) {
                tempData = ZVectors.get(word);
                if (ZVectors.get(word) != null) {
                    tempDistance = Compare.distanceBetweenScaled(goal, tempData);
                    if (tempDistance < minimumDistance) {
                        minimumDistance = tempDistance;
                        bestWord = word;
                        //System.out.println("New Best: " + bestWord);
                    }
                }
            } //System.out.println("Done.");
            //System.out.println(bestWord + ", Weighted at: " + Compare.correctScale(goal, wordVectors.get(bestWord)));
            tempData = ZVectors.get(bestWord);
            if (!containsNANInfinity(tempData)) {
                wt.add(bestWord, Compare.correctScale(goal, tempData));
                if (iterationGraphing) {
                    Grapher.createGraph(total, finalGoal, "iterations\\total\\" + indicator.getName() + "\\iteration" + iteration);
                    Grapher.createGraph(Compare.multiply(tempData, Compare.correctScale(goal, tempData)), goal, "iterations\\temporary\\" + indicator.getName() + "\\iteration" + iteration);
                }
                total = Compare.add(total, Compare.multiply(tempData, Compare.correctScale(goal, tempData)));
                goal = Compare.getDifference(finalGoal, total);
                //Grapher.createGraph(total, finalGoal, "Vector" + bestWord + "CTG" + iteration);
            }
        }
        Grapher.createGraph(total, finalGoal, "Vector" + indicator.getName() + "Approximation");
        System.out.println("Results for " + indicator.getName() + " for " + iterations + " iterations.");
        System.out.println(wt);
    }
    private static boolean containsNANInfinity(float[] tempData)
    {
        for (float f : tempData)
        {
            if((""+f).equals(""+Float.NaN) || (""+f).equals(Float.NEGATIVE_INFINITY) || (f+"").equals(Float.POSITIVE_INFINITY))
            {
                return true;
            }
        }
        return false;
    }

    private static float[] copy(float[] data) {
        float[] returns = new float[data.length];
        System.arraycopy(data, 0, returns, 0, data.length);
        return returns;
    }
}
