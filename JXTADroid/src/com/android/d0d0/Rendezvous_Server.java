
package com.android.d0d0;

import java.io.File;
import java.net.URI;
import java.text.MessageFormat;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import android.content.Context;

//import javax.swing.JOptionPane;

/**
 * A simple example which demonstrates the most basic JXTA operations. This
 * program configures the peer, starts JXTA, waits until the peer joins the
 * network and then concludes by stopping JXTA. 
 */
public class Rendezvous_Server {

	
	public static Context context = ApplicationClass.getApplicationClassContext();
	static File file;
	
    /**
     * Main method
     *
     * @param args Command line arguments. none defined
     */
    public static void fireUp() {

    
    	
        try {
            // Set the main thread name for debugging.
            Thread.currentThread().setName(Rendezvous_Server.class.getSimpleName());

  
             
//            String fileName = Uri.parse("Android_Client").getLastPathSegment();
            file = new File(context.getFilesDir(), "Android_Client.cache");
            System.out.println(file.exists());
            System.out.println("Starting JXTA Edge");
         
            final String d0d0EDGEdroid = "EDGE A";
            final PeerID d0d0EDGEdroidPID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, d0d0EDGEdroid.getBytes());
            final int TcpPort_EDGE_A = 9705;
            
            NetworkManager edgeManager = new NetworkManager(NetworkManager.ConfigMode.EDGE, d0d0EDGEdroid, file.toURI());
            PeerGroup TheNetPeerGroup=  edgeManager.startNetwork();
            boolean started = edgeManager.isStarted();
            System.out.println(MessageFormat.format("Edge started :{0}", started));
            

            // Retrieving the network configurator
            NetworkConfigurator MyNetworkConfigurator = edgeManager.getConfigurator();

            // Setting Configuration
            MyNetworkConfigurator.setTcpPort(TcpPort_EDGE_A);
            MyNetworkConfigurator.setTcpEnabled(true);
            MyNetworkConfigurator.setTcpIncoming(true);
            MyNetworkConfigurator.setTcpOutgoing(true);
            MyNetworkConfigurator.setUseMulticast(true);

            // Setting the Peer ID
            MyNetworkConfigurator.setPeerID(d0d0EDGEdroidPID);

            // Adding RDV a as a seed
            MyNetworkConfigurator.clearRendezvousSeeds();

            String TheSeed = "tcp://" +  "192.168.1.74:9708";
                
            URI LocalRendezVousSeedURI = URI.create(TheSeed);
            MyNetworkConfigurator.addSeedRendezvous(LocalRendezVousSeedURI);
//            
//            // Retrieving the membership service
//            PSEMembershipService ThePSEMembershipService = (PSEMembershipService)TheNetPeerGroup.getMembershipService(); 
//            
//            // Retrieving the configuration object
//            PSEConfig ThePSEConfig = ThePSEMembershipService.getPSEConfig();
//            
//            if( ThePSEConfig.isInitialized() ) {
//            	System.out.println("PSE has been initialised");
//            	String LoginPass = JOptionPane.showInputDialog(null, "Enter keystore password"); 
//            	ThePSEConfig.setKeyStorePassword(LoginPass.toCharArray());
//            	
//            } else {
//            	System.out.println("PSE is NOT initialized"); 
//            	// Retrieving initial password
//            	String InitPass = JOptionPane.showInputDialog(null,"Enter initial keystore password:");
//            	ThePSEConfig.setKeyStorePassword(InitPass.toCharArray()); 
//            	// Initializing PSE configuration
//            	ThePSEConfig.initialize();
//            	
//            }
//            	
//            
//            
//            System.out.println("Waiting for a rendezvous connection");
//            boolean connected = edgeManager.waitForRendezvousConnection(20 * 1000);
//            System.out.println(MessageFormat.format("Edge Connected :{0}", connected));
//            
//            
            
            
            /*// Stop JXTA
            System.out.println("Stopping JXTA");
            manager.stopNetwork();
            System.out.println("JXTA stopped");*/
        } catch (Throwable e) {
            // Some type of error occurred. Print stack trace and quit.
            System.err.println("Fatal error -- Quitting");
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}
