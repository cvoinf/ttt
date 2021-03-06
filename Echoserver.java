import java.util.Random;
/**
 * @author Finn Klessascheck, Jannick Mohr & Angelina Horn
 * @version 1601/2015
 */
public class Echoserver extends Server
{
    private String spieler1; //IP von Spieler1 als String
    private String spieler2; //IP von Spieler2 als String

    private int spieler1Port; //Port von Spieler1 als int
    private int spieler2Port; //Port von Spieler2 als int

    private String symbolSpieler1; //Symbol von Spieler1 als String
    private String symbolSpieler2; //Symbol von Spieler2 als String

    //private Rechner re;

    private boolean spieler1Dran; 
    private boolean spieler2Dran; 
    private boolean spieler1reset;
    private boolean spieler2reset;

    private boolean spielVorbei;
    private int spielfeld[][][]; // drei-dimensionale Int Array beschriebt Spielfeld: [bigBox] [column] [row]
    private final boolean debug = true;

    private Random ra;

    private int feld1;
    private int feld2;
    private int feld3;
    private int feld4;
    private int feld5;
    private int feld6;
    private int feld7;
    private int feld8;
    private int feld9;

    public Echoserver()
    {
        super(5557); //Hiermit wird der Port "5557" und der Konstruktor von der Klasse "Server" übernommen

        spieler1Dran = false; //Spieler 1 ist nicht dran
        spieler2Dran = false; //Spieler 2 ist nicht dran 
        spielVorbei = false;
        spieler1reset = false;
        spieler2reset = false;

        symbolSpieler1 = "symbol:Kreuz"; //Spieler1 Symbol "Kreuz" zugeteilt
        symbolSpieler2 = "symbol:Kreis"; //Spieler2 Symbol "Kreis" zugeteilt

        //re = new Rechner(); //der Rechenr wird erstellt

        ra = new Random();

        spielfeld = new int[12][4][4]; //Die Anzahl der "Felder" im Array werden festgelegt
        for (int bigBox=1 ; bigBox<=9; bigBox++) //Die Felder des Arrays werden auf den Wert "0" gestellt und das Feld ist spielbereit
        {
            for (int column=1; column <=3; column++)
            {
                for (int row=1; row <=3; row ++)
                {
                    spielfeld[bigBox][row][column] = 0; 
                }
            }            
        }

    }

    // Dienste
    public void processNewConnection(String pClientIP, int pClientPort) {
        if(spieler1 == null )
        {
            if (debug == true)
                System.out.println("Server: Client1 verbunden");
            spieler1 = pClientIP;
            spieler1Port = pClientPort;
            send(spieler1, spieler1Port, symbolSpieler1);
            if (debug == true)
                System.out.println("Server: Client1 verbunden fertig");
        }
        else if(spieler2 == null)
        {
            if (debug == true)
                System.out.println("Server: Client 2 verbunden");
            spieler2 = pClientIP;
            spieler2Port = pClientPort;
            send(spieler2, spieler2Port, symbolSpieler2  );
            if (debug == true)
                System.out.println("Server: Client2 verbunden fertig");
            start();            
        }
    }

    /**
     * Gibt den Array des Spielfelds als String aus.
     * @return Spielfeld als String
     */
    public String spielfeldAusgeben()
    {
        String xb = new String();
        for (int bigBox=1 ; bigBox<=9; bigBox++) 
        {
            for (int column=1; column <=3; column++)
            {
                for (int row=1; row <=3; row ++)
                {
                    xb = xb + spielfeld[bigBox][row][column] +":";
                }
            }      
        }
        return xb;
    }

