import sum.komponenten.*;
import sum.ereignis.*;
import java.awt.Color;
import sum.multimedia.Bild;
import java.net.InetAddress;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.InterfaceAddress;
import java.net.SocketException;
/**
 * Die Klasse SuMAnwendung wurde nicht automatisch erstellt: 
 * 
 * @author Jesko
 * @version 7.03.2014
 */

public class GUI extends EBAnwendung
{
    // Objekte
    //private Etikett hatEtikett1;

    // Hier werden die ticBoxen, also die einzelnen Knoepfe in einem Array definiert und die Hoehe, Breite etc. festgelegt.

    private Bildschirm meinBildschirm;
    //     private JPanel meinPanel;
    private boolean dran;

    public boolean Verbunden;

    private Knopf ticBox[][][];
    final int startx = 80;
    final int starty = 3;
    final int abstandBoxen = 3;
    final int ticBoxWidth=50;
    final int ticBoxHeight=50;
    final int distance = 5;

    // Hier wird der Reset Knopf definiert.
    private Knopf reset;
    final double pLinks = 130;
    final double pOben = 100;
    final double pBreite =100;
    final double pHoehe = 30;
    final String pAufschrift = "Reset";

    // Hier wird der Verbinden Knopf definiert.
    private Knopf Verbinden;
    final double aLinks=230 ;
    final double aOben = 100;
    final double aBreite =100;
    final double aHoehe = 30;
    final String aAufschrift = "Verbinden";

    // Hier wird der Lokales Spiel-Knopf definiert.
    private Knopf lokalHosten;
    final double vLinks=230 ;
    final double vOben = 140;
    final double vBreite =100;
    final double vHoehe = 30;
    final String vAufschrift = "lokal Hosten";

    // Hier wird das Textfeld fuer die Ip-Eingabe definiert.
    private Textfeld textfeldAddresse;
    final double tLinks = 130;
    final double tOben = 50;
    final double tBreite =500;
    final double tHoehe = 30;
    final String tText = "localhost";

    // Hier wird das Textfeld fuer die Port-Eingabe definiert.
    private Textfeld textfeldPort;
    final double tLinks2 = 130;
    final double tOben2 = 10;
    final double tBreite2 =500;
    final double tHoehe2 = 30;
    final String tText2 = "5557";  

    // Hier wird der Knopf zum Wuerfeln definiert.
    private Knopf Wuerfeln;
    final double bLinks = 330;
    final double bOben = 100;
    final double bBreite = 100;
    final double bHoehe = 30;
    final String bAufschrift = "Wuerfeln";

    private Bild wuerfel1;
    private Bild wuerfel2;

    private Bild w1;
    private Bild w2;   
    private Bild w3;
    private Bild w4;
    private Bild w5;
    private Bild w6;
    private Bild victory;
    private Bild siegesBild;
    private Bild lose;
    private Bild loseBild;

    private Etikett werSpielt;
    final double fLinks = 130;
    final double fOben = 150;
    final double fBreite = 200;
    final double fHoehe = 30;
    final String fText = "am Zug:";

    final boolean debug = true;

    private static int guiNr=0;

    // Es wird der Echoclient vorausgesetzt.
    private Echoclient echo;

    private static GUI spieler1;
    private static GUI spieler2;

    private Echoserver echoserver;

    private Color hellgrau;
    private Color weiss;
    private String spielerNr;;

