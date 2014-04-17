/*
 * Copyright (c) 2001-2007 Sun Microsystems, Inc.  All rights reserved.
 *
 *  The Sun Project JXTA(TM) Software License
 *
 *  Redistribution and use in source and binary forms, with or without 
 *  modification, are permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice, 
 *     this list of conditions and the following disclaimer in the documentation 
 *     and/or other materials provided with the distribution.
 *
 *  3. The end-user documentation included with the redistribution, if any, must 
 *     include the following acknowledgment: "This product includes software 
 *     developed by Sun Microsystems, Inc. for JXTA(TM) technology." 
 *     Alternately, this acknowledgment may appear in the software itself, if 
 *     and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Sun", "Sun Microsystems, Inc.", "JXTA" and "Project JXTA" must 
 *     not be used to endorse or promote products derived from this software 
 *     without prior written permission. For written permission, please contact 
 *     Project JXTA at http://www.jxta.org.
 *
 *  5. Products derived from this software may not be called "JXTA", nor may 
 *     "JXTA" appear in their name, without prior written permission of Sun.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 *  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL SUN 
 *  MICROSYSTEMS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 *  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 *  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  JXTA is a registered trademark of Sun Microsystems, Inc. in the United 
 *  States and other countries.
 *
 *  Please see the license information page at :
 *  <http://www.jxta.org/project/www/license.html> for instructions on use of 
 *  the license in source files.
 *
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many individuals 
 *  on behalf of Project JXTA. For more information on Project JXTA, please see 
 *  http://www.jxta.org.
 *
 *  This license is based on the BSD license adopted by the Apache Foundation. 
 */

package net.jxta.impl.membership.pse;



/*
	 d0d0 commented this out as AWT is not part of android
	 This class presents the users a GUI where the user enter the "Key Store Password", "Identity" & "Identity Password"
	 Since AWT is not available in android, a new activity com.android.d0d0.jxta.impl.membership.pse.DialogAuthenticatorActivity
	 has been created. This activity presents the user with a GUI where he is asked to enter these details for Android.
	 The three parameters ("Key Store Password", "Identity" & "Identity Password") will be fed in this class from the activity. 
 */


import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;

import javax.crypto.EncryptedPrivateKeyInfo;

import net.jxta.credential.AuthenticationCredential;
import net.jxta.id.ID;
import net.jxta.membership.InteractiveAuthenticator;
import net.jxta.peer.PeerID;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.android.d0d0.ApplicationClass;

//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.GridLayout;
//import java.awt.Insets;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JDialog;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JPasswordField;

/**
 * An interactive graphical authenticator associated with the PSE membership
 * service.
 *
 * @see net.jxta.membership.Authenticator
 * @see net.jxta.membership.InteractiveAuthenticator
 * @see net.jxta.membership.MembershipService
 * @see net.jxta.impl.membership.pse.PSEMembershipService
 **/
public final class DialogAuthenticator extends StringAuthenticator implements InteractiveAuthenticator {

	/**
	 *  Entries we stick into the combo list
	 **/
	private static class JComboEntry {
		ID itsID;

		X509Certificate itsCertificate;

		String itsName;

		JComboEntry(ID entryID, X509Certificate itsCert) {
			itsID = entryID;
			itsCertificate = itsCert;
			itsName = PSEUtils.getCertSubjectCName(itsCertificate);

			if (null == itsName) {
				itsName = "< no common name >";
			}

			// remove the -CA which is common to ca root certs.
			if (itsName.endsWith("-CA")) {
				itsName = itsName.substring(0, itsName.length() - 3);
			}
		}

		/**
		 *  {@inheritDoc}
		 **/
		@Override
		public String toString() {
			return itsName;
		}
	}

	/**
	 *  Swing user interface for password entry and identity selection.
	 *
	 *  <p/>FIXME bondolo 20040329 should be localizable.
	 **/
	// 	private class PasswordDialog extends JDialog implements ActionListener {
	// d0d0 This is not 'nice' but I am running out of time :-( 
	// But I will work on getting the results from the activity later. 
	private class PasswordDialog  {