    /**
     * Spiel wird gestartet, Startspieler wird ausgewürfelt.
     * Beiden Spielern wird das Spielfeld übergeben.
     */
    public void start()
    {        
        if(debug)
        {
            System.out.println("Server: Start aufgerufen");
        }
        if (wuerfeln(2)== 1)
        {
            send(spieler1, spieler1Port, "aktualisiere:"+spielfeldAusgeben());
            send(spieler2, spieler2Port, "aktualisiere:"+spielfeldAusgeben());
            send(spieler2, spieler2Port, "nichtAmZug: ");
            send(spieler1, spieler1Port, "zugBeginnt: ");
            spieler1Dran = true;
            if(debug)
            {
                System.out.println("Server: Spieler 1 dran");
            }
        }
        else 
        {
            send(spieler2, spieler2Port, "aktualisiere:"+spielfeldAusgeben());
            send(spieler1, spieler1Port, "aktualisiere:"+spielfeldAusgeben());
            send(spieler1, spieler1Port, "nichtAmZug: ");
            send(spieler2, spieler2Port, "zugBeginnt: ");
            spieler2Dran = true;
            if(debug)
            {
                System.out.println("Server: Spieler 2 dran");
            }
        }
        spieler1reset = false;
        spieler2reset = false;

    }

    /**
     * Methode, um ganze Zufallszahlen zwischen zwei Grenzwerten zu erzeugen
     * @param pMin untere Grenze
     * @
     * @return Zufallszahl
     */
    public int wuerfeln(int pMax)
    {
        if(debug)
        {
            System.out.println("Server: gewürfelt");
        }
        int ergebnis = 0;        
        while (ergebnis ==0)
        {
            ergebnis = ra.nextInt(pMax +1);
        }
        if (debug) System.out.println("Server würfelt: "+ergebnis);
        return ergebnis;        
    }

    /**
     * Nachrichten, die von den Clients an den Server gesendet werden, werden verarbeitet.
     * Es wird 
     *     -für den Client gewürfelt
     *     -ein Spielzug durchgeführt und das aktualisierte Spielfeld an alle Clients gesendet
     */
    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
        String a = pMessage;
        String b[] = a.split(":");
        String zClientIP = pClientIP;
        if (b[0].compareTo("wuerfeln")==0)
        {
            int xa = wuerfeln(6);
            int xb = wuerfeln(6);

            if (xa+xb ==2 || xa+xb ==12 || wuerfelUeberpruefen(xa,xb) == false)
            {
                if(spieler1Dran == true)
                {
                    spieler1Dran = false;
                    spieler2Dran = true;
                    send(spieler1, spieler1Port, "wuerfel:" + xa+","+xb);
                    send(spieler2, spieler2Port, "wuerfel:" + xa+","+xb);
                    send(spieler2, spieler2Port, "zugBeginnt: ");
                    send(spieler1, spieler1Port, "nichtAmZug: ");
                    if(debug)
                    {
                        System.out.println("Spieler1 muss wegen 12 oder 2 aussetzen");
                    }
                }
                else if(spieler2Dran == true)
                {
                    spieler1Dran = true;
                    spieler2Dran = false;
                    send(spieler1, spieler1Port, "wuerfel:" + xa+","+xb);
                    send(spieler2, spieler2Port, "wuerfel:" + xa+","+xb);
                    send(spieler2, spieler2Port, "nichtAmZug: ");
                    send(spieler1, spieler1Port, "zugBeginnt: ");
                    if(debug)
                    {
                        System.out.println("Spieler2 muss wegen 12 oder 2 aussetzen");
                    }
                }
            }
            else 
            {
                send(spieler1, spieler1Port, "wuerfel:" + xa+","+xb);
                send(spieler2, spieler2Port, "wuerfel:" + xa+","+xb);
                if(debug)
                {
                    System.out.println("Server: Würfelergebnisse "+xa+", "+xb+" weitergegeben");
                }
            }
        }
        else if(b[0].compareTo("feld")==0)
        {                      
            if (spieler1Dran) //=Kreuz
            {
                String c[] = b[1].split(",");
                int z0 = Integer.parseInt(c[0]);
                int z1 = Integer.parseInt(c[1]);
                int z2 = Integer.parseInt(c[2]);
                spielfeld[z0][z1][z2] = 1;
                if(debug)
                {
                    System.out.println("Server: Spielfeld von Spieler 1 bekommen");
                }
            }
            else if (spieler2Dran)           //=Kreis
            {
                String c[] = b[1].split(",");
                int z0 = Integer.parseInt(c[0]);
                int z1 = Integer.parseInt(c[1]);
                int z2 = Integer.parseInt(c[2]);
                spielfeld[z0][z1][z2] = 2;
                if (debug)
                {
                    System.out.println("Server: Spielfeld von Spieler 2 bekommen");
                }
            }
            feldUeberpruefen();
            send(spieler1, spieler1Port, "aktualisiere:" +spielfeldAusgeben());
            send(spieler2, spieler2Port, "aktualisiere:" +spielfeldAusgeben());           
            if (debug)

            {
                System.out.print("Server Spielfeld weitergegeben: ");
                System.out.println("");
                String ausgabe = "";
                for (int bigBox=1 ; bigBox<=9; bigBox++) 
                {
                    for (int column=1; column <=3; column++)
                    {
                        for (int row=1; row <=3; row ++)
                        {
                            ausgabe +=spielfeld[bigBox][row][column];
                        }
                        ausgabe +=" |";
                    }

                    ausgabe +="\n";
                }
                System.out.println(ausgabe);
            }
            if (spieler1Dran && spielVorbei == false)
            {
                send(spieler2, spieler2Port, "zugBeginnt: ");
                send(spieler1, spieler1Port, "nichtAmZug: ");
                spieler1Dran = false;
                spieler2Dran = true;
                if(debug)
                {
                    System.out.println("Spieler 2 dran");
                }
            }
            else if (spieler2Dran && spielVorbei == false)
            {
                send(spieler1, spieler1Port, "zugBeginnt: ");
                send(spieler2, spieler2Port, "nichtAmZug: ");
                spieler2Dran = false;
                spieler1Dran = true;
                if(debug)
                {
                    System.out.println("Spieler 1 dran");
                }
            }
            else
            {
                if(spieler1Dran)
                {           
                    send(spieler1, spieler1Port, "gewonnen:spieler1");
                    send(spieler2, spieler2Port, "gewonnen:spieler1");
                }
                else 
                {
                    send(spieler1, spieler1Port, "gewonnen:spieler2");
                    send(spieler2, spieler2Port, "gewonnen:spieler2");
                }
            }
        }

