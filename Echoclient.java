
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
    
    private boolean zugAktiv;

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
        zGUI.deaktiviere(); 
    }

    // Dienste
    public void processMessage(java.lang.String text)
    {
        nachricht = text;
        String b[] = text.split(":");
        if (b[0].compareTo("symbol")==0) //Spieler1 hat Kreuz, 2 hat Kreis
        {
            if(b[1]== "Kreuz")
            {
                symbol = 1;
                spielerNummer = "spieler1";
            }
            else if(b[1]== "Kreis")
            {
                symbol = 2;
                spielerNummer = "spieler2";
            }
        }
        else if (b[0].compareTo("zugBeginnt")==0)
        {
            System.out.println("Client: zugBeginnt");
            zugAktiv = true;
            zGUI.aktiviere();
        }
        else if (b[0].compareTo("nichtAmZug")==0)
        {
            System.out.println("Client: nicht am Zug");
            zugAktiv = false;
            zGUI.werSpielt(false);
            zGUI.deaktiviere();
        }
        else if (b[0].compareTo("wuerfel")==0)
        {
            System.out.println("Client: compareToWürfel");
            String c[] = b[1].split(",");
            zGUI.gewuerfelt(Integer.parseInt(c[0]),Integer.parseInt(c[1]));
        }        
        else if (b[0].compareTo("aktualisiere")==0)
        {
            System.out.println("Client: compareToAktualisiereAnfang");
            int zaehler=1;
            for (int bigBox=1 ; bigBox<=9; bigBox++) 
            {
                for (int column=1; column <=3; column++)
                {
                    for (int row=1; row <=3; row ++)
                    {

                        ArrayTicBox[bigBox][row][column] = Integer.parseInt( b[zaehler]);
                        zaehler++;

                    }
                }            
            }
            zGUI.aktualisiere(ArrayTicBox);
            System.out.println("Client: compareToAktualisiereEnde");
        }
        else if (b[0].compareTo("gewonnen")==0)
        {
            if(b[1]== spielerNummer)
            {
                zGUI.gewonnen();
            }
            else
            {
                zGUI.verloren();
            }
        }
    }
    
    public void knopfGedrueckt(int bigBox, int row, int column)
    {
        send("feld:"+bigBox+","+row+","+column); //kommentar
    }
}
