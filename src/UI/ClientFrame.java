package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
 
class ClientFrame extends JFrame {
 
    //时间
    SimpleDateFormat timeLoadIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //窗口
    final int WIDTH = 700;
    final int HEIGHT = 600;
    //按钮
    JButton buttonClear = new JButton("Clear");
 
    //创建聊天消息框
    JTextArea textAreaChatting = new JTextArea("Chatting box\n");
 
    //在线列表标题
    String[] colTitles = {"IP", "Port"};
    //在线列表的数据,暂存动态数组
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
 
    //创建当前在线列表
    JTable jtbOnline = new JTable
            (
                    new DefaultTableModel(rowDatatrue, colTitles) {
                        //表格不可编辑，只可显示
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    }
            );
 
    //消息框滚动窗
    JScrollPane scrollPaneChatting = new JScrollPane(textAreaChatting);
    //在线列表滚动窗
    JScrollPane scrollPaneOnline = new JScrollPane(jtbOnline);
 
    //设置默认窗口属性，连接窗口组件
    public ClientFrame() {
        //标题，大小，缩放，布局,按钮
        setTitle("Instant Messaging Utility");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLayout(null);
        buttonClear.setBounds(320, 460, 100, 40);
 
        //设置按钮文本的字体
        buttonClear.setFont(new Font("宋体", Font.BOLD, 20));
 
 
        this.add(buttonClear);
 
        //聊天框设置
        textAreaChatting.setLineWrap(true);
        textAreaChatting.setEditable(false);
        textAreaChatting.setFont(new Font("宋体", Font.BOLD, 16));
 
        scrollPaneChatting.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneChatting.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneChatting.setBounds(20, 20, 360, 400);
        this.add(scrollPaneChatting);
 
        //在线列表设置
        scrollPaneOnline.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneOnline.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneOnline.setBounds(420, 20, 250, 400);
        this.add(scrollPaneOnline);
 
        //添加按钮的响应事件
        buttonClear.addActionListener
                (
                        new ActionListener() {
                            public void actionPerformed(ActionEvent event) {
                                //聊天框
                                textAreaChatting.setText("");
                            }
                        }
                );
 
        this.show();
    }
}