		private boolean initKeyStore;
		private final PeerID seedPeer;
		private final X509Certificate seedCert;
		private final EncryptedPrivateKeyInfo seedKey;

		// d0d0 the below fields have been declared to resemble the original fields used but without their AWT related declarations
		 String storePassField;
		 ID identityList;
		 String identityPassField;
		 boolean okButton;
		 boolean cancelButton;
		 
		
		
		
		public void ResultFromActivity(String KeyStorePass, String IdentityPass ){
			
			this.storePassField = KeyStorePass;
			this.identityPassField = IdentityPass;
		}
		
		//this.Activity = com.android.d0d0.jxta.impl.membership.pse.DialogAuthenticatorActivity.class;
		//password_window = com.android.d0d0.jxta.impl.membership.pse.DialogAuthenticatorActivity.class;

		//        private final JLabel storePassLabel;
		//        private final JPasswordField storePassField;
		//        private final JLabel identityLabel;
		//        private final JComboBox identityList;
		//        private final JLabel identityPassLabel;
		//        private final JPasswordField identityPassField;
		//        private final JButton okButton;
		//        private final JButton cancelButton;

		private boolean canceled = true;

		/**
		 *  Dialog to prompt for a password
		 **/
		PasswordDialog(PeerID seedPeer, X509Certificate seedCert, EncryptedPrivateKeyInfo seedKey) {

			//  the below commented section initializes the window Initialize JXTA Keystore       	
			//        	super(JOptionPane.getRootFrame(), ((null != seedCert) ? "Initialize JXTA Keystore" : "JXTA Secure Login")
			//                    , /* modal*/
			//                    true);
			//
			//            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			//            addWindowListener(new WindowAdapter() {

			//                /**
			//                 * @inheritDoc
			//                 */
			//
			//                @Override
			//                public void windowClosing(WindowEvent e) {
			//                    canceled = true;
			//                }
			//            }
			
			
			Uri UriClassName = Uri.parse("com.android.d0d0.jxta.impl.membership.pse.DialogAuthenticatorActivity.class");
			Intent ViewIntent = new Intent( Intent.ACTION_VIEW, UriClassName);
			Context context = ApplicationClass.getApplicationClassContext();
			context.startActivity(ViewIntent);
			
			
			
			//storePassField = com.android.d0d0.jxta.impl.membership.pse.DialogAuthenticatorActivity.this.KeyStorePassword;
			

			//EditText KeyStorePassword = (EditText) findViewById(R.id.editText1);

			this.seedPeer = seedPeer;
			this.seedCert = seedCert;
			this.seedKey = seedKey;

			initKeyStore = (null != seedCert);

			//            JPanel contentPane = new JPanel(new GridBagLayout());
			//            GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START
			//                    ,
			//                    GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0);
			//
			//            storePassField = new JPasswordField("", 10);
			//
			//            if (!initKeyStore) {
			//                // add listener to populate identities list
			//                storePassField.addKeyListener(new PasswordDialogKeyHandler());
			//            }
			//
			//            if (!initKeyStore) {
			//                identityList = new JComboBox();
			//            } else {
			//                JComboEntry seedEntry = new JComboEntry(seedPeer, seedCert);
			//
			//                Object[] names = { seedEntry };
			//
			//                identityList = new JComboBox(names);
			//                identityList.setMaximumRowCount(1);
			//            }
			//
			//            identityPassField = new JPasswordField("", 10);
			//
			//            identityPassField.addKeyListener(new PasswordDialogKeyHandler());
			//
			//            storePassLabel = new JLabel("Key Store Password");
			//            storePassLabel.setLabelFor(storePassField);
			//            contentPane.add(storePassLabel, c);
			//            c.gridx = 1;
			//            contentPane.add(storePassField, c);
			//
			//            c.gridx = 0;
			//            c.gridy = 1;
			//            c.anchor = GridBagConstraints.LINE_START;
			//            identityLabel = new JLabel("Identity");
			//            identityLabel.setLabelFor(identityList);
			//            contentPane.add(identityLabel, c);
			//            c.gridx = 1;
			//            c.fill = GridBagConstraints.BOTH;
			//            contentPane.add(identityList, c);
			//
			//            c.gridx = 0;
			//            c.gridy = 2;
			//            c.fill = GridBagConstraints.BOTH;
			//            identityPassLabel = new JLabel("Identity Password");
			//            identityPassLabel.setLabelFor(identityPassField);
			//            contentPane.add(identityPassLabel, c);
			//            c.gridx = 1;
			//            contentPane.add(identityPassField, c);
			//
			//            JPanel buttonPanel = new JPanel(new GridLayout(/* rows*/1, /* cols*/0));
			//
			//            okButton = new JButton("OK");
			//            okButton.addActionListener(this);
			//            buttonPanel.add(okButton);
			//
			//            cancelButton = new JButton("Cancel");
			//            cancelButton.addActionListener(this);
			//            buttonPanel.add(cancelButton);
			//
			//            c.gridx = 0;
			//            c.gridy = 3;
			//            c.gridwidth = 2;
			//            c.anchor = GridBagConstraints.LAST_LINE_END;
			//            c.fill = GridBagConstraints.VERTICAL;
			//            cancelButton.addActionListener(this);
			//            contentPane.add(buttonPanel, c);
			//
			//            setContentPane(contentPane);
			//
			//            if (initKeyStore) {
			//                identityPassField.requestFocusInWindow();
			//            } else {
			//                storePassField.requestFocusInWindow();
			//            }
		}

