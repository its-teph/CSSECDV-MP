
package View;

import Model.User;
import Model.Logs;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Controller.Main;
import Controller.SQLite;
import java.sql.Timestamp;
import java.util.Date;

public class Login extends javax.swing.JPanel {

    public Frame frame;
    SQLite sqlite = Main.getInstance().getSqlite();
    
    public Login(){
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        usernameFld = new javax.swing.JTextField();
        passwordFld = new javax.swing.JTextField();
        registerBtn = new javax.swing.JButton();
        loginBtn = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SECURITY Svcs");
        jLabel1.setToolTipText("");

        usernameFld.setBackground(new java.awt.Color(240, 240, 240));
        usernameFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        usernameFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usernameFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "USERNAME", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        passwordFld.setBackground(new java.awt.Color(240, 240, 240));
        passwordFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        passwordFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "PASSWORD", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        registerBtn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        registerBtn.setText("REGISTER");
        registerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBtnActionPerformed(evt);
            }
        });

        loginBtn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        loginBtn.setText("LOGIN");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(200, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(registerBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(usernameFld)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordFld, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(usernameFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(126, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        String username = usernameFld.getText();
        String password = passwordFld.getText();
        
        boolean user = findUser(username); //check if username exists
        User u = getUser(username); // curr user
        
        if (user) {
            // count failed log in attemps for user, assign to failedAttempts
            ArrayList<Logs> logs = sqlite.getLogs();
            
            int failedAttempts = countFailedAttempts(username,logs);
            
            if (u.getLocked() == 1) { // user is locked
                JOptionPane.showMessageDialog(this, "Your account is locked. Please contact support.", "Account Locked", JOptionPane.ERROR_MESSAGE);
                sqlite.addLogs("ACCOUNT LOCKED", username, username + " account locked due to 3 or more wrong password attempts.", new Timestamp(new Date().getTime()).toString());   
            } else if (failedAttempts >= 3){ // not locked but 3 failed attempts
                JOptionPane.showMessageDialog(this, "Your account is locked. Please contact support.", "Account Locked", JOptionPane.ERROR_MESSAGE);
                 sqlite.addLogs("ACCOUNT LOCKED", username, username + " account locked due to 3 or more wrong password attempts.", new Timestamp(new Date().getTime()).toString());
                u.setLocked(1); // set lock to 1 or true
            }
            
            boolean pw = checkPassword(username, password); // Check if password matches username
        
            if (!pw) { // Wrong password
                // Log the failed login attempt
                sqlite.addLogs("FAILED LOGIN", username, username + " wrong password.", new Timestamp(new Date().getTime()).toString());
            
                // check failed attempts, if >= 3, lock user (if block)
                
                // (else block) display            JOptionPane.showMessageDialog(this, "Username or password incorrect. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                
            } else { // right pw
                sqlite.addLogs("LOGIN", username, username + " logged in successfully.", new Timestamp(new Date().getTime()).toString());
                frame.mainNav(); // Login successful
            }
        } else { // (!user)
            JOptionPane.showMessageDialog(this, "Username or password incorrect. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }    
    }//GEN-LAST:event_loginBtnActionPerformed

    private boolean findUser(String username) {
        ArrayList<Model.User> users = sqlite.getUsers(); // get list of users
        for (Model.User user : users) {
        if (user.getUsername().equals(username)) {
            return true; //username found
        }
    }
    return false; // username does not exist
    }
    
    private User getUser(String username) {
        ArrayList<Model.User> users = sqlite.getUsers(); // get list of users
        for (Model.User user : users) {
            if (user.getUsername().equals(username)) {
            return user; //username found
            }
        }
        return null; // username does not exist
    }
    
    private boolean checkPassword(String username, String password) {
        ArrayList<Model.User> users = sqlite.getUsers();
        for (Model.User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true; // username and password match
            }
        }
        return false; // wrong pw
    }
    
    public int countFailedAttempts(String username, ArrayList<Logs> logs) {
        int failedAttempts = 0;
        for (Logs log : logs) {
            if (log.getUsername().equals(username) && log.getEvent().equals("FAILED LOGIN")) {
            failedAttempts++;
            }
        }
        return failedAttempts;
    }

    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerBtnActionPerformed
        frame.registerNav();
    }//GEN-LAST:event_registerBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginBtn;
    private javax.swing.JTextField passwordFld;
    private javax.swing.JButton registerBtn;
    private javax.swing.JTextField usernameFld;
    // End of variables declaration//GEN-END:variables
}
