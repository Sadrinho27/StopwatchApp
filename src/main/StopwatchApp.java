package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StopwatchApp extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable cutTable;
	private DefaultTableModel model;
	private long startTime;
	private long elapsedBeforePause = 0;
	private Timer timer;
	private boolean enCours = false;

	private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss,SS");

	@SuppressWarnings("serial")
	public StopwatchApp() {
		setTitle("Stopwatch for LeFreshii - By S27");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 370, 330);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(44, 47, 51));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Model
		model = new DefaultTableModel(new Object[] { "Time", "Note", "Checker" }, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 2)
					return Boolean.class;
				return String.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1 || column == 2;
			}
		};

		// Label counter
		JLabel lblCounter = new JLabel("00:00:00,00");
		lblCounter.setHorizontalAlignment(SwingConstants.CENTER);
		lblCounter.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblCounter.setForeground(new Color(234, 234, 234));
		lblCounter.setBackground(new Color(35, 39, 42));
		lblCounter.setOpaque(true);
		lblCounter.setBounds(23, 20, 287, 41);
		contentPane.add(lblCounter);

		// Start/Stop button
		RoundedButton btnStartStop = new RoundedButton("Start/Stop", new Color(76, 175, 80));
		btnStartStop.setBounds(16, 72, 100, 30);
		contentPane.add(btnStartStop);

		// Cut button
		RoundedButton btnCut = new RoundedButton("Cut", new Color(33, 150, 243));
		btnCut.setBounds(126, 72, 100, 30);
		contentPane.add(btnCut);

		// Reset button
		RoundedButton btnReset = new RoundedButton("Reset", new Color(244, 67, 54));
		btnReset.setBounds(236, 72, 100, 30);
		contentPane.add(btnReset);

		// Table
		cutTable = new JTable(model);
		cutTable.setForeground(new Color(234, 234, 234));
		cutTable.setBackground(new Color(35, 39, 42));
		cutTable.setGridColor(new Color(80, 80, 80));
		cutTable.setSelectionBackground(new Color(70, 70, 70));
		cutTable.setRowHeight(25);
		cutTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cutTable.setFillsViewportHeight(true);
		cutTable.setShowGrid(false);
		cutTable.setIntercellSpacing(new Dimension(0, 0));
		cutTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		cutTable.getTableHeader().setBackground(new Color(60, 63, 65));
		cutTable.getTableHeader().setForeground(new Color(220, 220, 220));
		cutTable.getTableHeader().setReorderingAllowed(false);
		cutTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(80, 80, 80)));

		// ScrollPane
		JScrollPane scrollPane = new JScrollPane(cutTable);
		scrollPane.setBounds(16, 113, 320, 165);
		scrollPane.getViewport().setBackground(new Color(35, 39, 42));
		scrollPane.setBackground(new Color(44, 47, 51));
		scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = new Color(80, 80, 80);
				this.trackColor = new Color(44, 47, 51);
			}

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return createZeroButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return createZeroButton();
			}

			private JButton createZeroButton() {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(0, 0));
				button.setMinimumSize(new Dimension(0, 0));
				button.setMaximumSize(new Dimension(0, 0));
				button.setBorder(null);
				button.setFocusable(false);
				button.setBackground(new Color(44, 47, 51));
				return button;
			}
		});
		contentPane.add(scrollPane);

		// Renderer modernisé
		cutTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
				c.setForeground(new Color(234, 234, 234));

				Boolean isChecked = (Boolean) model.getValueAt(row, 2);
				if (isChecked != null && isChecked) {
					c.setBackground(new Color(60, 63, 65));
				} else {
					c.setBackground(new Color(35, 39, 42));
				}

				return c;
			}
		});

		cutTable.setDefaultRenderer(Boolean.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {

				JCheckBox checkBox = new JCheckBox();
				checkBox.setSelected(Boolean.TRUE.equals(value));
				checkBox.setHorizontalAlignment(SwingConstants.CENTER);
				checkBox.setOpaque(true);
				checkBox.setBackground(Boolean.TRUE.equals(value) ? new Color(60, 63, 65) : new Color(35, 39, 42));
				checkBox.setForeground(Color.WHITE);
				return checkBox;
			}
		});

		// Mise à jour dynamique
		model.addTableModelListener(e -> {
			if (e.getColumn() == 2) {
				cutTable.repaint();
			}
		});

		// Timer
		timer = new Timer(10, e -> {
			long now = System.currentTimeMillis();
			long elapsed = now - startTime;
			lblCounter.setText(format.format(new java.util.Date(elapsed - 3600000)));
		});

		// ACTIONS
		AbstractAction startStopAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!enCours) {
					startTime = System.currentTimeMillis() - elapsedBeforePause;
					timer.start();
					btnStartStop.setBackground(new Color(244, 67, 54));
					enCours = true;
				} else {
					elapsedBeforePause = System.currentTimeMillis() - startTime;
					timer.stop();
					btnStartStop.setBackground(new Color(76, 175, 80));
					enCours = false;
				}
			}
		};

		AbstractAction cutAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!enCours) {
					JOptionPane.showMessageDialog(null, "You should start the timer to cut", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				} else {
					model.addRow(new Object[] { lblCounter.getText(), "None", false });
				}
			}
		};

		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.ALT_DOWN_MASK), "startStop");
		contentPane.getActionMap().put("startStop", startStopAction);

		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK), "cut");
		contentPane.getActionMap().put("cut", cutAction);

		btnStartStop.addActionListener(startStopAction);
		btnCut.addActionListener(cutAction);

		btnReset.addActionListener(e -> {
			timer.stop();
			lblCounter.setText("00:00:00,00");
			model.setRowCount(0);
			btnStartStop.setBackground(new Color(76, 175, 80));
			elapsedBeforePause = 0;
			enCours = false;
		});

	}

}
