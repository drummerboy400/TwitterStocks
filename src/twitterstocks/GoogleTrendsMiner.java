/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package twitterstocks;

/**
 *
 * @author Roberto
 */
public class GoogleTrendsMiner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        WebRobot robot = new WebRobot();
        Database.load();
        robot.launchChrome();
        robot.mineGoogleTrends();
    }
}
