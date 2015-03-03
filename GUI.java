
import sum.komponenten.*;
import sum.werkzeuge.*;
import sum.ereignis.*;

/**
 * Die Klasse SuMAnwendung wurde nicht automatisch erstellt: 
 * 
 * @author Die üblichen Verdächtigen
 * @version 15.01.2014
 */

public class GUI extends EBAnwendung
{
    // Objekte
    //private Etikett hatEtikett1;

    // Hier werden die ticBoxen, also die einzelnen Knöpfe in einem Array definiert und die Höhe, Breite etc. festgelegt.
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

    // Hier wird das Textfeld für die Ip-Eingabe definiert.
    private Textfeld textfeldAddresse;
    final double tLinks = 130;
    final double tOben = 50;
    final double tBreite =500;
    final double tHoehe = 30;
    final String tText = "10.68.112.";

    // Hier wird das Textfeld für die Port-Eingabe definiert.
    private Textfeld textfeldPort;
    final double tLinks2 = 130;
    final double tOben2 = 10;
    final double tBreite2 =500;
    final double tHoehe2 = 30;
    final String tText2 = "5557";  

    // Hier wird das Würfel Ergebnis ausgegeben.
    private Etikett wuerfelErgebnis;
    final double cLinks = 130;
    final double cOben = 130;
    final double cBreite = 200;
    final double cHoehe = 30;
    final String cText = "Würfel 1: "+"Würfel 2:";

    // Hier wird der Knopf zum Wuerfeln definiert.
    private Knopf Wuerfeln;
    final double bLinks = 330;
    final double bOben = 100;
    final double bBreite = 100;
    final double bHoehe = 30;
    final String bAufschrift = "Wuerfeln";

    private Etikett werSpielt;
    final double fLinks = 130;
    final double fOben = 150;
    final double fBreite = 200;
    final double fHoehe = 30;
    final String fText = "am Zug:";

    final boolean DEBUG = true;

    // Es wird der Echoclient vorausgesetzt.
    private Echoclient echo;

    // Attribute
    /**
     * Konstruktor
     */
    public GUI()
    {
        //Initialisierung der Oberklasse
        super(700, 750); 
        int i=0;

        /**
         * Hier werden die einzelnen ticBoxen aufwendig erzeugt. Dazu wird erst einmal das Array definiert,
         * es umfasst [12](-3) BigBoxen für die einzelnen Würfelergebnisse, und je [4] für Breite und Höhe.
         * Dann werden erst die bigBoxen durchgezählt, dann die Spalten, dann die Reihen.
         * Nach und nach wird nun mittels i hochgezählt, es werden ticBoxen erzeugt, wobei der Abstand, die Breite und die Höhe miteingerechnet wird.
         * Zusätzlich bekommen die ticBoxen Knöpfe, welche auch den wert "knopfGeklickt" haben.
         */
        ticBox = new Knopf [12][4][4];
        for(int bigBox = 1; bigBox <=9; bigBox++)
        { for (int column = 1; column<4; column++)
            { for (int row = 1; row < 4; row++)
                {
                    i++;
                    int x=startx+((bigBox+2)%3)*3*(ticBoxWidth+abstandBoxen)+row*(ticBoxWidth);
                    int y= starty+(((int)((bigBox+2)/3.0))*(ticBoxHeight+abstandBoxen))*3+column*(ticBoxHeight);

                    ticBox[bigBox][row][column] = new Knopf(x,y, ticBoxWidth, ticBoxHeight, ""+i, "knopfGeklickt");
                }  
            }
        }
        if ( DEBUG == true) System.out.println("GUI: Es wurden alle Knöpfe erzeugt.");
        /**
         * Es werden Knöpfe erzeugt: Reset, Verbinden, Adress- und Portfeld, sowie Wuerfeln und das Textfeld für die Ausgabe des Würfelergebnisses.
         */
        reset = new Knopf(pLinks, pOben, pBreite, pHoehe, pAufschrift);
        //Aufgeben = new Knopf(aLinks, aOben, aBreite, aHoehe, aAufschrift);
        Verbinden = new Knopf(aLinks, aOben, aBreite, aHoehe, aAufschrift);
        Verbinden.setzeBearbeiterGeklickt("VerbindenGeklickt");     

        textfeldAddresse = new Textfeld( tLinks, tOben, tBreite, tHoehe, tText);
        textfeldPort = new Textfeld (tLinks2, tOben2, tBreite2, tHoehe2, tText2);

        Wuerfeln = new Knopf(bLinks, bOben, bBreite, bHoehe, bAufschrift);
        Wuerfeln.setzeBearbeiterGeklickt("WuerfelnGeklickt");

        wuerfelErgebnis = new Etikett(cLinks,cOben,cBreite,cHoehe,cText);

        werSpielt = new Etikett(fLinks,fOben,fBreite,fHoehe,fText);
        Wuerfeln.deaktiviere();
    }