		/**
		 * Handler for key events.
		 **/
		//        private class PasswordDialogKeyHandler extends KeyAdapter {
		//
		//            /**
		//             *  {@inheritDoc}
		//             **/
		//            @Override
		//            public void keyReleased(KeyEvent e) {
		//                setOKState();
		//            }
		//        }

		/**
		 **/
		private void setOKState() {
			boolean enableOK = false;

			if (initKeyStore) {
				enableOK = (null
						!= PSEUtils.pkcs5_Decrypt_pbePrivateKey(identityPassField.toCharArray()
								,
								seedCert.getPublicKey().getAlgorithm(), seedKey));

				//                enableOK = (null
						//                		!= PSEUtils.pkcs5_Decrypt_pbePrivateKey(identityPassField.getPassword()
				//                		,
				//                		seedCert.getPublicKey().getAlgorithm(), seedKey));

				// d0d0 If given the password, key algorithm and the encrypted key, if we are able to decrypt the key, then enableOK is set to 'true'
				// Once enableOK is set to true, then password field and password label are enabled. 
				//                   

				//               storePassLabel.setEnabled(enableOK);
				//               storePassField.setEnabled(enableOK);
				
				

			} else {
				boolean enableIdentityList = false;

				ID[] roots = getIdentities(storePassField.toCharArray());

				if (null != roots) {
					Iterator eachRoot = Arrays.asList(roots).iterator();

					while (eachRoot.hasNext()) {
						ID aPeer = (ID) eachRoot.next();

						try {
							X509Certificate aCert = DialogAuthenticator.this.source.getPSEConfig().getTrustedCertificate(aPeer);
							JComboEntry anEntry = new JComboEntry(aPeer, aCert);

							if (!enableIdentityList) {
								enableIdentityList = true;
								//                                identityList.removeAllItems();
								//                                identityList.setSelectedIndex(-1);
							}

							identityList = anEntry.itsID;

						} catch (Exception ignore) {
						}
					}
				}

				if (enableIdentityList) {
					identityList.toString();

				} else {
					//                    identityList.removeAllItems();
					//                    identityList.setSelectedIndex(-1);
					identityList = null;
				}

				//                identityLabel.setEnabled(enableIdentityList);
				//                identityList.setEnabled(enableIdentityList);
				//                identityPassLabel.setEnabled(enableIdentityList);
				//                identityPassField.setEnabled(enableIdentityList);
			}

			if ((null != getIdentity()) && (null != getKeyStorePassword()) && (null != getIdentityPassword())) {
				setAuth1_KeyStorePassword(getKeyStorePassword());
				setAuth2Identity(getIdentity());
				setAuth3_IdentityPassword(getIdentityPassword());
				enableOK = isReadyForJoin();
			}
			//  for now I will leave the OK button always enabled.
			//            okButton.setEnabled(enableOK);
		}

