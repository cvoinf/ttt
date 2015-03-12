
import sum.komponenten.*;
import sum.werkzeuge.*;
import sum.ereignis.*;
import sum.multimedia.Bild;

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

    // Hier werden die ticBoxen, also die einzelnen Knöpfe in einem Array definiert und die Höhe, Breite etc. festgelegt.

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

    // Hier wird das Textfeld für die Ip-Eingabe definiert.
    private Textfeld textfeldAddresse;
    final double tLinks = 130;
    final double tOben = 50;
    final double tBreite =500;
    final double tHoehe = 30;
    final String tText = "10.68.112.9";

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

    private Etikett werSpielt;
    final double fLinks = 130;
    final double fOben = 150;
    final double fBreite = 200;
    final double fHoehe = 30;
    final String fText = "am Zug:";

    final boolean debug = true;

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
        if (debug) System.out.println("GUI: Es wurden alle Knöpfe erzeugt.");
        /**
         * Es werden Knöpfe erzeugt: Reset, Verbinden, Adress- und Portfeld, sowie Wuerfeln und das Textfeld für die Ausgabe des Würfelergebnisses.
         */
        reset = new Knopf(pLinks, pOben, pBreite, pHoehe, pAufschrift);
        reset.setzeBearbeiterGeklickt("resetGeklickt");
        //Aufgeben = new Knopf(aLinks, aOben, aBreite, aHoehe, aAufschrift);
        Verbinden = new Knopf(aLinks, aOben, aBreite, aHoehe, aAufschrift);
        Verbinden.setzeBearbeiterGeklickt("VerbindenGeklickt");     

        textfeldAddresse = new Textfeld( tLinks, tOben, tBreite, tHoehe, tText);
        textfeldPort = new Textfeld (tLinks2, tOben2, tBreite2, tHoehe2, tText2);

        Wuerfeln = new Knopf(bLinks, bOben, bBreite, bHoehe, bAufschrift);
        Wuerfeln.setzeBearbeiterGeklickt("WuerfelnGeklickt");
       //Bilder müssen sich aktualisieren oder gelöscht und neu erzeugt werden.
       

        werSpielt = new Etikett(fLinks,fOben,fBreite,fHoehe,fText);
		w1=  new Bild(30,80,20,20,"wR1.jpg");
	w2=  new Bild(30,80,20,20,"wR2.jpg");
	w3=  new Bild(30,80,20,20,"wR3.jpg");
	w4=  new Bild(30,80,20,20,"wR4.jpg");
	w5=  new Bild(30,80,20,20,"wR5.jpg");
	w6=  new Bild(30,80,20,20,"wR6.jpg");


        wuerfel1= new Bild(30,80,20,20,w1);
        wuerfel2= new Bild(30,160,20,20,w2);
        
        Wuerfeln.deaktiviere();
    }
  
    /**
     * Der Reset Knopf wurde gedrückt, das Spiel soll sich von neuem starten, das wird dem Server mitgeteilt.
     */
    public void resetGeklickt()
    {
        echo.send("reset");
        if(debug)
        {
            System.out.println("Alles resetet von GUI-Seite her.");
            
        }
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
                echo.isConnected(); //Der Client wird gefragt, ob er eine Verbindung hat.
                if (Verbunden==true)
                {
                    if (debug) System.out.println("GUI: Es wurde ein Client("+textfeldAddresse.inhaltAlsText()+","+textfeldPort.inhaltAlsText()+") erzeugt");
                    Verbinden.deaktiviere();
                    textfeldAddresse.deaktiviere();
                    textfeldPort.deaktiviere();
                }
                else
                {
                    System.out.println("GUI: Fehler beim Erzeugen der Verbindung!");
                }
            }
        }
        catch (Exception pFehler)
        {
            System.err.println("GUI: Fehler beim Erzeugen der Verbindung: " + pFehler);
        }      
    }

    /**
     * Hier können alle ticBoxen deaktiviert werden. Dies sollte nach jedem Spielzug geschehen.
     */
    public void WuerfelnGeklickt()
    {
        echo.send("wuerfeln");
        Wuerfeln.deaktiviere();
    }

    public void deaktiviere()
    { for (int bigBox = 1; bigBox <=9; bigBox++)
        { for (int column = 1; column<4; column++)
            { for (int row = 1; row < 4; row++)
                    ticBox[bigBox][row][column].deaktiviere();
            }
        }
        this.hatBildschirm.repaint();      
        if (debug) System.out.println("GUI: Alles Knöpfe wurden deaktiviert.");
    }

    /**
     * Hier können spezifische ticBoxen in bigBoxen angesteuert werden. Dazu werden lediglich Zeile und Spalte übergeben.
     */
    public void aktiviere(int row,int column)
    {

        if (dran == true)
        {

            for (int bigBox = 1; bigBox <= 9; bigBox++)
            {
                if(Integer.parseInt(ticBox[bigBox][row][column].inhaltAlsText())==0)
                {
                    ticBox[bigBox][row][column].aktiviere();
                }

            }
            this.hatBildschirm.repaint();
            if (debug) System.out.println("GUI: Hat alle ticBoxen in Reihe " + row +"und Spalte "+ column+" aktiviert.");
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
        this.hatBildschirm.repaint();
        if (debug) System.out.println("GUI: Es wurde alles aktiviert.");
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
        this.hatBildschirm.repaint();
        if (debug) System.out.println("GUI: Der Knopf in Spalte "+geklicktRow+" und in Reihe "+geklicktColumn+" wurde geklickt.");
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
        this.hatBildschirm.repaint();
        if (debug)
        {
            System.out.println("GUI: Das Spielfeld wurde aktualisiert.");
        }
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
        wuerfelErgebnis.setzeInhalt("Würfel 1:"+pa+" Würfel 2:"+pb);
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
            case 7:   wuerfel2.setzeBild(w1);
            break;
            case 8:   wuerfel2.setzeBild(w2);
            break;
            case 9:   wuerfel2.setzeBild(w3);
            break;
            case 10:   wuerfel2.setzeBild(w4);
            break;
            case 11:   wuerfel2.setzeBild(w5);
            break;
            case 12:   wuerfel2.setzeBild(w6);
            break;
        }
       
        Wuerfeln.deaktiviere();
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
    }

    /**
     * Sei jetzt bloß kein schlechter Verlierer.
     */
    public void verloren()
    {
        Wuerfeln.deaktiviere();
        if (debug)
        {
            System.out.println("Du hast verloren!");
        }
    }

    public void duSpielst (boolean pAktiv)
    {
        if (pAktiv == true)
        {
            werSpielt.setzeInhalt("am Zug: Du");
            werSpielt.aktiviere();
            Wuerfeln.aktiviere();
            if (debug) System.out.println("GUI: Du bist dran mit Würfeln.");
            dran = true;
        }
        else
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
}