    // Attribute
    /**
     * Konstruktor
     */
    public GUI()
    {
        //Initialisierung der Oberklasse
        super(700, 750); 

        guiNr++;
        if (guiNr==1)
        {
            //meineSuMPrivateAnwendung1 = hatSuMPrivateAnwendung;
            spieler1=this;
        }
        if (guiNr==2)
        {
            //meineSuMPrivateAnwendung2 = hatSuMPrivateAnwendung;
            spieler2=this;
        }
        meinBildschirm = this.hatBildschirm;
        //         meinPanel = meinBildschirm.privatPanel();
        meinBildschirm.setTitle("Spieler "+guiNr);
        int i=0;

        /**
         * Hier werden die einzelnen ticBoxen AUFWEDNIGST erzeugt. Dazu wird erst einmal das Array definiert,
         * es umfasst [12](-3) BigBoxen fuer die einzelnen Wuerfelergebnisse, und je [4] fuer Breite und Hoehe.
         * Dann werden erst die bigBoxen durchgezaehlt, dann die Spalten, dann die Reihen.
         * Nach und nach wird nun mittels i hochgezaehlt, es werden ticBoxen erzeugt, wobei der Abstand, die Breite und die Hoehe miteingerechnet wird.
         * Zusaetzlich bekommen die ticBoxen Knoepfe, welche auch den wert "knopfGeklickt" haben.
         */
        ticBox = new Knopf [12][4][4];
        for(int bigBox = 1; bigBox <=9; bigBox++)
        { for (int column = 1; column<4; column++)
            { for (int row = 1; row < 4; row++)
                {
                    i++;
                    int x=startx+((bigBox+2)%3)*3*(ticBoxWidth+abstandBoxen)+row*(ticBoxWidth);
                    int y= starty+(((int)((bigBox+2)/3.0))*(ticBoxHeight+abstandBoxen))*3+column*(ticBoxHeight);

                    ticBox[bigBox][row][column] = new Knopf(x,y, ticBoxWidth, ticBoxHeight, ""+i, "knopfGeklickt"+guiNr);
                }  
            }
        }
        if (debug) System.out.println("GUI"+guiNr+": Es wurden alle Knoepfe erzeugt.");
        /**
         * Es werden Knoepfe erzeugt: Reset, Verbinden, Adress- und Portfeld, sowie Wuerfeln und das Textfeld fuer die Ausgabe des Wuerfelergebnisses.
         */
        reset = new Knopf(pLinks, pOben, pBreite, pHoehe, pAufschrift);        
        reset.setzeBearbeiterGeklickt("resetGeklickt"+guiNr);
        //Aufgeben = new Knopf(aLinks, aOben, aBreite, aHoehe, aAufschrift);
        Verbinden = new Knopf(aLinks, aOben, aBreite, aHoehe, aAufschrift);
        Verbinden.setzeBearbeiterGeklickt("VerbindenGeklickt");     

        textfeldAddresse = new Textfeld( tLinks, tOben, tBreite, tHoehe, tText);
        textfeldPort = new Textfeld (tLinks2, tOben2, tBreite2, tHoehe2, tText2);

        lokalHosten = new Knopf(vLinks, vOben, vBreite, vHoehe, vAufschrift);
        lokalHosten.setzeBearbeiterGeklickt("lokalHostenGeklickt");

        Wuerfeln = new Knopf(bLinks, bOben, bBreite, bHoehe, bAufschrift);
        Wuerfeln.setzeBearbeiterGeklickt("WuerfelnGeklickt");

        //Bilder muessen sich aktualisieren oder geloescht und neu erzeugt werden.
        werSpielt = new Etikett(fLinks,fOben,fBreite,fHoehe,fText);
        w1=  new Bild(-130,-180,20,20,"WW1.png");
        w2=  new Bild(-130,-180,20,20,"WW2.png");
        w3=  new Bild(-130,-180,20,20,"WW3.png");
        w4=  new Bild(-130,-180,20,20,"WW4.png");
        w5=  new Bild(-130,-180,20,20,"WW5.png");
        w6=  new Bild(-130,-180,20,20,"WW6.png");
        victory= new Bild (625, 200, 200, 270);
        siegesBild= new Bild (-200, 200, 200,270,"victorySkaliert.jpg");
        lose= new Bild (625, 200, 200, 270);
        loseBild= new Bild (-200, 200, 200,270,"loseSkaliert.jpg");
        wuerfel1= new Bild(30,80,20,20,w1);
        wuerfel2= new Bild(30,160,20,20,w2);
        Wuerfeln.deaktiviere();

        hellgrau = new Color(235,235,235);
        weiss = Color.WHITE;
    }

