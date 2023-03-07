/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plh24_ge3;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class GraphStats extends javax.swing.JFrame {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("plh24_ge3PU");
    EntityManager em = emf.createEntityManager();
    GameJpaController gjc = new GameJpaController(emf);

    /**
     * Creates new form GraphStats
     */
    public GraphStats() {
        JFrame frame = new JFrame("Προβολή στατιστικών στοιχείων κληρώσεων σε γραφική μορφή");

        //Listener ώστε να ξεκινήσω την διαδικασία όταν το παράθυρο θα έχει ανοίξει
        //και να κλείσω το μενού επιλογών σε περίπτωση που ο χρήστης κλείσει το παράθυρο
        this.addWindowListener(new WindowAdapter() {
            // Invoked when a window has been opened.
            @Override
            public void windowOpened(WindowEvent e) {
                //Listener ώστε να κλείσω το παράθυρο σε περίπτωση που ο χρήστης κλείσει το μενού επιλογών
                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        dispose();
                    }
                });
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel panel = new JPanel();
                panel.setLayout(null);

                //Δημιουργία CheckBoxes
                JCheckBox checkbox1 = new JCheckBox("Συχνότητας εμφάνισης αριθμών");
                Dimension size = checkbox1.getPreferredSize();
                checkbox1.setBounds(10, 10, size.width, size.height);
                panel.add(checkbox1);

                JCheckBox checkbox2 = new JCheckBox("Συχνότητα εμφάνισης αριθμών joker");
                Dimension size25 = checkbox2.getPreferredSize();
                checkbox2.setBounds(10, 30, size25.width, size25.height);
                panel.add(checkbox2);

                JCheckBox checkbox3 = new JCheckBox("Μέσος όρος κερδών ανά κατηγορία");
                Dimension size26 = checkbox3.getPreferredSize();
                checkbox3.setBounds(10, 50, size26.width, size26.height);
                panel.add(checkbox3);

                //Δημιουργία Label
                JLabel lblFrom = new JLabel();
                lblFrom.setText("Από:");
                Dimension size30 = lblFrom.getPreferredSize();
                lblFrom.setBounds(15, 100, size30.width, size30.height);
                panel.add(lblFrom);

                // Δημιουργία DatePicker from
                DatePicker datePickerFrom = new DatePicker();
                DatePickerSettings dateSettings = new DatePickerSettings();
                Dimension size1 = datePickerFrom.getPreferredSize();
                datePickerFrom.setBounds(45, 97, size1.width, size1.height);
                dateSettings.setFormatForDatesCommonEra("dd/MM/yyyy");
                datePickerFrom.setDateToToday();
                datePickerFrom.setSettings(dateSettings);
                datePickerFrom.setEnabled(false);
                //Προσθήκη στο panel του frame
                panel.add(datePickerFrom);

                //Δημιουργία Label
                JLabel lblTo = new JLabel();
                lblTo.setText("Έως:");
                Dimension size0 = lblTo.getPreferredSize();
                lblTo.setBounds(215, 100, size0.width, size0.height);
                panel.add(lblTo);

                // Δημιουργία DatePicker to
                DatePicker datePickerTo = new DatePicker();
                DatePickerSettings dateSettings2 = new DatePickerSettings();
                Dimension size2 = datePickerTo.getPreferredSize();
                datePickerTo.setBounds(247, 97, size2.width, size2.height);
                dateSettings2.setFormatForDatesCommonEra("dd/MM/yyyy");
                datePickerTo.setDateToToday();
                datePickerTo.setSettings(dateSettings2);
                datePickerTo.setEnabled(false);
                //Προσθήκη στο panel του frame
                panel.add(datePickerTo);

                JCheckBox checkbox4 = new JCheckBox("Εντός ημερομηνιών:");
                Dimension size27 = checkbox4.getPreferredSize();
                checkbox4.setBounds(10, 70, size27.width, size27.height);
                checkbox4.addItemListener((ItemEvent e1) -> {
                    if (e1.getStateChange() == 2) {
                        datePickerFrom.setEnabled(false);
                        datePickerTo.setEnabled(false);
                    } else {
                        datePickerFrom.setEnabled(true);
                        datePickerTo.setEnabled(true);
                    }
                });
                panel.add(checkbox4);

                // Δημιουργία Button
                JButton button = new JButton("Προβολή Στατιστικών");
                Dimension size3 = button.getPreferredSize();
                button.setBounds(130, 140, size3.width, size3.height);

                //ActionListener για το Button
                button.addActionListener((ActionEvent ev) -> {

                    //Συχνότητα εμφάνισης αριθμών
                    if (checkbox1.isSelected()) {
                        try {
                            numbersFrequency(datePickerFrom, datePickerTo, checkbox4.isSelected(), frame);
                            createGraph(tblNums, panelNums, "Συχνότητα εμφάνισης αριθμών", "Αριθμοί", "Εμφανίσεις");
                        } catch (ParseException ex) {
                            Logger.getLogger(GraphStats.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        jPanel2.setVisible(false);
                    }

                    //Συχνότητα εμφάνισης αριθμών joker
                    if (checkbox2.isSelected()) {
                        try {
                            jokerFrequency(datePickerFrom, datePickerTo, checkbox4.isSelected(), frame);
                            createGraph(tblJoker, panelJoker, "Συχνότητα εμφάνισης αριθμών Joker", "Αριθμοί Joker", "Εμφανίσεις");
                        } catch (ParseException ex) {
                            Logger.getLogger(GraphStats.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        jPanel1.setVisible(false);
                    }

                    //Μέσος όρος Κερδών ανα Κατηγορία
                    if (checkbox3.isSelected()) {
                        try {
                            avgPerCategory(datePickerFrom, datePickerTo, checkbox4.isSelected(), frame);
                            createGraph(tblCat, panelCat, "Μέσος όρος κερδών ανά κατηγορία", "Κατηγορία Κερδών", "Μέσος Όρος Κερδών");
                        } catch (ParseException ex) {
                            Logger.getLogger(GraphStats.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        jPanel3.setVisible(false);
                    }
                });
                //Προσθήκη στο panel του frame
                panel.add(button);

                frame.getContentPane().add(panel, BorderLayout.CENTER);
                frame.setSize(430, 230);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }

            //Κλείσιμο GraphStats
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });

        this.setTitle("Προβολή στατιστικών στοιχείων κληρώσεων σε γραφική μορφή");//Τίτλος της φόρμας
        initComponents();
    }

    private void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > 300) {
                width = 300;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    private void avgPerCategory(DatePicker datePickerFrom, DatePicker datePickerTo, boolean dates, JFrame frame) {
        try {
            //λίστα αντικειμένων games                        
            List<Game> games = new ArrayList<>();
            int c = 0; //Μετρητής για το πλήθος των κληρώσεων
            //Αθροίσματα για τα κέρδη κάθε κατηγορίας
            double sum5_1 = 0;
            double sum5 = 0;
            double sum4_1 = 0;
            double sum4 = 0;
            double sum3_1 = 0;
            double sum3 = 0;
            double sum2_1 = 0;
            double sum1_1 = 0;

            if (dates) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date dateFrom = format.parse(datePickerFrom.getDateStringOrEmptyString());
                java.util.Date dateTo = format.parse(datePickerTo.getDateStringOrEmptyString());

                //Βρίσκω τα drawIds των κληρώσεων, μεταξύ των ημερομηνιών που έδωσε ο χρήστης,
                //και τα αποθηκεύω στην λίστα αντικειμένων games
                em.getTransaction().begin();
                Query query = em.createNamedQuery("Game.findBetweenDrawdate");
                query.setParameter("drawdateFrom", dateFrom);
                query.setParameter("drawdateTo", dateTo);

                //λίστα αντικειμένων games                
                games = query.getResultList();
                if (games.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Δεν υπάρχουν διαθέσιμα δεδομένα", this.getTitle(), JOptionPane.WARNING_MESSAGE);
                    em.getTransaction().commit();
                    return;
                }

                Query q = em.createNativeQuery("SELECT * FROM PRIZECATEGORIES a WHERE a.DRAWID BETWEEN ?1 AND ?2");
                q.setParameter(1, games.get(0).getDrawid());
                q.setParameter(2, games.get(games.size() - 1).getDrawid());
                List<Object> result = (List<Object>) q.getResultList();
                em.getTransaction().commit();

                c = 0;
                Iterator itr = result.iterator();
                while (itr.hasNext()) {
                    Object[] obj = (Object[]) itr.next();

                    int prizeId = Integer.parseInt(String.valueOf(obj[2]));
                    double divident = (double) (obj[3]);
                    int winners = Integer.parseInt(String.valueOf(obj[4]));

                    switch (prizeId) {
                        case 1:
                            sum5_1 += divident * winners;
                            c += 1;
                            break;
                        case 2:
                            sum5 += divident * winners;
                            break;
                        case 3:
                            sum4_1 += divident * winners;
                            break;
                        case 4:
                            sum4 += divident * winners;
                            break;
                        case 5:
                            sum3_1 += divident * winners;
                            break;
                        case 6:
                            sum3 += divident * winners;
                            break;
                        case 7:
                            sum2_1 += divident * winners;
                            break;
                        case 8:
                            sum1_1 += divident * winners;
                            break;
                    }
                }
            } else {
                List<Prizecategories> prizeCategories = new ArrayList<>();
                //Select όλων των εγγραφών του πίνακα PrizeCategories
                em.getTransaction().begin();
                Query query = em.createNamedQuery("Prizecategories.findAll");

                prizeCategories = query.getResultList();
                em.getTransaction().commit();

                c = 0;
                //για κάθε κλήρωση βρίσκω το άθροισμα των κερδών για κάθε κατηγορία, καθώς και το πλήθος των κληρώσεων
                for (Prizecategories category : prizeCategories) {
                    switch (category.getPrizeid()) {
                        case 1:
                            sum5_1 += category.getDivedent() * category.getWinners();
                            c += 1;
                            break;
                        case 2:
                            sum5 += category.getDivedent() * category.getWinners();
                            break;
                        case 3:
                            sum4_1 += category.getDivedent() * category.getWinners();
                            break;
                        case 4:
                            sum4 += category.getDivedent() * category.getWinners();
                            break;
                        case 5:
                            sum3_1 += category.getDivedent() * category.getWinners();
                            break;
                        case 6:
                            sum3 += category.getDivedent() * category.getWinners();
                            break;
                        case 7:
                            sum2_1 += category.getDivedent() * category.getWinners();
                            break;
                        case 8:
                            sum1_1 += category.getDivedent() * category.getWinners();
                            break;
                    }
                }
            }

            //Πίνακας για τους μέσους όρους κερδών
            String[] row = {"Μέσος όρος κερδών:", String.format(Locale.getDefault(), "%,.2f", (sum5_1 / c)),
                String.format(Locale.getDefault(), "%,.2f", (sum5 / c)),
                String.format(Locale.getDefault(), "%,.2f", (sum4_1 / c)),
                String.format(Locale.getDefault(), "%,.2f", (sum4 / c)),
                String.format(Locale.getDefault(), "%,.2f", (sum3_1 / c)),
                String.format(Locale.getDefault(), "%,.2f", (sum3 / c)),
                String.format(Locale.getDefault(), "%,.2f", (sum2_1 / c)),
                String.format(Locale.getDefault(), "%,.2f", (sum1_1 / c))};

            //Πέρασμα στο Jtable
            DefaultTableModel dTable1 = (DefaultTableModel) tblCat.getModel();
            dTable1.addRow(row);

            resizeColumnWidth(tblCat);//Προσαρμογή του πλάτους των στηλών του Jtable στα περιεχόμενα των στηλών
            frame.dispose();

        } catch (HeadlessException | ParseException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        }
    }

    private void numbersFrequency(DatePicker datePickerFrom, DatePicker datePickerTo, boolean dates, JFrame frame) {
        int[][] numbers = new int[45][2];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i][0] = i + 1;
            numbers[i][1] = 0;
        }
        try {
            //λίστα αντικειμένων games
            List<Game> games = new ArrayList<>();
            if (dates) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date dateFrom = format.parse(datePickerFrom.getDateStringOrEmptyString());
                java.util.Date dateTo = format.parse(datePickerTo.getDateStringOrEmptyString());

                //Βρίσκω τα drawIds των κληρώσεων, μεταξύ των ημερομηνιών που έδωσε ο χρήστης,
                //και τα αποθηκεύω στην λίστα αντικειμένων games
                em.getTransaction().begin();
                Query query = em.createNamedQuery("Game.findBetweenDrawdate");
                query.setParameter("drawdateFrom", dateFrom);
                query.setParameter("drawdateTo", dateTo);

                //λίστα αντικειμένων games                
                games = query.getResultList();
                em.getTransaction().commit();
            } else {
                //Βρίσκω τα drawIds των κληρώσεων και τα αποθηκεύω στην λίστα αντικειμένων games
                em.getTransaction().begin();
                Query query = em.createNamedQuery("Game.findAll");

                games = query.getResultList();
                em.getTransaction().commit();
            }
            if (games.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Δεν υπάρχουν διαθέσιμα δεδομένα", this.getTitle(), JOptionPane.WARNING_MESSAGE);
                return;
            }

            //Για κάθε κλήρωση που βρέθηκε αποθηκεύω τον αριθμό Joker στον πίνακα συχνοτήτων jokers
            for (Game game : games) {
                numbers[game.getNum1() - 1][0] = game.getNum1();
                numbers[game.getNum1() - 1][1] += 1;

                numbers[game.getNum2() - 1][0] = game.getNum2();
                numbers[game.getNum2() - 1][1] += 1;

                numbers[game.getNum3() - 1][0] = game.getNum3();
                numbers[game.getNum3() - 1][1] += 1;

                numbers[game.getNum4() - 1][0] = game.getNum4();
                numbers[game.getNum4() - 1][1] += 1;

                numbers[game.getNum5() - 1][0] = game.getNum5();
                numbers[game.getNum5() - 1][1] += 1;
            }

            //Ταξινόμηση πίνακα jokers με βάση την συχότητα εμφάνισης
            Arrays.sort(numbers, (int[] o1, int[] o2) -> Integer.compare(o2[1], o1[1]));

            //Εμφάνιση αποτελεσμάτων στο Jtable1
            DefaultTableModel dTable1 = (DefaultTableModel) tblNums.getModel();
            String[] row = new String[6];
            row[0] = "Εμφανίσεις:";
            for (int i = 0; i < 5; i++) {
                JTableHeader th = tblNums.getTableHeader();
                TableColumnModel tcm = th.getColumnModel();
                TableColumn tc = tcm.getColumn(i + 1);
                tc.setHeaderValue(numbers[i][0]);
                th.repaint();

                row[i + 1] = String.valueOf(numbers[i][1]);
            }
            dTable1.addRow(row);
            resizeColumnWidth(tblNums);
            frame.dispose();

        } catch (HeadlessException | ParseException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        }
    }

    private void jokerFrequency(DatePicker datePickerFrom, DatePicker datePickerTo, boolean dates, JFrame frame) {
        int[][] jokers = new int[20][2];
        for (int i = 0; i < jokers.length; i++) {
            jokers[i][0] = i + 1;
            jokers[i][1] = 0;
        }
        try {
            //λίστα αντικειμένων games
            List<Game> games = new ArrayList<>();
            if (dates) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date dateFrom = format.parse(datePickerFrom.getDateStringOrEmptyString());
                java.util.Date dateTo = format.parse(datePickerTo.getDateStringOrEmptyString());

                //Βρίσκω τα drawIds των κληρώσεων, μεταξύ των ημερομηνιών που έδωσε ο χρήστης,
                //και τα αποθηκεύω στην λίστα αντικειμένων games
                em.getTransaction().begin();
                Query query = em.createNamedQuery("Game.findBetweenDrawdate");
                query.setParameter("drawdateFrom", dateFrom);
                query.setParameter("drawdateTo", dateTo);

                //λίστα αντικειμένων games                
                games = query.getResultList();
                em.getTransaction().commit();
            } else {
                //Βρίσκω τα drawIds των κληρώσεων και τα αποθηκεύω στην λίστα αντικειμένων games
                em.getTransaction().begin();
                Query query = em.createNamedQuery("Game.findAll");

                games = query.getResultList();
                em.getTransaction().commit();
            }

            if (games.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Δεν υπάρχουν διαθέσιμα δεδομένα", this.getTitle(), JOptionPane.WARNING_MESSAGE);
                return;
            }

            //Για κάθε κλήρωση που βρέθηκε αποθηκεύω τον αριθμό Joker στον πίνακα συχνοτήτων jokers
            for (Game game : games) {
                jokers[game.getBonus() - 1][0] = game.getBonus();
                jokers[game.getBonus() - 1][1] += 1;
            }

            //Ταξινόμηση πίνακα jokers με βάση την συχότητα εμφάνισης
            Arrays.sort(jokers, (int[] o1, int[] o2) -> Integer.compare(o2[1], o1[1]));

            //Εμφάνιση αποτελεσμάτων στο Jtable1
            DefaultTableModel dTable1 = (DefaultTableModel) tblJoker.getModel();
            String[] row = new String[6];
            row[0] = "Εμφανίσεις:";
            for (int i = 0; i < 5; i++) {
                JTableHeader th = tblJoker.getTableHeader();
                TableColumnModel tcm = th.getColumnModel();
                TableColumn tc = tcm.getColumn(i + 1);
                tc.setHeaderValue(jokers[i][0]);
                th.repaint();

                row[i + 1] = String.valueOf(jokers[i][1]);
            }
            dTable1.addRow(row);
            resizeColumnWidth(tblJoker);
            frame.dispose();
        } catch (HeadlessException | ParseException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        }
    }

    //Μέθοδος για την δημιουργία γραφήματος
    private void createGraph(JTable table, JPanel panel, String title, String xLabel, String yLabel) throws ParseException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        int nCol = dtm.getColumnCount();
        for (int i = 1; i < nCol; i++) {
            JTableHeader th = table.getTableHeader();
            TableColumnModel tcm = th.getColumnModel();
            TableColumn tc = tcm.getColumn(i);

            if ("Μέσος όρος κερδών ανά κατηγορία".equals(title)) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator('.');
                DecimalFormat format = new DecimalFormat("0.#", DecimalFormatSymbols.getInstance(Locale.getDefault()));
                format.setDecimalFormatSymbols(symbols);
                float f = format.parse(dtm.getValueAt(0, i).toString()).floatValue();

                dataset.setValue(f, xLabel, tc.getHeaderValue().toString());
            } else {
                dataset.setValue(Integer.parseInt(dtm.getValueAt(0, i).toString()), xLabel, tc.getHeaderValue().toString());
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(title, xLabel, yLabel, dataset, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.BLACK);

        ChartPanel pa = new ChartPanel(chart);
        panel.removeAll();
        panel.add(pa, BorderLayout.CENTER);
        panel.validate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblJoker = new javax.swing.JTable();
        panelJoker = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblNums = new javax.swing.JTable();
        panelNums = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblCat = new javax.swing.JTable();
        panelCat = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblJoker.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Αριθμοί Joker:", "Num1", "Num2", "Num3", "Num4", "Num5"
            }
        ));
        jScrollPane1.setViewportView(tblJoker);
        if (tblJoker.getColumnModel().getColumnCount() > 0) {
            tblJoker.getColumnModel().getColumn(0).setPreferredWidth(150);
        }

        panelJoker.setBackground(new java.awt.Color(153, 153, 153));
        panelJoker.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(panelJoker, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelJoker, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setPreferredSize(new java.awt.Dimension(604, 338));

        tblNums.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Αριθμοί:", "Num1", "Num2", "Num3", "Num4", "Num5"
            }
        ));
        jScrollPane2.setViewportView(tblNums);
        if (tblNums.getColumnModel().getColumnCount() > 0) {
            tblNums.getColumnModel().getColumn(0).setPreferredWidth(150);
        }

        panelNums.setBackground(new java.awt.Color(153, 153, 153));
        panelNums.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelNums, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelNums, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setPreferredSize(new java.awt.Dimension(604, 338));

        tblCat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Κατηγορίες Επιτυχιών:", "5+1", "5", "4+1", "4", "3+1", "3", "2+1", "1+1"
            }
        ));
        jScrollPane3.setViewportView(tblCat);
        if (tblCat.getColumnModel().getColumnCount() > 0) {
            tblCat.getColumnModel().getColumn(0).setPreferredWidth(410);
        }

        panelCat.setBackground(new java.awt.Color(153, 153, 153));
        panelCat.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelCat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCat, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1243, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(GraphStats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GraphStats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GraphStats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GraphStats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GraphStats().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel panelCat;
    private javax.swing.JPanel panelJoker;
    private javax.swing.JPanel panelNums;
    private javax.swing.JTable tblCat;
    private javax.swing.JTable tblJoker;
    private javax.swing.JTable tblNums;
    // End of variables declaration//GEN-END:variables
}
