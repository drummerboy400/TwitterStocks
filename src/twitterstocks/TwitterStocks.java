package twitterstocks;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class TwitterStocks {

    //public final static int DOLLAR_SIGN = 28129870;
    //public final static int GOOGLE_FINANCE_TABS = 13;
    //public final static int TWITTER_TABS = 5;

    public static void main(String args[]) throws AWTException, InterruptedException, FileNotFoundException, UnsupportedFlavorException, IOException {
    /*    Scanner sn = new Scanner(System.in);
        sn.nextLine();
        final int ITERATIONS = 1;
        Robot r = new Robot();
        ArrayList<String> positive = new ArrayList<String>();
        ArrayList<String> negative = new ArrayList<String>();
        ArrayList<String> tickers = new ArrayList<String>();
        loadFiles(positive, negative, tickers);
        HashMap<String, RollingAverage> values = new HashMap<String, RollingAverage>();
        for (String ticker : tickers) {
            values.put(ticker, new RollingAverage(10));
        }
        HashMap<String, PriceLog> prices = new HashMap<String, PriceLog>();
        for (String ticker : tickers) {
            prices.put(ticker, new PriceLog());
        }

        switchWindows(r);


        //Begin Stock Algorithems

        checkStocks(r, tickers, prices);

        checkTwitter(r, ITERATIONS, tickers, positive, negative, values, prices);

        display(tickers, prices, values);

        r.delay(1000);

        checkStocks(r, tickers, prices);

        switchWindows(r);

        display(tickers, prices, values);
        
        //Database.load();
        //Database.add(new Article((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor)));
        
    }

    private static void display(ArrayList<String> tickers, HashMap<String, PriceLog> prices, HashMap<String, RollingAverage> values) {
        for (String t : tickers) {
            System.out.println(t.toUpperCase() + ": " + prices.get(t) + " : " + values.get(t).get());
        }
        System.out.println();
    }

    private static void checkStocks(Robot r, ArrayList<String> tickers, HashMap<String, PriceLog> prices) throws UnsupportedFlavorException, IOException {
        //Stock Pull
        type(KeyEvent.VK_F6, r);
        type("finance.google.com", r);
        r.delay(3000);
        for (String t : tickers) {
            String price = "";
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(""), null);
            do {
                //r.mouseMove(380, 130);
                //click(r);

                select(GOOGLE_FINANCE_TABS, r);

                type(t, r);

                r.delay(4000);

                selectAll(r);

                price = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            } while (price.equals(""));
            
            price = price.substring(price.indexOf('\t')+1);
            
            prices.get(t).put(Double.parseDouble(price.substring(0, price.indexOf('\t'))));

        }//price check
    }

    private static void checkTwitter(Robot r, int ITERATIONS, ArrayList<String> tickers, ArrayList<String> positive, ArrayList<String> negative, HashMap<String, RollingAverage> values, HashMap<String, PriceLog> prices) throws UnsupportedFlavorException, IOException {
        //Twitter Check
        type(KeyEvent.VK_F6,r);
        type("www.twitter.com", r);
        r.delay(4000);

        for (int iterations = 0; iterations < ITERATIONS; iterations++) {
            for (String t : tickers) {
                String data = "";
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(""), null);
                do {
                select(TWITTER_TABS, r);
                //r.mouseMove(1100, 88);
                //click(r);
                /*r.keyPress(KeyEvent.VK_CONTROL);
                r.delay(50);
                r.keyPress(KeyEvent.VK_A);
                r.delay(200);
                r.keyRelease(KeyEvent.VK_A);
                r.delay(50);
                r.keyRelease(KeyEvent.VK_CONTROL);*/
      /*          type("$" + t, r);
                r.delay(3000);
                //r.mouseMove(1100, 150);
                //click(r);

                selectAll(r);
                
                data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                
                } while (data.equals(""));

                ArrayList<String> parsed = new ArrayList<String>();
                boolean blankLine = false;
                int postBlankCount = 0;
                while (data.indexOf('\n') != -1) {

                    if (blankLine)// && lineNumber%3 == 0)
                    {
                        postBlankCount++;
                        if (postBlankCount > 3 && !(data.substring(0, data.indexOf('\n')).equals(""))) {
                            parsed.add(data.substring(0, data.indexOf('\n')));
                        }
                    }
                    if (data.substring(0, data.indexOf('\n')).equals("")) {
                        blankLine = true;
                    }
                    data = data.substring(data.indexOf('\n') + 1);
                }
                for (int i = 0; i < parsed.size(); i++) {
                    if (parsed.get(i).charAt(0) == ' ' || parsed.get(i).equals("Expand")) {
                        parsed.remove(i);
                        i--;
                    }
                }

                //System.out.println(parsed);

                double score = 0.0;
                for (String p : parsed) {
                    while (p.indexOf(' ') != -1) {
                        String word = p.substring(0, p.indexOf(' '));
                        for (String positiveW : positive) {
                            if (word.toLowerCase().equals(positiveW)) {
                                score += .1;
                            }
                        }
                        for (String negativeW : negative) {
                            if (word.toLowerCase().equals(negativeW)) {
                                score -= .1;
                            }
                        }
                        p = p.substring(p.indexOf(' ') + 1);
                    }
                }

                values.get(t).update(score);

            }
            System.out.println();
        }
        //Twitter Check end
    }

    private static void type(int k, Robot r) {
        r.keyPress(k);
        r.delay(30);
        r.keyRelease(k);
        r.delay(30);
    }

    private static void type(String text, Robot r) {
        int i = 0;
        char c;
        int k = 0;
        text = text.toLowerCase();
        while (i < text.length()) {
            c = text.charAt(i);
            switch (c) {
                case 'a':
                    k = KeyEvent.VK_A;
                    break;
                case 'b':
                    k = KeyEvent.VK_B;
                    break;
                case 'c':
                    k = KeyEvent.VK_C;
                    break;
                case 'd':
                    k = KeyEvent.VK_D;
                    break;
                case 'e':
                    k = KeyEvent.VK_E;
                    break;
                case 'f':
                    k = KeyEvent.VK_F;
                    break;
                case 'g':
                    k = KeyEvent.VK_G;
                    break;
                case 'h':
                    k = KeyEvent.VK_H;
                    break;
                case 'i':
                    k = KeyEvent.VK_I;
                    break;
                case 'j':
                    k = KeyEvent.VK_J;
                    break;
                case 'k':
                    k = KeyEvent.VK_K;
                    break;
                case 'l':
                    k = KeyEvent.VK_L;
                    break;
                case 'm':
                    k = KeyEvent.VK_M;
                    break;
                case 'n':
                    k = KeyEvent.VK_N;
                    break;
                case 'o':
                    k = KeyEvent.VK_O;
                    break;
                case 'p':
                    k = KeyEvent.VK_P;
                    break;
                case 'q':
                    k = KeyEvent.VK_Q;
                    break;
                case 'r':
                    k = KeyEvent.VK_R;
                    break;
                case 's':
                    k = KeyEvent.VK_S;
                    break;
                case 't':
                    k = KeyEvent.VK_T;
                    break;
                case 'u':
                    k = KeyEvent.VK_U;
                    break;
                case 'v':
                    k = KeyEvent.VK_V;
                    break;
                case 'w':
                    k = KeyEvent.VK_W;
                    break;
                case 'x':
                    k = KeyEvent.VK_X;
                    break;
                case 'y':
                    k = KeyEvent.VK_Y;
                    break;
                case 'z':
                    k = KeyEvent.VK_Z;
                    break;
                case '.':
                    k = KeyEvent.VK_DECIMAL;
                    break;
                case '$':
                    k = DOLLAR_SIGN;
                    break;
            }
            if (k != DOLLAR_SIGN) {
                r.keyPress(k);
                r.delay(10);
                r.keyRelease(k);
            } else {
                r.keyPress(KeyEvent.VK_SHIFT);
                r.delay(30);
                r.keyPress(KeyEvent.VK_4);
                r.delay(30);
                r.keyRelease(KeyEvent.VK_SHIFT);
                r.keyRelease(KeyEvent.VK_4);
            }
            i++;
        }
        r.delay(500);
        r.keyPress(KeyEvent.VK_ENTER);
        r.delay(100);
        r.keyRelease(KeyEvent.VK_ENTER);
        r.delay(300);
    }

    private static void click(Robot r) {
        r.mousePress(MouseEvent.BUTTON1_MASK);
        r.delay(10);
        r.mouseRelease(MouseEvent.BUTTON1_MASK);
        r.delay(10);
    }

    private static void select(int tabs, Robot r) {
        //type(KeyEvent.VK_F6, r);
        for (int t = 0; t < tabs; t++) {
            type(KeyEvent.VK_TAB, r);
        }
    }

    public static void switchWindows(Robot r) {
        r.keyPress(KeyEvent.VK_ALT);
        r.delay(500);
        r.keyPress(KeyEvent.VK_TAB);
        r.delay(500);
        r.keyRelease(KeyEvent.VK_ALT);
        r.keyRelease(KeyEvent.VK_TAB);
        r.delay(500);
    }

    private static void selectAll(Robot r) {
        r.keyPress(KeyEvent.VK_CONTROL);
        r.delay(50);
        r.keyPress(KeyEvent.VK_A);
        r.delay(50);
        r.keyRelease(KeyEvent.VK_A);
        r.delay(500);
        r.keyPress(KeyEvent.VK_C);
        r.delay(50);
        r.keyRelease(KeyEvent.VK_C);
        r.delay(50);
        r.keyRelease(KeyEvent.VK_CONTROL);
        r.delay(400);
    }

    private static void loadFiles(ArrayList<String> positive, ArrayList<String> negative, ArrayList<String> tickers) throws FileNotFoundException {
        Scanner positiveIn = new Scanner(new File("positive.txt"));
        Scanner negativeIn = new Scanner(new File("negative.txt"));
        Scanner tickersIn = new Scanner(new File("tickers.txt"));
        while (positiveIn.hasNext()) {
            positive.add(positiveIn.nextLine());
        }
        positiveIn.close();
        while (negativeIn.hasNext()) {
            negative.add(negativeIn.nextLine());
        }
        negativeIn.close();
        while (tickersIn.hasNext()) {
            tickers.add(tickersIn.nextLine());
        }
        tickersIn.close();
    }
}
*/
        Database.load();
    }
}