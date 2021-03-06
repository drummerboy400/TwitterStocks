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
        HashMap<String, ZVector> ZVectors = (percentBased) ? Database.getGSONPerMap(indicator.getName()) : Database.getGSONMap(indicator.getName());
        ZVector indicatorData = Database.getGSONIndicator(indicator.getName());
        //double[][] indicatorData = Database.getIndicatorGraph(indicator);
        //float[] indicatorPerData = Database.getIndicatorVector(indicatorData);

        //Example of How to load in the Percent GSON data

        //HashMap<String, float[]> percentZVectors = Database.getGSONPerMap(indicator.getName());
        //float[] indicatorPerData = Database.getGSONIndicatorPer(indicator.getName());
        float[] tempData;
        float[] goal = copy(indicatorData.getZData());
        final float[] finalGoal = copy(goal);
        final float[] dates = ZVectors.get("IDATES").getScaledData();
        float[] total = new float[goal.length];
        String bestWord = "";
        double tempDistance;

        for (int iteration = 0; iteration < iterations; iteration++) {
            double maxDistance = Double.MIN_VALUE;
            //System.out.println("Searching for closest vector... Iteration: " + iteration);
            for (String word : Database.RevWords) {
                if (ZVectors.get(word) == null) {
                } else {
                    tempData = copy(ZVectors.get(word).getZData());
                    if (ZVectors.get(word) != null) {
                        tempDistance = Math.abs(Compare.dotProduct(goal, tempData));
                        if (tempDistance > maxDistance) {
                            maxDistance = tempDistance;
                            bestWord = word;
                            //System.out.println(word+maxDistance);
                            //System.out.println("New Best: " + bestWord);
                        }
                    }
                }

            } //System.out.println("Done.");
            tempData = copy(ZVectors.get(bestWord).getZData());
            //System.out.println(bestWord + ", Weighted at: " + Compare.correctScale(goal, tempData));
            if (!containsNANInfinity(tempData)) {
                wt.add(bestWord, Compare.correctScale(goal, tempData));
                if (iterationGraphing) {
                    Grapher.createGraph(total, finalGoal, "iterations\\total\\" + indicator.getName() + "\\iteration" + iteration,dates);
                    Grapher.createGraph(Compare.multiply(tempData, Compare.correctScale(goal, tempData)), goal, "iterations\\temporary\\" + indicator.getName() + "\\iteration" + iteration,dates);
                }
                total = Compare.add(total, Compare.multiply(tempData, Compare.correctScale(goal, tempData)));
                goal = Compare.getDifference(finalGoal, total);
                //Grapher.createGraph(total, finalGoal, "Vector" + bestWord + "CTG" + iteration);
            }
        }
        Grapher.createGraph(total, finalGoal, "Vector" + indicator.getName() + "Approximation",dates);
        System.out.println("Results for " + indicator.getName() + " for " + iterations + " iterations.");
        System.out.println(wt);
    }
    public static void dotProductWeightingLimitedSequence(Indicator indicator, boolean percentBased, boolean iterationGraphing, double percentStart, double percentEnd, double increment, int predictionCorrelation)
    {
        System.out.println("\nBeginning Vector Analysis on " + indicator.getName() + ".");
        for(double percent = percentStart; percent <= percentEnd; percent = round(percent+increment, 4))
        {
            dotProductWeightingLimitedRegression(indicator, percentBased, iterationGraphing, percent, predictionCorrelation);
        }
    }
    public static void dotProductWeightingLimitedSequence(Indicator indicator, boolean percentBased, boolean iterationGraphing, int iterations, int increment, int prediction, int analysis)
    {
        System.out.println("\nBeginning Vector Analysis on " + indicator.getName() + ".");
        ZVector gsonIndicator = Database.getGSONIndicator(indicator.getName());
        for(int irrelevent = 0; irrelevent + prediction + analysis < gsonIndicator.getZData().length; irrelevent+=increment)
        {
            dotProductWeightingLimitedRegression(indicator, percentBased, iterationGraphing, iterations, irrelevent, prediction, analysis);
        }
    }

    public static void dotProductWeightingLimitedRegression(Indicator indicator, boolean percentBased, boolean iterationGraphing, double percentAnalyzed, int predictionCorrelation) {
        int iterations = 0;
        
        WordWeightTable wt = new WordWeightTable();
        //Example of How to lod in Data using the GSON loaders (REDO WHEN INDICATOR CHANGES)
        HashMap<String, ZVector> ZVectors = (percentBased) ? Database.getGSONPerMap(indicator.getName()) : Database.getGSONMap(indicator.getName());
        ZVector indicatorPerData = Database.getGSONIndicator(indicator.getName());
        //double[][] indicatorData = Database.getIndicatorGraph(indicator);
        //float[] indicatorPerData = Database.getIndicatorVector(indicatorData);

        //Example of How to load in the Percent GSON data

        //HashMap<String, float[]> percentZVectors = Database.getGSONPerMap(indicator.getName());
        //float[] indicatorPerData = Database.getGSONIndicatorPer(indicator.getName());
        float[] tempData;
        float[] goal = copy(indicatorPerData.getZData());
        float[] tempGoal;
        final float[] finalGoal = getPercent(copy(goal), percentAnalyzed);
        final float[] dates = ZVectors.get("IDATES").getScaledData();
        iterations = finalGoal.length * 1 / 4 - 2;
        final float[] finalDisplay = copy(goal);
        float[] total = new float[goal.length];

        String bestWord = "";
        double tempDistance;

        for (int iteration = 0; iteration < iterations; iteration++) {
            double maxDistance = Double.MIN_VALUE;
            tempGoal = getPercent(goal, percentAnalyzed);
            //System.out.println("Searching for closest vector... Iteration: " + iteration);
            for (String word : Database.RevWords) {
                if (ZVectors.get(word) == null) {
                } else {
                    tempData = getPercent(copy(ZVectors.get(word).getZData()), percentAnalyzed);
                    if (ZVectors.get(word) != null) {
                        tempDistance = Math.abs(Compare.dotProduct(tempGoal, tempData));
                        if (tempDistance > maxDistance) {
                            maxDistance = tempDistance;
                            bestWord = word;
                            //System.out.println(word+maxDistance);
                            //System.out.println("New Best: " + bestWord);
                        }
                    }
                }

            } //System.out.println("Done.");
            tempData = getPercent(copy(ZVectors.get(bestWord).getZData()), percentAnalyzed);
            if (!containsNANInfinity(Compare.correctScale(tempGoal, tempData))) {

                //System.out.println(bestWord + ", Weighted at: " + Compare.correctScale(tempGoal, tempData));
                if (!containsNANInfinity(tempData)) {
                    wt.add(bestWord, Compare.correctScale(tempGoal, tempData));
                    if (iterationGraphing) {
                        Grapher.createGraph(total, finalDisplay, "percents\\iterations\\total\\" + indicator.getName() + "\\iteration" + iteration,dates);
                        Grapher.createGraph(Compare.multiply(tempData, Compare.correctScale(tempGoal, tempData)), tempGoal, "percents\\iterations\\temporary\\" + indicator.getName() + "\\iteration" + iteration,dates);
                    }
                    total = Compare.add(total, Compare.multiply(ZVectors.get(bestWord).getZData(), Compare.correctScale(tempGoal, tempData)));
                    goal = Compare.getDifference(finalGoal, total);
                    if (iterationGraphing) {
                        Grapher.createGraph(goal, "percents\\iterations\\temporary\\" + indicator.getName() + "\\iterationR" + iteration,dates);
                    }
                    //Grapher.createGraph(total, finalGoal, "Vector" + bestWord + "CTG" + iteration);
                }
            }
        }
        Grapher.createGraph(total, finalDisplay, "percents\\" + indicator.getName() + "\\" + round(percentAnalyzed * 100, 2) + "%", percentAnalyzed,predictionCorrelation,dates);
        System.out.println("Results for " + indicator.getName() + " for " + iterations + " iterations and " + (round(percentAnalyzed * 100, 2)) + "%.");
        //System.out.println(wt);
    }
    public static void dotProductWeightingLimitedRegression(Indicator indicator, boolean percentBased, boolean iterationGraphing, int iterations, int irrelevent, int prediction, int analysis) {
        WordWeightTable wt = new WordWeightTable();
        HashMap<String, ZVector> ZVectors = (percentBased) ? Database.getGSONPerMap(indicator.getName()) : Database.getGSONMap(indicator.getName());
        ZVector indicatorData = Database.getGSONIndicator(indicator.getName());
        
        float[] tempData;
        float[] tempGoal;
        
        final float[] goal = copy(indicatorData.getZData());
        final float[] finalGoal = getLimited(copy(goal), irrelevent, analysis);
        final float[] dates = ZVectors.get("IDATES").getScaledData();
        
        float[] total = new float[goal.length];

        String bestWord = "";
        double tempDistance;

        for (int iteration = 0; iteration < iterations; iteration++) {
            double maxDistance = Double.MIN_VALUE;
            tempGoal = Compare.getDifference(finalGoal, getLimited(total,irrelevent, analysis));
            //System.out.println("Searching for closest vector... Iteration: " + iteration);
            for (String word : Database.RevWords) {
                if (ZVectors.get(word) == null) {
                } else {
                    tempData = getLimited(copy(ZVectors.get(word).getZData()), irrelevent, analysis);
                    if (ZVectors.get(word) != null) {
                        tempDistance = Math.abs(Compare.dotProduct(tempGoal, tempData));
                        if (tempDistance > maxDistance) {
                            maxDistance = tempDistance;
                            bestWord = word;
                            //System.out.println(word+maxDistance);
                            //System.out.println("New Best: " + bestWord);
                        }
                    }
                }

            } //System.out.println("Done.");
            
            tempData = getLimited(copy(ZVectors.get(bestWord).getZData()), irrelevent, analysis);
            if (!containsNANInfinity(Compare.correctScale(tempGoal, tempData))) {

                //System.out.println(bestWord + ", Weighted at: " + Compare.correctScale(tempGoal, tempData));
                if (!containsNANInfinity(tempData)) {
                    wt.add(bestWord, Compare.correctScale(tempGoal, tempData));
                    if (iterationGraphing) {
                        Grapher.createGraph(total, goal, "percents\\iterations\\total\\" + indicator.getName() + "\\iteration" + iteration,dates);
                        Grapher.createGraph(Compare.multiply(tempData, Compare.correctScale(tempGoal, tempData)), tempGoal, "percents\\iterations\\temporary\\" + indicator.getName() + "\\iteration" + iteration,dates);
                    }
                    total = Compare.add(total, Compare.multiply(ZVectors.get(bestWord).getZData(), Compare.correctScale(tempGoal, tempData)));
                    if (iterationGraphing) {
                        Grapher.createGraph(goal, "percents\\iterations\\temporary\\" + indicator.getName() + "\\iterationR" + iteration,dates);
                    }
                    //Grapher.createGraph(total, finalGoal, "Vector" + bestWord + "CTG" + iteration);
                }
            }
        }
        Grapher.createGraph(total, goal, "percents\\" + indicator.getName() + "\\" + irrelevent+"_"+analysis+"_"+prediction, irrelevent,analysis,prediction,dates);
        System.out.println("Results for " + indicator.getName() + " for " + iterations + " iterations and analyzing " + analysis + " disregarding "+irrelevent);
        System.out.println(wt);
    }
    public static float[] getLimited(float[] original, int irrelevant, int analyze)
    {
        float[] newData = new float[analyze];
        for(int i = irrelevant; i < irrelevant + analyze; i++)
        {
            newData[i - irrelevant] = original[Math.min(i, original.length-1)];
        }
        return newData;
    }
    /*
    public static void dotProductWeightingLimitedRegression(Indicator indicator, boolean percentBased, boolean iterationGraphing, double percentAnalyzed, int predictionCorrelation, int analysis) {
        int iterations = 0;
        
        WordWeightTable wt = new WordWeightTable();
        //Example of How to lod in Data using the GSON loaders (REDO WHEN INDICATOR CHANGES)
        HashMap<String, ZVector> ZVectors = (percentBased) ? Database.getGSONPerMap(indicator.getName()) : Database.getGSONMap(indicator.getName());
        ZVector indicatorPerData = Database.getGSONIndicator(indicator.getName());
        //double[][] indicatorData = Database.getIndicatorGraph(indicator);
        //float[] indicatorPerData = Database.getIndicatorVector(indicatorData);

        //Example of How to load in the Percent GSON data

        //HashMap<String, float[]> percentZVectors = Database.getGSONPerMap(indicator.getName());
        //float[] indicatorPerData = Database.getGSONIndicatorPer(indicator.getName());
        float[] tempData;
        float[] goal = copy(indicatorPerData.getZData());
        float[] tempGoal;
        final float[] finalGoal = getPastNum(copy(goal), percentAnalyzed, analysis);
        final float[] dates = ZVectors.get("IDATES").getScaledData();
        iterations = finalGoal.length * 1 / 4 - 2;
        final float[] finalDisplay = copy(goal);
        float[] total = new float[goal.length];

        String bestWord = "";
        double tempDistance;

        for (int iteration = 0; iteration < iterations; iteration++) {
            double maxDistance = Double.MIN_VALUE;
            tempGoal = getPastNum(goal, percentAnalyzed, analysis);
            //System.out.println("Searching for closest vector... Iteration: " + iteration);
            for (String word : Database.RevWords) {
                if (ZVectors.get(word) == null) {
                } else {
                    tempData = getPastNum(copy(ZVectors.get(word).getZData()), percentAnalyzed, analysis);
                    if (ZVectors.get(word) != null) {
                        tempDistance = Math.abs(Compare.dotProduct(tempGoal, tempData));
                        if (tempDistance > maxDistance) {
                            maxDistance = tempDistance;
                            bestWord = word;
                            //System.out.println(word+maxDistance);
                            //System.out.println("New Best: " + bestWord);
                        }
                    }
                }

            } //System.out.println("Done.");
            tempData = getPastNum(copy(ZVectors.get(bestWord).getZData()), percentAnalyzed, analysis);
            if (!containsNANInfinity(Compare.correctScale(tempGoal, tempData))) {

                //System.out.println(bestWord + ", Weighted at: " + Compare.correctScale(tempGoal, tempData));
                if (!containsNANInfinity(tempData)) {
                    wt.add(bestWord, Compare.correctScale(tempGoal, tempData));
                    if (iterationGraphing) {
                        Grapher.createGraph(total, finalDisplay, "percents\\iterations\\total\\" + indicator.getName() + "\\iteration" + iteration,dates);
                        Grapher.createGraph(Compare.multiply(tempData, Compare.correctScale(tempGoal, tempData)), tempGoal, "percents\\iterations\\temporary\\" + indicator.getName() + "\\iteration" + iteration,dates);
                    }
                    total = Compare.add(total, Compare.multiply(ZVectors.get(bestWord).getZData(), Compare.correctScale(tempGoal, tempData)));
                    goal = Compare.getDifference(finalGoal, total);
                    if (iterationGraphing) {
                        Grapher.createGraph(goal, "percents\\iterations\\temporary\\" + indicator.getName() + "\\iterationR" + iteration,dates);
                    }
                    //Grapher.createGraph(total, finalGoal, "Vector" + bestWord + "CTG" + iteration);
                }
            }
        }
        Grapher.createGraph(total, finalDisplay, "percents\\" + indicator.getName() + "\\" + round(percentAnalyzed * 100, 2) + "%"+analysis, percentAnalyzed,predictionCorrelation,analysis,dates);
        System.out.println("Results for " + indicator.getName() + " for " + iterations + " iterations and " + (round(percentAnalyzed * 100, 2)) + "%.");
        System.out.println(wt);
    }
*/
    private static boolean containsNANInfinity(float[] tempData) {
        for (float f : tempData) {
            if (("" + f).equals("" + Float.NaN) || ("" + f).equals(Float.NEGATIVE_INFINITY) || (f + "").equals(Float.POSITIVE_INFINITY)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsNANInfinity(double tempData) {
        if (("" + tempData).equals("" + Double.NaN) || ("" + tempData).equals(Double.NEGATIVE_INFINITY) || (tempData + "").equals(Double.POSITIVE_INFINITY)) {
            return true;
        }
        return false;
    }

    private static float[] copy(float[] data) {
        float[] returns = new float[data.length];
        System.arraycopy(data, 0, returns, 0, data.length);
        return returns;
    }

    private static float[] getPercent(float[] data, double percent) {
        percent = Math.sqrt(percent);
        float[] newData = new float[(int) (Math.ceil((double) data.length * percent))];
        for (int i = 0; i < newData.length; i++) {
            newData[i] = data[i];
        }
        return newData;
    }
    private static float[] getPastNum(float[] data, double percent, int length) {
        float[] newData = new float[length];
        for (int i = 0; i < newData.length; i++) {
            newData[i] = data[Math.max((int)(Math.ceil((double)data.length * percent) - i),0)];
        }
        return newData;
    }

    private static double round(double number, int digits) {
        long temp = Math.round(number * Math.pow(10, digits));
        return (double) temp / (Math.pow(10, digits));
    }
}