    public void resetGeklickt1()
    {
        if (debug) System.out.println("GUI"+guiNr+".resetGeklickt1: Es wurde ein Knopf geklickt.");
        if(spielerNr != null)
        {
            // spieler1.resetGeklickt();
            
            if ("spieler1".compareTo(spielerNr)==0)
                echo.send("reset1");
            else 
                echo.send("reset2");
        }
        else 
            System.out.println("Fehler in knopfGeklickt1()");
    }

    public void resetGeklickt2()
    {
        if (debug) System.out.println("GUI"+guiNr+".resetGeklickt2: Es wurde ein Knopf geklickt.");

        if(spielerNr != null)
        {
            //spieler2.resetGeklickt();
            if (spielerNr.compareTo("spieler1")==0)
                echo.send("reset1");
            else 
                echo.send("reset2");
        }
        else 
            System.out.println("Fehler in knopfGeklickt2()");
    }

    public void lokalHostenGeklickt()
    {
        echoserver = new Echoserver();
        textfeldAddresse.setzeInhalt("localhost");
        VerbindenGeklickt();
        textfeldAddresse.setzeInhalt(getLocalAddress());
        Verbinden.deaktiviere();
        lokalHosten.deaktiviere();
    }

    private  String getLocalAddress(){
        String adr = "localhost";
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while( b.hasMoreElements()){
                for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                {                       
                    String adresse = f.getAddress().toString();
                    adresse = adresse.replace("/", "");
                    if (debug) System.out.println("GUI"+guiNr+": gefundene Adresse:"+adresse);
                    if ( f.getAddress().isSiteLocalAddress())
                    {

                        adr= adresse;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return adr;
    }


    /**
     * Hier wurde der Knopf "Verbinden" geklickt, wodurch dem Echoclient uebergeben wird, mit welcher
     * Ip-Adresse und welchem Port man sich verbinden will.
     */
    public void VerbindenGeklickt()
    {
        try
        {
            echo = new Echoclient(textfeldAddresse.inhaltAlsText(), Integer.parseInt(textfeldPort.inhaltAlsText()), this);
            if (echo != null)
            {
                echo.isConnected(); //Der Client wird gefragt, ob er eine Verbindung hat.
                if (Verbunden==true)
                {
                    if (debug) System.out.println("GUI"+guiNr+": Es wurde ein Client("+textfeldAddresse.inhaltAlsText()+","+textfeldPort.inhaltAlsText()+") erzeugt");
                    Verbinden.deaktiviere();
                    textfeldAddresse.deaktiviere();
                    textfeldPort.deaktiviere();
                }
                else
                {
                    System.out.println("GUI"+guiNr+": Fehler beim Erzeugen der Verbindung!");
                }
            }
        }
        catch (Exception pFehler)
        {
            System.err.println("GUI"+guiNr+": Fehler beim Erzeugen der Verbindung: " + pFehler);
        }      
    }

    /**
     * Hier koennen alle ticBoxen deaktiviert werden. Dies sollte nach jedem Spielzug geschehen.
     */
    public void WuerfelnGeklickt()
    {
        echo.send("wuerfeln");
        Wuerfeln.deaktiviere();
    }

    public void deaktiviere()
    { 
        for (int bigBox = 1; bigBox <=9; bigBox++)
        { 
            for (int column = 1; column<4; column++)
            { 
                for (int row = 1; row < 4; row++)
                {
                    ticBox[bigBox][row][column].deaktiviere();
                    ticBox[bigBox][row][column].setzeFarbe(weiss);
                }
            }
        }
        meinBildschirm.repaint();      
        if (debug) System.out.println("GUI"+guiNr+": Alle Knoepfe wurden deaktiviert.");
    }

    /**
     * Hier koennen spezifische ticBoxen in bigBoxen angesteuert werden. Dazu werden lediglich Zeile und Spalte uebergeben.
     */
    public void aktiviere(int row,int column)
    {
        if (dran == true)
        {
            for (int bigBox = 1; bigBox <= 9; bigBox++)
            {
                if((ticBox[bigBox][row][column].inhaltAlsText())==" ")
                {
                    ticBox[bigBox][row][column].aktiviere();
                    ticBox[bigBox][row][column].setzeFarbe(hellgrau);
                }
                else
                {
                    ticBox[bigBox][row][column].deaktiviere();
                    ticBox[bigBox][row][column].setzeFarbe(weiss);
                }

            }
            meinBildschirm.repaint();
            if (debug) System.out.println("GUI"+guiNr+": Hat alle ticBoxen in Reihe " + row +" und Spalte "+ column+" aktiviert.");
        }

    }

    /**
     * Hier kann das gesamte Feld aktiviert werden. Dies sollte lediglich als Dummy-Funktion bereit sein.
     */
    public void aktiviere()
    {
        for (int bigBox = 1; bigBox <=9; bigBox++)
        {  for (int column = 1; column<4; column++)
            {  for (int row = 1; row < 4; row++)

                { if((ticBox[bigBox][row][column].inhaltAlsText())==" ")
                    {
                        ticBox[bigBox][row][column].aktiviere();
                        ticBox[bigBox][row][column].setzeFarbe(hellgrau);
                    }
                    else
                    {
                        ticBox[bigBox][row][column].setzeFarbe(weiss);
                    }
                }
            }
        }
        meinBildschirm.repaint();
        if (debug) System.out.println("GUI"+guiNr+": Es wurde alles aktiviert.");
    }

    public void knopfGeklickt1()
    {
        if (debug) System.out.println("GUI"+guiNr+".knopfGeklickt1: Es wurde ein Knopf geklickt.");
        if(spieler1 != null)
        {
            spieler1.knopfGeklickt();
            if(spieler2 != null)
            {
                spieler2.nachVorne();
            }
        }
        else 
            System.out.println("Fehler in knopfGeklickt1()");
    }

    public void knopfGeklickt2()
    {
        if (debug) System.out.println("GUI"+guiNr+".knopfGeklickt2: Es wurde ein Knopf geklickt.");
        if(spieler2 != null)
        {
            spieler2.knopfGeklickt();
            if(spieler1 != null)
            {
                spieler1.nachVorne();
            }
        }
        else 
            System.out.println("Fehler in knopfGeklickt2()");
    }

    /**
     * Hier wird eine ticBox geklickt. Dabei wird erkannt, in welcher bigBox, Reihe und Spalte diese liegt,
     * was wiederum dem Client uebergeben wird. Im Nachhinein werden alle ticBoxen deaktiviert.
     */
    public void knopfGeklickt()
    {
        int geklicktBigbox=0;
        int geklicktRow=0;
        int geklicktColumn=0;
        //meinBildschirm.nachVorn();
        for(int bigBox = 1; bigBox <= 9; bigBox++)
        { for (int column = 1; column<4; column++)
            { for (int row = 1; row < 4; row++)
                {
                    if (ticBox[bigBox][row][column].besitztFokus())
                    {
                        geklicktBigbox = bigBox;
                        geklicktRow = row;
                        geklicktColumn=column;
                    }
                }  
            }
        }
        echo.knopfGedrueckt(geklicktBigbox, geklicktRow, geklicktColumn);
        deaktiviere();
        meinBildschirm.repaint();
        if (debug) System.out.println("GUI"+guiNr+": Der Knopf in Spalte "+geklicktRow+" und in Reihe "+geklicktColumn+" wurde geklickt.");
        duSpielst(false);
    }

    /**
     * Das gesamte Feld wird aktualisiert, indem wir den Inhalt der ticBoxen gleich dem Array vom Client setzen.
     */
    public void aktualisiere(int pFeld[][][])
    {
        for (int bigBox=1 ; bigBox<=9; bigBox++) 
        {
            for (int column=1; column <=3; column++)
            {
                for (int row=1; row <=3; row ++)
                {
                    switch (pFeld[bigBox][row][column])
                    {
                        case 0: ticBox[bigBox][row][column].setzeInhalt(" "); 
                        break;
                        case 1: ticBox[bigBox][row][column].setzeInhalt("X");
                        break;
                        case 2: ticBox[bigBox][row][column].setzeInhalt("O"); 
                        break; 
                    }
                }
            }            
        }      
        meinBildschirm.repaint();
        if (debug)
        {
            System.out.println("GUI"+guiNr+": Das Spielfeld wurde aktualisiert.");
        }
    }

    /**
     * Yay, wir haben das Wuerfelergebnis bekommen. Ab damit ins Etikett und aktiviert die ticBoxen!
     */
    public void gewuerfelt(int pa, int pb)
    {
        {
            deaktiviere(); 
            int e = pa+pb;
            switch (e) {
                case 3:  aktiviere(1,1);
                break;
                case 4: aktiviere(2,1);
                break;
                case 5: aktiviere(3,1);
                break;
                case 6:  aktiviere(1,2);
                break;
                case 7:  aktiviere(2,2);
                break;
                case 8:  aktiviere(3,2);
                break;
                case 9:  aktiviere(1,3);
                break;
                case 10:  aktiviere(2,3);
                break;
                case 11: aktiviere(3,3);
                break;
            }
            switch (pa) {
                case 1:  wuerfel1.setzeBild(w1);
                break;
                case 2:  wuerfel1.setzeBild(w2);
                break;
                case 3:  wuerfel1.setzeBild(w3);
                break;
                case 4:  wuerfel1.setzeBild(w4);
                break;
                case 5:  wuerfel1.setzeBild(w5);
                break;
                case 6:  wuerfel1.setzeBild(w6);
                break;
            }
            switch (pb) {
                case 1:   wuerfel2.setzeBild(w1);
                break;
                case 2:   wuerfel2.setzeBild(w2);
                break;
                case 3:   wuerfel2.setzeBild(w3);
                break;
                case 4:   wuerfel2.setzeBild(w4);
                break;
                case 5:   wuerfel2.setzeBild(w5);
                break;
                case 6:   wuerfel2.setzeBild(w6);
                break;
            }
            Wuerfeln.deaktiviere();
            if(dran==true)
            {
                meinBildschirm.nachVorn();
            }
            else
            {
                //meinBildschirm.nachHinten();
            }
        }
    }

    /**
     * Spiel, Satz und Sieg.
     */
    public void gewonnen()
    {
        Wuerfeln.deaktiviere();
        if (debug)
        {
            System.out.println("Du hast gewonnen!");
        }
        victory.setzeBild(siegesBild);
    }

    /**
     * Sei jetzt bloss kein schlechter Verlierer.
     */
    public void verloren()
    {
        Wuerfeln.deaktiviere();
        if (debug)
        {
            System.out.println("Du hast verloren!");
        }
        lose.setzeBild(loseBild);
    }

    public void duSpielst (boolean pAktiv)
    {
        if (pAktiv == true)
        {
            werSpielt.setzeInhalt("am Zug: Du");
            werSpielt.aktiviere();
            Wuerfeln.aktiviere();
            if (debug) System.out.println("GUI"+guiNr+": Du bist dran mit Wuerfeln.");
            dran = true;
        }
        else if (pAktiv == false)
        {
            werSpielt.setzeInhalt("am Zug: Gegner");
            Wuerfeln.deaktiviere();
            dran = false;
        }
    }

    public void requestReset()
    {
        System.out.println("Der andere Spieler verlangt einen RESET!!!!");
    }

    public void nachVorne()
    {
        meinBildschirm.nachVorn();
    }
    
    public void duBistSpieler(String pSpielerNr)
    {
        pSpielerNr = spielerNr;
         if (debug) System.out.println("GUI"+guiNr+".duBistSpieler:"+spielerNr);
    }
}

