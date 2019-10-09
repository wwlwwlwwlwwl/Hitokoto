package hitokoto;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EasyGUI {
	public static JFrame frmIpa;
	public static JTextArea textArea;
	private static String msg = "";
	public EasyGUI() {
		frmIpa = new JFrame();
		frmIpa.setTitle("Hitokoto");
		frmIpa.setBounds(600, 300, 1200, 800);
		frmIpa.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frmIpa.getContentPane().add(panel, BorderLayout.NORTH);
		JButton button = new JButton("启动");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Core.thread == null) {
				new Core().GetJSON();
				addMSG("Console > 已成功新建线程!");
				} else {
					JOptionPane.showMessageDialog(null, "进程已经在运行!");
				}
			}
		});
		panel.add(button);
		JButton button2 = new JButton("停止");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Core.thread != null) {
					Core.enable = false;
					addMSG("Console > 线程已终止!");
					frmIpa.setTitle("Hitokoto");
					Core.thread = null;
				} else {
					JOptionPane.showMessageDialog(null, "进程并没有启动!");
					frmIpa.setTitle("Hitokoto");
				}
			}
		});
		panel.add(button2);
		JScrollPane scrollPane = new JScrollPane();
		frmIpa.getContentPane().add(scrollPane, BorderLayout.CENTER);
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		frmIpa.setVisible(true);
		addMSG("Console > 控制台初始化成功!");
	}
	
	public static void addMSG(String s) {
		msg = msg + s + "\r\n";
		textArea.setText(msg);
	}
	
	public static void setTitle(String s) {
		frmIpa.setTitle(s);
	}
}
