/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plh24_ge3;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class StatistictsPerMonth extends javax.swing.JFrame {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("plh24_ge3PU");
    EntityManager em = emf.createEntityManager();
    GameJpaController gjc = new GameJpaController(emf);
    private String year; //Το έτος που έχει επιλεχθεί στο προηγούμενο παράθυρο
    private int month;//Ο μήνας που έχει επιλεχθεί στο προηγούμενο παράθυρο

    /**
     * Creates new form StatistictsPerMonth
     */
    public StatistictsPerMonth() {
        this.setTitle("Προβολή δεδομένων ανά μήνα");//Τίτλος της φόρμας
        initComponents();

        //Listener ώστε να ξεκινήσω την διαδικασία όταν το παράθυρο θα έχει ανοίξει
        this.addWindowListener(new WindowAdapter() {
            // Invoked when a window has been opened.
            @Override
            public void windowOpened(WindowEvent e) {
                try {
                    //Δημιουργώ την ημερομηνία από την οποία θα αρχίσει η ανάζητηση του sql query
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date parsed = format.parse(year + "-" + month + "-" + "01");
                    java.sql.Date dateFrom = new java.sql.Date(parsed.getTime());

                    //Δημιουργώ την ημερομηνία στην οποία θα τελειώνει η ανάζητηση του sql query
                    parsed = format.parse(year + "-" + month + "-" + "31");
                    java.sql.Date dateTo = new java.sql.Date(parsed.getTime());

                    //Επιλέγω όλες τις εγγραφές από τον πίνακα GAME που ανήκουν χρονικά στο έτος και στον μήνα που έχει επιλέξει ο χρήστης
                    em.getTransaction().begin();
                    Query query = em.createNamedQuery("Game.findBetweenDrawdate");
                    query.setParameter("drawdateFrom", dateFrom);
                    query.setParameter("drawdateTo", dateTo);

                    //λίστα αντικειμένων games του έτους και του μήνα που ζητήθηκε από τον χρήστη
                    List<Game> games = new ArrayList<>();
                    games = query.getResultList();
                    em.getTransaction().commit();

                    jLabel4.setText(String.valueOf(games.size()));
                    int jackpots = 0;
                    double dist = 0;
                    for (Game game : games) {
                        Game gameDrawId = gjc.findGame(game.getDrawid());
                        em.getTransaction().begin();
                        Query query2 = em.createNamedQuery("Prizecategories.findByDrawId");
                        query2.setParameter("drawid", gameDrawId);

                        //-----------------------λίστα αντικειμένων games του έτους και του μήνα που ζητήθηκε από τον χρήστη
                        List<Prizecategories> prizeCategories = new ArrayList<>();
                        prizeCategories = query2.getResultList();
                        em.getTransaction().commit();

                        for (Prizecategories prizeCategorie : prizeCategories) {
                            if (prizeCategorie.getPrizeid() == 1 && prizeCategorie.getWinners() == 0) {
                                jackpots++;
                            }

                            if (prizeCategorie.getPrizeid() == 1 && prizeCategorie.getWinners() > 0) {
                                dist += prizeCategorie.getDist() + prizeCategorie.getJackpot();
                            } 
                            
                            if (prizeCategorie.getPrizeid() == 2 && prizeCategorie.getWinners() > 0) {
                                dist += prizeCategorie.getDist();
                            }
                            
                            if (prizeCategorie.getPrizeid() != 1 && prizeCategorie.getPrizeid() != 2) {
                                dist += prizeCategorie.getDist();
                            }                           
                        }
                    }

                    jLabel5.setText(String.format(String.format(Locale.getDefault(), "%,.2f",dist))+" €");
                    jLabel6.setText(String.valueOf(jackpots));
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });

    }

    //Setter για το έτος, ο οποίος καλείται στην προηγούμενη οθόνη
    public void setYear(String year) {
        this.year = year;
    }

    //Setter για τον μήνα, ο οποίος καλείται στην προηγούμενη οθόνη
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel1.setText("Πλήθος παιχνιδιών:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel2.setText("Διανεμηθέντα χρήματα:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel3.setText("Πλήθος ΤΖΑΚ-ΠΟΤ:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel4.setText("jLabel4");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel5.setText("jLabel5");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel6.setText("jLabel6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(229, 229, 229)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)))
                .addContainerGap(223, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addContainerGap(139, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StatistictsPerMonth.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StatistictsPerMonth.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StatistictsPerMonth.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StatistictsPerMonth.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StatistictsPerMonth().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    // End of variables declaration//GEN-END:variables
}