        else if (b[0].compareTo("reset1")==0)
        {
            spieler1reset = true;
            send (spieler2, spieler2Port, "requestReset:");
            if(debug)
            {
                System.out.println("Spieler 1 hat Reset geklickt.");
            }
            if (spieler1reset == true && spieler2reset == true)
            {
                for (int bigBox=1 ; bigBox<=9; bigBox++) //Die Felder des Arrays werden auf den Wert "0" gestellt und das Feld ist spielbereit
                {
                    for (int column=1; column <=3; column++)
                    {
                        for (int row=1; row <=3; row ++)
                        {
                            spielfeld[bigBox][row][column] = 0; 
                        }
                    }            
                }
                send(spieler1, spieler1Port, "aktualisiere:" +spielfeldAusgeben());
                send(spieler2, spieler2Port, "aktualisiere:" +spielfeldAusgeben());  
                if(debug)
                {
                    System.out.println("Das Spielfeld wurde resettet und Start() aufgerufen.");
                }
                spieler1reset = false;
                spieler2reset = false;
            }
        }
        else if (b[0].compareTo("reset2")==0)
        {
            spieler2reset = true;
            send (spieler1, spieler1Port, "requestReset:");
            if(debug)
            {
                System.out.println("Spieler 2 hat Reset geklickt.");
            }
            if (spieler1reset == true && spieler2reset == true)
            {
                for (int bigBox=1 ; bigBox<=9; bigBox++) //Die Felder des Arrays werden auf den Wert "0" gestellt und das Feld ist spielbereit
                {
                    for (int column=1; column <=3; column++)
                    {
                        for (int row=1; row <=3; row ++)
                        {
                            spielfeld[bigBox][row][column] = 0; 
                        }
                    }            
                }
                send(spieler1, spieler1Port, "aktualisiere:" +spielfeldAusgeben());
                send(spieler2, spieler2Port, "aktualisiere:" +spielfeldAusgeben());  
                if(debug)
                {
                    System.out.println("Das Spielfeld wurde resettet und Start() aufgerufen.");
                }
                spieler1reset = false;
                spieler2reset = false;
            }
        }
    }

    /**
     * Überprüft, ob das Spiel vorbei ist, setzt boolean spielVorbei dementsprechend     
     */
    public void feldUeberpruefen() //Reihenfolge der Felder in der GUI relevant?
    {
        if (feldGleich(1,2,3))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else if (feldGleich(4,5,6))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else if (feldGleich(7,8,9))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else if (feldGleich(1,4,7))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else if (feldGleich(2,5,8))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else if (feldGleich(3,6,9))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else if (feldGleich(1,5,9))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else if (feldGleich(3,5,7))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else
        {
            spielVorbei = false;
            if(debug)
            {
                System.out.println("Spielvorbei = false");
            }
        } 
        if(debug)
        {
            System.out.println("Server: auf Sieger überprüft (feldUeberpruefen())");
        }
    }

    /**     
     * Methode, die überprüft, ob 3 Felder den gleichen Inhalt haben;
     * von Relevanz für die Siegbedingung
     * @param pFeld1  int erstes Feld
     * @param pFeld2  int zweites Feld
     * @param pFeld3  int drittes Feld
     * @return true, wenn alle Felder gleich
     */
    public boolean feldGleich(int pFeld1, int pFeld2, int pFeld3)
    {
        if (boxUeberpruefen(pFeld1) == boxUeberpruefen(pFeld2) && boxUeberpruefen(pFeld1) == boxUeberpruefen(pFeld3) && boxUeberpruefen(pFeld1) !=0)
        {
            if(debug)
            {
                System.out.println("feldGleich = true");
            }
            return true;
        }
        else
        {
            //             if(debug)
            //             {
            //                 System.out.println("feldGleich = false" + 
            //                     " boxUeberpruefen("+pFeld1+") = "+boxUeberpruefen(pFeld1) +
            //                     " boxUeberpruefen("+pFeld2+") = "+boxUeberpruefen(pFeld2) +
            //                     " boxUeberpruefen("+pFeld3+") = "+boxUeberpruefen(pFeld3) 
            //                 );
            //             }
            return false;
        }
    }

    /**
     * Überpfrüft, ob eine Box von einem Spieler gewonnen wurde, setzt  die Felder dementsprechend
     */
    public int boxUeberpruefen(int pBox)
    {
        int sieger = 0;
        if (spielfeld[pBox][1][1] == spielfeld[pBox][1+1][1]&& spielfeld[pBox][1][1] == spielfeld[pBox][2+1][1] &&spielfeld[pBox][1][1]!=0)
        {
            for (int i =1; i<=2+1;i++)
            {
                for (int j=1;  j<=2+1;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][1][1];
                }
            }
            sieger = spielfeld[pBox][0+1][0+1];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][1][1+1] == spielfeld[pBox][1+1][1+1]&& spielfeld[pBox][1][1+1]  == spielfeld[pBox][2+1][1+1] && spielfeld[pBox][2+1][1+1] !=0)
        {
            for (int i =1; i<=2+1;i++)
            {
                for (int j=1;  j<=2+1;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][1][1+1];
                }
            }
            sieger = spielfeld[pBox][0+1][1+1];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][1][2+1] == spielfeld[pBox][1+1][2+1] && spielfeld[pBox][1][2+1]  == spielfeld[pBox][2+1][2+1] && spielfeld[pBox][2+1][2+1] != 0)
        {
            for (int i =1; i<=2+1;i++)
            {
                for (int j=1;  j<=2+1;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][1][2+1];
                }
            }
            sieger = spielfeld[pBox][0+1][2+1];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][1][1] == spielfeld[pBox][1][2] && spielfeld[pBox][1][1] == spielfeld[pBox][1][3] && spielfeld[pBox][1][2+1] !=0) // bei mletzten vor !=0 spielfeld[pBox][1+1][2+1]
        {
            for (int i =1; i<=2+1;i++)
            {
                for (int j=1;  j<=2+1;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][1][1];
                }
            }
            sieger = spielfeld[pBox][0+1][0+1];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][1+1][1] == spielfeld[pBox][1+1][1+1] && spielfeld[pBox][1+1][1] == spielfeld[pBox][1+1][2+1] && spielfeld[pBox][1+1][2+1]!=0)
        {
            for (int i =1; i<=2+1;i++)
            {
                for (int j=1;  j<=2+1;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][1+1][1];
                }
            }
            sieger = spielfeld[pBox][1+1][0+1];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][2+1][1] == spielfeld[pBox][2+1][1+1] && spielfeld[pBox][2+1][1] == spielfeld[pBox][2+1][2+1] && spielfeld[pBox][2+1][2+1] != 0)
        {
            for (int i =1; i<=2+1;i++)
            {
                for (int j=1;  j<=2+1;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][2+1][1];
                }
            }
            sieger = spielfeld[pBox][2+1][0+1];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][1][1] == spielfeld[pBox][1+1][1+1] &&spielfeld[pBox][1][1] == spielfeld[pBox][2+1][2+1] && spielfeld[pBox][2+1][2+1] !=0)
        {
            for (int i =1; i<=2+1;i++)
            {
                for (int j=1;  j<=2+1;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][1][1];
                }
            }
            sieger = spielfeld[pBox][0+1][0+1];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][1][2+1] == spielfeld[pBox][1+1][1+1] && spielfeld[pBox][1][2+1] == spielfeld[pBox][2+1][1] && spielfeld[pBox][2+1][1] !=0)
        {
            for (int i =1; i<=2+1;i++)
            {
                for (int j=1;  j<=2+1;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][0+1][2+1];
                }
            }
            sieger = spielfeld[pBox][0+1][2+1];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else
        {
            sieger = 0;
        }
        if(debug)
        {
            System.out.println("Server: box überprüft");
        }
        return sieger;
    }

    private boolean wuerfelUeberpruefen(int pa, int pb)
    {
        int e = pa+pb;        
        boolean ergebnis = false;
        switch (e) {
            case 3:  
            if (feld1 < 8)
            {
                feld1++;
                ergebnis = true;
                if (debug) System.out.println("Feld1: "+feld1);
            }
            break;

            case 4: 
            if (feld2 < 8)
            {
                feld2++;
                ergebnis = true;
                if (debug) System.out.println("Feld2: "+feld2);
            }
            break;

            case 5:             
            if (feld3 < 8)
            {
                feld3++;
                ergebnis = true;
                if (debug) System.out.println("Feld3: "+feld3);
            }
            break;

            case 6:              
            if (feld4 < 8){
                feld4++;
                ergebnis = true;
                if (debug) System.out.println("Feld4: "+feld4);
            }
            break;

            case 7:              
            if (feld5 < 8){
                feld5++;
                ergebnis = true;
                if (debug) System.out.println("Feld5: "+feld5);
            }
            break;

            case 8:              
            if (feld6 < 8){
                feld6++;
                ergebnis = true;
                if (debug) System.out.println("Feld6: "+feld6);
            }
            break;

            case 9:              
            if (feld7 < 8){
                feld7++;
                ergebnis = true;
                if (debug) System.out.println("Feld7: "+feld7);
            }
            break;

            case 10:              
            if (feld8 < 8){
                feld8++;
                ergebnis = true;
                if (debug) System.out.println("Feld8: "+feld8);
            }
            break;

            case 11:             
            if (feld9 < 8){
                feld9++;
                ergebnis = true;
                if (debug) System.out.println("Feld9: "+feld9);
            }
            break;
        }
        return ergebnis;
    }

    public void processClosedConnection(String pClientIP, int pClientPort) 
    {
        closeConnection(pClientIP,pClientPort);
    }
}
