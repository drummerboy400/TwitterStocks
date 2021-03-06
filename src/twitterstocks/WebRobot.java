package twitterstocks;

/**
 *
 * @author Bradley Holloway
 */
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebRobot {

    Robot robot;

    public WebRobot() {
        while (robot == null) {
            try {
                robot = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(WebRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void launchChrome() {
        System.out.println("Warning: Make sure that Chrome is not running!");
        System.out.println("You have 10 Seconds before it could cause problems.");
        robot.delay(10000);
        launchRun();
        type("chrome", true);
        waitTillDone(10, 1);
    }

    public void typeURL(String URL) {
        //System.out.println(URL);
        setClipboard(URL);
        type(KeyEvent.VK_F6);
        waitTillDone();

        paste();
        enter();

        waitTillDone();

    }

    public void mineNYTimes(int day, int month, int year, int results) {

        DecimalFormat dayMonth = new DecimalFormat("00");
        DecimalFormat yearFormat = new DecimalFormat("0000");
        String date = "" + yearFormat.format(year) + dayMonth.format(month) + dayMonth.format(day);
        for (int result = 0; result < results; result++) {
            typeURL("http://query.nytimes.com/search/sitesearch/#/a/from" + date + "to" + date + "/allresults/1/allauthors/oldest/");
            tabTo(12 + result);
            enter();
            waitTillDone();
            selectAll();
            try {
                Database.add(new Article(findNYTimesArticle(getClipboard()), year * 10000 + month * 100 + day));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(WebRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public void mineGoogleTrends()
    {
        int num = 0;
        for(String word: Database.words){
                if(num>2439&&num<3000){
                typeURL("http://www.google.com/trends/explore#q="+ word + "&cmpt=q");
                robot.delay(500);
                typeURL("http://www.google.com/trends/explore#q="+ word + "&cmpt=q");
                robot.delay(500);
                tabTo(10);
                type(KeyEvent.VK_DOWN);
                enter();
                robot.delay(4500);
                type("sheet" + num, true); 
                enter();
                robot.delay(300);
            }//cases larger than 100
            num++;
            
        }//for each of the words in database.words
    }//public void mine google trends

    public void mineNYTimes(int day, int month, int year) {
        mineNYTimes(day, month, year, 1);
    }

    public void mineWSJ(int day, int month, int year, int results) {

        DecimalFormat dayMonth = new DecimalFormat("00");
        DecimalFormat yearFormat = new DecimalFormat("0000");
        for (int result = 0; result < results; result++) {
            typeURL("http://pqasb.pqarchiver.com/djreprints/results.html?st=advanced&QryTxt=*&type=current&sortby=RELEVANCE&datetype=6&frommonth=" + dayMonth.format(month) + "&fromday=" + dayMonth.format(day) + "&fromyear=" + yearFormat.format(year) + "&tomonth=" + dayMonth.format(month) + "&today=" + dayMonth.format(day + 1) + "&toyear=" + yearFormat.format(year) + "&By=&Title=");
            tabTo(23 + result * 2);
            enter();
            waitTillDone();
            selectAll();
            try {
                Database.add(new Article(findWSJArticle(getClipboard()), year * 10000 + month * 100 + day, "WallStreetJournal"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(WebRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void mineWSJ(int day, int month, int year) {
        mineWSJ(day, month, year, 1);
    }
    
    public void mineGoogleFinance()
    {
         int year = 2011;
        int dateTab = 49;
        while (year < 2015) {
            typeURL("http://web.archive.org/web/" + year + "0615000000*/https://www.google.com/finance/");
            tabTo(28 + dateTab);
            enter();
            waitTillDone(2, 1);
            String URL;
            URL = getURL();
            if (URL.indexOf("faqs") != -1) {
                year++;
                dateTab = 0;
                tabTo(1);
                type(KeyEvent.VK_ESCAPE);
                robot.delay(1000);
            } else {
                enter();
                waitTillDone(2, 1);
                selectAll();
                tabTo(1);
                robot.delay(500);
                selectAll();
                robot.delay(100);
                tabTo(1);
                robot.delay(2000);
                dateTab++;

                try {
                    //System.out.println(URL.substring(27,35));
                    Database.add(new Article((getClipboard()), Integer.parseInt(URL.substring(27, 35)), "GoogleFinance"));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(WebRobot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void mineReddit() {
        int year = 2010;
        int dateTab = 0;
        while (year < 2011) {
            typeURL("http://web.archive.org/web/" + year + "0601000000*/http://www.reddit.com/");
            tabTo(28 + 81+ dateTab);
            enter();
            waitTillDone(2, 1);
            String URL;
            URL = getURL();
            if (URL.indexOf("faqs") != -1) {
                year++;
                dateTab = 0;
                tabTo(1);
                type(KeyEvent.VK_ESCAPE);
                robot.delay(1000);
            } else {
                tabTo(1);
                type(KeyEvent.VK_ESCAPE);
                selectAll();
                dateTab++;

                try {
                    //System.out.println(URL.substring(27,35));
                    Database.add(new Article((getClipboard()), Integer.parseInt(URL.substring(27, 35)), "REDDIT"));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(WebRobot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void mineYahooFinance() {
        int year = 2005;
        int dateTab = 0;
        robot.delay(10000);
        while (year < 2015) {
            typeURL("http://web.archive.org/web/" + year + "0215000000*/http://finance.yahoo.com/");
            tabTo(28 + dateTab);
            enter();
            waitTillDone();
            String URL;
            URL = getURL();
            if (URL.indexOf("faqs") != -1) {

                year++;
                dateTab = 0;
                tabTo(1);
                type(KeyEvent.VK_ESCAPE);
                robot.delay(1000);
            } else {
                tabTo(1);
                type(KeyEvent.VK_ESCAPE);
                selectAll();
                dateTab++;
                try {
                    //System.out.println(URL.substring(27,35));
                    Database.add(new Article((getClipboard()), Integer.parseInt(URL.substring(27, 35)), "YahooFinance"));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(WebRobot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void mineCnnMoney() {
        int year = 2003;
        int dateTab = 0;
        while (year < 2015) {
            typeURL("http://web.archive.org/web/" + year + "0801000000*/http://money.cnn.com/");
            tabTo(28 + dateTab);
            enter();
            waitTillDone();
            String URL;
            URL = getURL();
            if (URL.indexOf("faqs") != -1) {

                year++;
                dateTab = 0;
                tabTo(1);
                type(KeyEvent.VK_ESCAPE);
                robot.delay(1000);
            } else {
                tabTo(1);
                type(KeyEvent.VK_ESCAPE);
                selectAll();
                dateTab++;
                try {
                    //System.out.println(URL.substring(27,35));
                    Database.add(new Article((getClipboard()), Integer.parseInt(URL.substring(27, 35)), "CnnMoney"));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(WebRobot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
     public void mineFoxNews() {
        int year = 2011;
        int dateTab = 0;
        while (year < 2012) {
            typeURL("http://web.archive.org/web/"+ year + "0515000000*/http://www.foxnews.com/");
            tabTo(28+ 79+ dateTab);
            enter();
            waitTillDone();
            String URL;
            URL = getURL();
            if (URL.indexOf("faqs") != -1) {

                year++;
                dateTab = 0;
                tabTo(1);
                type(KeyEvent.VK_ESCAPE);
                robot.delay(1000);
            } else {
                tabTo(1);
                type(KeyEvent.VK_ESCAPE);
                selectAll();
                dateTab++;
                try {
                    //System.out.println(URL.substring(27,35));
                    Database.add(new Article((getClipboard()), Integer.parseInt(URL.substring(27, 35)), "FoxNews"));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(WebRobot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void type(int k) {
        robot.keyPress(k);
        robot.delay(30);
        robot.keyRelease(k);
        robot.delay(30);
    }

    private void typeFast(int k) {
        robot.keyPress(k);
        robot.delay(10);
        robot.keyRelease(k);
        robot.delay(10);
    }

    private void enter() {
        robot.delay(500);
        type(KeyEvent.VK_ENTER);
    }

    private void type(String text, boolean enter) {
        int i = 0;
        char c;
        int k = 0;
        text = text.toLowerCase();
        boolean shift = false;
        while (i < text.length()) {
            c = text.charAt(i);

            switch (c) {
                case 'a':
                    k = KeyEvent.VK_A;
                    shift = false;
                    break;
                case 'b':
                    k = KeyEvent.VK_B;
                    shift = false;
                    break;
                case 'c':
                    k = KeyEvent.VK_C;
                    shift = false;
                    break;
                case 'd':
                    k = KeyEvent.VK_D;
                    shift = false;
                    break;
                case 'e':
                    k = KeyEvent.VK_E;
                    shift = false;
                    break;
                case 'f':
                    k = KeyEvent.VK_F;
                    shift = false;
                    break;
                case 'g':
                    k = KeyEvent.VK_G;
                    shift = false;
                    break;
                case 'h':
                    k = KeyEvent.VK_H;
                    shift = false;
                    break;
                case 'i':
                    k = KeyEvent.VK_I;
                    shift = false;
                    break;
                case 'j':
                    k = KeyEvent.VK_J;
                    shift = false;
                    break;
                case 'k':
                    k = KeyEvent.VK_K;
                    shift = false;
                    break;
                case 'l':
                    k = KeyEvent.VK_L;
                    shift = false;
                    break;
                case 'm':
                    k = KeyEvent.VK_M;
                    shift = false;
                    break;
                case 'n':
                    k = KeyEvent.VK_N;
                    shift = false;
                    break;
                case 'o':
                    k = KeyEvent.VK_O;
                    shift = false;
                    break;
                case 'p':
                    k = KeyEvent.VK_P;
                    shift = false;
                    break;
                case 'q':
                    k = KeyEvent.VK_Q;
                    shift = false;
                    break;
                case 'r':
                    k = KeyEvent.VK_R;
                    shift = false;
                    break;
                case 's':
                    k = KeyEvent.VK_S;
                    shift = false;
                    break;
                case 't':
                    k = KeyEvent.VK_T;
                    shift = false;
                    break;
                case 'u':
                    k = KeyEvent.VK_U;
                    shift = false;
                    break;
                case 'v':
                    k = KeyEvent.VK_V;
                    shift = false;
                    break;
                case 'w':
                    k = KeyEvent.VK_W;
                    shift = false;
                    break;
                case 'x':
                    k = KeyEvent.VK_X;
                    shift = false;
                    break;
                case 'y':
                    k = KeyEvent.VK_Y;
                    shift = false;
                    break;
                case 'z':
                    k = KeyEvent.VK_Z;
                    shift = false;
                    break;
                case ' ':
                    k = KeyEvent.VK_SPACE;
                    shift = false;
                    break;
                case '.':
                    k = KeyEvent.VK_DECIMAL;
                    shift = false;
                    break;
                case '$':
                    shift = true;
                    k = KeyEvent.VK_4;
                    break;
                case ':':
                    shift = true;
                    k = KeyEvent.VK_SEMICOLON;
                    break;
                case '/':
                    shift = false;
                    k = KeyEvent.VK_SLASH;
                    break;
                case '#':
                    shift = true;
                    k = KeyEvent.VK_3;
                    break;
                case '0':
                    shift = false;
                    k = KeyEvent.VK_0;
                    break;
                case '1':
                    shift = false;
                    k = KeyEvent.VK_1;
                    break;
                case '2':
                    shift = false;
                    k = KeyEvent.VK_2;
                    break;
                case '3':
                    shift = false;
                    k = KeyEvent.VK_3;
                    break;
                case '4':
                    shift = false;
                    k = KeyEvent.VK_4;
                    break;
                case '5':
                    shift = false;
                    k = KeyEvent.VK_5;
                    break;
                case '6':
                    shift = false;
                    k = KeyEvent.VK_6;
                    break;
                case '7':
                    shift = false;
                    k = KeyEvent.VK_7;
                    break;
                case '8':
                    shift = false;
                    k = KeyEvent.VK_8;
                    break;
                case '9':
                    shift = false;
                    k = KeyEvent.VK_9;
                    break;

            }
            if (shift) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.delay(30);

            }
            type(k);

            if (shift) {
                robot.keyRelease(KeyEvent.VK_SHIFT);

            }

            i++;
        }
        robot.delay(500);
        if (enter) {
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(300);
        }
    }

    private void click() {
        robot.mousePress(MouseEvent.BUTTON1_MASK);
        robot.delay(10);
        robot.mouseRelease(MouseEvent.BUTTON1_MASK);
        robot.delay(10);
    }

    private void tabTo(int tabs) {
        //type(KeyEvent.VK_F6, r);
        for (int t = 0; t < tabs; t++) {
            typeFast(KeyEvent.VK_TAB);
        }
    }

    public void switchWindows() {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.delay(500);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.delay(500);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.delay(500);
    }

    private void selectAll() {
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_A);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_A);
        robot.delay(500);
        robot.keyPress(KeyEvent.VK_C);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_C);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(400);
    }

    private void setClipboard(String text) {
        StringSelection sText = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sText, null);
    }

    private void launchRun() {
        robot.keyPress(KeyEvent.VK_WINDOWS);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_R);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_R);
        robot.keyRelease(KeyEvent.VK_WINDOWS);
        robot.delay(200);
    }

    private void paste() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(40);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(40);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(30);
    }

    private void waitTillDone() {
        boolean waiting = true;
        int waitTime = 1;
        BufferedImage first = robot.createScreenCapture(new Rectangle(0, 0, java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, 100));
        BufferedImage second;
        while (waiting) {
            robot.delay(waitTime * 1000);
            second = robot.createScreenCapture(new Rectangle(0, 0, java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, 100));
            if (equal(first, second)) {
                waiting = false;
            } else {
                first = second;
            }
        }
    }

    private void waitTillDone(int firstWait, int afterWait) {
        boolean waiting = true;
        int waitTime = firstWait;
        BufferedImage first = robot.createScreenCapture(new Rectangle(0, 0, java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, 100));
        BufferedImage second;
        while (waiting) {
            robot.delay(waitTime * 1000);
            second = robot.createScreenCapture(new Rectangle(0, 0, java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, 100));
            if (equal(first, second)) {
                waiting = false;
            } else {
                first = second;
                waitTime = afterWait;
            }
        }
    }

    private boolean equal(BufferedImage first, BufferedImage second) {
        for (int x = 0; x < first.getWidth(); x++) {
            for (int y = 0; y < first.getHeight(); y++) {
                if (first.getRGB(x, y) != second.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getClipboard() {
        String data = "";
        int tries = 25;
        while (tries > 0 && data.equals("")) {
            try {
                data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
                try {
                    data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.getTextPlainUnicodeFlavor());
                } catch (Exception e) {
                    try {
                        DataFlavor[] df = {DataFlavor.stringFlavor, DataFlavor.plainTextFlavor, DataFlavor.getTextPlainUnicodeFlavor()};
                        data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.selectBestTextFlavor(df));
                    } catch (Exception ent) {
                        System.out.println("Clipboard failed");
                        //Logger.getLogger(WebRobot.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //Logger.getLogger(WebRobot.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                //Logger.getLogger(WebRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
            robot.delay(200);

        }
        return data;
    }

    public String getURL() {

        type(KeyEvent.VK_F6);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(100);
        robot.keyPress(KeyEvent.VK_C);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_C);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(100);
        return getClipboard();
    }

    private String findNYTimesArticle(String data) {
        if (data.indexOf("REPRINTS") != -1 && data.indexOf("FACEBOOK", data.indexOf("REPRINTS")) != -1) {
            return data.substring(data.indexOf("REPRINTS") + 9, data.indexOf("FACEBOOK", data.indexOf("REPRINTS")));
        }
        return "";
    }

    private String findWSJArticle(String data) {
        if (data.indexOf("ProQuest") != -1 && data.indexOf("Reproduced", data.indexOf("ProQuest")) != -1) {
            return data.substring(data.indexOf("ProQuest") + 9, data.indexOf("Reproduced", data.indexOf("ProQuest")));
        }
        return "";
    }
}