    /**
     * Hier wurde der Knopf "Verbinden" geklickt, wodurch dem Echoclient übergeben wird, mit welcher
     * Ip-Adresse und welchem Port man sich verbinden will.
     */
    public void VerbindenGeklickt()
    {
        try
        {
            echo = new Echoclient(textfeldAddresse.inhaltAlsText(), Integer.parseInt(textfeldPort.inhaltAlsText()), this);
            if (echo != null)
            {
                if (0==echo.toString().compareTo("Verbindung mit Socket: null\n"))
                {
                    if (DEBUG == true) System.out.println("GUI: Es wurde ein Client erzeugt");
                    Verbinden.deaktiviere();
                    textfeldAddresse.deaktiviere();
                    textfeldPort.deaktiviere();
                }
                else
                {
                    System.out.println("GUI: Fehler beim Erzeugen der Verbindung: ");
                }

            }
        }
        catch (Exception pFehler)
        {
            System.err.println("GUI: Fehler beim Erzeugen der Verbindung: " + pFehler);
        }       

        if ( DEBUG == true) System.out.println("GUI: VerbindenGeklickt: " +"IP "+textfeldAddresse.inhaltAlsText()+ " Port "+textfeldPort.inhaltAlsText() + " ");
    }

    /**
     * Hier können alle ticBoxen deaktiviert werden. Dies sollte nach jedem Spielzug geschehen.
     */
    public void WuerfelnGeklickt()
    {
        echo.send("wuerfeln");
        if (DEBUG == true) System.out.println("GUI: Es wurden die Würfel angefordert");
        Wuerfeln.deaktiviere();
    }

    public void deaktiviere()
    { for (int bigBox = 1; bigBox <=9; bigBox++)
        { for (int column = 1; column<4; column++)
            { for (int row = 1; row < 4; row++)
                    ticBox[bigBox][row][column].deaktiviere();
            }
        }
        if ( DEBUG == true) System.out.println("GUI: Alles wurde deaktiviert");
    }

    /**
     * Hier können spezifische ticBoxen in bigBoxen angesteuert werden. Dazu werden lediglich Zeile und Spalte übergeben.
     */
    public void aktiviere(int row,int column)
    {
        for (int bigBox = 1; bigBox <= 9; bigBox++)
        {
            if(Integer.parseInt(ticBox[bigBox][row][column].inhaltAlsText())==0)
            {
                ticBox[bigBox][row][column].aktiviere();
            }
            if ( DEBUG == true) System.out.println("GUI: aktiviere ticBox " + row + column);
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

                { if(Integer.parseInt(ticBox[bigBox][row][column].inhaltAlsText())==0)
                    {
                        ticBox[bigBox][row][column].aktiviere();
                    }
                }
            }
        }
        if ( DEBUG == true) System.out.println("GUI: Es wurde alles aktiviert. Achtung, dies ist lediglich eine Dummy-Funktion!");
    }

    /**
     * Hier wird eine ticBox geklickt. Dabei wird erkannt, in welcher bigBox, Reihe und Spalte diese liegt,
     * was wiederum dem Client übergeben wird. Im Nachhinein werden alle ticBoxen deaktiviert.
     */
    public void knopfGeklickt()
    {
        int geklicktBigbox=0;
        int geklicktRow=0;
        int geklicktColumn=0;

        for(int bigBox = 1; bigBox <= 9; bigBox++)
        { for (int column = 1; column<4; column++)
            { for (int row = 1; row < 4; row++)
                {
                    if (ticBox[bigBox] [row] [column].besitztFokus())
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
        if ( DEBUG == true) System.out.println("GUI: knopfGeklickt "+" Spalte "+geklicktRow+" Reihe "+geklicktColumn);
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
                    ticBox[bigBox][row][column].setzeInhalt(pFeld[bigBox][row][column]);
                }
            }            
        }      
        if ( DEBUG == true) System.out.println("GUI: Aktualisiere wurde aufgerufen");
    }

    /**
     * Yay, wir haben das Würfelergebnis bekommen. Ab damit ins Etikett und aktiviert die ticBoxen!
     */
    public void gewuerfelt(int pa, int pb)
    {
        deaktiviere(); 

        int e = pa+pb;
        switch (e) {
            case 3:  aktiviere(1,1);
            break;
            case 4: aktiviere(1,2);
            break;
            case 5: aktiviere(1,3);
            break;
            case 6:  aktiviere(2,1);
            break;
            case 7:  aktiviere(2,2);
            break;
            case 8:  aktiviere(2,3);
            break;
            case 9:  aktiviere(3,1);
            break;
            case 10:  aktiviere(3,2);
            break;
            case 11: aktiviere(3,3);
            break;

        }
        wuerfelErgebnis.setzeInhalt("Würfel 1:"+pa+" Würfel 2:"+pb);
        Wuerfeln.deaktiviere();
        if ( DEBUG == true) System.out.println("GUI: Es wurde gewürfelt!");
    }

    /**
     * Spiel, Satz und Sieg.
     */
    public void gewonnen()
    {

    }

    /**
     * Sei jetzt bloß kein schlechter Verlierer.
     */
    public void verloren()
    {

    }

    public void duSpielst (boolean pAktiv)
    {
        if (pAktiv == true)
        {
            werSpielt.setzeInhalt("am Zug: Du");
            werSpielt.aktiviere();
            Wuerfeln.aktiviere();
            if ( DEBUG == true) System.out.println("GUI: Würfelknopf aktiviert");
        }
        else
        {
            werSpielt.setzeInhalt("am Zug: Gegner");
            Wuerfeln.deaktiviere();
        }
    }
}

