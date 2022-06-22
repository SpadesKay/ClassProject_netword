package UI;
 
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
 
public class GroupChatServer {
 
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private ClientFrame clientFrame;
 
    private static final int PORT = 6667;
 
    //������
    //��ʼ������
    public GroupChatServer() {
        clientFrame = new ClientFrame();
        try {
            //�õ�ѡ����
            selector = Selector.open();
            //ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //�󶨶˿�
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //���÷�����ģʽ
            listenChannel.configureBlocking(false);
            //���� listenChannel ע�ᵽ selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public void listen() {
        try {
            //ѭ������
            while (true) {
                int count = selector.select();
                if (count > 0) { //���¼�����
                    // �����õ� selectionKey ����
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        //ȡ�� selectionkey
                        SelectionKey key = iterator.next();
                        //������ accept
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //���� sc ע�ᵽ seletor
                            sc.register(selector, SelectionKey.OP_READ);
                            //��ʾ
//                            System.out.println(sc.getRemoteAddress() + " ���� ");
                            String remoteaddress = sc.getRemoteAddress().toString();
                            String[] split = remoteaddress.split(":");
 
                            clientFrame.setRowData(split[0],split[1]);
 
                        }
                        if (key.isReadable()) {//ͨ������read�¼�����ͨ���ǿɶ���״̬
                            // �����(ר��д����..)
                            readData(key);
                        }
                        //��ǰ�� key ɾ������ֹ�ظ�����
                        iterator.remove();
                    }
                } else {
                    System.out.println("�ȴ�....");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
 
    //��ȡ�ͻ�����Ϣ
    public void readData(SelectionKey key) {
        SocketChannel channel = null;
        try {
            //�õ� channel
            channel = (SocketChannel) key.channel();
            //���� buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            //���� count ��ֵ������
            if (count > 0) {
                //�ѻ�����������ת���ַ���
                String msg = new String(buffer.array());
                //�������Ϣ
                System.out.println("from Client: " + msg);
                clientFrame.addinfo(msg);
                //�������Ŀͻ���ת����Ϣ(ȥ���Լ�),ר��дһ������������
                sendInfoToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "disconnect..");
                String remoteaddress = channel.getRemoteAddress().toString();
                String[] split = remoteaddress.split(":");
                clientFrame.delRowData(split[0],split[1]);
                //ȡ��ע��
                key.cancel();
                //�ر�ͨ��
                channel.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
 
    //ת����Ϣ�������ͻ�(ͨ��)
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
 
        System.out.println("������ת����Ϣ��...");
        //��������ע�ᵽ selector �ϵ� SocketChannel,���ų� self
        for (SelectionKey key : selector.keys()) {
            //ͨ�� key ȡ����Ӧ�� SocketChannel
            Channel targetChannel = key.channel();
            //�ų��Լ�
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                //ת��
                SocketChannel dest = (SocketChannel) targetChannel;
                //�� msg �洢�� buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //�� buffer ������д��ͨ��
                dest.write(buffer);
            }
        }
    }
 
    public static void main(String[] args) {
        //��������������
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
 
    }
}