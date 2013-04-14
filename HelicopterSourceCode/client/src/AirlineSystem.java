/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package airline.system;

import java.io.*;
//import airline.system.resource.*;

/**
 *
 * @author Juntao Gu <juntao.gu@gmail.com>
 */
public class AirlineSystem
{
    /**
     * @param args the command line arguments
     */
    public AirlineSystem()
    {
        ui = new ClientUI();
    }

    public boolean connectToServer(ClientUI ui)
    {
        String serverIpAddress = new String();
        while (true)
        {
            ui.getToUser().print("Input Server IPAddress to Connect:");
            ui.getToUser().flush();
            try
            {
                serverIpAddress = ui.getFromUser().readLine();
                if (serverIpAddress.length() == 0)
                {
                    continue;
                }
            }
            catch (IOException e)
            {
                //e.printStackTrace();
            	System.out.println("IO ERROR");
            }

            if (ui.ipAddressFilter(serverIpAddress))
            {
                if (ui.UIConnectToServer(serverIpAddress))
                {
                    return true;
                }
            }
            else
            {
                ui.getToUser().println(serverIpAddress + " is not a valid ip address");
            }
        }
    }

    public static void main(String[] args)
    {
        AirlineSystem client = new AirlineSystem();
        if (client.connectToServer(client.ui))
        {
            client.ui.start();
        }

    }
    private ClientUI ui;
}