		/**
		 *  {@inheritDoc}
		 **/
		//        public void actionPerformed(ActionEvent e) {
		//
		//            if (okButton == e.getSource()) {
		//                canceled = false;
		//                dispose();
		//            } else if (cancelButton == e.getSource()) {
		//                canceled = true;
		//                dispose();
		//            } else {}
		//        }

		//        public void showDialog() {
		//            pack();
		//            setLocationRelativeTo(null);
		//
		//            setOKState();
		//
		//            setVisible(true);
		//        }

		/**
		 *  Returns the KeyStore password.
		 *
		 *  @return the KeyStore password.
		 **/
		public char[] getKeyStorePassword() {
			//            if (!storePassField.isEnabled()) {
			//                return null;
			//            }

			char[] result = storePassField.toCharArray();

			return result;
		}

		/**
		 *  Returns the selected Identity.
		 *
		 *  @return the selected Identity.
		 **/
		public ID getIdentity() {
			//            if (!identityList.isEnabled()) {
			//                return null;
			//            }

			
			
			

			//            if (null == selectedIdentity) {
			//                return null;
			//            }
			
			return identityList;
		}

		/**
		 *  Returns the Identity password.
		 *
		 *  @return the Identity password.
		 **/
		public char[] getIdentityPassword() {

			//        	if (!identityPassField.isEnabled()) {
			//                return null;
			//            }

			char[] result = identityPassField.toCharArray();

			return result;
		}

		/**
		 *  Returns the final state of the dialog. Until the "OK" button is
		 *  pressed the dialog is "cancelled".
		 *
		 *  @param returns the final state of the dialog.
		 **/
		public boolean wasCanceled() {
			return canceled;
		}
	}

	/**
	 * Creates an authenticator for the PSE membership service. Anything entered
	 * into the identity info section of the Authentication credential is
	 * ignored.
	 *
	 *  @param source The instance of the PSE membership service which
	 *  created this authenticator.
	 *  @param application Anything entered into the identity info section of
	 *  the Authentication credential is ignored.
	 **/
	DialogAuthenticator(PSEMembershipService source, AuthenticationCredential application) {
		super(source, application);

		// XXX 20010328 bondolo@jxta.org Could do something with the authentication credential here.
	}

	/**
	 * Creates an authenticator for the PSE membership service. Anything entered
	 * into the identity info section of the Authentication credential is
	 * ignored.
	 *
	 *  @param source The instance of the PSE membership service which
	 *  created this authenticator.
	 *  @param application Anything entered into the identity info section of
	 *  the Authentication credential is ignored.
	 **/
	DialogAuthenticator(PSEMembershipService source, AuthenticationCredential application, X509Certificate seedCert, EncryptedPrivateKeyInfo seedKey) {
		super(source, application, seedCert, seedKey);

		// XXX 20010328 bondolo@jxta.org Could do something with the authentication credential here.
	}

	/**
	 * {@inheritDoc}
	 **/
	@Override
	public String getMethodName() {
		return "DialogAuthentication";
	}

	/**
	 * {@inheritDoc}
	 **/
	@Override
	public boolean interact() {
		PasswordDialog p = new PasswordDialog(source.getPeerGroup().getPeerID(), seedCert, seedKey);


		if (p.cancelButton) {
			setAuth1_KeyStorePassword((char[]) null);
			setAuth2Identity((ID) null);
			setAuth3_IdentityPassword((char[]) null);

		} else if (p.okButton) {
			setAuth1_KeyStorePassword(p.getKeyStorePassword());
			setAuth2Identity(p.getIdentity());
			setAuth3_IdentityPassword(p.getIdentityPassword());
		}

		return !p.wasCanceled();
	}
}
