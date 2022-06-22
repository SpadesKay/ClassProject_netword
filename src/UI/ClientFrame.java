package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
 
class ClientFrame extends JFrame {
 
    //ʱ��
    SimpleDateFormat timeLoadIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //����
    final int WIDTH = 700;
    final int HEIGHT = 600;
    //��ť
    JButton buttonClear = new JButton("Clear");
 
    //����������Ϣ��
    JTextArea textAreaChatting = new JTextArea("Chatting box\n");
 
    //�����б����
    String[] colTitles = {"IP", "Port"};
    //�����б������,�ݴ涯̬����
    String[][] rowDatatrue;
 
    public void setRowData(String address,String port){
        String[] newaddress = {address,port};
        textAreaChatting.append(timeLoadIn.format(new Date()) + "\n: " + address+":"+port  + " load in\n");
        DefaultTableModel  model = (DefaultTableModel)jtbOnline.getModel();
        model.addRow(new Object[]{newaddress[0],newaddress[1]});
        jtbOnline.setModel(model);
    }
    public void addinfo(String msg){
        textAreaChatting.append(timeLoadIn.format(new Date()) + "\n: " + msg.trim() +"\n");
 
    }
 
    public void delRowData(String address,String port){
        String[] deladdress = {address,port};
        textAreaChatting.append(timeLoadIn.format(new Date()) + "\n: " + address+":"+port  + " log out\n");
        DefaultTableModel  model = (DefaultTableModel)jtbOnline.getModel();
        int rowcount= model.getRowCount();
        for(int i=0;i<rowcount;i++){
            if(model.getValueAt(i,1).equals(deladdress[1])){
                model.removeRow(i);
                break;
            }
        }
        model.fireTableDataChanged();
    }
 
    //������ǰ�����б�
    JTable jtbOnline = new JTable
            (
                    new DefaultTableModel(rowDatatrue, colTitles) {
                        //��񲻿ɱ༭��ֻ����ʾ
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    }
            );
 
    //��Ϣ�������
    JScrollPane scrollPaneChatting = new JScrollPane(textAreaChatting);
    //�����б������
    JScrollPane scrollPaneOnline = new JScrollPane(jtbOnline);
 
    //����Ĭ�ϴ������ԣ����Ӵ������
    public ClientFrame() {
        //���⣬��С�����ţ�����,��ť
        setTitle("Instant Messaging Utility");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLayout(null);
        buttonClear.setBounds(320, 460, 100, 40);
 
        //���ð�ť�ı�������
        buttonClear.setFont(new Font("����", Font.BOLD, 20));
 
 
        this.add(buttonClear);
 
        //���������
        textAreaChatting.setLineWrap(true);
        textAreaChatting.setEditable(false);
        textAreaChatting.setFont(new Font("����", Font.BOLD, 16));
 
        scrollPaneChatting.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneChatting.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneChatting.setBounds(20, 20, 360, 400);
        this.add(scrollPaneChatting);
 
        //�����б�����
        scrollPaneOnline.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneOnline.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneOnline.setBounds(420, 20, 250, 400);
        this.add(scrollPaneOnline);
 
        //��Ӱ�ť����Ӧ�¼�
        buttonClear.addActionListener
                (
                        new ActionListener() {
                            public void actionPerformed(ActionEvent event) {
                                //�����
                                textAreaChatting.setText("");
                            }
                        }
                );
 
        this.show();
    }
}