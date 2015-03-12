
/**
 * @author 
 * @version 
 */
public class Echoclient extends Client
{
    // Bezugsobjekte
    private GUI zGUI;
    private String zIP;
    private int zPort;
    private String nachricht;
    private int  ArrayTicBox[][][];

    private boolean verbunden;

    private int symbol;
    private String spielerNummer;

    private boolean zugAktiv = false;

    private boolean debug = true;

    // Attribute
    // Konstruktor
    // Konstruktor
    public Echoclient(String pIP,int pPort, GUI pGUI)
    {
        super(pIP, pPort);  
        zIP = pIP;
        zPort = pPort;

        ArrayTicBox = new int[12][4][4];
        zGUI = pGUI;
        zugAktiv = false;
        aktualisiere();
    }

    // Dienste
    public void processMessage(java.lang.String text)
    {
        nachricht = text;
        String b[] = text.split(":");
        if (b[0].compareTo("symbol")==0) //Spieler1 hat Kreuz, 2 hat Kreis
        {
            if(b[1].compareTo("Kreuz")==0)
            {
                symbol = 1;
                spielerNummer = "spieler1";
            }
            else if(b[1].compareTo("Kreis")==0)
            {
                symbol = 2;
                spielerNummer = "spieler2";
            }
            else
            {
                System.out.println("Error EchoClient processMessage symbol");
            }
        }
        else if (b[0].compareTo("zugBeginnt")==0)
        {
            System.out.println("Client "+spielerNummer+": zugBeginnt");
            zugAktiv = true;
            aktualisiere();
        }
        else if (b[0].compareTo("nichtAmZug")==0)
        {
            System.out.println("Client "+spielerNummer+": nicht am Zug");
            zugAktiv = false;
            aktualisiere();
        }
        else if (b[0].compareTo("wuerfel")==0)
        {
            System.out.println("Client: compareToWürfel");
            String c[] = b[1].split(",");
            zGUI.gewuerfelt(Integer.parseInt(c[0]),Integer.parseInt(c[1]));
        }        
        else if (b[0].compareTo("aktualisiere")==0)
        {
            if (debug) System.out.println("Client: compareToAktualisiereAnfang");
            int zaehler=1;
            for (int bigBox=1 ; bigBox<=9; bigBox++) 
            {
                for (int column=1; column <=3; column++)
                {
                    for (int row=1; row <=3; row++)
                    {

                        ArrayTicBox[bigBox][row][column] = Integer.parseInt( b[zaehler]);
                        zaehler++;

                    }
                }            
            }

            if (debug)
            {
                System.out.print("EchoClient.processMessage() : ");
                System.out.println("");
                String ausgabe = "";
                for (int bigBox=1 ; bigBox<=9; bigBox++) 
                {
                    for (int column=1; column <=3; column++)
                    {
                        for (int row=1; row <=3; row ++)
                        {
                            ausgabe +=ArrayTicBox[bigBox][row][column];
                        }
                        ausgabe +=" |";
                    }
                    ausgabe +="\n";
                }
                System.out.println(ausgabe);
            }
            zGUI.aktualisiere(ArrayTicBox);
            aktualisiere();
            System.out.println("Client: compareToAktualisiereEnde");
        }
        else if (b[0].compareTo("gewonnen")==0)
        {
            if(b[1].compareTo(spielerNummer)==0)
            {
                zGUI.gewonnen();
            }
            else
            {
                zGUI.verloren();
            }
        }
        else if (b[0].compareTo("requestReset")==0)
        {
            zGUI.requestReset();
        }
    }

    public void isConnected ()
    {
        if (hatVerbindung.isConnected())
        { 
            zGUI.Verbunden=true;
        }
        else
        {
            System.out.println("GUI: Fehler beim Erzeugen der Verbindung!");
        }
    }

    private void aktualisiere()
    {
        if(zugAktiv)
        {
            zGUI.deaktiviere();
            zGUI.duSpielst(true);
        }
        else
        {
            zGUI.deaktiviere();
            zGUI.duSpielst(false);
        }
    }

    public void knopfGedrueckt(int bigBox, int row, int column)
    {
        send("feld:"+bigBox+","+row+","+column);
    }
}
