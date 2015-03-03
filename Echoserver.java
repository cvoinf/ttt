import sum.werkzeuge.*;
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

    private Rechner re;

    private boolean spieler1Dran; 
    private boolean spieler2Dran; 
    

    private boolean spielVorbei;

    private int spielfeld[][][]; // drei-dimensionale Int Array beschriebt Spielfeld: [bigBox] [column] [row]

    private boolean debug;
    private final boolean DEBUG = true;

    public Echoserver()
    {
        super(5557); //Hiermit wird der Port "5557" und der Konstruktor von der Klasse "Server" übernommen

        spieler1Dran = false; //Spieler 1 ist nicht dran
        spieler2Dran = false; //Spieler 2 ist nicht dran 
        spielVorbei = false;

        symbolSpieler1 = "symbol:Kreuz"; //Spieler1 Symbol "Kreuz" zugeteilt
        symbolSpieler2 = "symbol:Kreis"; //Spieler2 Symbol "Kreis" zugeteilt

        re = new Rechner(); //der Rechenr wird erstellt

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

        debug = true;
    }

    // Dienste
    public void processNewConnection(String pClientIP, int pClientPort) {
        if(spieler1 == null )
        {
            if (DEBUG == true)
                System.out.println("Server: Client1 verbunden");
            spieler1 = pClientIP;
            spieler1Port = pClientPort;
            send(spieler1, spieler1Port, symbolSpieler1);
            if (DEBUG == true)
                System.out.println("Server: Client1 verbunden fertig");
        }
        else if(spieler2 == null)
        {
             if (DEBUG == true)
                System.out.println("Server: Client 2 verbunden");
            spieler2 = pClientIP;
            spieler2Port = pClientPort;
            send(spieler2, spieler2Port, symbolSpieler2  );
            if (DEBUG == true)
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
        if (wuerfeln(1,2)== 1)
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

    }

    /**
     * Methode, um ganze Zufallszahlen zwischen zwei Grenzwerten zu erzeugen
     * @param pMin untere Grenze
     * @
     * @return Zufallszahl
     */
    public int wuerfeln(int pMin, int pMax)
    {
        if(debug)
        {
            System.out.println("Server: gewürfelt");
        }
        return re.ganzeZufallsZahl(pMin, pMax);        
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
        if (b[0].compareTo("wuerfeln")==0)
        {
            int xa = wuerfeln(1,6);
            int xb = wuerfeln(1,6);
            if (xa+xb ==2 || xa+xb == 12)
            {
                if(pClientIP == spieler1)
                {
                    send(spieler2, spieler2Port, "zugBeginnt: ");
                    send(spieler1, spieler1Port, "nichtAmZug: ");
                    spieler2Dran = true;
                    spieler1Dran = false;
                    if(debug)
                    {
                        System.out.println("Spieler 2 dran");
                    }
                }
                else if (pClientIP == spieler2)
                {
                    send(spieler1, spieler1Port, "zugBeginnt: ");
                    send(spieler2, spieler2Port, "nichtAmZug: ");
                    spieler1Dran = true;
                    spieler2Dran = false;
                    if(debug)
                    {
                        System.out.println("Spieler 1 dran");
                    }
                }
                if(debug)
                {
                    System.out.println("Server: Reihenfolge gewechselt");
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
                if(debug)
                {
                    System.out.println("Server: Spielfeld von Spieler 2 bekommen");
                }
            }
            feldUeberpruefen();

            send(spieler1, spieler1Port, "aktualisiere:" +spielfeldAusgeben());
            send(spieler2, spieler2Port, "aktualisiere:" +spielfeldAusgeben());            
            if(debug)

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
            if (spieler1Dran)
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
            else if (spieler2Dran)
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
                    send(spieler1, spieler1Port, "gewonnen:spieler1 ");
                    send(spieler2, spieler2Port, "gewonnen:spieler1 ");
                }
                else 
                {
                    send(spieler1, spieler1Port, "gewonnen:spieler2 ");
                    send(spieler2, spieler2Port, "gewonnen:spieler2 ");
                }
            }
        }
    }

    /**
     * Überprüft, ob das Spiel vorbei ist, setzt boolean spielVorbei dementsprechend     
     */
    public void feldUeberpruefen() //Reihenfolge der Felder in der GUI relevant?
    {
        if (feldGleich(0,1,2))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else if (feldGleich(3,4,5))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else if (feldGleich(6,7,8))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else if (feldGleich(0,3,6))
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
        else if (feldGleich(0,4,8))
        {
            spielVorbei = true;
            if(debug)
            {
                System.out.println("Spielvorbei = true");
            }
        }
        else if (feldGleich(2,4,6))
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
            System.out.println("Server: auf Sieger überprüft  (feldUeberpruefen())");
        }
    }

    /**     
     * Mehtode, die überprüft, ob 3 Felder den gleichen Inhalt haben;
     * von Relevanz für die Siegbedingung
     * @param pFeld1  int erstes Feld
     * @param pFeld2  int zweites Feld
     * @param pFeld3  int drittes Feld
     * @return true, wenn alle Felder gleich
     */
    public boolean feldGleich(int pFeld1, int pFeld2, int pFeld3)
    {
        if (boxUeberpruefen(pFeld1) == boxUeberpruefen(pFeld2) && boxUeberpruefen(pFeld1) == boxUeberpruefen(pFeld3))
        {
            if(debug)
            {
                System.out.println("feldGleich= true");
            }
            return true;
            
        }
        else
        {
            if(debug)
            {
                System.out.println("feldGleich= false");
            }
            return false;
        }
    }

    /**
     * Überpfrüft, ob eine Box von einem Spieler gewonnen wurde, setzt  die Felder dementsprechend
     */
    public int boxUeberpruefen(int pBox)
    {
        int sieger = 0;
        if (spielfeld[pBox][0][0] == spielfeld[pBox][1][0]&& spielfeld[pBox][0][0] == spielfeld[pBox][2][0] &&spielfeld[pBox][0][0]!=0)
        {
            for (int i =0; i<=2;i++)
            {
                for (int j=0;  j<=2;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][0][0];
                }
            }
            sieger = spielfeld[pBox][0][0];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][0][1] == spielfeld[pBox][1][1]&& spielfeld[pBox][0][1]  == spielfeld[pBox][2][1] && spielfeld[pBox][2][1] !=0)
        {
            for (int i =0; i<=2;i++)
            {
                for (int j=0;  j<=2;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][0][1];
                }
            }
            sieger = spielfeld[pBox][0][1];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][0][2] == spielfeld[pBox][1][2] && spielfeld[pBox][0][2]  == spielfeld[pBox][2][2] && spielfeld[pBox][2][2] != 0)
        {
            for (int i =0; i<=2;i++)
            {
                for (int j=0;  j<=2;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][0][2];
                }
            }
            sieger = spielfeld[pBox][0][2];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][0][0] == spielfeld[pBox][0][1] && spielfeld[pBox][0][0] == spielfeld[pBox][1][2] && spielfeld[pBox][1][2] !=0)
        {
            for (int i =0; i<=2;i++)
            {
                for (int j=0;  j<=2;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][0][0];
                }
            }
            sieger = spielfeld[pBox][0][0];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][1][0] == spielfeld[pBox][1][1] && spielfeld[pBox][1][0] == spielfeld[pBox][1][2] && spielfeld[pBox][1][2]!= 0)
        {
            for (int i =0; i<=2;i++)
            {
                for (int j=0;  j<=2;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][1][0];
                }
            }
            sieger = spielfeld[pBox][1][0];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][2][0] == spielfeld[pBox][2][1] && spielfeld[pBox][2][0] == spielfeld[pBox][2][2] && spielfeld[pBox][2][2] != 0)
        {
            for (int i =0; i<=2;i++)
            {
                for (int j=0;  j<=2;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][2][0];
                }
            }
            sieger = spielfeld[pBox][2][0];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][0][0] == spielfeld[pBox][1][1] &&spielfeld[pBox][0][0] == spielfeld[pBox][2][2] && spielfeld[pBox][2][2] !=0)
        {
            for (int i =0; i<=2;i++)
            {
                for (int j=0;  j<=2;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][0][0];
                }
            }
            sieger = spielfeld[pBox][0][0];
            if (debug)
            {
                System.out.println("Boxgewonnen "+ pBox);
            }
        }
        else if (spielfeld[pBox][0][2] == spielfeld[pBox][1][1] && spielfeld[pBox][0][2] == spielfeld[pBox][2][0] && spielfeld[pBox][2][0] !=0)
        {
            for (int i =0; i<=2;i++)
            {
                for (int j=0;  j<=2;j++)
                {
                    spielfeld[pBox][i][j] = spielfeld[pBox][0][2];
                }
            }
            sieger = spielfeld[pBox][0][2];
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

    public void processClosedConnection(String pClientIP, int pClientPort) 
    {
        closeConnection(pClientIP,pClientPort);
    }
}
